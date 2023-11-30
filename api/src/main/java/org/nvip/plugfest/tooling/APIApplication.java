package org.nvip.plugfest.tooling;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

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

     /**
     * CORS configuration
     */
    @Configuration
    public static class CorsConfig extends WebMvcConfigurationSupport {
        @Override
        public void addCorsMappings(CorsRegistry registry) {
            registry.addMapping("/**")
                    .allowedOrigins("http://localhost:4200") //TODO: Update accordingly to deployent, make config file
                    .allowedMethods("GET", "POST", "PUT", "DELETE")
                    .allowedHeaders("*")
                    .allowCredentials(true);
        }
    }
}