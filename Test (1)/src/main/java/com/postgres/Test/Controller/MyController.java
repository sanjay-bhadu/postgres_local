package com.postgres.Test.Controller;

import com.postgres.Test.Model.Book;
import com.postgres.Test.Repo.BookRepo;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Log4j2
//we can also use logger but this is easy.
public class MyController {

    @Autowired
    private BookRepo bookRepo;
    @GetMapping("/book")
    public List<Book> allBook(){
        log.info("All Books is accessed");
        List<Book> books=bookRepo.findAll();
        books=books.stream()
                .filter(s->s.isAvailable())
                .collect(Collectors.toList());
        return books;
    }

    @PostMapping("/book")
    public Book addBook(@RequestBody Book book)
    {
        try{
            Book b=bookRepo.getReferenceById(book.getId());
            if(b!=null)
            {
                throw new RuntimeException("The Book with id "+book.getId()+" is Already present");
            }
            else{
                bookRepo.save(book);
                log.info("The Book is Added: "+book);
                return book;
            }
        }
        catch (Exception e)
        {
            log.error(e);
            return null;
        }
    }
    @PutMapping("/book/{id}")
    public Book updateBook(@PathVariable int id,@RequestBody Book book)
    {
        try{
            Book b=bookRepo.getReferenceById(id);
            if(b!=null && book.getId()==id)
            {
                bookRepo.save(book);
                log.info("The book has been updated "+book);
                return book;
            }
            else if(b==null) {
                throw  new RuntimeException("The Book is with "+ id+ " is not present in the Database");
            }
            else{
                throw new RuntimeException("Invalid Input");
            }
        }
        catch (Exception e)
        {
            log.error(e);
            return null;
        }
    }

    @DeleteMapping("/book/{id}")
    public String deleteBook(@PathVariable int id) {
        try{
            Book b=bookRepo.getReferenceById(id);
            if(b!=null){
                log.info("The Book " +b+" has been deleted from the Database");
                bookRepo.delete(b);
                return "The Book is Successfully Deleted";
            }
            else{
                throw new RuntimeException("The Book with id : "+id+ " is not present in Database");
            }
        }
        catch (Exception e)
        {
            log.error(e);
            return "Error Occured.. The Book is not present in the Database";
        }
    }

    @GetMapping("/book/{id}")
    public String getBook(@PathVariable int id)
    {
       try{
           Book b=bookRepo.getReferenceById(id);
           return b.toString();
       }
       catch (Exception e){
           log.error(e);
           return "Book Not Found";
       }
    }

    @PutMapping("/issue/{id}")
    public Book issueBook(@PathVariable int id)
    {
        try{
            Book b=bookRepo.getReferenceById(id);
            if(b!=null && b.isAvailable())
            {
                b.setAvailable(false);
                bookRepo.save(b);
                return b;
            }
            else{
                return null;
            }
        }
        catch (Exception e)
        {
            log.error(e);
            return null;
        }
    }
    @PutMapping("/return/{id}")
    public String returnBook(@PathVariable int id)
    {
        log.info("Return Book is Accessed");
        try{
            Book b=bookRepo.getReferenceById(id);
            if(b!=null & !b.isAvailable())
            {
                b.setAvailable(true);
                bookRepo.save(b);
                log.info("The Book :"+b+"is Returned");
                return "Book Returned";
            }
            else{
                log.warn("The Book is Not Issued");
                return "The Book is not Issued>>>";
            }
        }
        catch (Exception e)
        {
            log.error(e);
            return "Sorry.. Error Occured";
        }
    }

}
