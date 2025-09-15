package com.example.armtek_parcer_new.aliance.apiAsync;

import com.example.armtek_parcer_new.aliance.domain.AlianceGoodInfo;
import com.example.armtek_parcer_new.aliance.domain.GoodUrl;
import com.example.armtek_parcer_new.aliance.repository.GoodInfoJpaRepository;
import com.fasterxml.jackson.databind.introspect.TypeResolutionContext;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApiGetProductsInfoAsync {
    static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2";
    ExecutorService executorService;
    GoodInfoJpaRepository repository;

    public List<AlianceGoodInfo> getProductsInfo(List<GoodUrl> urls, String producer) {
        log.info("Start getting goods info for producer {}", producer);

        List<AlianceGoodInfo> alianceGoodInfoList;
        List<CompletableFuture<AlianceGoodInfo>> futureList =
                urls.stream().map(goodUrl -> {
                    CompletableFuture<AlianceGoodInfo> future = CompletableFuture.supplyAsync(() -> createGoodInfoObject(goodUrl), executorService);
                    return future.exceptionally(ex -> {
                        log.error("Error for parsing goods URL: {}", goodUrl, ex);
                        return new AlianceGoodInfo();
                    });
                }).toList();
        alianceGoodInfoList = futureList.stream().map(CompletableFuture::join).collect(Collectors.toList());
        repository.saveAll(alianceGoodInfoList);
        log.info("Parsing goods info finish, goods info size saved {} шт", alianceGoodInfoList.size());
        return alianceGoodInfoList;
    }


    private AlianceGoodInfo createGoodInfoObject(GoodUrl goodUrl) {
        AlianceGoodInfo good = new AlianceGoodInfo();
        try {
            String code;
            String description;
            String article;
            String name;
            Element doc = Jsoup.connect(goodUrl.getUrl()).timeout(30000).userAgent(USER_AGENT).get();
            List<Element> elements = doc.getElementsByAttributeValueContaining("itemprop", "description");
            description = elements.isEmpty() ? "" : elements.get(0).text();
            elements = doc.getElementsByAttributeValueContaining("class", "card-product-article");
            article = elements.isEmpty() ? "" : elements.get(0).text().replace("Артикул: ", "");
            elements = doc.getElementsByAttributeValueContaining("class", "card-product-title");
            name = elements.isEmpty() ? "" : elements.get(0).text();
            elements = doc.getElementsByAttributeValueContaining("itemprop", "sku");
            code = elements.isEmpty() ? "" : elements.get(0).text();

            elements = doc.getElementsByTag("tr");
            List<Elements> lengths = new ArrayList<>();
            elements.forEach(element -> lengths.add(element.getElementsMatchingText("Длина, м")));
            List<Elements> lenghtList = lengths.stream().filter(e -> !e.isEmpty()).toList();
            String length = !lenghtList.isEmpty() ? lenghtList.get(0).stream().filter(e -> !e.text().equals("Длина, м")).toList().get(0).text().replace("Длина, м", "") : "";
            good = AlianceGoodInfo.builder()
                    .producer(goodUrl.getProducer())
                    .code(code)
                    .article(article)
                    .name(name)
                    .description(description)
                    .width(doc.getElementsByAttributeValue("itemprop", "width").text())
                    .height(doc.getElementsByAttributeValue("itemprop", "height").text())
                    .length(length)
                    .weight(doc.getElementsByAttributeValue("itemprop", "weight").text())
                    .codeTnVED("")
                    .build();
        } catch (IOException e) {
            log.error(String.valueOf(e));
        }
        return good;
    }
}
