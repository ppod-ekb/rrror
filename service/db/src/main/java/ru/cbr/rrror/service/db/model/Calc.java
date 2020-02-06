package ru.cbr.rrror.service.db.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@Table(name="CALC_MAIN")
public class Calc extends AbstractPersistable<Long> {

    public Calc withRepSubj(RepSubj repSubj) {
        this.repSubj = repSubj;
        return this;
    }

    public Calc withCalcStatus(CalcStatus calcStatus) {
        this.calcStatus = calcStatus;
        return this;
    }

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private RepSubj repSubj;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private RegulationCalendar calendar;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    private CalcStatus calcStatus;

    private boolean dutyAverage;

    @Column(name = "reserved_liability", precision = 19, scale = 2)
    private BigDecimal reservedLiability;

    @Column(name = "standart_value", precision = 19, scale = 2)
    private BigDecimal standartValue;

    @Column(name = "average_value", precision = 19, scale = 2)
    private BigDecimal averageValue;

    @Column(name = "estimate_value", precision = 19, scale = 2)
    private BigDecimal estimateValue;

    @Column(name = "prev_average_value", precision = 19, scale = 2)
    private BigDecimal previousAverageValue;

    @Column(name = "support_average_value", precision = 19, scale = 2)
    private BigDecimal supportAverageValue;

    @Column(name = "average_failure_sum", precision = 19, scale = 2)
    private BigDecimal averageFailureSum;

    @Column(name = "actual_account_balance", precision = 19, scale = 2)
    private BigDecimal actualAccountBalance;

    @Column(name = "over_payment", precision = 19, scale = 2)
    private BigDecimal overPayment;





}
