package ru.cbr.rrror.service.db.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureJdbc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import ru.cbr.rrror.service.db.model.Person;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureJdbc
@Slf4j
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class PersonRepositoryTest {
    @Autowired PersonRepository repository;
    @Test
    public void findAllTest() {
        log.debug(">>> run test");
        repository.findAll().forEach(person -> {
            log.debug(">>> person: " + person.toString());
        });
    }

    @Test
    public void getAllTest() {
            repository.all().forEach((p) -> log.debug(">>> p: " + p.toString()));
    }

    @Test
    public void insertTest() {
        repository.save(Person.of("test1", "test2", "test3", "test4"));

    }
}
