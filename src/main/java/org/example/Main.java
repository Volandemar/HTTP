package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Main {
    public static final String REMOTE_SERVICE_URL =
            "https://raw.githubusercontent.com/netology-code/jd-homeworks/master/http/task1/cats";

    public static void main(String[] args) {
        try (CloseableHttpClient httpClient = HttpClientBuilder.create()
                .setDefaultRequestConfig(RequestConfig.custom()
                        .setConnectTimeout(5000)    // максимальное время ожидание подключения к серверу
                        .setSocketTimeout(30000)    // максимальное время ожидания получения данных
                        .setRedirectsEnabled(false) // возможность следовать редиректу в ответе
                        .build())
                .build();) {
            HttpGet request = new HttpGet(REMOTE_SERVICE_URL);
            try (CloseableHttpResponse httpResponse = httpClient.execute(request)) {
                String body = new String(httpResponse.getEntity().getContent().readAllBytes(), StandardCharsets.UTF_8);
                System.out.println(body);

                ObjectMapper mapper = new ObjectMapper();
                List<Cat> catList = mapper.readValue(httpResponse.getEntity().getContent(), new TypeReference<List<Cat>>() {
                });
                for (int i = 0; i < catList.size(); i++) {
                    if (catList.get(i).getUpvotes() > 0) {
                        System.out.println(catList.get(i).toString());
                    }
                }

            }
        } catch (IOException e) {
            e.getMessage();
        }

    }
}