package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class LoginRepository implements UserRepository<User> {

    private final Logger logger = Logger.getLogger(LoginRepository.class);

    private static List<User> userList = new ArrayList<>();

    @Override
    public List<User> retreiveAll() {
        return new ArrayList<>(userList);
        //return userList;
    }

    @Override
    public void store(User user) {
        logger.info("add new user: " + user);
        userList.add(user);
        logger.info("userList.size() = " + userList.size());
    }

    @Override
    public boolean contains(User user){
        logger.info("contains method ...");
        for (User userElement : retreiveAll()){
            logger.info("contains cycle");
            logger.info("user.getLogin() = " + user.getLogin() + " user.getPassword() = " + user.getPassword());
            if (userElement.getLogin().equals(user.getLogin())
                    && userElement.getPassword().equals(user.getPassword())){
                return true;
            }
        }
        return false;
    }
}
