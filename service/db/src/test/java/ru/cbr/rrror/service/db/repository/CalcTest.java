package ru.cbr.rrror.service.db.repository;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import ru.cbr.rrror.service.db.model.*;
import ru.cbr.rrror.service.db.util.SearchOperation;

import static org.springframework.data.jpa.domain.Specification.*;

import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class CalcTest {

    @Autowired
    CalcRepository repository;

    @Test
    public void findAllTest() {
        repository.findAll();
    }

    @Test
    public void findAllWithSpecifiactionTest() {

        Page<Calc> page = repository.findAll(where(CalcSpecifications.calcDirtyAvg(true)
                                                .and(CalcSpecifications.repSubjRegNumEqualTo("3309-К")))
                                              ,PageRequest.of(0,2));

        List<Calc> calcs = page.getContent();
        calcs.forEach((c) -> log.debug(">>> calc: " + c.toString()));
    }

    /*
   repSubj.regNum eq '3309-K' and

   spec: [
       {field: 'repSubj.regNum', operation: 'eq', value: '3309-K'},
       {field: 'durtyAvg', operation: 'eq', value: 'true'},
       {field: 'repSubj.regNum', operation: 'in', value: ['3309-K', '1000', '1']},
   ]
    */

    @Test
    public void customSpecParserTest() {
        String specJson = "[\n" +
                "       {field: 'repSubj.regNum', operation: 'eq', value: '3309-К'},\n" +
                "       {field: 'dutyAverage', operation: 'eq', value: true}" +
                "]";

        CustomSpecification.FilterSpecParser parser = new CustomSpecification.FilterSpecParser.FilterSpecParserImpl(specJson);
        parser.parse();
        List<CustomSpecification.Filter> filters = parser.get();
        log.debug(">>> filters:  "+ filters);

        Specification<Calc> spec = new CustomSpecification.CustomSpecificationBuilder<Calc>(filters).build();
        List<Calc> calcs = repository.findAll(spec);
        log.debug(">>> calcs: " + calcs.toString());
    }

    @Test
    public void customSpecTest() {
        Specification<Calc> spec = new CustomSpecification.CustomSpecificationBuilder<Calc>(
                Arrays.asList(
                        new CustomSpecification.Filter(Calc_.DUTY_AVERAGE, SearchOperation.EQUALITY, Arrays.asList(true)),
                        new CustomSpecification.Filter(Calc_.CALENDAR + "." + RegulationCalendar_.YEAR, SearchOperation.EQUALITY, Arrays.asList("2016")),
                        new CustomSpecification.Filter(Calc_.REP_SUBJ + "." + RepSubj_.REG_NUM, SearchOperation.EQUALITY, Arrays.asList("3309-К"))
                )
        ).build();

        List<Calc> calcs = repository.findAll(spec);
        log.debug(">>> calcs: " + calcs.toString());
    }
}
