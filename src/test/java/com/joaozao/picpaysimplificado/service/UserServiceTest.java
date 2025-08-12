package com.joaozao.picpaysimplificado.service;

import com.joaozao.picpaysimplificado.domain.User;
import com.joaozao.picpaysimplificado.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserServiceTest {
    private UserService userService;
    private User user;
    @Autowired
    private UserRepository userRepository;

    @BeforeEach
    public void setUp() { //usuário para inicializar o ambiente de teste
        userService = new UserService();
        user = new User();
        user.setFirstName("João");
        user.setLastName("Silva");
        user.setDocument("12345678900");
        user.setEmail("joao@teste.com");
        user.setBalance(BigDecimal.valueOf(100.00));
    }

    @Test
    public void testSaveUser() {
        User savedUser = userService.saveUser(user);
        assertNotNull(savedUser);
        assertEquals(user.getFirstName(), savedUser.getFirstName());
        assertEquals(user.getLastName(), savedUser.getLastName());
        assertEquals(user.getDocument(), savedUser.getDocument());
        assertEquals(user.getEmail(), savedUser.getEmail());
        assertEquals(user.getBalance(), savedUser.getBalance());

    }
}
