/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ivietech.demo.utils;

import java.text.Normalizer;
import java.util.regex.Pattern;
import org.springframework.stereotype.Service;

/**
 *
 * @author HoangMinh
 */
@Service
public class DataProcessing {

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("");
    }

}
