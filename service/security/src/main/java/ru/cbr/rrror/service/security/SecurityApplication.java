package ru.cbr.rrror.service.security;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class SecurityApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .main(SecurityApplication.class)
                .sources(SecurityApplication.class)
                .run(args);
    }

    @RestController
    static class HelloController {

        @GetMapping("/hello")
        public String sayHello() {
            return "hello";
        }

    }
}
