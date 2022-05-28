package com.rick.ftpal;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// @SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@SpringBootApplication
public class FtPalApplication {

    public static void main(String[] args) {
        SpringApplication.run(FtPalApplication.class, args);
    }

}
