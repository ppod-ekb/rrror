package ru.cbr.rrror.service.db.model;


import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.relational.core.mapping.Table;


@Data
@Table("user")
@AllArgsConstructor(access = AccessLevel.PRIVATE, onConstructor = @__(@PersistenceConstructor))
@NoArgsConstructor
public class Person implements BaseEntity {
    @Id
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String description;

    public static Person of(String login, String firstName, String lastName, String description) {
        return new Person(null, login, firstName, lastName, description);
    }

    public Person withId(Long id) {
        return new Person(id, login, firstName, lastName, description);
    }

}
