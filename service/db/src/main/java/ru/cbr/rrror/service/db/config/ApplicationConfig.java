package ru.cbr.rrror.service.db.config;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.cbr.rrror.service.db.repository.PersonRepository;

@Configuration
public class ApplicationConfig {

    @Bean
    public PersonNextId getPersonNextId(@Autowired PersonRepository repository) {
        return new PersonNextId(repository);
    }

    @AllArgsConstructor
    public static class PersonNextId {

        final PersonRepository repository;

        public Long next() {
            return repository.count() + 1;
        }
    }
}
