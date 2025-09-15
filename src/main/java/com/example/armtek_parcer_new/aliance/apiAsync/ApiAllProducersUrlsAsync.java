package com.example.armtek_parcer_new.aliance.apiAsync;

import com.example.armtek_parcer_new.aliance.domain.ProdUrl;
import com.example.armtek_parcer_new.aliance.util.TextAlianceCleaner;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApiAllProducersUrlsAsync {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2";
    ExecutorService executorService;
    private final TextAlianceCleaner textCleaner;
    public Map<String, ProdUrl> findAllProducersUrls(List<String> urls) {

        return urls.stream()
                .map(url -> CompletableFuture.supplyAsync(() -> (getProducersUrls(url)), executorService).exceptionally(ex -> {
                    log.warn("Ошибка при обработке URL: {}", url, ex);
                    return Collections.emptyMap(); // возвращаем пустой Map при ошибке
                })).map(CompletableFuture::join).flatMap(map -> map.entrySet().stream()).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

    private Map<String, ProdUrl> getProducersUrls(String url) {
        return getDoc(url)                           // Optional<Document>
                .stream()                                // Stream<Document> (0 или 1 элемент)
                .flatMap(doc -> doc.select(".brand-item[href]").stream()) // Stream<Element>
                .flatMap(item -> {
                    String href = item.attr("abs:href");
                    String altText = item.select(".brand-n").first() != null
                            ? item.select(".brand-n").text().trim()
                            : null;

                    if (href != null && !href.isBlank() && altText != null && !altText.isBlank()) {
                        String cleanKey = textCleaner.cleanText(altText);
                        return Stream.of(new AbstractMap.SimpleEntry<>(cleanKey, new ProdUrl(altText, href)));
                    } else {
                        return Stream.empty();
                    }
                })
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1 // при дубликате — оставляем первый
                ));
    }

    private Optional<Document> getDoc(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .timeout(10000)
                    .userAgent(USER_AGENT)
                    .get();
            return Optional.of(doc);
        } catch (IOException e) {
            log.error("Error for get doc for producers pages in url {}", url);
            return Optional.empty();
        }
    }
}
