package ru.cbr.rrror.service.db.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.cbr.rrror.service.db.model.Group2;
import ru.cbr.rrror.service.db.model.User;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class Group2Test {

    @Autowired Group2Repository repository;
    @Autowired UserRepository userRepository;

    @Test
    public void FindAllTest(){
        repository.findAll().forEach(g->log.debug(">>> g: " + g.toString()));
    }

    @Test
    public void deleteTest() {

        User frodo = userRepository.findByLogin("frodo");
        log.debug(">>> frodo before group delete: " + frodo);

        Group2 g = repository.findByGroupName("ALL");
        repository.delete(g);

        log.debug(">>> all: " + repository.findByGroupName("ALL"));

        log.debug(">>> frodo after group delete: " + frodo);
        log.debug(">>> get frodo: " + userRepository.findByLogin("frodo"));
        userRepository.refresh(frodo);

        log.debug(">>> frodo after refresh: " + frodo);

    }
}
