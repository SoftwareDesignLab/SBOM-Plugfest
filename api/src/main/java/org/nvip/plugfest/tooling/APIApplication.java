package org.nvip.plugfest.tooling;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class APIApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(APIApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args);
    }
}
