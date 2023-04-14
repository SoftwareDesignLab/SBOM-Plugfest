package org.nvip.plugfest.tooling;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;

@SpringBootApplication
public class apiApplication{
    public static void main(String[] args) {
        SpringApplication.run(apiController.class, args);
    }
}
