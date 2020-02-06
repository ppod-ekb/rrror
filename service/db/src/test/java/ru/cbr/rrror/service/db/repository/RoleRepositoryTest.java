package ru.cbr.rrror.service.db.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class RoleRepositoryTest {
    @Autowired
    RoleRepository repository;
    @Test
    public void test() {
        repository.findAll().forEach((r) -> log.debug(">>> role: " + r.toString()));
    }
}
