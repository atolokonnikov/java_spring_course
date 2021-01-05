package org.example.app.services;

import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {

    private final ProjectRepository<Book> bookRepo;

    @Autowired
    public BookService(ProjectRepository<Book> bookRepo) {
        this.bookRepo = bookRepo;
    }

    public List<Book> getAllBooks() {
        return bookRepo.retreiveAll();
    }
    public List<Book> getAllBooksFiltered() {
        return bookRepo.retreiveAllFiltered();
    }

    public void saveBook(Book book) {
        bookRepo.store(book);
    }

    public boolean removeBookById(Integer bookIdToRemove) {
        return bookRepo.removeItemById(bookIdToRemove);
    }

    public boolean removeBookByAuthor(String authorToRemove) { return bookRepo.removeItemByAuthor(authorToRemove);
    }

    public boolean removeBookByTitle(String bookTitleToRemove) { return bookRepo.removeItemByTitle(bookTitleToRemove);
    }

    public boolean removeBookBySize(String bookSizeToRemove) { return bookRepo.removeItemBySize(bookSizeToRemove);
    }

    public void setFilter(String authorFilter, String titleFilter, String sizeFilter) {
        bookRepo.setFilter(authorFilter, titleFilter, sizeFilter);
    }
}
