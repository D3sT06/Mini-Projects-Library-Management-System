package com.sahin.library_management.config;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.BasicAuth;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.annotation.PostConstruct;
import java.util.*;

@Configuration
@Profile("swagger")
@EnableSwagger2
public class SwaggerConfig implements BeanFactoryAware {

    private static final String ROLE_PREFIX = "ROLE_";

    @Autowired
    private RequestMappingHandlerMapping mapping;

    private BeanFactory beanFactory;
    private Map<String, Set<String>> pathsForRoles;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @PostConstruct
    void init() {
        setPathsForRoles();
        createBeans();
    }

    private BasicAuth basicAuth() {
        return new BasicAuth("basicAuth");
    }

    private ApiInfo metaData() {
        Contact contact = new Contact("Serkan Şahin", "http://sahin.com", "serkansahin27@gmail.com");

        return new ApiInfoBuilder()
                .contact(contact)
                .title("Library Management Application")
                .description("Manage your libraries easily")
                .version("1.0")
                .build();
    }



    private void createBeans() {

        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;

        for (Map.Entry<String,Set<String>> entry : pathsForRoles.entrySet()) {
            String beanName = entry.getKey() + "Api";
            Docket bean = new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .paths(entry.getValue()::contains)
                    .apis(RequestHandlerSelectors.basePackage("com.sahin.library_management.controller"))
                    .build()
                    .groupName(entry.getKey())
                    .apiInfo(metaData())
                    .pathMapping("/")
                    .securitySchemes(Arrays.asList(basicAuth()));

            configurableBeanFactory.registerSingleton(beanName, bean);
        }
    }

    private void setPathsForRoles() {
        this.pathsForRoles = new HashMap<>();

        Map<RequestMappingInfo, HandlerMethod> handlerMethodMap = mapping.getHandlerMethods();
        handlerMethodMap.forEach((mappingInfo, handlerMethod) -> {
            PreAuthorize preAuthorize = handlerMethod.getMethod().getAnnotation(PreAuthorize.class);

            if (preAuthorize != null && preAuthorize.value().contains(ROLE_PREFIX)) {
                String[] values = preAuthorize.value().split("[\\W]");
                for (String val : values) {
                    if (val.contains(ROLE_PREFIX)) {
                        String role = val.replace(ROLE_PREFIX, "");
                        Set<String> paths = pathsForRoles.getOrDefault(role, new HashSet<>());
                        paths.addAll(mappingInfo.getPatternsCondition().getPatterns());
                        pathsForRoles.put(role, paths);
                    }
                }
            }
        });
    }
}
