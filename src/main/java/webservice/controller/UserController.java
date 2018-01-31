package webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import webservice.model.User;
import webservice.repository.UserRepository;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @RequestMapping(value = "/user", consumes = "application/json",method = RequestMethod.POST)
    public void create(@RequestBody User user){
        userRepository.save(user);
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public @ResponseBody
    String fetchDataByFileName(@PathVariable("user") String user){
        return userRepository.fetchById(user);
    }

}
