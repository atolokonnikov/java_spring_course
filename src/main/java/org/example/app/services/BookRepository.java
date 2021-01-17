package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class BookRepository implements ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final List<Book> repo = new ArrayList<>();
    private String authorFilter = "";
    private String titleFilter = "";
    private String sizeFilter = "";
    private ApplicationContext context;

    private final NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> retreiveAll() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
        return new ArrayList<>(books);
    }

    @Override
    public void store(Book book) {
   //         book.setId(context.getBean(IdProvider.class).provideId(book));
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", book.getAuthor());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("size", book.getSize());
        jdbcTemplate.update("INSERT INTO books (author, title, size) VALUES (:author, :title, :size)", parameterSource);
        logger.info("store new book: " + book);
   //     repo.add(book);
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
     MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", bookIdToRemove);
        jdbcTemplate.update("DELETE FROM books WHERE id = :id", parameterSource);
     logger.info("remove book completed");
     return true;
     }

    @Override
    public boolean removeItemByAuthor(String bookAuthorToRemove){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("pattern", bookAuthorToRemove);
        jdbcTemplate.update("DELETE FROM books WHERE regexp_like(author, :pattern)", parameterSource);
        logger.info("remove books by author completed");
        return true;
    }

    @Override
    public boolean removeItemByTitle(String bookTitleToRemove){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("pattern", bookTitleToRemove);
        jdbcTemplate.update("DELETE FROM books WHERE regexp_like(title, :pattern)", parameterSource);
        logger.info("remove books by title completed");
        return true;
    }

    @Override
    public boolean removeItemBySize(String bookSizeToRemove){
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("pattern", bookSizeToRemove);
        jdbcTemplate.update("DELETE FROM books WHERE regexp_like(size, :pattern)", parameterSource);
        logger.info("remove books by title completed");
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
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author_filter", authorFilter);
        parameterSource.addValue("title_filter", titleFilter);
        parameterSource.addValue("size_filter", sizeFilter);
        List<Book> books = jdbcTemplate.query("SELECT * from books WHERE regexp_like(author, :author_filter)" +
                " and regexp_like(title, :title_filter) and regexp_like(size, :size_filter)", parameterSource,
                (ResultSet rs, int rowNum) -> {
            Book book = new Book();
            book.setId(rs.getInt("id"));
            book.setAuthor(rs.getString("author"));
            book.setTitle(rs.getString("title"));
            book.setSize(rs.getInt("size"));
            return book;
        });
        return new ArrayList<>(books);

//        return retreiveAll().stream()
//                .filter(w -> authorFilter.isEmpty() || w.getAuthor().matches(authorFilter))
//                .filter(w -> titleFilter.isEmpty() || w.getTitle().matches(titleFilter))
//                .filter(w -> sizeFilter.isEmpty() || Integer.toString(w.getSize()).matches(sizeFilter))
//                .collect(Collectors.toList());
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
