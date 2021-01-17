package org.example.web.dto;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class User {

    @NotEmpty
    private String login;
    @NotEmpty
    @Size(min=2)
    private String password;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin () {
            return login;
        }

        public String getPassword () {
            return password;
        }

        public void setLogin (String login){
            this.login = login;
        }

        public void setPassword (String password){
            this.password = password;
        }

    @Override
    public String toString() {
        return "User{" +
                "login='" + login + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
