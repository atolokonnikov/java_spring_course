package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.RegisterForm;
import org.example.web.dto.User;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    private final UserRepository<User> userRepository;

    private Logger logger = Logger.getLogger(RegisterService.class);

    public RegisterService(UserRepository<User> userRepository) {
        this.userRepository = userRepository;
    }

    public void add_user(User user) {
        if (user.getLogin().isEmpty()) {
            logger.info("login is empty");
        } else {
            userRepository.store(user);
        }
    }
}
