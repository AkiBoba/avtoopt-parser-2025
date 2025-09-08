package com.example.armtek_parcer_new.facade;

import com.example.armtek_parcer_new.aliance.domain.ProdUrl;
import com.example.armtek_parcer_new.api.ApiAccessParser;
import com.example.armtek_parcer_new.api.ApiProducersParser;
import com.example.armtek_parcer_new.domain.ArmGoodInfo;
import com.example.armtek_parcer_new.domain.FileLine;
import com.example.armtek_parcer_new.util.ProducersFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class FacadeAPI {
    private final ApiProducersParser producersParser;
    private final ProducersFilter producersFilter;
    public void getWorks(List<FileLine> lines) throws IOException, InterruptedException {
//        Map<String, String> producersUrls = producersParser.parseProducersUrlsAsync();
//        Map<String, ProdUrl> producersCleanedUrls = producersFilter.filter(producersUrls, lines);
        System.out.println();

    }

}
