package com.jasonvillar.works.register.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class UserClient {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_client_id_seq")
    @SequenceGenerator(name = "user_client_id_seq", sequenceName = "user_client_id_seq", allocationSize = 1)
    Long id;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "client_id")
    Long clientId;

    @ManyToOne
    @JoinColumn(name = "client_id", insertable = false, updatable = false)
    Client client;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    User user;

    @Builder
    public UserClient(long userId, long clientId) {
        this.userId = userId;
        this.clientId = clientId;
    }
}
