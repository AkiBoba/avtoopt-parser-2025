package com.example.armtek_parcer_new.aliance.api;

import com.example.armtek_parcer_new.aliance.domain.GoodUrl;
import com.example.armtek_parcer_new.aliance.domain.ProdUrl;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

@Component
public class ApiAllProdGoods {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/535.2 (KHTML, like Gecko) Chrome/15.0.874.120 Safari/535.2";

    public List<GoodUrl> findAllProdGoodsUris(Map<String, ProdUrl> prodUrls) {
        long start = System.currentTimeMillis();
        List<Callable<List<GoodUrl>>> callables = prodUrls.entrySet()
                .stream()
                .map(entry -> getCallableGoodsUrls(entry.getValue().getUrl(), entry)
        ).collect(Collectors.toList());
        final List<GoodUrl> producersGoodsUrls = new ArrayList<>(execute(callables));
        long elapsedMillis = System.currentTimeMillis() - start;
        double elapsedSeconds = Math.round(elapsedMillis / 10.0) / 100.0; // округление до 2 знаков
        System.out.println("time of complete = " + elapsedSeconds + " seconds");
        System.out.println("Size of result = " + producersGoodsUrls.size());
        return producersGoodsUrls;
    }

    private List<GoodUrl> execute(List<Callable<List<GoodUrl>>> callables) {
        List<GoodUrl> goodsUrls = new ArrayList<>();
        ExecutorService executorService = Executors.newFixedThreadPool(6);
        callables.stream().map(executorService::submit).toList().forEach(feature -> {
            try {
                goodsUrls.addAll(feature.get());
            } catch (InterruptedException | ExecutionException e) {
                throw new RuntimeException(e);
            }

        });
        executorService.shutdown();
        return goodsUrls;
    }

    private Callable<List<GoodUrl>> getCallableGoodsUrls(String url, Map.Entry<String, ProdUrl> entry) {
        Callable<List<GoodUrl>> listCallable = () -> {
            int pageCount;
            List<GoodUrl> goodUrlList = new ArrayList<>();
            try {
                Document doc = getDoc(url);
                pageCount = getPageCount(doc);
                for (int i = 1; i <= pageCount; i++) {
                    String urlNextPage = String.format("%s?PAGEN_2=%d#producers-page", entry.getValue().getUrl(), i);
                    try {
//                        System.out.println("next page is " + urlNextPage);
                        goodUrlList.addAll(getGoodsFromBasePage(urlNextPage, entry));
                    } catch (Exception e) {

                    }
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return goodUrlList;
        };

        return listCallable;
    }

    private int getPageCount(Document doc) {
        int pageCount = 0;
        Element formGroup = doc.select(".form-group.d-flex.align-items-center").first();
        if (formGroup != null) {
            Element boldSpan = formGroup.select("span.bold").first();
            if (boldSpan != null) {
                pageCount = Integer.parseInt(boldSpan.text());
                pageCount = (int) Math.ceil((double) pageCount / 20);
            }
        }
        return pageCount == 0 ? 1 : pageCount;
    }

    private List<GoodUrl> getGoodsFromBasePage(String url,  Map.Entry<String, ProdUrl> entry) {
        List<GoodUrl> returnedList = new ArrayList<>();
        try {
            Document doc = getDoc(url);
            Elements elements = doc.select(".n-catalog-item__name.__add-property a.n-catalog-item__name-link"); // Выбираем именно ссылку

            for (Element link : elements) {
                String goodUrl = link.absUrl("href");     // Получаем абсолютный адрес ссылки
                returnedList.add(
                        GoodUrl.builder()
                                .producer(entry.getValue().getProducer())
                                .producerClean(entry.getKey())
                                .url(goodUrl)
                                .build()
                );
            }

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return returnedList;
    }

    private Document getDoc(String url) throws IOException {
        return Jsoup.connect(url)
                .timeout(10000)
                .userAgent(USER_AGENT)
                .get();
    }
}




