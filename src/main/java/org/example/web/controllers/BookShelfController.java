package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.services.BookService;
import org.example.web.dto.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping(value = "/books")
public class BookShelfController {

    private Logger logger = Logger.getLogger(BookShelfController.class);
    private BookService bookService;

    @Autowired
    public BookShelfController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/shelf")
    public String books(Model model) {
        logger.info("got book shelf");
        model.addAttribute("book", new Book());
        model.addAttribute("bookListFiltered", bookService.getAllBooksFiltered());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(Book book) {
        bookService.saveBook(book);
        logger.info("current repository size: " + bookService.getAllBooks().size());
        return "redirect:/books/shelf";
    }

    @PostMapping("/remove")
    public String removeBook(@RequestParam(value = "bookIdToRemove") Integer bookIdToRemove,
                             @RequestParam(value = "bookAuthorToRemove") String bookAuthorToRemove,
                             @RequestParam(value = "bookTitleToRemove") String bookTitleToRemove,
                             @RequestParam(value = "bookSizeToRemove") String bookSizeToRemove) {
        logger.info("remove bookIdToRemove = " + bookIdToRemove);
        logger.info("remove authorToRemove = " + bookAuthorToRemove);
        logger.info("remove bookTitleToRemove = " + bookTitleToRemove);
        logger.info("remove bookSizeToRemove = " + bookSizeToRemove);

        if (bookIdToRemove != null) {
            logger.info("bookIdToRemove != null");
            if (bookService.removeBookById(bookIdToRemove)) {
                return "redirect:/books/shelf";
            }
        } else if (!bookAuthorToRemove.isEmpty()) {
            logger.info("!(bookAuthorToRemove.isEmpty())");
            if (bookService.removeBookByAuthor(bookAuthorToRemove)) {
                return "redirect:/books/shelf";
            }
        } else if (!bookTitleToRemove.isEmpty()) {
            logger.info("!bookTitleToRemove.isEmpty()");
            if (bookService.removeBookByTitle(bookTitleToRemove)) {
                return "redirect:/books/shelf";
            }
        } else if (!bookSizeToRemove.isEmpty()) {
            logger.info("!bookSizeToRemove.isEmpty()");
            if (bookService.removeBookBySize(bookSizeToRemove)) {
                return "redirect:/books/shelf";
            }
        }
            return "redirect:/books/shelf";
    }

    @PostMapping("/setFilter")
    public String setFilter(
            @RequestParam(value = "authorFilter") String authorFilter,
            @RequestParam(value = "titleFilter") String titleFilter,
            @RequestParam(value = "sizeFilter") String sizeFilter
    ) {
        bookService.setFilter(authorFilter, titleFilter, sizeFilter);
        return "redirect:/books/shelf";
    }

}
