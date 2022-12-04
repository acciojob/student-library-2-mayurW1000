package com.driver.services;

import com.driver.models.Book;
import com.driver.repositories.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class BookService {

    @Autowired
    BookRepository bookRepository2;

    public void createBook(Book book){
        bookRepository2.save(book);
    }

    public List<Book> getBooks(String genre, boolean available, String author){
        List<Book> books = new ArrayList<>();
        //find the elements of the list by yourself
        if(author==null || author.equals("")){
            return bookRepository2.findBooksByGenre(genre,available);
        }
        else if(genre==null || genre.equals("")){
            return bookRepository2.findBooksByAuthor(author,available);
        }
        else{
            return bookRepository2.findBooksByGenreAuthor(genre,author,available);
        }
    }
}
