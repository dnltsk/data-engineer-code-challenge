package org.dnltsk.d2d.challenge.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfiguration {

    @Bean
    public Docket forecastEndpointsDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
            .apiInfo(
                new ApiInfoBuilder().description("# data-engineer-code-challenge 🚀").build()
            )
            .select().apis(
                RequestHandlerSelectors.basePackage("org.dnltsk.d2d.challenge")
            ).build();
    }

    @Bean
    public WebMvcConfigurerAdapter forwardIndexPagesToSwaggerDoc() {
        return new WebMvcConfigurerAdapter() {
            public void addViewControllers(ViewControllerRegistry registry) {
                registry.addViewController("/").setViewName("redirect:/swagger-ui.html");
                registry.addViewController("/index.html").setViewName("redirect:/swagger-ui.html");
            }
        };
    }

}
