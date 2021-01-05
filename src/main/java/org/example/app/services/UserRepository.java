package org.example.app.services;

import java.util.List;

public interface UserRepository <U>{
    List<U> retreiveAll();

    void store(U user);

    boolean contains(U user);

}
