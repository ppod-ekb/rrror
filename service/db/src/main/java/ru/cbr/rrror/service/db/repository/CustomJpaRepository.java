package ru.cbr.rrror.service.db.repository;

public interface CustomJpaRepository<T> {

    void refresh(T t);
}
