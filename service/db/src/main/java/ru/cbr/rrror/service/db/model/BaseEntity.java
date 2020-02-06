package ru.cbr.rrror.service.db.model;

import org.springframework.data.annotation.Id;

public interface BaseEntity {

    Long getId();
    void setId(Long id);
}
