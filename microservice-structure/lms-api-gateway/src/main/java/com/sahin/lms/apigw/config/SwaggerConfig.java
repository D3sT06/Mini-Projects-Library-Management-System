package com.sahin.lms.apigw.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Configuration
@Profile("swagger")
@EnableSwagger2
public class SwaggerConfig implements BeanFactoryAware {

    @Value("${app.security.url.password-login}")
    private String passwordLoginUrl;

    @Value("${app.security.url.facebook-login}")
    private String facebookLoginUrl;

    private BeanFactory beanFactory;
    private Map<String, Set<String>> pathsForRoles;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @PostConstruct
    void init() {
        createBeans();
    }

    private ApiInfo metaData() {
        Contact contact = new Contact("Serkan Şahin", "http://sahin.com", "serkansahin27@gmail.com");

        return new ApiInfoBuilder()
                .contact(contact)
                .title("LMS Api Gateway")
                .description("Main gateway for all apis")
                .version("1.0")
                .build();
    }

    private void createBeans() {

        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;
        String[] roles = {"LIBRARIAN", "MEMBER"};
        this.pathsForRoles = new HashMap<>();

        for (String role : roles) {
            Set<String> paths = pathsForRoles.getOrDefault(role, new HashSet<>());
            paths.add(passwordLoginUrl);
            paths.add(facebookLoginUrl);
            this.pathsForRoles.put(role, paths);
        }

        for (Map.Entry<String,Set<String>> entry : pathsForRoles.entrySet()) {
            String beanName = entry.getKey() + "Api";
            Docket bean = new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .paths(entry.getValue()::contains)
                    .apis(RequestHandlerSelectors.basePackage("com.sahin.lms.apigw"))
                    .build()
                    .groupName(entry.getKey())
                    .apiInfo(metaData())
                    .pathMapping("/");

            configurableBeanFactory.registerSingleton(beanName, bean);
        }
    }
}
