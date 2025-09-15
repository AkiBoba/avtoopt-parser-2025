package com.example.armtek_parcer_new.aliance;

import com.example.armtek_parcer_new.aliance.api.ApiAllProdGoods;
import com.example.armtek_parcer_new.aliance.apiAsync.ApiAllProdGoodsAsync;
import com.example.armtek_parcer_new.aliance.api.ApiAllProducersSymbolsUrls;
import com.example.armtek_parcer_new.aliance.apiAsync.ApiAllProducersUrlsAsync;
import com.example.armtek_parcer_new.aliance.api.ApiIterateSymbols;
import com.example.armtek_parcer_new.aliance.domain.ProdUrl;
import com.example.armtek_parcer_new.aliance.facade.FacadeAlianceAPI;
import com.example.armtek_parcer_new.util.ProducersFilter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@SpringBootTest
public class baseTest {
    @Autowired
    private FacadeAlianceAPI facadeAPI;
    @Autowired
    private ApiAllProducersSymbolsUrls symbolsUrls;
    @Autowired
    private ApiIterateSymbols iterateSymbols;
    @Autowired
    private ApiAllProducersUrlsAsync producersUrls;
    @Autowired
    private ProducersFilter producersFilter;
    @Autowired
    private ApiAllProdGoods apiAllProdGoods;
    @Autowired
    private ApiAllProdGoodsAsync apiAllProdGoodsAsync;
    @Test
    void parse() throws IOException, InterruptedException {
//        apiAllProdGoodsAsync.findAllProdGoodsUrls(Map.of("KAMAZ", new ProdUrl("KAMAZ", "https://www.autoopt.ru/producers/436283"), "AVVA", new ProdUrl("AVVA", "https://www.autoopt.ru/producers/439552")));


//        iterateSymbols.findAllPaginationUris(symbolsUrls.getAllSymbolsProducersUrls());

    }
}
