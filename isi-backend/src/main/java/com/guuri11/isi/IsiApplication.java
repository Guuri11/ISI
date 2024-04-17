package com.guuri11.isi;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.modelmapper.ModelMapper;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@OpenAPIDefinition(info = @Info(title = "ISI Backend service", version = "1.0", description = "Documentation ISI Management APIs v1.0"))
public class IsiApplication {

	public static void main(String[] args) {
		// This is made due to allow awt for read clipboard
		ApplicationContext springApplicationBuilder = new SpringApplicationBuilder(IsiApplication.class)
				.web(WebApplicationType.SERVLET)
				.headless(false)
				.run(args);
	}

	@Bean
	public ModelMapper modelMapper() {

		return new ModelMapper();
	}
}
