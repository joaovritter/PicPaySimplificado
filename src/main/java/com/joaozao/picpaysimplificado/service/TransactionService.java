package com.joaozao.picpaysimplificado.service;

import com.joaozao.picpaysimplificado.domain.Transaction;
import com.joaozao.picpaysimplificado.domain.User;
import com.joaozao.picpaysimplificado.dto.TransactionDTO;
import com.joaozao.picpaysimplificado.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;


@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private AuthorizationService authorizationService;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO dto) throws Exception {
        // Procura os usuários pelo ID
        User sender = userService.findUserById(dto.senderId());
        User receiver = userService.findUserById(dto.receiverId());

        // Valida se a transação é válida
        userService.validateTransaction(sender, dto.amount());

        // Verifica na API externa se a transação é autorizada
        boolean isAuthorized = authorizationService.authorizeTransaction(sender, dto.amount());
        if (!isAuthorized) {
            throw new Exception("Transação não autorizada pela API externa");
        }

        // Cria a nova transação
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(dto.amount());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        // Atualiza os saldos dos usuários
        sender.setBalance(sender.getBalance().subtract(dto.amount()));
        receiver.setBalance(receiver.getBalance().add(dto.amount()));

        // Salva a transação e atualiza o saldo dos usuários no repositório
        transactionRepository.save(newTransaction);
        userService.saveUser(sender);
        userService.saveUser(receiver);

        // Envia notificações para os usuários
        this.notificationService.sendNotification(sender, "Transação realizada com sucesso para " + receiver.getFirstName() + "!");
        this.notificationService.sendNotification(receiver,"Você recebeu uma transação de " + sender.getFirstName() + "!");

        return newTransaction;
    }


}