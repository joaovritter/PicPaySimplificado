package com.joaozao.picpaysimplificado.mapper;

import com.joaozao.picpaysimplificado.domain.User;
import com.joaozao.picpaysimplificado.dto.UserDTO;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserMapper() {

    }
    public static UserDTO toDto(User user) {
       if (user == null) {
           return null;
       }
       return new UserDTO(
               user.getFirstName(),
               user.getLastName(),
               user.getDocument(),
               user.getBalance(),
               user.getUserType(),
               user.getEmail(),
               user.getPassword());

    }

    public static User toEntity(UserDTO userDto) {
        if (userDto == null) {
            return null;
        }
        User user = new User();
        user.setFirstName(userDto.firstName());
        user.setLastName(userDto.lastName());
        user.setEmail(userDto.email());
        user.setUserType(userDto.userType());
        user.setDocument(userDto.document());
        user.setBalance(userDto.balance());
        user.setPassword(userDto.password());
        return user;


    }


}
