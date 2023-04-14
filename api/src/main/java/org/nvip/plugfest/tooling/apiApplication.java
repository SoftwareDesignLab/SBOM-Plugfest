package org.nvip.plugfest.tooling;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class apiApplication{
    public static void main(String[] args) {
        new SpringApplicationBuilder(apiApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
