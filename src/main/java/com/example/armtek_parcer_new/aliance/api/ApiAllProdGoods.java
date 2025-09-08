package com.example.armtek_parcer_new.aliance.api;

import com.example.armtek_parcer_new.aliance.domain.GoodUrl;
import com.example.armtek_parcer_new.aliance.domain.ProdUrl;
import org.apache.poi.ss.formula.functions.T;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class ApiAllProdGoods {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2";
    private final List<GoodUrl> producersGoodsUrls = new ArrayList<>();

    public List<GoodUrl> findAllProdGoodsUris(Map<String, ProdUrl> prodUrls) {

        List<Callable<List<GoodUrl>>> callables = new ArrayList<>();
        prodUrls.entrySet().forEach(entry -> {
            callables.add(getCallableGoodsUrls(entry.getValue().getUrl(), entry));
//            getGoodsFromBasePage(entry.getValue().getUrl(), producersGoodsUrls, entry);
//            int pageCount;
//            try {
//                Document doc = getDoc(entry.getValue().getUrl());
//                Element formGroup = doc.select(".form-group.d-flex.align-items-center").first();
//                if (formGroup != null) {
//                    Element boldSpan = formGroup.select("span.bold").first();
//                    if (boldSpan != null) {
//                        pageCount = Integer.parseInt(boldSpan.text());
//                        pageCount = (int) Math.ceil((double) pageCount / 20);
//                        if(pageCount == 0) pageCount = 1;
//                        System.out.println("Count of pages : " + pageCount);
//                        for(int i = 2; i <= pageCount; i++) {
//                            String urlNextPage = String.format("%s?PAGEN_2=%d#producers-page", entry.getValue().getUrl(), i);
//                            try {
//                                System.out.println("next page is " + urlNextPage);
//                                getGoodsFromBasePage(urlNextPage, producersGoodsUrls, entry);
//                            } catch (Exception e) {
//
//                            }
//                        }
//                    }
//                }
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        });
        execute(callables);
        return producersGoodsUrls;
    }

    private void execute( List<Callable<List<GoodUrl>>> callables) {
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        callables.stream().map(executorService::submit).forEach(feature -> {
            try {
                producersGoodsUrls.addAll(feature.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }
        });
        executorService.shutdown();
    }

    private Callable<List<GoodUrl>> getCallableGoodsUrls(String url, Map.Entry<String, ProdUrl> entry) {
        Callable<List<GoodUrl>> task = () ->  {
            getGoodsFromBasePage(entry.getValue().getUrl(), producersGoodsUrls, entry);
            int pageCount;
            try {
                Document doc = getDoc(entry.getValue().getUrl());
                Element formGroup = doc.select(".form-group.d-flex.align-items-center").first();
                if (formGroup != null) {
                    Element boldSpan = formGroup.select("span.bold").first();
                    if (boldSpan != null) {
                        pageCount = Integer.parseInt(boldSpan.text());
                        pageCount = (int) Math.ceil((double) pageCount / 20);
                        if(pageCount == 0) pageCount = 1;
                        System.out.println("Count of pages : " + pageCount);
                        for(int i = 2; i <= pageCount; i++) {
                            String urlNextPage = String.format("%s?PAGEN_2=%d#producers-page", entry.getValue().getUrl(), i);
                            try {
                                System.out.println("next page is " + urlNextPage);
                                getGoodsFromBasePage(urlNextPage, producersGoodsUrls, entry);
                            } catch (Exception e) {

                            }
                        }
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return producersGoodsUrls;
        };

        return task;
    }

    private void getGoodsFromBasePage(String url, List<GoodUrl> producersGoodsUrls, Map.Entry<String, ProdUrl> entry) {
        try {
            Document doc = getDoc(url);
            Elements elements = doc.select(".n-catalog-item__name.__add-property a.n-catalog-item__name-link"); // Выбираем именно ссылку

            for (Element link : elements) {
                String goodUrl = link.absUrl("href");     // Получаем абсолютный адрес ссылки
                producersGoodsUrls.add(
                        new GoodUrl(
                                entry.getValue().getProducer(),
                                entry.getKey(),
                                goodUrl
                        )
                );
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private Document getDoc(String url) throws IOException {
        return Jsoup.connect(url)
                .timeout(10000)
                .userAgent(USER_AGENT)
                .get();
    }
}

///href="/producers/193390569?PAGEN_2=2#producers-page


