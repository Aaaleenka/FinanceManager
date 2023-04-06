package org.example.server;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.finance.FinanceManager;
import org.example.finance.Purchase;
import org.example.finance.Categories;
import org.json.simple.JSONObject;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static void main(String[] args) throws IOException {

        //получили список из категорий из файла
        File file = new File("categories.tsv");
        List<Categories> categories = loadFromTSVFile(file);
        categories.add(new Categories("", "другое"));

        File fileBin = new File("data.bin");
        FinanceManager financeManager;

        //создали пустую мапу из категорий и трат по месяцам или считали из файла
        //
        if (fileBin.isFile()) {
            financeManager = FinanceManager.loadFromBinFile(fileBin);
            financeManager.toPrint(); //проверка - вывод на экарн
        } else {
            financeManager = new FinanceManager(categories);
            financeManager.toPrint();
        }

        try (ServerSocket serverSocket = new ServerSocket(ServerConfig.PORT);) { // стартуем сервер один раз
            while (true) { // в цикле принимаем подключения
                try (
                        Socket socket = serverSocket.accept();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter writer = new PrintWriter(socket.getOutputStream());
                ) {
                    // получаем покупку json
                    String s = reader.readLine();

                    //преобразуем Json в объект
                    Purchase purchase = readAnswer(s);

                    String name = purchase.getTitle().toLowerCase();
                    String date = purchase.getDate();
                    int sum = purchase.getSum();

                    //узнаем к какой категории относится покупка
                    String nameCategory = getCategory(name, categories);

                    //добавляем новую покупку
                    financeManager.addPurchase(nameCategory, date, sum);

                    //записываем все в файл
                    File newFile = new File("data.bin");
                    financeManager.saveBin(newFile);

                    //максимальная трата
                    JSONObject answer = financeManager.maxCategory();
                    System.out.println(answer.toJSONString());
                    writer.println(answer.toJSONString());

                    //макс траты по году, месяцу, дате (на введенную дату)
                    List<JSONObject> list = financeManager.maxYearMonthDayCategory(date);

                    for (int i=0; i < list.size(); i++){
                        System.out.println(list.get(i).toJSONString());
                        writer.println(list.get(i).toJSONString());
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Не могу стартовать сервер");
            e.printStackTrace();
        }
    }

    static Purchase readAnswer(String body) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        Purchase purchase = mapper.readValue(body, Purchase.class);
        System.out.println(purchase.toString());
        return purchase;
    }

    public static List<Categories> loadFromTSVFile(File textFile) throws IOException {
        List<Categories> categoriesList = new ArrayList<>();
        try (BufferedReader input = new BufferedReader(new FileReader(textFile))) {
            String s = input.readLine();
            while (s != null) {
                String[] parts = s.split("\t");
                categoriesList.add(new Categories(parts[0], parts[1]));
                s = input.readLine();
            }
            return categoriesList;
        } catch (IOException ex) {
            System.out.println(ex.toString());
        }
        return categoriesList;
    }

    private static String getCategory(String name, List<Categories> categories){
        String nameCategory = "";
        int num = -1; //номер соответсвующей категории -1 если другое

        for (int i = 0; i < categories.size(); i++) {
            if (name.equals(categories.get(i).getName())) {
                num = i;
                break;
            }
        }
        //получаем ключ нашей категории
        if (num == -1) {
            nameCategory = "другое";
        } else {
            nameCategory = categories.get(num).getCategory();
        }
        return nameCategory;
    }

}