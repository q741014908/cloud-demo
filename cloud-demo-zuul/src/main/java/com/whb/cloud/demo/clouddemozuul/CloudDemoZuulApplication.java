package com.whb.cloud.demo.clouddemozuul;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

@SpringBootApplication
@EnableDiscoveryClient
@EnableZuulProxy
public class CloudDemoZuulApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudDemoZuulApplication.class, args);
    }
}
