package org.example.app.services;

import org.example.web.dto.Book;

public class IdProvider {
    public String provideId(Book book){
        return this.hashCode() + "_" + book.hashCode();
    }
}
