package com.dubbo.simulate.framework.protocol.http;

import com.alibaba.fastjson.JSONObject;
import com.dubbo.simulate.framework.Invocation;
import org.apache.dubbo.common.utils.IOUtils;

import java.io.*;
import java.net.*;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;

/**
 * @Date: 2022/10/24
 */
public class HttpClient {

    public String send(String hostname, int port, Invocation invocation) {
        HttpRequest request = null;
        try {
            //URL url = new URL("http", hostname, port, "/");
            //HttpURLConnection con = (HttpURLConnection) url.openConnection();
            //
            //con.setRequestMethod("GET");
            ////con.setRequestMethod("POST");
            //con.setDoOutput(true);
            //
            //ObjectOutputStream oos = new ObjectOutputStream(con.getOutputStream());
            //oos.writeObject(invocation);
            //oos.flush();
            //oos.close();
            //
            //InputStream inputStream = con.getInputStream();
            //String result = new BufferedReader(new InputStreamReader(inputStream))
            //        .lines().collect(Collectors.joining(System.lineSeparator()));
            //return result;

            request = HttpRequest.newBuilder()
                    .uri(new URI("http", null, hostname, port, "/", null, null))
                    .POST(HttpRequest.BodyPublishers.ofString(JSONObject.toJSONString(invocation)))
                    .build();
            java.net.http.HttpClient client = java.net.http.HttpClient.newHttpClient();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
