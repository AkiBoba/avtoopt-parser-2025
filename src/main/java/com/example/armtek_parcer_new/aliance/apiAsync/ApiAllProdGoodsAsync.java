package com.example.armtek_parcer_new.aliance.apiAsync;
import com.example.armtek_parcer_new.aliance.domain.GoodUrl;
import com.example.armtek_parcer_new.aliance.domain.ProdUrl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ApiAllProdGoodsAsync {

    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2";

    // Пул потоков — создаем один раз, можно вынести в конфиг
    private final ExecutorService executorService = Executors.newFixedThreadPool(6);

    public List<GoodUrl> findAllProdGoodsUrls(Map.Entry<String, ProdUrl> entry) {
        Long start = System.currentTimeMillis();
        log.info("Start getting goods urls for producer {}", entry.getValue().getProducer());

        // Создаём CompletableFuture для производителя
        CompletableFuture<List<GoodUrl>> futures = CompletableFuture
                        .supplyAsync(() -> getGoodsUrlsForProducer(entry), executorService)
                        .exceptionally(ex -> {
                            log.error("Error parsing producer: {}, URL: {}",
                                    entry.getValue().getProducer(), entry.getValue().getUrl(), ex);
                            return Collections.emptyList();
                        });
        // Ждём завершения всех задач и собираем результат
        List<GoodUrl> allGoodsUrls = futures.join();

        // Замер времени
        long elapsedMillis = System.currentTimeMillis() - start;
        double elapsedSeconds = Math.round(elapsedMillis / 10.0) / 100.0;
        log.info("Finish getting goods urls for producer {}, goods urls size = {} ", entry.getValue().getProducer(), allGoodsUrls.size());
        return allGoodsUrls;
    }

    private List<GoodUrl> getGoodsUrlsForProducer(Map.Entry<String, ProdUrl> entry) {
        String baseUrl = entry.getValue().getUrl();
        List<GoodUrl> list = new ArrayList<>();

        try {
            Document firstPage = getDoc(baseUrl);
            int pageCount = getPageCount(firstPage);

            // Собираем все страницы этого производителя
            for (int i = 1; i <= pageCount; i++) {
                String urlNextPage = String.format("%s?PAGEN_2=%d#producers-page", baseUrl, i);
//                System.out.println("Fetching page: " + urlNextPage);
                list.addAll(getGoodsFromBasePage(urlNextPage, entry));
            }

        } catch (Exception e) {
            System.err.println("Error fetching pages for producer: " + entry.getValue().getProducer() + ", URL: " + baseUrl);
            e.printStackTrace();
        }

        return list;
    }

    private int getPageCount(Document doc) {
        Element formGroup = doc.select(".form-group.d-flex.align-items-center").first();
        if (formGroup != null) {
            Element boldSpan = formGroup.select("span.bold").first();
            if (boldSpan != null) {
                int totalCount = Integer.parseInt(boldSpan.text());
                return (int) Math.ceil((double) totalCount / 20);
            }
        }
        return 1;
    }

    private List<GoodUrl> getGoodsFromBasePage(String url, Map.Entry<String, ProdUrl> entry) {
        List<GoodUrl> returnedList = new ArrayList<>();
        try {
            Document doc = getDoc(url);
            Elements links = doc.select(".n-catalog-item__name.__add-property a.n-catalog-item__name-link");

            for (Element link : links) {
                String goodUrl = link.absUrl("href");
                returnedList.add(GoodUrl
                        .builder()
                        .producer(entry.getValue().getProducer())
                        .producerClean(entry.getKey())
                        .url(goodUrl)
                        .build()

                );
            }
        } catch (IOException e) {
            System.err.println("Error fetching goods from page: " + url);
            e.printStackTrace();
        }
        return returnedList;
    }

    private Document getDoc(String url) throws IOException {
        return Jsoup.connect(url)
                .timeout(10000)
                .userAgent(USER_AGENT)
                .get();
    }

    // Опционально: graceful shutdown при остановке приложения
    // Можно добавить @PreDestroy, если используете Spring
    // @PreDestroy
    // public void shutdown() {
    //     executorService.shutdown();
    // }
}
