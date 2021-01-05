package org.example.app.services;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retreiveAll();

    void store(T book);

    boolean removeItemById(Integer bookIdToRemove);

    boolean removeItemByAuthor(String bookAuthorToRemove);

    boolean removeItemByTitle(String bookTitleToRemove);

    boolean removeItemBySize(String bookSizeToRemove);

    void setFilter(String A, String B, String C);

    List<T> retreiveAllFiltered();
}
