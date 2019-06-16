package com.cas.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("com.cas.demo.*")
@SpringBootApplication
public class CasClientCClientShiro {

    public static void main(String[] args) {
        SpringApplication.run(CasClientCClientShiro.class, args);
    }
}
