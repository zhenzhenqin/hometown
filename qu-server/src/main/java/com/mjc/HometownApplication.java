package com.mjc;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@SpringBootApplication(scanBasePackages = {"com.mjc"})
@EnableScheduling
@MapperScan("com.mjc.mapper")
public class HometownApplication {

    public static void main(String[] args) {
        SpringApplication.run(HometownApplication.class, args);
        log.info("server start~~~~");
    }
}
