package com.jasonvillar.works.register.client;

import com.jasonvillar.works.register.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "client_id_seq")
    @SequenceGenerator(name = "client_id_seq", sequenceName = "client_id_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String surname;

    private String identificationNumber;

    private Long userId;

    @Builder
    public Client(String name, String surname, String identificationNumber, long userId) {
        this.name = name;
        this.surname = surname;
        this.identificationNumber = identificationNumber;
        this.userId = userId;
    }
}
