package com.example.armtek_parcer_new.aliance.api;

import com.example.armtek_parcer_new.aliance.domain.ProdUrl;
import com.example.armtek_parcer_new.util.TextCleaner;
import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ApiAllProducersUrls {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2";
    private final TextCleaner textCleaner;
    public Map<String, ProdUrl> findAllProducersUrls(List<String> urls) {
        Map<String, ProdUrl> producersUrls = new HashMap<>();

        urls.forEach(url -> {
            try {
                Document doc = Jsoup.connect(url)
                        .timeout(10000)
                        .userAgent(USER_AGENT)
                        .get();

                // Ищем все элементы брендов
                Elements brandItems = doc.select(".brand-item[href]");

                // Извлекаем данные из каждого элемента
                brandItems.forEach(item -> {
                    String href = item.attr("abs:href");

                    // Получаем alt из изображения внутри элемента
                    String altText = item.select(".brand-n").text().trim();

                    if (href != null && !href.trim().isEmpty() &&
                            altText != null && !altText.trim().isEmpty()) {
                        producersUrls.put(textCleaner.cleanText(altText), new ProdUrl(altText, href));
                    }
                });

            } catch (IOException e) {
                throw new RuntimeException("Ошибка при подключении к URL: " + url, e);
            }
        });

        return producersUrls;
    }
}
