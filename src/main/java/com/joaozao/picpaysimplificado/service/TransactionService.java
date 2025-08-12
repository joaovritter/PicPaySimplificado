package com.joaozao.picpaysimplificado.service;

import com.joaozao.picpaysimplificado.domain.Transaction;
import com.joaozao.picpaysimplificado.domain.User;
import com.joaozao.picpaysimplificado.dto.TransactionDTO;
import com.joaozao.picpaysimplificado.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {
    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private NotificationService notificationService;

    public Transaction createTransaction(TransactionDTO dto) throws Exception {
        User sender = userService.findUserById(dto.senderId());
        User receiver = userService.findUserById(dto.receiverId());

        userService.validateTransaction(sender, dto.amount());

        boolean isAuthorized = authorizeTransaction(sender, dto.amount());
        if (!isAuthorized) {
            throw new Exception("Transação não autorizada pela API externa");
        }

        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(dto.amount());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        sender.setBalance(sender.getBalance().subtract(dto.amount())); // Deduz o valor da transação do saldo do remetente
        receiver.setBalance(receiver.getBalance().add(dto.amount()));

        transactionRepository.save(newTransaction);
        userService.saveUser(sender); // Atualiza o saldo do remetente
        userService.saveUser(receiver); // Atualiza o saldo do destinatário

        this.notificationService.sendNotification(sender, "Transação realizada com sucesso para " + receiver.getFirstName() + "!");
        this.notificationService.sendNotification(receiver,"Você recebeu uma transação de " + sender.getFirstName() + "!");

        return newTransaction;
    }

    public boolean authorizeTransaction(User sender, BigDecimal amount) {
    // Faz uma requisição GET para a API de autorização e espera receber um Map como resposta
    ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity("https://util.devi.tools/api/v2/authorize", Map.class);

    if (authorizationResponse.getStatusCode() == HttpStatus.OK) { //se a requisição foi bem sucedida
        // Extrai o campo "status" do JSON e converte para String
        String status = (String) authorizationResponse.getBody().get("status");
        
        // Extrai o objeto "data" do JSON e faz o cast para Map<String, Boolean>
        Map<String, Boolean> data = (Map<String, Boolean>) authorizationResponse.getBody().get("data");

        return "success".equalsIgnoreCase(status) && data.get("authorization");
    }
    else return false;
}
}