package org.example.finance;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.JsonAdapter;

public class Purchase {
    private String date;
    private int sum;
    private String title;

    public Purchase(
            @JsonProperty("название") String title,
            @JsonProperty("дата") String date,
            @JsonProperty("сумма") int sum
    ){
        this.date = date;
        this.title = title;
        this.sum = sum;
    }

    public int getSum() {
        return sum;
    }

    public String getDate() {
        return date;
    }

    public String getTitle() {
        return title;
    }

    @Override
    public String toString() {
        return
                "=======================" + "\n" +
                        "покупка " + title + "\n" +
                        "дата " + date + "\n" +
                        "сумма " + sum + "\n";
    }
}
