package ru.cbr.rrror.service.db.model;


import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "SEC_GROUP")
@Data @AllArgsConstructor @NoArgsConstructor
public class Group extends AbstractPersistable<Long> {

    @Column(unique = true)
    private String groupName;

}
