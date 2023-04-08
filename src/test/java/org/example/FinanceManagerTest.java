package org.example;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.example.finance.Categories;
import org.example.finance.FinanceManager;
import org.example.finance.Purchase;
import org.json.simple.JSONObject;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.*;

import static org.example.server.Main.readAnswer;
import static org.mockito.Mockito.verify;


public class FinanceManagerTest {

    //проверить коррекектно ли считывается JSON
    @Test
    public void jsonCorrect() throws JsonProcessingException {

        String expectedTitle = "акции";
        String expectedData = "2022.01.09";
        int expectedSum = 9000;

        String s = "{\"title\": \"акции\", \"date\": \"2022.01.09\", \"sum\": 9000}";
        Purchase actual = readAnswer(s);

        Assertions.assertEquals(actual.getTitle(), expectedTitle);
        Assertions.assertEquals(actual.getDate(), expectedData);
        Assertions.assertEquals(actual.getSum(), expectedSum);
    }

    @Test
    //проверить что действительно в мапе
    public void maxCategoryCorrect(){

        List<Categories> list = new ArrayList<>();
        list.add(new Categories("булка", "еда"));
        list.add(new Categories("тапки", "одежда"));
        list.add(new Categories("акции", "финансы"));

        FinanceManager  finManager = new FinanceManager(list);
        finManager.addPurchase("одежда", "2022.10.08", 3000);
        finManager.addPurchase("финансы", "2021.05.08", 1000);
        finManager.addPurchase("еда", "2022.12.23", 5000);
        finManager.addPurchase("еда", "2022.06.16", 5000);

        JSONObject expected = new JSONObject();
        JSONObject answer = new JSONObject();
        answer.put("sum", 10000);
        answer.put("category", "еда");
        expected.put("maxCategory", answer);

        JSONObject actual = finManager.maxCategory();
        
        Assertions.assertEquals(expected, actual);
    }

}