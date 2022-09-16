package com.sungchul.camping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling   //스케쥴러
@SpringBootApplication
public class CampingApplication {

    public static void main(String[] args) {
        SpringApplication.run(CampingApplication.class, args);
    }

}
