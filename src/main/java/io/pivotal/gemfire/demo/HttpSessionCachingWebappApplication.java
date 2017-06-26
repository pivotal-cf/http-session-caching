package io.pivotal.gemfire.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

@SpringBootApplication
public class HttpSessionCachingWebappApplication extends SpringBootServletInitializer {

	@Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(HttpSessionCachingWebappApplication.class);
    }

	public static void main(String[] args) {
		SpringApplication.run(HttpSessionCachingWebappApplication.class, args);
	}

}
