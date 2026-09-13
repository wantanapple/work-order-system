package com.acme.workorder;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class WorkOrderBaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkOrderBaseApplication.class, args);
    }
}
