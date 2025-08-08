package com.joaozao.picpaysimplificado.domain;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(of = "id")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstname;

    private String lastname;

    @Column(unique = true)
    private String document;

    @Column(unique = true)
    private String email;

    private String password;

    private String balance;

    @Enumerated(EnumType.STRING)
    private UserType userType;

}
