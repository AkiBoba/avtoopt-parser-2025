package com.example.armtek_parcer_new.aliance.api;

import com.example.armtek_parcer_new.aliance.domain.AlianceGoodInfo;
import com.example.armtek_parcer_new.aliance.domain.GoodUrl;
import com.example.armtek_parcer_new.aliance.repository.GoodInfoJpaRepository;
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
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ApiGetProductsInfo {
    static String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2";
    ExecutorService executorService;
    GoodInfoJpaRepository repository;
    public List<AlianceGoodInfo> getProductsInfo(List<GoodUrl> urls) {

        String code;
        String description;
        String article;
        String name;
        List<AlianceGoodInfo> alianceGoodInfoList = new ArrayList<>();
        for (GoodUrl goodUrl: urls) {
            try {
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
                String length = !lenghtList.isEmpty() ?  lenghtList.get(0).stream().filter(e -> !e.text().equals("Длина, м")).toList().get(0).text().replace("Длина, м", "") : "";
                AlianceGoodInfo good = AlianceGoodInfo.builder()
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
                alianceGoodInfoList.add(good);
            } catch (IOException e) {
                log.error(String.valueOf(e));
            }
        }
        repository.saveAll(alianceGoodInfoList);
        log.info("Парсинг введенного адреса завершен! Совпадение ссылок {} шт");
        return alianceGoodInfoList;
    }
}
