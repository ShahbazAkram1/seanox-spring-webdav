package com.seanox.seanoxspringwebdav;

import com.seanox.webdav.WebDavFilter;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication(scanBasePackages = {"com.seanox.seanoxspringwebdav"})
public class SeanoxSpringWebdavApplication {

	public static void main(String[] args) {
		SpringApplication.run(SeanoxSpringWebdavApplication.class, args);
	}


	@Autowired
	private ApplicationConfiguration applicationConfiguration;

	@Bean
	public FilterRegistrationBean webDavFilterRegistration() {
		final FilterRegistrationBean registration = new FilterRegistrationBean();
		registration.setFilter(new WebDavFilter());
		registration.addUrlPatterns("/*");
		return registration;
	}

	@Data
	@Configuration
	@ConfigurationProperties(prefix="example")
	@EnableConfigurationProperties
	public static class ApplicationConfiguration {
		// Example of the configuration as an inner class in combination with lombok
	}
}
