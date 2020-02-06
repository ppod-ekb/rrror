package ru.cbr.rrror.service.db.repository;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jdbc.core.JdbcAggregateTemplate;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import ru.cbr.rrror.service.db.model.Person;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Slf4j
public class ProcedureCrudImpl implements ProcedureCrud<Person> {

    @Autowired
    private JdbcAggregateTemplate jdbcAggregateTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Person save(Person t) {
        log.debug(">>> insert call: " + t.toString());
        return jdbcAggregateTemplate.insert(t);
    }

    public Iterable<Person> all() {
        return jdbcTemplate.query("select * from user", new RowMapper<Person>() {
            @Override
            public Person mapRow(ResultSet resultSet, int i) throws SQLException {
                return Person.of(resultSet.getString("login"),
                        resultSet.getString("first_name"),
                        resultSet.getString("last_name"),
                        resultSet.getString("description")).withId(resultSet.getLong("id"));
            }
        });
    }

}
