package ru.cbr.rrror.service.db.model;

import org.springframework.data.jpa.domain.Specification;

public class RepSubjSpecifications {

    public static Specification<RepSubj> regNumEqualTo(String value) {
        return (root, query, cb) -> cb.equal(root.get("regNum"), value);
    }
}
