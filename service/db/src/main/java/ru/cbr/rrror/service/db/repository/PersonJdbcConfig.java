package ru.cbr.rrror.service.db.repository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.AbstractJdbcConfiguration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;
import org.springframework.data.relational.core.mapping.event.BeforeSaveCallback;
import org.springframework.data.relational.core.mapping.event.RelationalEvent;
import ru.cbr.rrror.service.db.config.ApplicationConfig;
import ru.cbr.rrror.service.db.model.BaseEntity;
import ru.cbr.rrror.service.db.model.Person;

import javax.sql.DataSource;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

@Configuration
@EnableJdbcRepositories
@Slf4j
public class PersonJdbcConfig extends AbstractJdbcConfiguration {



    final AtomicLong id = new AtomicLong(100);

    /**
     * @return {@link ApplicationListener} for {@link RelationalEvent}s.
     */
    @Bean
    public ApplicationListener<?> loggingListener() {
        return (ApplicationListener<ApplicationEvent>) event -> {
            if (event instanceof RelationalEvent) {
                System.out.println(">>> Received an event: " + event);
            }
        };
    }

    /**
     * @return {@link BeforeSaveCallback} for {@link Person}.
     */
    @Bean
    public BeforeSaveCallback<BaseEntity> beforeSaveCallback(@Autowired DataSource ds) {
        return (entity, aggregateChange) -> {
            log.debug(">>> entity: " + entity.toString());



            entity.setId(id.incrementAndGet());

            return entity;
        };
    }


}
