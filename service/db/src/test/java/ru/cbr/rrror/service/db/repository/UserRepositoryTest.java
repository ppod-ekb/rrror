package ru.cbr.rrror.service.db.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.cbr.rrror.service.db.model.User;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class UserRepositoryTest {

    @Autowired UserRepository userRepository;

    @Test
    public void findAllTest() {
        Iterable<User> users = userRepository.findAll();
        users.forEach((c) -> log.debug(">>> " + c.toString()));

        log.debug(">>> count: " + userRepository.count());
    }

    @Test
    public void deleteTest() {
        Iterable<User> users = userRepository.findAll();
        for (User u : users) {
            userRepository.delete(u);
            log.debug(">>> " + userRepository.count());
        }


    }

}
