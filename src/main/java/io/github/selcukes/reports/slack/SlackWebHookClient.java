package io.github.selcukes.reports.slack;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.selcukes.core.exception.SelcukesException;
import io.github.selcukes.exception.SelcukesException;
import io.github.selcukes.helper.ExceptionHelper;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Logger;

public class SlackWebHookClient {

    private final Logger logger = Logger.getLogger(SlackWebHookClient.class.getName());

    private final String webHookUrl;
    private final CloseableHttpClient httpClient;
    private final ObjectMapper mapper;
    private HttpEntity httpEntity;

    public SlackWebHookClient(String webHookUrl) {
        this.webHookUrl = webHookUrl;
        this.httpClient=createHttpClient();
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
        return execute(httpClient,webHookUrl,createHttpEntity(message));
    }
    private HttpEntity createHttpEntity(String message)
    {
        try {
            this.httpEntity=new StringEntity(message);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return this.httpEntity;
    }
    public String post(FileBody fileBody)
    {
        return execute(httpClient,webHookUrl,createMultipartEntityBuilder(fileBody));
    }
    private HttpEntity createMultipartEntityBuilder(FileBody fileBody)
    {
        this.httpEntity= MultipartEntityBuilder.create().addPart("file", fileBody).build();
        return this.httpEntity;
    }
    private String execute(CloseableHttpClient httpClient, String url, HttpEntity httpEntity) {
        logger.warning("url : " + url);

        try {
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(httpEntity);
            String retStr = httpClient.execute(httpPost, new StringResponseHandler());

            logger.warning("return : " + retStr);

            return retStr;
        } catch (IOException e) {
            throw new SelcukesException(e);
        }

    }

    private class StringResponseHandler implements ResponseHandler<String> {

        private static final String RETRY_AFTER_HEADER = "Retry-After";

        @Override
        public String handleResponse(HttpResponse response) throws IOException {
            final StatusLine statusLine = response.getStatusLine();
            int status = statusLine.getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                return entity != null ? EntityUtils.toString(entity) : null;
            } else if (status == 429) {
                throw new SelcukesException(String.valueOf(Long.valueOf(response.getFirstHeader(RETRY_AFTER_HEADER).getValue())));
            } else {
                throw new HttpResponseException(statusLine.getStatusCode(), statusLine.getReasonPhrase());
            }
        }

    }

}
