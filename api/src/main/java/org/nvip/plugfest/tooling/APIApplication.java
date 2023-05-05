package org.nvip.plugfest.tooling;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;

/**
 * This class contains the main function which runs the API as a spring boot application
 * @author Justin Jantzi
 */
@SpringBootApplication
public class APIApplication {
    public static void main(String[] args) {
        new SpringApplicationBuilder(APIApplication.class)
                .web(WebApplicationType.SERVLET) //This is required to prevent "No valid webserver" error.
                .run(args);
    }

    /***
     * Overwrites the default max post size default of 2MB
     */
    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomcatCustomizer() {
        return (factory) -> {
            factory.addConnectorCustomizers((connector) -> {
                connector.setMaxPostSize(Integer.MAX_VALUE); //About 2GB
            });
        };
    }
}