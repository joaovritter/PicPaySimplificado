package com.joaozao.picpaysimplificado.service;

import com.joaozao.picpaysimplificado.domain.Transaction;
import com.joaozao.picpaysimplificado.domain.User;
import com.joaozao.picpaysimplificado.domain.UserType;
import com.joaozao.picpaysimplificado.dto.UserDTO;
import com.joaozao.picpaysimplificado.mapper.UserMapper;
import com.joaozao.picpaysimplificado.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;


    public void validateTransaction (User sender, BigDecimal amount) throws Exception {
        if (sender.getUserType() == UserType.MERCHANT){
            throw new Exception("Lojista não está autorizado a realizar transações");
        }
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception("Saldo Insuficiente");
        }

    }

    public User createUser(UserDTO userDto) {
      User newUser = UserMapper.toEntity(userDto);
      return this.saveUser(newUser);
    }

    public User saveUser(User user) {
        return userRepository.save(user);
    }

    public User findUserById(Long id) throws Exception {
        return userRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuário não encontrado"));
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
