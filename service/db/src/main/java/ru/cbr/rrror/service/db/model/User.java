package ru.cbr.rrror.service.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Entity
@AllArgsConstructor @NoArgsConstructor @Data
@EqualsAndHashCode(exclude = {"groups", "role"})
public class User extends AbstractPersistable<Long> {

    private String login;
    @Embedded
    private Name name;
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Role role;

    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(name = "user_group",
               joinColumns = @JoinColumn(name = "group_id", referencedColumnName = "id"),
               inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    final private List<Group> groups = new ArrayList<>();

    private @Version @JsonIgnore Long version;

    public User(String login, Name name, String description) {
        this.login = login;
        this.name = name;
        this.description = description;
    }

    public User withRole(Role role) {
        this.role = role;
        return this;
    }

    public User withGroups(Group... groups) {
        this.groups.addAll(Arrays.asList(groups));
        return this;
    }

    public User withGroup(Group group) {
        this.groups.add(group);
        return this;
    }
}