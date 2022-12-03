package com.driver.controller;

import com.driver.models.Book;
import com.driver.services.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

//Add required annotations
@RestController
public class BookController {

    @Autowired
    BookService bookService;
    //Write createBook API with required annotations
    @PostMapping("book")
    public ResponseEntity createBook(@RequestBody Book book){
        bookService.createBook(book);
        return new ResponseEntity("Success", HttpStatus.OK);
    }

    //Add required annotations
    @GetMapping("book")
    public ResponseEntity getBooks(@RequestParam(value = "genre", required = false) String genre,
                                   @RequestParam(value = "available", required = false, defaultValue = "false") boolean available,
                                   @RequestParam(value = "author", required = false) String author){

        List<Book> bookList = new ArrayList<>(); //find the elements of the list by yourself
        bookList = bookService.getBooks(genre,available,author);
        return new ResponseEntity<>(bookList, HttpStatus.OK);
    }
}