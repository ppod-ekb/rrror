package ru.cbr.rrror.service.db.model;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "users")
public class Role extends AbstractPersistable<Long> {

    @Column(unique=true)
    String roleName;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    @ToString.Exclude private final Set<User> users = new HashSet<User>();

    public Role withUsers(User ... u) {
        users.addAll(Arrays.asList(u));
        return this;
    }
}
