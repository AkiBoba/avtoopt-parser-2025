package com.example.armtek_parcer_new.aliance.util;

import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class TextAlianceCleaner {
    public static String cleanText(String input) {
        if (input == null) return "";

        return input.chars()
                .filter(Character::isLetter)
                .mapToObj(c -> String.valueOf((char) Character.toUpperCase(c)))
                .collect(Collectors.joining());
    }
}
