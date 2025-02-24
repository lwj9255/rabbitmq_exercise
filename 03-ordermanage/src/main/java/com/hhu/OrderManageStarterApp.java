package com.hhu;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@MapperScan("com.hhu.mapper")
public class OrderManageStarterApp {
    public static void main(String[] args) {
        SpringApplication.run(OrderManageStarterApp.class,args);
    }
}
