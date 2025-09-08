package com.example.armtek_parcer_new.aliance;

import com.example.armtek_parcer_new.aliance.api.ApiAllProdGoods;
import com.example.armtek_parcer_new.aliance.domain.ProdUrl;
import com.example.armtek_parcer_new.aliance.facade.FacadeAlianceAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class baseTest {
    @Autowired
    private FacadeAlianceAPI facadeAPI;
    @Autowired
    private ApiAllProdGoods apiAllProdGoods;
    @Test
    void parse() throws IOException, InterruptedException {
        apiAllProdGoods.findAllProdGoodsUris(Map.of("KAMAZ", new ProdUrl("KAMAZ", "https://www.autoopt.ru/producers/436283"), "AVVA", new ProdUrl("AVVA", "https://www.autoopt.ru/producers/439552")));

    }
}
