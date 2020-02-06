package ru.cbr.rrror.service.db.repository;

import org.springframework.data.repository.CrudRepository;
import ru.cbr.rrror.service.db.model.Group;

public interface GroupRepository extends CrudRepository<Group, Long> {

    Group findByGroupName(String groupName);
}
