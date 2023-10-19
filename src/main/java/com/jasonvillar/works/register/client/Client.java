package com.jasonvillar.works.register.client;

import com.jasonvillar.works.register.user_client.UserClient;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "client_id_seq")
    @SequenceGenerator(name = "client_id_seq", sequenceName = "client_id_seq", allocationSize = 1)
    Long id;

    String name;

    String surname;

    String identificationNumber;

    @Builder
    public Client(String name, String surname, String identificationNumber) {
        this.name = name;
        this.surname = surname;
        this.identificationNumber = identificationNumber;
    }

    @OneToMany(mappedBy = "client")
    List<UserClient> userClientList;
}
