package ru.cbr.rrror.service.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;

@Embeddable
@Data @AllArgsConstructor @NoArgsConstructor
public class Name {
    private String firstName;
    private String lastName;
}
