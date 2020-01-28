package ru.cbr.rrror.service.db;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import ru.cbr.rrror.service.db.model.User;
import ru.cbr.rrror.service.db.repository.UserRepository;

@Slf4j
@Component
public class DatabaseLoader implements CommandLineRunner {

    private final UserRepository repository;

    @Autowired
    public DatabaseLoader(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public void run(String... strings) throws Exception {
        log.debug(">>> run database loader");
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("ppod-ekb", "doesn't matter",
                        AuthorityUtils.createAuthorityList("ROLE_AIB", "ROLE_USER")));

        this.repository.save(new User("frodo", "Frodo", "Baggins", "ring bearer"));
        this.repository.save(new User("bilbo", "Bilbo", "Baggins", "burglar"));
        this.repository.save(new User("gandalf", "Gandalf", "the Grey", "wizard"));
        this.repository.save(new User("samwise", "Samwise", "Gamgee", "gardener"));
        this.repository.save(new User("meriadoc", "Meriadoc", "Brandybuck", "pony rider"));
        this.repository.save(new User("peregrin", "Peregrin", "Took", "pipe smoker"));
        this.repository.save(new User("ppod-ekb", "Pppod-ekb", "Pppod-ekb", "Pppod-ekb developer"));

        log.debug("created " + this.repository.count() + " users");
    }
}

