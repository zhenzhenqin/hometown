package com.mjc;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@Slf4j
@SpringBootApplication
public class HometownApplication {

    public static void main(String[] args) {
        SpringApplication.run(HometownApplication.class, args);
        log.info("server start~~~~");
    }

}
