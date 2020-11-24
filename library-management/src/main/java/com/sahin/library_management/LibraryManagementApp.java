package com.sahin.library_management;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
public class LibraryManagementApp {
    public static void main(String[] args) {
        SpringApplication.run(LibraryManagementApp.class);
    }
}
