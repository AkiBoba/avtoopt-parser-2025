package com.example.armtek_parcer_new.aliance.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class SiteParser {
    private static final String BASE_URL = "https://armtek.ru/brand";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2";

    public Map<String, String> parseProducersUrlsAsync() throws IOException {
        Map<String, String> producersUrls = new HashMap<>();
        Document doc = Jsoup.connect(BASE_URL).timeout(10000).userAgent(USER_AGENT).get();
        Elements elements = doc.select(".brand-item.ng-star-inserted");
        elements.forEach(element -> {
            String text = element.text(); // Получаем текст элемента
            String href = element.attr("href"); // Получаем значение href
            producersUrls.put(text, href);
        });

        return producersUrls;
    }
}