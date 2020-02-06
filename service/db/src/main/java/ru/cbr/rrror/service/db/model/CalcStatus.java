package ru.cbr.rrror.service.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="FOR_DUTY_AVERAGE")
public class CalcStatus extends AbstractPersistable<Long> {

    private String name;
    private String code;
}
