package ru.cbr.rrror.service.db.repository;

import org.springframework.data.jdbc.repository.query.Query;
import ru.cbr.rrror.service.db.model.Person;

public interface ProcedureCrud<T> {

    T save(T t);

    Iterable<T> all();
}
