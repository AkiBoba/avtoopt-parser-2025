package com.example.armtek_parcer_new;

import com.example.armtek_parcer_new.api.ApiFiltersDesktop;
import com.example.armtek_parcer_new.api.ApiProducersParser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class test {
    @Autowired
    private ApiProducersParser parser;

    @Autowired
    private ApiFiltersDesktop filtersDesktop;

//    @Test
//    void parse() throws IOException, InterruptedException {
//        parser.parseProducersUrlsAsync();
//
//    }
    @Test
    void parse() throws IOException {
        filtersDesktop.getAllProducersUrls();

    }
}
