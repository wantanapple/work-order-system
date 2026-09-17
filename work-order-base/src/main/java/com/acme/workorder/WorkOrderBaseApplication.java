package com.acme.workorder;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.acme.workorder.base.mapper")
public class WorkOrderBaseApplication {
    public static void main(String[] args) {
        SpringApplication.run(WorkOrderBaseApplication.class, args);
    }
}
