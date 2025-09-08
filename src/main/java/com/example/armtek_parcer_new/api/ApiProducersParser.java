package com.example.armtek_parcer_new.api;

import com.example.armtek_parcer_new.domain.BrandsResponse;
import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ApiProducersParser {

    private static final String BRANDS_URL = "https://armtek.ru/rest/ru/assortment-microservice/v1/widgets/brands";
    private String cookieHeader = "app_options=SlRkQ0pUSXljMmh2ZDBWNGRISmhUV1Z6YzJGblpYTWxNaklsTTBGbVlXeHo0NzMwMjcxWlNVeVF5VXlNbk5oY0VScGMyRmliR1ZrSlRJeUpUTkJabUZzYzJVbE4wUSUzRAqlsqls4730271; referrer=; _ga_ArmtekRu-DG=GS2.1.s1756894730$o1$g0$t1756894730$j60$l0$h1401229487; _ga=GA1.1.1576520075.1756894731; _ym_uid=175689473186307763; _ym_d=1756894731";
    private final HttpClient client;
    private final Gson gson;
    private final ApiAccessParser accessParser;

    public Map<String, String> parseProducersUrlsAsync() throws IOException, InterruptedException {
        String token = accessParser.getAccessToken();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(BRANDS_URL))
                .GET()
                .header("authorization", "Bearer " + token)
                .header("accept", "application/json, text/plain, */*")
                .header("content-type", "application/json")
                .header("cookie", cookieHeader)
                .header("origin", "https://armtek.ru")
                .header("referer", "https://armtek.ru/brand")
                .header("sec-ch-ua", "\"Not;A=Brand\";v=\"99\", \"Microsoft Edge\";v=\"139\", \"Chromium\";v=\"139\"")
                .header("sec-ch-ua-mobile", "?0")
                .header("sec-ch-ua-platform", "\"Windows\"")
                .header("sec-fetch-dest", "empty")
                .header("sec-fetch-mode", "cors")
                .header("sec-fetch-site", "same-origin")
                .header("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/139.0.0.0 Safari/537.36 Edg/139.0.0.0")
                .header("x-app-version", "1.0.219")
                .header("x-ca-external-system", "IM_RU")
                .header("x-ca-vkorg", "4000")
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Не удалось получить бренды: " + response.statusCode());
        }

        String responseBody = response.body();
        BrandsResponse brandsResponse = gson.fromJson(responseBody, BrandsResponse.class);

        Map<String, String> producersUrls = new HashMap<>();

        // Обрабатываем все категории
        for (BrandsResponse.BrandCategory category : brandsResponse.data.values()) {
            for (BrandsResponse.BrandItem item : category.items) {
                String brandUrl = "https://armtek.ru/brand/" + item.ALIAS;
                producersUrls.put(item.BRAND, brandUrl);
            }
        }

        return producersUrls;
    }
}
