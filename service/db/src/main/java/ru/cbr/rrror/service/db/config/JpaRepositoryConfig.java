package ru.cbr.rrror.service.db.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories(basePackages = "ru.cbr.rrror.service.db.repository")
public class JpaRepositoryConfig {
}
