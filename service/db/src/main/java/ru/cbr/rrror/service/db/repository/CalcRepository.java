package ru.cbr.rrror.service.db.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.cbr.rrror.service.db.model.Calc;

public interface CalcRepository extends PagingAndSortingRepository<Calc, Long>, JpaSpecificationExecutor<Calc> {
}
