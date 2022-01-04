package io.github.vinogradoff.testdatabroker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.*;
import springfox.documentation.builders.*;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SpringFoxConfiguration {

    @Autowired
    BuildProperties buildProperties;

    @Bean
    public Docket api() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("io.github.vinogradoff"))
                .build()
                .useDefaultResponseMessages(false)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        var contact = new Contact("Alexei Vinogradov",
                "https://github.com/vinogradoff/test-data-broker",
                "alexei@vinogradoff.de");

        return new ApiInfoBuilder()
                .title("Test Data Broker")
                .version(buildProperties.getVersion().replace("'", ""))
                .description("API for managing test data")
                .license("License: MIT")
                .licenseUrl("https://github.com/vinogradoff/test-data-broker/blob/main/LICENSE")
                .contact(contact)
                .build();
    }
}
