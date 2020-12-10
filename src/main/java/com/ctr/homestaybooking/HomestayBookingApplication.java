package com.ctr.homestaybooking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties
public class HomestayBookingApplication {

    public static void main(String[] args) {
        SpringApplication.run(HomestayBookingApplication.class, args);
    }

}
