package org.example.server;

import org.json.simple.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

import static org.example.server.ServerConfig.HOST;
import static org.example.server.ServerConfig.PORT;

public class Client {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        JSONObject sampleObject = new JSONObject();
        sampleObject.put("title", "булка");
        sampleObject.put("date", "2022.01.08");
        sampleObject.put("sum", 10000);

        try (Socket socket = new Socket(HOST, PORT);
             PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
             BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()))
        ) {

            //передаем на сервер тестовый json
            System.out.println(sampleObject.toJSONString());
            writer.println(sampleObject);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

    }
}
