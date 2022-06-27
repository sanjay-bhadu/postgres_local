package com.postgres.Test.Controller;

import com.postgres.Test.Model.Book;
import com.postgres.Test.Repo.BookRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class MyController {
    @Autowired
    private BookRepo bookRepo;

    @GetMapping("/book")
    public List<Book> allBook(){
        return bookRepo.findAll();
    }

    @PostMapping("/book")
    public Book addBook(@RequestBody Book book)
    {
        bookRepo.save(book);
        return book;
    }
    @PutMapping("/book/{id}")
    public Book updateBook(@PathVariable int id,@RequestBody Book book)
    {
        Book b=bookRepo.findById(id).orElse(book);
        if(b!=null){
            b.setAuthor(book.getAuthor());
            b.setName(book.getName());
            bookRepo.save(b);
        }
        else{
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
            return "Book is deleted";
        }
        else{
            return "Book is Not Present in Database";
        }
    }
}
