package ru.cbr.rrror.service.db.repository;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import ru.cbr.rrror.service.db.model.Person;

public interface PersonRepository extends CrudRepository<Person, Long>, ProcedureCrud<Person> {

    @Override
    @Query("select * from user where user.id > 5")
    Iterable<Person> findAll();
}
