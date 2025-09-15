package com.example.armtek_parcer_new.aliance.facade;

import com.example.armtek_parcer_new.aliance.api.ApiAllProducersSymbolsUrls;
import com.example.armtek_parcer_new.aliance.apiAsync.ApiAllProdGoodsAsync;
import com.example.armtek_parcer_new.aliance.apiAsync.ApiAllProducersUrlsAsync;
import com.example.armtek_parcer_new.aliance.apiAsync.ApiGetProductsInfoAsync;
import com.example.armtek_parcer_new.aliance.apiAsync.ApiIterateSymbolsAsync;
import com.example.armtek_parcer_new.aliance.domain.GoodUrl;
import com.example.armtek_parcer_new.aliance.domain.ProdUrl;
import com.example.armtek_parcer_new.aliance.repository.GoodUrlRepositoryJpa;
import com.example.armtek_parcer_new.aliance.util.ProducersAlianceFilter;
import com.example.armtek_parcer_new.aliance.domain.FileLine;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class FacadeAlianceAPI {
    ApiAllProducersSymbolsUrls symbolsUrls;
    ApiIterateSymbolsAsync iterateSymbolsAsync;
    ApiAllProducersUrlsAsync producersUrls;
    ProducersAlianceFilter producersAlianceFilter;
    ApiAllProdGoodsAsync apiAllProdGoodsAsync;
    ApiGetProductsInfoAsync apiGetProductsInfo;
    GoodUrlRepositoryJpa goodUrlRepositoryJpa;
    public void getWorks(List<FileLine> producers) throws IOException, InterruptedException {
        //Получаем ссылки на все буквы указанные в алфавитном перечне производителей
        List<String> symUrls = symbolsUrls.getAllSymbolsProducersUrls();
        //Открываем каждую букву и ищем там пагинацию, находим ссылки на все страницы > 1
        List<String> itUrls = iterateSymbolsAsync.findAllPaginationUris(symUrls);
        //Объеденяем в один список все ссылки алфавитного указателя с их пагинациями
        symUrls.addAll(itUrls);
        //Ищем ссылки на производителей
        Map<String, ProdUrl> prodUrls = producersUrls.findAllProducersUrls(symUrls);
//        System.out.println("ProdUrls.size() = " + prodUrls.size());
        //Выбираем производителей, совпадающий с переданным списком
        Map<String, ProdUrl> cleanProducersUrls = producersAlianceFilter.filter(prodUrls, producers);
        System.out.println("cleanProducersUrls.size() = " + cleanProducersUrls.size());
//        Находим все ссылки на товары на всех страницах производителей, включая пагинацию
//        List<GoodUrl> goodUrls = apiAllProdGoods.findAllProdGoodsUris(cleanProducersUrls);
//        List<GoodUrl> existGoodUrls = goodUrlRepositoryJpa.findAll();
//        Собираем информацию о товарах
        cleanProducersUrls.entrySet().forEach(entry ->
                {
                        if (goodUrlRepositoryJpa.findUrlsByProducerClean(entry.getKey()) == 0) {
                            List<GoodUrl> goodUrlsAsync = apiAllProdGoodsAsync.findAllProdGoodsUrls(entry);
                            goodUrlRepositoryJpa.saveAll(goodUrlsAsync);
                            apiGetProductsInfo.getProductsInfo(goodUrlsAsync, entry.getValue().getProducer());
                            log.info("goods of producers {} saved in DB", entry.getKey());
                        }
                }
        );

        System.out.println();

    }

}
