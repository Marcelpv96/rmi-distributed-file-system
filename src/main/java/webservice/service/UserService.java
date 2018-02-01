package webservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import webservice.model.User;
import webservice.repository.UserRepository;

/**
 * Created by Marcelpv96 on 1/2/18.
 */
@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public User fetchById(String id) {
        return repository.findById(id);
    }

    public void saveUser(User user) {
        repository.save(user);
    }
}
