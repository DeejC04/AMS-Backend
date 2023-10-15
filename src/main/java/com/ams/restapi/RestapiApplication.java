package com.ams.restapi;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.ams.restapi.config.AppConfig;
import com.ams.restapi.service.ConfigServiceImpl;

import edu.ksu.lti.launch.service.ConfigService;

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
                    .allowedOrigins("http://localhost:3000", "https://canvas.ams-lti.com", "https://localhost")
                    .allowedMethods("*")
                    .exposedHeaders("Total-Pages");
			}
		};
	}

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        DefaultHttpFirewall firewall = new DefaultHttpFirewall();
        return (web) -> web.httpFirewall(firewall);
    }

    /**
     * Initializing static values here is unusual. We are doing it so we don't
     * have to depend on a database or other config source in this example
     * project. Usually you would provide a bean (probably in your AppConfig class)
     * that is able to look up values from a database or application properties.
     * @return ConfigService to look up config items.
     */
    @Bean
    public ConfigService getConfigService() {
        ConfigServiceImpl configService = new ConfigServiceImpl();
        configService.setConfigValue("canvas_url", canvasUrl);
        configService.setConfigValue("canvas_url_2", canvasUrl2);
        configService.setConfigValue("lti_launch_key", ltiLaunchKey);
        configService.setConfigValue("lti_launch_secret", ltiLaunchSecret);
        return configService;
    }

}
