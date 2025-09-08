package com.example.armtek_parcer_new.util;

import com.example.armtek_parcer_new.aliance.domain.ProdUrl;
import com.example.armtek_parcer_new.domain.FileLine;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class ProducersFilter {
    private final TextCleaner textCleaner;
    public Map<String, ProdUrl> filter(Map<String, ProdUrl> producersUrls, List<FileLine> lines) {
        List<String> noCheckedLines = new ArrayList<>();
        List<String> cleanedLines = lines.stream().map(line -> textCleaner.cleanText(line.getName())).toList();
        Map<String, ProdUrl> producersFilteredUrls = new HashMap<>();
        producersUrls.keySet().stream().filter(cleanedLines::contains).forEach(key -> producersFilteredUrls.put(key, new ProdUrl(producersUrls.get(key).getProducer(), producersUrls.get(key).getUrl())));
        lines.stream().map(line -> textCleaner.cleanText(line.getName())).filter(line -> !producersFilteredUrls.keySet().contains(line)).forEach(noCheckedLines::add);
        return producersFilteredUrls;
    }
}
