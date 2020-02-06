package ru.cbr.rrror.service.db.model;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name="FOR_REG_CALENDAR")
public class RegulationCalendar extends AbstractPersistable<Long> {

    private int year;
    private int month;

    private LocalDate regBeginDt;
    private LocalDate regEndDt;
    private String regPeriodNm;

    private LocalDate avgBeginDt;
    private LocalDate avgEndDt;
    private String avgPeriodNm;
}
