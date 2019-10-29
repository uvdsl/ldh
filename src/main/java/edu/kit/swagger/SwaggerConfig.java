package edu.kit.swagger;

import com.google.common.base.Predicates;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@ComponentScan(basePackages = { "edu.kit" })
@Configuration
public class SwaggerConfig {

	private static final String SWAGGER_API_VERSION = "0.1";
	private static final String TITLE = "LD-Hasher REST API";
	private static final String DESCRIPTION = "RESTful API for LinkedData-Hasher-Server.";

	private ApiInfo apiInfo() {
		return new ApiInfoBuilder().title(TITLE).description(DESCRIPTION).version(SWAGGER_API_VERSION).build();
	}

	@Bean
	public Docket apiLDH() {
		return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo()).select()
				.apis(RequestHandlerSelectors.basePackage("edu.kit")).paths(PathSelectors.any())
				.paths(Predicates.not(PathSelectors.regex("/swagger"))).build();
	}
}