package com.whb.cloud.demo.common.security;

import com.whb.cloud.demo.common.util.RequestUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;

public class UserAgentInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
        HttpHeaders headers = request.getHeaders();
        headers.add(RequestUtils.HEADER_AUTHORIZATION, RequestUtils.JWT_BEARER+RequestUtils.getCurrentRequestToken());
        return execution.execute(request, body);
    }
}
