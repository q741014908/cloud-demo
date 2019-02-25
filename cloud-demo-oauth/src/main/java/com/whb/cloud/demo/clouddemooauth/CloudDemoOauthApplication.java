package com.whb.cloud.demo.clouddemooauth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CloudDemoOauthApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudDemoOauthApplication.class, args);
    }

}

