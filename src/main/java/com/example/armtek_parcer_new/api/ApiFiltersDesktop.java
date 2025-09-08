package com.example.armtek_parcer_new.api;

import lombok.RequiredArgsConstructor;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ApiFiltersDesktop {
//    private static final String BRANDS_URL = "https://armtek.ru/brand";
    private static final String BRANDS_URL = "https://www.autoopt.ru/producers";
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2";

    public void getAllProducersUrls() throws IOException {
        Document doc = Jsoup.connect(BRANDS_URL)
                .timeout(50000)
                .userAgent(USER_AGENT).get();
        Elements elements = doc.select(".firstLetterFilterRadioGroup");
        System.out.println((long) elements.size());
    }
}
