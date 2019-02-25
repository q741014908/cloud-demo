package com.whb.cloud.demo.clouddemoorder;

import com.whb.cloud.demo.common.security.EnabledClientSecurity;
import com.whb.cloud.demo.common.security.UserAgentInterceptor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Collections;

@SpringBootApplication
@EnabledClientSecurity
@EnableEurekaClient
public class CloudDemoOrderApplication {
    public static void main(String[] args) {
        SpringApplication.run(CloudDemoOrderApplication.class, args);
    }

    @Bean
    @Primary
    @LoadBalanced
    public RestTemplate restTemplate(){
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        restTemplate.setInterceptors(Collections.singletonList(new UserAgentInterceptor()));
        return restTemplate;
    }
}
