package com.joaozao.picpaysimplificado.repository;

import com.joaozao.picpaysimplificado.domain.User;
import com.joaozao.picpaysimplificado.domain.UserType;
import com.joaozao.picpaysimplificado.dto.UserDTO;
import com.joaozao.picpaysimplificado.mapper.UserMapper;
import com.joaozao.picpaysimplificado.service.UserService;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.util.Optional;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes unitários para o repositório de usuários.
 * Verifica se o método de busca por documento funciona corretamente.

 */
@DataJpaTest
@ActiveProfiles("test")
class UserRepositoryTest {
    @Autowired
    EntityManager entityManager;

    @Autowired
    UserRepository userRepository;

    /**
     * Método executado antes de cada teste para configurar o ambiente.
     *
     * Explicação dos passos:
     * - `entityManager.persist(newUser)`: persiste o usuário no banco de dados.
     * - `entityManager.flush()`: força a persistência dos dados no banco,
     *   garantindo que as operações pendentes no EntityManager sejam efetivadas imediatamente.
     *
     * Dessa forma, assegura que os testes utilizem sempre um estado inicial conhecido.
     */

    @BeforeEach
    public void setUp( ) { //usuário para inicializar o ambiente de teste
        User newUser = new User();
        newUser.setFirstName("João");
        newUser.setLastName("Silva");
        newUser.setDocument("12345678900");
        newUser.setEmail("joao@teste.com");
        newUser.setBalance(BigDecimal.valueOf(100.00));
        newUser.setUserType(UserType.COMMON);

        entityManager.persist(newUser);
        entityManager.flush();
    }

    @Test
    @DisplayName("Should get user successfully by document")
    void findUserByDocumentSuccess() {
        // Arrange
        String document = "12345678900"; // Documento existente no banco (do setUp)

        // Act
        Optional<User> foundUser = userRepository.findUserByDocument(document);

        // Assert
        assertThat(foundUser.isPresent()).isTrue();
        assertThat(foundUser.get().getDocument()).isEqualTo(document);
        assertThat(foundUser.get().getFirstName()).isEqualTo("João");
    }

    @Test
    @DisplayName("Shouldn't get user successfully by non-existent document")
    void findUserByDocumentFailure() {
        // Arrange
        String nonExistentDocument = "000000000"; // Documento não existente no banco (do setUp)

        // Act
        Optional<User> foundUser = userRepository.findUserByDocument(nonExistentDocument);

        // Assert
        assertThat(foundUser.isEmpty()).isTrue(); // Verifica que o Optional está vazio (sem usuário encontrado)


    }


}