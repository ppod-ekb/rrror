package ru.cbr.rrror.service.db.repository;

import org.springframework.data.repository.CrudRepository;
import ru.cbr.rrror.service.db.model.Group;
import ru.cbr.rrror.service.db.model.Group2;

public interface Group2Repository extends CrudRepository<Group2, Long> {

    Group2 findByGroupName(String groupName);
}
