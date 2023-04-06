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

    //конструктор по заполнению мап из файла .bin
    public FinanceManager(Map<String, Map<String, Integer>> map, Map<String, Integer> mapMaxCategory) {
        this.map = map;
        this.mapMaxCategory = mapMaxCategory;
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

        JSONObject answerObject = createAnswer(maxSum, maxCategory, "maxCategory");
        return answerObject;
    }

    public JSONObject createAnswer(int maxSum, String maxCategory, String title ){

        JSONObject answerObject = new JSONObject();
        JSONObject answer = new JSONObject();
        answer.put("sum", maxSum);
        answer.put("category", maxCategory);
        answerObject.put(title, answer);
        return answerObject;
    }

    public void saveBin(File textFile) throws IOException {
        try (PrintWriter out = new PrintWriter(textFile);) {

            for (Map.Entry<String, Map<String, Integer>> pair : map.entrySet()) {
                String key = pair.getKey();
                out.println(key);
                Map<String, Integer> mapNew = pair.getValue();
                for (Map.Entry<String, Integer> pair1 : mapNew.entrySet()) {
                    String key1 = pair1.getKey();
                    int value = pair1.getValue();
                    out.println(":" + key1 + ":" + value);
                }
            }
            out.println("mapMaxCategory");
            for (Map.Entry<String, Integer> pair : mapMaxCategory.entrySet()) {
                String key = pair.getKey();
                int value = pair.getValue();
                out.println(key + ":" + value);
            }
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
    }

    public static FinanceManager loadFromBinFile(File textFile) throws IOException {

        Map<String, Map<String, Integer>> map = new HashMap<>();
        Map<String, Integer> mapMaxCategory = new HashMap<>();
        FinanceManager financeManager;

        try (BufferedReader input = new BufferedReader(new FileReader(textFile))) {

            String key = "";
            String s = input.readLine();
            Map<String, Integer> mapPurchase = new HashMap<>();

            while (s != null) {

                if (s.charAt(0) == ':') {
                    String[] parts = s.split(":");
                    for (int j = 1; j < parts.length - 1; j = j + 2) {
                        mapPurchase.put(parts[j], Integer.parseInt(parts[j + 1]));
                    }
                    map.put(key, mapPurchase);
                } else if (!s.equals("mapMaxCategory")) {
                    key = s;
                    mapPurchase = new HashMap<>();
                    map.put(key, mapPurchase);
                } else if (s.equals("mapMaxCategory")) {
                    s = input.readLine();
                    ;
                    while (s != null) {
                        String[] parts = s.split(":");
                        mapMaxCategory.put(parts[0], Integer.parseInt(parts[1]));
                        s = input.readLine();
                        ;
                    }
                }
                s = input.readLine();
                ;
            }
            financeManager = new FinanceManager(map, mapMaxCategory);
            return financeManager;
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        financeManager = new FinanceManager(map, mapMaxCategory);
        return financeManager;
    }

}
