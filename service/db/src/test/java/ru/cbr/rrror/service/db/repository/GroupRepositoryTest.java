package ru.cbr.rrror.service.db.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.cbr.rrror.service.db.model.Group;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class GroupRepositoryTest {

    @Autowired GroupRepository repository;

    @Test
    public void deleteTest() {
        Group g = repository.findByGroupName("ALL");
        log.debug(">>> group: " + g);

        repository.delete(g);
        log.debug(">>> " + repository.findByGroupName("ALL"));
    }
}
