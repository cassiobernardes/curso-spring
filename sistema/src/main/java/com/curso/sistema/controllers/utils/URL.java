package com.curso.sistema.controllers.utils;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class URL {

    public static String decodeParam(String s){
        return URLDecoder.decode(s, StandardCharsets.UTF_8);
    }

    public static List<Long> decodeIntList(String s){
        String[] vet = s.split(",");
        List<Long> ids = new ArrayList<>();

        for(String num : vet){
            ids.add(Long.valueOf(num));
        }

        return ids;
    }
}
