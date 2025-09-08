package com.example.armtek_parcer_new.aliance.util;

import com.example.armtek_parcer_new.domain.FileLine;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class ProducersAlianceFilter {
    public Map<String, String> filter(Map<String, String> producersUrls, List<FileLine> lines) {
        List<String> noCheckedLines = new ArrayList<>();
        List<String> cleanedLines = lines.stream().map(line -> TextAlianceCleaner.cleanText(line.getName())).toList();
        Map<String, String> producersFilteredUrls = new HashMap<>();
        producersUrls.keySet().stream().map(TextAlianceCleaner::cleanText).filter(cleanedLines::contains).forEach(key -> producersFilteredUrls.put(key, producersUrls.get(key)));
        lines.stream().map(line -> TextAlianceCleaner.cleanText(line.getName())).filter(line -> !producersFilteredUrls.keySet().contains(line)).forEach(noCheckedLines::add);
        return producersFilteredUrls;
    }
}
