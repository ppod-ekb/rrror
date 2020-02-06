package ru.cbr.rrror.service.db.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.*;
import ru.cbr.rrror.service.db.model.Calc;
import ru.cbr.rrror.service.db.model.CustomSpecification;
import ru.cbr.rrror.service.db.repository.CalcRepository;
import ru.cbr.rrror.service.db.model.CalcSpecifications;

import java.util.List;

@RestController
@AllArgsConstructor
@Slf4j
public class CalcController {

    private final CalcRepository repository;

    @RequestMapping(method = RequestMethod.GET, value = "/calcs/spec", consumes = "application/json")
    @ResponseBody
    public List<Calc> findAllBySpecification(
            @RequestBody String filter
            //@RequestParam(value = "filter",required = true) String filter
            ) {
        log.debug(">>> filter: " + filter);

        CustomSpecification.FilterSpecParser parser = new CustomSpecification.FilterSpecParser.FilterSpecParserImpl(filter);
        parser.parse();
        List<CustomSpecification.Filter> filters = parser.get();
        Specification<Calc> spec = new CustomSpecification.CustomSpecificationBuilder<Calc>(filters).build();

        return repository.findAll(spec);
        //return Collections.emptyList();
    }
}
