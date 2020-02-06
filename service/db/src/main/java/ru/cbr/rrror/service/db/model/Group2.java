package ru.cbr.rrror.service.db.model;

import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "SEC_GROUP")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Group2 extends AbstractPersistable<Long> {

    @Column(unique = true)
    private String groupName;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_group",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"))
    private final List<User2> users = new ArrayList<>();

    public Group2 withUsers(User2 ... u) {
        users.addAll(Arrays.asList(u));
        return this;
    }
}
