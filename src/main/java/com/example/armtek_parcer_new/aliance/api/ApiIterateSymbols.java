package com.example.armtek_parcer_new.aliance.api;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ApiIterateSymbols {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2";

    public List<String> findAllPaginationUris(List<String> symUrls) {
        List<String> paginationUrls = new ArrayList<>();

        symUrls.forEach(url -> {
            try {
                Document doc = Jsoup.connect(url)
                        .timeout(10000)
                        .userAgent(USER_AGENT)
                        .get();

                // Ищем все ссылки пагинации
                Elements paginationLinks = doc.select(".pagination__link[href]");

                // Извлекаем атрибут href из каждой ссылки
                paginationLinks.forEach(link -> {
                    String href = link.attr("abs:href");
                    if (href != null && !href.trim().isEmpty()) {
                        paginationUrls.add(href);
                    }
                });

            } catch (IOException e) {
                throw new RuntimeException("Ошибка при подключении к URL: " + url, e);
            }
        });

        return paginationUrls;
    }
}
