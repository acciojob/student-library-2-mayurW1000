package com.driver.services;

import com.driver.models.Book;
import com.driver.models.Card;
import com.driver.models.Transaction;
import com.driver.models.TransactionStatus;
import com.driver.repositories.BookRepository;
import com.driver.repositories.CardRepository;
import com.driver.repositories.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.temporal.Temporal;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.driver.models.TransactionStatus.SUCCESSFUL;
import static java.time.temporal.ChronoUnit.DAYS;

@Service
public class TransactionService {

    @Autowired
    BookRepository bookRepository5;

    @Autowired
    CardRepository cardRepository5;

    @Autowired
    TransactionRepository transactionRepository5;

    @Value("${books.max_allowed}")
    public int max_allowed_books;

    @Value("${books.max_allowed_days}")
    public int getMax_allowed_days;

    @Value("${books.fine.per_day}")
    public int fine_per_day;

    public String issueBook(int cardId, int bookId) throws Exception {
        //check whether bookId and cardId already exist
        //conditions required for successful transaction of issue book:
        //1. book is present and available
        // If it fails: throw new Exception("Book is either unavailable or not present");
        Book book = bookRepository5.findById(bookId).get();
        boolean present = book.isAvailable();
        Card card = cardRepository5.findById(cardId).get();
        Transaction transaction = new Transaction();
        if(bookRepository5.existsById(bookId) == false || present==false){
            throw new Exception("Book is either unavailable or not present");
        }
        //2. card is present and activated
        // If it fails: throw new Exception("Card is invalid");

        if(cardRepository5.existsById(cardId) == false || card.getCardStatus().equals("DEACTIVATED")){
            throw new Exception("Card is invalid");
        }
        //3. number of books issued against the card is strictly less than max_allowed_books
        // If it fails: throw new Exception("Book limit has reached for this card");
        if(card.getBooks().size() == max_allowed_books){
            throw new Exception("Book limit has reached for this card");
        }
        //connect book with card

        book.setAvailable(false);
        book.setCard(card);

        transaction.setIssueOperation(true);
        transaction.setTransactionStatus(SUCCESSFUL);
        transaction.setFineAmount(0);
        transaction.setBook(book);
        transaction.setCard(card);

        transactionRepository5.save(transaction);
        String transactionID = null;

        transactionID=transaction.getTransactionId();
        //If the transaction is successful, save the transaction to the list of transactions and return the id
        //Note that the error message should match exactly in all cases
        return transactionID; //return transactionId instead
    }

    public Transaction returnBook(int cardId, int bookId) throws Exception{

        List<Transaction> transactions = transactionRepository5.find(cardId, bookId, TransactionStatus.SUCCESSFUL, true);
        Transaction transaction = transactions.get(transactions.size() - 1);

        //for the given transaction calculate the fine amount considering the book has been returned exactly when this function is called
        Date date = new Date();
        Date transactionDate = transaction.getTransactionDate();

        long diff = transactionDate.getTime()-date.getTime();
        long days = TimeUnit.DAYS.convert(diff,TimeUnit.MILLISECONDS);
        long fine = days*5;
        int fineamount = (int)(fine);

        //make the book available for other users
        Book book = transaction.getBook();
        book.setAvailable(true);
        book.setCard(null);
        //make a new transaction for return book which contains the fine amount as well

        Transaction returnBookTransaction  = null;
        returnBookTransaction=new Transaction();
        returnBookTransaction.setFineAmount(fineamount);
        returnBookTransaction.setBook(book);
        returnBookTransaction.setCard(transaction.getCard());
        returnBookTransaction.setTransactionStatus(SUCCESSFUL);
        transactionRepository5.save(returnBookTransaction);
        return returnBookTransaction; //return the transaction after updating all details
    }
}
