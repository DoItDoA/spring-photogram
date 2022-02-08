package com.cos.insta.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {
    public static List<String> tagParse(String tags) {
        if (tags.isBlank()) {
            return new ArrayList<>();
        }

        String[] temp = tags.split("[#, ]"); //# , 공백이면 분리
        List<String> list = new ArrayList<>();

        for (String t : temp) {
            if (!t.isBlank()) {
                list.add(t);
            }
        }

        return list
                .stream()
                .map(tag -> tag.startsWith("#") ? tag.substring(1) :tag)
                .collect(Collectors.toList());
    }
}
