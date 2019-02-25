package com.whb.cloud.demo.clouddemogood;

import com.whb.cloud.demo.common.security.EnabledClientSecurity;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnabledClientSecurity
@EnableEurekaClient
public class CloudDemoGoodApplication {

    public static void main(String[] args) {
        SpringApplication.run(CloudDemoGoodApplication.class, args);
    }
}
