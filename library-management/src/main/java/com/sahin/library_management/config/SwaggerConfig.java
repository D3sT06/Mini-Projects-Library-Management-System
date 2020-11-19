package com.sahin.library_management.config;

import com.fasterxml.classmate.ResolvedType;
import com.fasterxml.classmate.TypeResolver;
import com.sahin.library_management.swagger.model.*;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

    @Value("${app.security.url.password-login}")
    private String passwordLoginUrl;

    @Value("${app.security.url.facebook-login}")
    private String facebookLoginUrl;

    @Autowired
    private RequestMappingHandlerMapping mapping;

    @Autowired
    private TypeResolver typeResolver;

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

    private ApiKey apiKey() {
        return new ApiKey("bearer", "Authorization", "header");
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

    private ResolvedType[] resolvedTypes() {
        List<ResolvedType> resolvedTypes = new ArrayList<>();
        resolvedTypes.add(typeResolver.resolve(AuthorUpdateRequest.class));
        resolvedTypes.add(typeResolver.resolve(CategoryCreateRequest.class));
        resolvedTypes.add(typeResolver.resolve(CategoryUpdateRequest.class));
        resolvedTypes.add(typeResolver.resolve(BookItemCreateRequest.class));
        resolvedTypes.add(typeResolver.resolve(BookItemUpdateRequest.class));
        resolvedTypes.add(typeResolver.resolve(BookLoanUpdateRequest.class));
        resolvedTypes.add(typeResolver.resolve(BookReservationUpdateRequest.class));
        resolvedTypes.add(typeResolver.resolve(BookCreateRequest.class));
        resolvedTypes.add(typeResolver.resolve(BookUpdateRequest.class));
        resolvedTypes.add(typeResolver.resolve(RackCreateRequest.class));
        resolvedTypes.add(typeResolver.resolve(RackUpdateRequest.class));
        resolvedTypes.add(typeResolver.resolve(AccountCreateRequest.class));
        resolvedTypes.add(typeResolver.resolve(AccountUpdateRequest.class));
        resolvedTypes.add(typeResolver.resolve(BookFilterRequest.class));
        resolvedTypes.add(typeResolver.resolve(ModelWithOnlyId.class));

        ResolvedType[] array = new ResolvedType[resolvedTypes.size()];
        return resolvedTypes.toArray(array);
    }

    private void createBeans() {

        ConfigurableBeanFactory configurableBeanFactory = (ConfigurableBeanFactory) beanFactory;

        for (Map.Entry<String,Set<String>> entry : pathsForRoles.entrySet()) {
            String beanName = entry.getKey() + "Api";
            Docket bean = new Docket(DocumentationType.SWAGGER_2)
                    .select()
                    .paths(entry.getValue()::contains)
                    .apis(RequestHandlerSelectors.basePackage("com.sahin.library_management"))
                    .build()
                    .groupName(entry.getKey())
                    .apiInfo(metaData())
                    .pathMapping("/")
                    .additionalModels(typeResolver.resolve(AuthorCreateRequest.class), resolvedTypes())
                    .securitySchemes(Arrays.asList(apiKey()));

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

        for (String role : pathsForRoles.keySet()) {
            Set<String> paths = pathsForRoles.getOrDefault(role, new HashSet<>());
            paths.add(passwordLoginUrl);
            paths.add(facebookLoginUrl);
            this.pathsForRoles.put(role, paths);
        }
    }
}
