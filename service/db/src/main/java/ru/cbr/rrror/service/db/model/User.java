package ru.cbr.rrror.service.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Getter @Setter @ToString @NoArgsConstructor @AllArgsConstructor
public class User {

    private @Id @GeneratedValue Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String description;

    private @Version @JsonIgnore Long version;

    public User(String login, String firstName, String lastName, String description) {
        this.login = login;
        this.firstName = firstName;
        this.lastName = lastName;
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User u = (User) o;
        return Objects.equals(id, u.id) &&
                Objects.equals(firstName, u.firstName) &&
                Objects.equals(lastName, u.lastName) &&
                Objects.equals(description, u.description) &&
                Objects.equals(version, u.version);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id, firstName, lastName, description, version);
    }
}