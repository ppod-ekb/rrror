package ru.cbr.rrror.service.db.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.data.jpa.domain.Specification;
import ru.cbr.rrror.service.db.util.SearchOperation;

import javax.persistence.criteria.*;
import java.util.*;
import java.util.function.Supplier;

public class CustomSpecification<T> implements Specification<T> {

    /*
    repSubj.regNum eq '3309-K' and

    spec: [
        {field: 'repSubj.regNum', operation: 'eq', value: '3309-K'},
        {field: 'durtyAvg', operation: 'eq', value: 'true'},
        {field: 'repSubj.regNum', operation: 'in', value: ['3309-K', '1000', '1']},
    ]
     */

    @Override
    public Predicate toPredicate(Root<T> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
       return null;
    }

    @Slf4j
    @AllArgsConstructor
    public static class CustomSpecificationBuilder<T> {

        private final List<Filter> filters;

        private Specification<T> create(Filter f) {
            log.debug("create filter: " + f.toString());
            return (root, query, cb) -> cb.equal(new PathBuilder<T>(root, f.getField()).build(), f.getValues().get(0));
        }

        public Specification<T> build() {
            Specification<T> spec = create(filters.get(0));
            for (int i = 1; i < filters.size(); i++) {
               spec = spec.and(create(filters.get(i)));
            }

            return spec;
        }

        @AllArgsConstructor
        static class PathBuilder<T> {

            private final Root<T> root;
            private final String field;

            private Path<T> build() {
                String[] fields = field.split("\\.");
                if (fields.length > 1) {
                    return join(fields);
                } else {
                    return root.get(field);
                }
            }

            private Path<T> join(final String[] fields) {
                Join join = root.join(fields[0]);
                for (int i = 1; i < fields.length-1; i++) {
                    join = join.join(fields[i]);
                }
                return join.get(fields[fields.length-1]);
            }
        }
    }

    @AllArgsConstructor @Getter @ToString
    public static class Filter {
        private final String field;
        private final SearchOperation operation;
        private final List<?> values;
    }

    public interface FilterSpecParser {

        void parse();
        List<Filter> get();

        @Slf4j
        @AllArgsConstructor
        class FilterSpecParserImpl implements FilterSpecParser {

            private final String spec;
            private final List<Filter> filters = new ArrayList<>();

            @Override
            public void parse() {
                JSONArray specAsJson = new JSONArray(spec);
                Iterator it = specAsJson.iterator();
                while (it.hasNext()) {
                    JSONObject j = (JSONObject) it.next();

                    filters.add(new Filter(j.getString("field"), SearchOperation.EQUALITY, Arrays.asList(j.get("value"))));
                }
            }

            @Override
            public List<Filter> get() {
                return filters;
            }

        }
    }
}
