package org.nvip.plugfest.tooling;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

/**
 * This class contains the main function which runs the API as a spring boot application
 */
@SpringBootApplication
public class APIApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(APIApplication.class)
                .web(WebApplicationType.SERVLET) //This is required to prevent "No valid webserver" error.
                .run(args);
    }
}
