package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.LoginForm;
import org.example.web.dto.User;
import org.springframework.stereotype.Service;

@Service
public class LoginService {

    private Logger logger = Logger.getLogger(LoginService.class);
    private final UserRepository<User> repo;

    public LoginService(UserRepository<User> userRepo) {
        this.repo = userRepo;
        this.repo.store(new User("root", "123"));
    }

    public boolean authenticate(LoginForm loginFrom) {
        boolean result;
        User user;

        logger.info("try auth with user-form: " + loginFrom);
        user = new User(loginFrom.getUsername(), loginFrom.getPassword());
        logger.info("username = " + loginFrom.getUsername() + " password = " + loginFrom.getPassword());
        result = repo.contains(user);
        return result;
    }
}
