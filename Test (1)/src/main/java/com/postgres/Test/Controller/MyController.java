package com.postgres.Test.Controller;

import com.postgres.Test.Model.Book;
import com.postgres.Test.Repo.BookRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Log4j2
//we can also use logger but this is easy.
public class MyController {

    @Autowired
    private BookRepo bookRepo;

    @GetMapping("/log")
    public void logChecking(){
        log.trace("trace");
        log.info("info");
        log.error("Error");
    }
    @GetMapping("/book")
    public List<Book> allBook(){
        log.info("All Books is accessed");
        return bookRepo.findAll();
    }

    @PostMapping("/book")
    public Book addBook(@RequestBody Book book)
    {
        log.info("A Book is added in the Database");
        bookRepo.save(book);
        return book;
    }
    @PutMapping("/book/{id}")
    public Book updateBook(@PathVariable int id,@RequestBody Book book)
    {
        Book b=bookRepo.findById(id).orElse(null);
        if(b!=null){
            b.setAuthor(book.getAuthor());
            b.setName(book.getName());
            log.info("The Book has been updated");
            bookRepo.save(b);
        }
        else{
            log.info("The books with id "+ id+" is not present");
            log.info("We added the given book as new Book");
            log.info(book+" is added");
            bookRepo.save(book);
        }
        return book;
    }

    @DeleteMapping("/book/{id}")
    public String deleteBook(@PathVariable int id) {
        Book b=bookRepo.findById(id).orElse(null);
        if(b!=null)
        {
            bookRepo.delete(b);
            log.info("Book is deleted from database");
            return "Book is deleted";
        }
        else{
            log.error("book is not present in the Database");
            return "Book is Not Present in Database";
        }
    }

    @GetMapping("/book/{id}")
    public String getBook(@PathVariable int id)
    {
         Book b=bookRepo.findById(id).orElse(null);
         if(b!=null) {
             log.info("Book is Searched and Founded:");
             return b.toString();
         }
         else{
             log.error("Book is not in the database");
             return "Book is not present in Database";
         }
    }
}
