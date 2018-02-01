package webservice.repository;

import org.springframework.data.repository.CrudRepository;
import webservice.model.User;

public interface UserRepository extends CrudRepository<User, Long> {
    User findById(String user);
}
