package com.example.armtek_parcer_new.aliance.api;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ApiAllProducersSymbolsUrls {
    private static final String BRANDS_URL = "https://www.autoopt.ru/producers";
    private static final String BASE_URL = "https://www.autoopt.ru/producers?firstLetter=";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2";

    public List<String> getAllSymbolsProducersUrls() throws IOException {
        List<String> urls = new ArrayList<>();
        Document doc = Jsoup.connect(BRANDS_URL)
                .timeout(50000)
                .userAgent(USER_AGENT).get();
        Elements elements = doc.select(".firstLetterFilterRadioGroup");
        elements.forEach(element -> urls.add(BASE_URL + element.text()));
        System.out.println((long) elements.size());
        return urls;
    }
}

