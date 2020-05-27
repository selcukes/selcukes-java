package io.github.selcukes.reports.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.core.logging.Logger;
import io.cucumber.core.logging.LoggerFactory;
import io.github.selcukes.core.exception.SelcukesException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import java.io.IOException;
import java.io.UnsupportedEncodingException;


public class SlackWebHookClient {

    private final Logger logger = LoggerFactory.getLogger(SlackWebHookClient.class);

    private final String webHookUrl;
    private final CloseableHttpClient httpClient;
    private final ObjectMapper mapper;
    private HttpEntity httpEntity;

    public SlackWebHookClient(String webHookUrl) {
        this.webHookUrl = webHookUrl;
        this.httpClient = createHttpClient();
        this.mapper = new ObjectMapper();
    }

    private CloseableHttpClient createHttpClient() {
        return HttpClients.createDefault();
    }

    public void shutdown() {
        if (httpClient != null) try {
            httpClient.close();
        } catch (Exception ignored) {
        }
    }

    public String post(SlackMessage payload) {
        String message = null;
        try {
            message = mapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new SelcukesException(e);
        }
        return execute(httpClient, webHookUrl, createHttpEntity(message));
    }

    private HttpEntity createHttpEntity(String message) {
        try {
            this.httpEntity = new StringEntity(message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this.httpEntity;
    }

    public String post(FileBody fileBody) {
        return execute(httpClient, webHookUrl, createMultipartEntityBuilder(fileBody));
    }

    private HttpEntity createMultipartEntityBuilder(FileBody fileBody) {
        this.httpEntity = MultipartEntityBuilder.create().addPart("file", fileBody).build();
        return this.httpEntity;
    }

    private String execute(CloseableHttpClient httpClient, String url, HttpEntity httpEntity) {
        logger.warn(()->"url : " + url);

        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(httpEntity);
            String retStr = httpClient.execute(httpPost, new StringResponseHandler());

            logger.warn(()->"return : " + retStr);

            return retStr;
        } catch (IOException e) {
            throw new SelcukesException(e);
        }

    }

}
