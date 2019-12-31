package io.github.selcukes.core.runtime;

import io.github.selcukes.core.exception.SelcukesException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpResponseException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

class StringResponseHandler implements ResponseHandler<String> {

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
