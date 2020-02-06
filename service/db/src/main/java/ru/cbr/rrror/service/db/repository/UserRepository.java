package ru.cbr.rrror.service.db.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import ru.cbr.rrror.service.db.model.User;

@PreAuthorize("hasRole('ROLE_AIB') && hasRole('ROLE_USER')")
public interface UserRepository extends PagingAndSortingRepository<User, Long>, CustomJpaRepository<User> {

    User findByLogin(String firstName);

}


