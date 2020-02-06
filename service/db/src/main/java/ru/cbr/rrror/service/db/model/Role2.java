package ru.cbr.rrror.service.db.model;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@Entity
@AllArgsConstructor @NoArgsConstructor
@Data
@EqualsAndHashCode(exclude = "users")
@Table(name="role")
public class Role2 extends AbstractPersistable<Long> {

    @Column(unique=true)
    String roleName;

    @OneToMany(cascade = CascadeType.ALL)
    @ToString.Exclude private final Set<User2> users = new HashSet<User2>();

    public Role2 withUsers(User2 ... u) {
        users.addAll(Arrays.asList(u));
        return this;
    }
}
