package com.ams.restapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ams.restapi.config.AppConfig;

@SpringBootApplication
public class RestapiApplication extends SpringBootServletInitializer {
	@Value("${config.property.canvasUrl}")
    private String canvasUrl;
    @Value("${config.property.canvasUrl2}")
    private String canvasUrl2;
    @Value("${config.property.lti_launch_key}")
    private String ltiLaunchKey;
    @Value("${config.property.lti_launch_secret}")
    private String ltiLaunchSecret;


	public static void main(String[] args) {
		SpringApplication.run(RestapiApplication.class, args);
	}

	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(AppConfig.class);
    }

    @Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
                System.out.println("setting CORS config");
				registry.addMapping("/**")
                    .allowedOrigins("http://localhost:3000")
                    .allowedMethods("*")
                    .exposedHeaders("Total-Pages");
			}
		};
	}

}
