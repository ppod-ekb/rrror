package ru.cbr.rrror.service.db.model;

import org.springframework.data.jpa.domain.Specification;

public class CalcSpecifications {

    public static Specification<Calc> calcDirtyAvg(boolean value) {
        return (root, query, cb) -> {
            return cb.equal(root.get(Calc_.DUTY_AVERAGE), value);
        };
    }

    public static Specification<Calc> repSubjRegNumEqualTo(String value) {
        return (root, query, cb) -> {
            return cb.equal(root.join(Calc_.REP_SUBJ).get(RepSubj_.REG_NUM), value);
        };
    }
}
