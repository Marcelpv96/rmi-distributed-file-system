package webservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import webservice.model.User;
import webservice.service.UserService;

@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/user", consumes = "application/json",method = RequestMethod.POST)
    public void create(@RequestBody User user){
        userService.saveUser(user);
    }

    @RequestMapping(value = "/user/{user}", method = RequestMethod.GET)
    public @ResponseBody
    User fetchDataByFileName(@PathVariable("user") String user){

        return userService.fetchById(user);
    }

}
