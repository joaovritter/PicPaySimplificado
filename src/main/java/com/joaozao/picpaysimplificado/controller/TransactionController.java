package com.joaozao.picpaysimplificado.controller;

import com.joaozao.picpaysimplificado.domain.Transaction;
import com.joaozao.picpaysimplificado.dto.TransactionDTO;
import com.joaozao.picpaysimplificado.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody TransactionDTO transactionDto) throws Exception {
        Transaction newTransaction = transactionService.createTransaction(transactionDto);
        return ResponseEntity.ok(newTransaction);
    }
}
