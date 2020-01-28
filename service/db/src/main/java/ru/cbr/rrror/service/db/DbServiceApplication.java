package ru.cbr.rrror.service.db;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@EnableResourceServer
@SpringBootApplication
public class DbServiceApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .main(DbServiceApplication.class)
                .sources(DbServiceApplication.class)
                .run(args);
    }

    @Slf4j
    @AllArgsConstructor
    @RestController
    static class WhoamiController {
        @GetMapping("/hello")
        public String whoami(Principal principal) {
            return principal.getName();
        }
    }
}
