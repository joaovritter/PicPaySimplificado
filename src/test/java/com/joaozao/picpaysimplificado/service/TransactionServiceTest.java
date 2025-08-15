package com.joaozao.picpaysimplificado.service;

import com.joaozao.picpaysimplificado.domain.User;
import com.joaozao.picpaysimplificado.domain.UserType;
import com.joaozao.picpaysimplificado.dto.TransactionDTO;
import com.joaozao.picpaysimplificado.repository.TransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserService userService;

    @Mock
    private AuthorizationService authorizationService;

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    @DisplayName("Should create transaction successfully when all conditions are met")
    void createTransactionSuccess() throws Exception {

        // Arrange: preparar os dados e mocks
        User sender = new User(1L, "João", "Bala", "12345678900","joao@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L, "Fabio", "Bala", "12345678902","fabio@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authorizationService.authorizeTransaction(any(), any())).thenReturn(true);

        TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal(10), 1L, 2L);

        // Act: chamar o metodo a ser testado
        transactionService.createTransaction(transactionDTO);

        // Assert: verificar se o estado final é o esperado
        verify(transactionRepository, times(1)).save(any());


        sender.setBalance(new BigDecimal(0));
        verify(userService, times(1)).saveUser(sender);

        receiver.setBalance(new BigDecimal(20));
        verify(userService, times(1)).saveUser(receiver);

        verify(notificationService, times(1)).sendNotification(sender, "Transação realizada com sucesso para " + receiver.getFirstName() + "!");
        verify(notificationService, times(1)).sendNotification(receiver, "Você recebeu uma transação de " + sender.getFirstName() + "!");

    }

    @Test
    @DisplayName("Should throw exception when transaction is not authorized by external API")
    void createTransactionFailure() throws Exception {
        // Arrange: preparar os dados e mocks
        User sender = new User(1L, "João", "Bala", "12345678900","joao@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);
        User receiver = new User(2L, "Fabio", "Bala", "12345678902","fabio@gmail.com", "12345", new BigDecimal(10), UserType.COMMON);

        when(userService.findUserById(1L)).thenReturn(sender);
        when(userService.findUserById(2L)).thenReturn(receiver);

        when(authorizationService.authorizeTransaction(any(), any())).thenReturn(false);

        // Act: chamar o metodo a ser testado e capturar a exceção
        Exception thrown = Assertions.assertThrows(Exception.class, () -> {
            TransactionDTO transactionDTO = new TransactionDTO(new BigDecimal(10), 1L, 2L);
            transactionService.createTransaction(transactionDTO);
        });

        // Assert: verificar se a exceção foi lançada
        Assertions.assertEquals("Transação não autorizada pela API externa", thrown.getMessage());


    }
}