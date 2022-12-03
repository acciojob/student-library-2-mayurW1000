package com.driver.controller;

import com.driver.models.Transaction;
import com.driver.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//Add required annotations
@RestController
@RequestMapping("transaction")
public class TransactionController {

    @Autowired
    TransactionService transactionService;
    //Add required annotations

    @PostMapping("issueBook")
    public ResponseEntity issueBook(@RequestParam("cardId") int cardId, @RequestParam("bookId") int bookId) throws Exception{
        try{transactionService.issueBook(cardId, bookId);
            return new ResponseEntity<>("transaction completed", HttpStatus.ACCEPTED);}
        catch (Exception e){
            return new ResponseEntity<>("Exception occured",HttpStatus.BAD_GATEWAY);
        }
    }

    //Add required annotations
    @PostMapping("returnBook")
    public ResponseEntity returnBook(@RequestParam("cardId") int cardId, @RequestParam("bookId") int bookId) throws Exception{
        Transaction request = transactionService.returnBook(cardId, bookId);
        return new ResponseEntity<>(request, HttpStatus.ACCEPTED);
    }
}
