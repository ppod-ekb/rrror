package ru.cbr.rrror.service.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Table;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "FOR_REP_SUBJ")
public class RepSubj extends AbstractPersistable<Long> {

   private String regNum;
   private String name;
   private String subjType;
   private String licType;
   private String innNum;

}
