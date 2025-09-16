package com.sstinternaltools.sstinternal_tools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SstInternalToolsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SstInternalToolsApplication.class, args);
    }

}
