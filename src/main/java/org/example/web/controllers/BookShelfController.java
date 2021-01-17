package org.example.web.controllers;

import org.apache.log4j.Logger;
import org.example.app.exceptions.DownloadFileException;
import org.example.app.exceptions.UploadFileException;
import org.example.app.services.BookService;
import org.example.app.services.FileService;
import org.example.web.dto.Book;
import org.example.web.dto.BookIdToRemove;
import org.example.web.dto.FilesystemPath;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

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
        model.addAttribute("bookIdToRemove", new BookIdToRemove());
        model.addAttribute("downloadPath", new FilesystemPath("c:\\"));
        model.addAttribute("bookListFiltered", bookService.getAllBooksFiltered());
        return "book_shelf";
    }

    @PostMapping("/save")
    public String saveBook(@Valid Book book, BindingResult bindingResult, Model model) {

        if (bindingResult.hasFieldErrors("author")) {
            logger.info("bindingResult.hasFieldErrors(\"author\")");
        }

        if (bindingResult.hasFieldErrors("title")) {
            logger.info("bindingResult.hasFieldErrors(\"title\")");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("book", book);
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("downloadPath", new FilesystemPath("c:\\"));
            model.addAttribute("bookListFiltered", bookService.getAllBooksFiltered());
            return "book_shelf";
        } else {
            bookService.saveBook(book);
            logger.info("current repository size: " + bookService.getAllBooks().size());
            return "redirect:/books/shelf";
        }
    }

    @PostMapping("/remove")
    public String removeBook(@Valid BookIdToRemove bookIdToRemove, BindingResult bindingResult, Model model) {
        logger.info("remove bookIdToRemove getId() = " + bookIdToRemove.getId());

        if (bindingResult.hasErrors()) {
            model.addAttribute("book", new Book());
            model.addAttribute("downloadPath", new FilesystemPath("c:\\"));
            model.addAttribute("bookListFiltered", bookService.getAllBooksFiltered());
            return "book_shelf";
        } else {
            if (bookService.removeBookById(bookIdToRemove.getId())) {
                return "redirect:/books/shelf";
            }
        }
        return "redirect:/books/shelf";
    }

    @PostMapping("/removeByBookParams")
    public String removeByBookParams(@RequestParam(value = "bookAuthorToRemove") String bookAuthorToRemove,
                                     @RequestParam(value = "bookTitleToRemove") String bookTitleToRemove,
                                     @RequestParam(value = "bookSizeToRemove") String bookSizeToRemove) {
        logger.info("remove authorToRemove = " + bookAuthorToRemove);
        logger.info("remove bookTitleToRemove = " + bookTitleToRemove);
        logger.info("remove bookSizeToRemove = " + bookSizeToRemove);

        if (!bookAuthorToRemove.isEmpty()) {
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

    @PostMapping("/uploadFile")
    public String uploadFile(@RequestParam("file")MultipartFile file) throws Exception{
        if (!file.isEmpty()) {
        String name = file.getOriginalFilename();
        byte[] bytes = file.getBytes();

        //create dir
            File dir = FileService.TouchDir("external_uploads");

        // create file
        File serverfile = new File(dir.getAbsolutePath() + File.separator + name);
        BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(serverfile));
        stream.write(bytes);
        stream.close();

        logger.info("new file saved at: " + serverfile.getAbsolutePath());

        return "redirect:/books/shelf";
    } else {
            throw new UploadFileException("file not defined");
        }
    }

    @PostMapping("/downloadFile")
    public String DownloadFile (@Valid FilesystemPath downloadPath, BindingResult bindingResult, Model model) throws Exception {

        logger.info("downloadPath = " + downloadPath.getPath());

        if (bindingResult.hasErrors()) {
            logger.info("bindingResult.hasErrors()");
            model.addAttribute("book", new Book());
            model.addAttribute("bookIdToRemove", new BookIdToRemove());
            model.addAttribute("downloadPath", new FilesystemPath(""));
            model.addAttribute("bookListFiltered", bookService.getAllBooksFiltered());
            return "book_shelf";
        } else {
            try {
            String fileName = "readme.txt";
            String fileFullPath = System.getProperty("catalina.home") + File.separator + "shared_files_to_download" + File.separator + fileName;
            String contentText = "This is the readme.txt";
            byte[] content = contentText.getBytes();
            MultipartFile file = new MockMultipartFile(fileFullPath, fileName, "text/plain", content);
            byte[] bytes = file.getBytes();


            // download file
            File targetFile = new File(downloadPath.getPath() + File.separator + fileName);
            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(targetFile));
            stream.write(bytes);
            stream.close();

            logger.info("file download complete");

            return "redirect:/books/shelf";
            }
            catch (IOException e){
                logger.info(e.getMessage());
                throw new DownloadFileException(e.getMessage());
            }
        }
    }

    @ExceptionHandler(UploadFileException.class)
    public String handleError(Model model, UploadFileException exception) {
        model.addAttribute("errorMessage",exception.getMessage());
        return "errors/500";
    }

    @ExceptionHandler(DownloadFileException.class)
    public String handleError(Model model, DownloadFileException exception) {
        model.addAttribute("errorMessage",exception.getMessage());
        return "errors/500";
    }
}
