package ru.cbr.rrror.service.db.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import ru.cbr.rrror.service.db.model.Role;

public interface RoleRepository extends PagingAndSortingRepository<Role, Long> {
}
