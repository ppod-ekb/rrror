package ru.cbr.rrror.service.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = {"groups", "role"})
@Table(name="user")
public class User2 extends AbstractPersistable<Long> {

    private String login;
    @Embedded
    private Name name;
    private String description;

    private @Version @JsonIgnore Long version;
}
