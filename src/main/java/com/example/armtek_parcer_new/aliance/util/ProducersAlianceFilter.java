package com.example.armtek_parcer_new.aliance.util;

import com.example.armtek_parcer_new.aliance.domain.Owner;
import com.example.armtek_parcer_new.aliance.domain.ProdUrl;
import com.example.armtek_parcer_new.aliance.domain.Producer;
import com.example.armtek_parcer_new.aliance.repository.OwnerJpaRepository;
import com.example.armtek_parcer_new.aliance.domain.FileLine;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
    Проверяем какие производители сайта соответствуют производителям из переданного списка.
 Возвращаем соответсвующий списку производителей.
 При необходимости, сохраняем в БД.
 */

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProducersAlianceFilter {
    @Value("${sparox.producer.id}")
    String sparoxProducerId;
    @Value("${sparox.producer.name}")
    String sparoxProducerName;
    @Value("${sparox.producer.url}")
    String sparoxProducerUrl;
    @Value("${avtoopt.producer.id}")
    String avtooptProducerId;
    @Value("${avtoopt.producer.name}")
    String avtooptProducerName;
    @Value("${avtoopt.producer.url}")
    String avtooptProducerUrl;
    final OwnerJpaRepository repository;
    final TextAlianceCleaner textCleaner;
    public Map<String, ProdUrl> filter(Map<String, ProdUrl> producersUrls, List<FileLine> sparoxLines) {

        Owner sparoxOwner = new Owner(Long.parseLong(sparoxProducerId), sparoxProducerName, sparoxProducerUrl, new ArrayList<>());
        Owner avtooptOwner = new Owner(Long.parseLong(avtooptProducerId), avtooptProducerName, avtooptProducerUrl, new ArrayList<>());

        List<String> cleanedLines = sparoxLines.stream().map(line -> textCleaner.cleanText(line.getName())).toList();

        Map<String, Producer> avtooptProducersMap = new HashMap<>();

        List<Producer> sparoxProducers = sparoxLines.stream()
                .map(line -> Producer.builder()
                        .cleanName(textCleaner.cleanText(line.getName()))
                        .name(line.getName())
                        .owner(sparoxOwner)
                        .checked(false)
                        .url(null)
                        .build()).toList();

        Map<String, ProdUrl> producersFilteredUrls = new HashMap<>();

        /*
        Итерируемся по ключам producersUrls, заполняем одновременно новый словарь Map<String, Producer> avtooptProducersMap
        и проверяем что очищенный ключ-призводитель соответствует списку sparox, соответсвующие кладем в словарь producersFilteredUrls
         */
        producersUrls.keySet().stream().peek(key -> avtooptProducersMap.put(key, Producer.builder()
                        .cleanName(textCleaner.cleanText(key))
                        .name(producersUrls.get(key).getProducer())
                        .owner(avtooptOwner)
                        .checked(false)
                        .url(producersUrls.get(key).getUrl())
                        .build()))
                .filter(cleanedLines::contains)
                .forEach(key -> {
                    producersFilteredUrls.put(key,
                            new ProdUrl(producersUrls.get(key).getProducer(), producersUrls.get(key).getUrl()));
                    avtooptProducersMap.get(key).setChecked(true);

                });


        // Обновляем checked статус sparox производителей
//        sparoxProducers.stream()
//                .filter(producer -> producersFilteredUrls.containsKey(producer.getCleanName()))
//                .forEach(producer -> producer.setChecked(true));
//
//        sparoxOwner.setProducer(sparoxProducers);
//
//        repository.save(sparoxOwner);
//
//        List<Producer> avtooptProducersList = avtooptProducersMap.values().stream().toList();
//
//        avtooptOwner.setProducer(avtooptProducersList);
//
//        repository.save(avtooptOwner);

        return producersFilteredUrls;
    }
}
