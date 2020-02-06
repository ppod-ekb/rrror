package ru.cbr.rrror.service.db.repository;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

public class CustomJpaRepositoryImpl<T> implements CustomJpaRepository {

    @Autowired
    private EntityManager entityManager;

    @Override
    public void refresh(Object o) {
        entityManager.refresh(o);
    }
}
