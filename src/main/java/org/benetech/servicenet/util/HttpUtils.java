package org.benetech.servicenet.util;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public final class HttpUtils {

    private static final String AUTHORIZATION = "Authorization";
    private static final String CONTENT_TYPE = "Content-Type";
    private static final String APPLICATION_JSON = "application/json";

    public static Header[] getStandardHeaders(String token) {
        List<Header> result = new ArrayList<>();
        result.add(new BasicHeader(AUTHORIZATION, getBearerAuthValue(token)));
        result.add(new BasicHeader(CONTENT_TYPE, APPLICATION_JSON));
        return result.toArray(new Header[0]);
    }

    public static String executePOST(String urlString, String body, Header[] headers) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(urlString);

        httpPost.setEntity(new StringEntity(body));
        httpPost.setHeaders(headers);

        HttpResponse response = httpClient.execute(httpPost);
        return readContentAsString(response.getEntity());
    }

    public static String executeGET(String urlString, Header[] headers) throws IOException {
        HttpClient httpClient = HttpClients.createDefault();
        HttpGet httpGet = new HttpGet(urlString);

        httpGet.setHeaders(headers);

        HttpResponse response = httpClient.execute(httpGet);
        return readContentAsString(response.getEntity());
    }

    private static String readContentAsString(HttpEntity responseEntity) throws IOException {
        StringBuilder content = new StringBuilder();
        if (responseEntity != null) {
            try (BufferedReader in = new BufferedReader(
                new InputStreamReader(responseEntity.getContent()))) {
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
            }
        }
        return content.toString();
    }

    private static String getBearerAuthValue(String token) {
        return "Bearer " + token;
    }

    private HttpUtils() {
    }
}
