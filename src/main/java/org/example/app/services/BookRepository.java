package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class BookRepository implements ProjectRepository<Book> {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();
    private String authorFilter = "";
    private String titleFilter = "";
    private String sizeFilter = "";

    @Override
    public List<Book> retreiveAll() {
        return new ArrayList<>(repo);
    }

    @Override
    public void store(Book book) {
        if (!(book.getAuthor().isEmpty() & book.getTitle().isEmpty() & book.getSize() == null)) {
            book.setId(book.hashCode());
        logger.info("store new book: " + book);
        repo.add(book);
        }
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        for (Book book : retreiveAll()) {
            if (book.getId().equals(bookIdToRemove)) {
                logger.info("remove book by id: " + book);
                return repo.remove(book);
            }
        }
        logger.info("id not found: " + bookIdToRemove);
        return true;
    }

    @Override
    public boolean removeItemByAuthor(String bookAuthorToRemove){
        for (Book book : retreiveAll()){
            if (book.getAuthor().matches(bookAuthorToRemove)){
                logger.info("remove book by author" + book);
                repo.remove(book);
            }
        }
        return true;
    }

    @Override
    public boolean removeItemByTitle(String bookTitleToRemove){
        for (Book book : retreiveAll()){
            if (book.getTitle().matches(bookTitleToRemove)){
                logger.info("remove book by title" + book);
                repo.remove(book);
            }
        }
        return true;
    }

    @Override
    public boolean removeItemBySize(String bookSizeToRemove){
        for (Book book : retreiveAll()){
            if (Integer.toString(book.getSize()).matches(bookSizeToRemove)){
                logger.info("remove book by title" + book);
                repo.remove(book);
            }
        }
        return true;
    }

    @Override
    public void setFilter(String newAuthorFilter, String newTitleFilter, String newSizeFilter){
        logger.info("setFilter newAuthorFilter = " + newAuthorFilter + " newTitleFilter = " + newTitleFilter + " newSizeFilter = " + newSizeFilter);
        authorFilter = newAuthorFilter;
        titleFilter = newTitleFilter;
        sizeFilter = newSizeFilter;
    }

    @Override
    public List<Book> retreiveAllFiltered() {
        logger.info("retreiveAllFiltered");
        return retreiveAll().stream()
                .filter(w -> authorFilter.isEmpty() || w.getAuthor().matches(authorFilter))
                .filter(w -> titleFilter.isEmpty() || w.getTitle().matches(titleFilter))
                .filter(w -> sizeFilter.isEmpty() || Integer.toString(w.getSize()).matches(sizeFilter))
                .collect(Collectors.toList());
    }
}
