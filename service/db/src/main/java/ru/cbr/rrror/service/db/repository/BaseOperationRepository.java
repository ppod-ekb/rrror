package ru.cbr.rrror.service.db.repository;

public interface BaseOperationRepository<T> {

    Iterable<T> all();
}
