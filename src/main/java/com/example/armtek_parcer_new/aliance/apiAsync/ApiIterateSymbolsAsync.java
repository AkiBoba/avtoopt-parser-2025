package com.example.armtek_parcer_new.aliance.apiAsync;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApiIterateSymbolsAsync {
    static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2";
    ExecutorService executorService;

    public List<String> findAllPaginationUris(List<String> symUrls) {
        List<CompletableFuture<List<String>>> completableFutureList = symUrls.stream().map(url -> CompletableFuture.supplyAsync(() -> getPaginationUrls(url), executorService)).toList();
        List<String> returnList = completableFutureList.stream().map(CompletableFuture::join).flatMap(List::stream).collect(Collectors.toList());
        return returnList;

    }

    private List<String> getPaginationUrls(String url) {
        Document doc = getDoc(url);
        Elements paginationLinks = doc.select(".pagination__link[href]");
        return paginationLinks.stream().map(link -> link.attr("abs:href")).filter(href -> !href.isBlank()).filter(href -> !href.trim().isEmpty()).toList();
    }

    private Document getDoc(String url) {
        Document document = null;
        try {
            document = Jsoup.connect(url)
                    .timeout(10000)
                    .userAgent(USER_AGENT)
                    .get();
        } catch (IOException e) {
            log.error("Error for get doc pagination symbols in url {}", url);
        }
        return document;
    }
}
