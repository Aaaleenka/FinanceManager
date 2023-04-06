package org.example.finance;

import org.example.finance.Categories;

import org.json.simple.JSONObject;

import java.io.*;
import java.util.*;

public class FinanceManager extends Categories {

    //создали мапу где ключ категория, значение - тоже мапа, в кот. ключ дата, значение - трата на дату
    Map<String, Map<String, Integer>> map = new HashMap<>();

    //мапа mapMaxCategory - хранит все траты на категорию за все время
    Map<String, Integer> mapMaxCategory = new HashMap<>();

    //конструктор по заполнению мап если нет файла .bin
    public FinanceManager(List<Categories> categories) {

        for (int i = 0; i < categories.size(); i++) {
            Map<String, Integer> mapPurchase = new HashMap<>();
            map.put(categories.get(i).category, mapPurchase);
            mapMaxCategory.put(categories.get(i).category, 0);
        }
    }

    //добавили покупку
    public void addPurchase(String category, String date, int sum) {

        if (map != null) {
            Map<String, Integer> getValue = map.get(category);
            int sumAll = 0;
            if (getValue.containsKey(date)) {
                sumAll = getValue.get(date);
            }
            getValue.put(date, sum + sumAll);
        }

        //получаем сумму, которая была уже по категории
        if (mapMaxCategory != null) {
            int sumAll = mapMaxCategory.get(category);
            mapMaxCategory.put(category, sum + sumAll);
        }
    }

    public void toPrint() {

        for (Map.Entry<String, Map<String, Integer>> pair : map.entrySet()) {
            String key = pair.getKey();
            System.out.println(key + ":");
            Map<String, Integer> mapNew = pair.getValue();

            for (Map.Entry<String, Integer> pair1 : mapNew.entrySet()) {
                String key1 = pair1.getKey();
                int value = pair1.getValue();
                System.out.println(key1 + ":" + value + ":");
            }
        }
    }

    public JSONObject maxCategory() {

        String maxCategory = null;
        int maxSum = 0;

        for (Map.Entry<String, Integer> pair : mapMaxCategory.entrySet()) {
            String key = pair.getKey();
            int value = pair.getValue();

            if (value > maxSum) {
                maxSum = value;
                maxCategory = key;
            }
        }

        JSONObject answerObject = new JSONObject();
        JSONObject answer1 = new JSONObject();
        answer1.put("sum", maxSum);
        answer1.put("category", maxCategory);
        answerObject.put("maxCategory", answer1);

        return answerObject;
    }

}
