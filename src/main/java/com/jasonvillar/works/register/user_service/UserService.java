package com.jasonvillar.works.register.user_service;

import com.jasonvillar.works.register.service.Service;
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
public class UserService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_service_id_seq")
    @SequenceGenerator(name = "user_service_id_seq", sequenceName = "user_service_id_seq", allocationSize = 1)
    Long id;

    @Column(name = "user_id")
    Long userId;

    @Column(name = "service_id")
    Long serviceId;

    @ManyToOne
    @JoinColumn(name = "service_id", insertable = false, updatable = false)
    Service service;

    @ManyToOne
    @JoinColumn(name = "user_id", insertable = false, updatable = false)
    User user;

    @Builder
    public UserService(long id, long userId, long serviceId) {
        this.id = id;
        this.userId = userId;
        this.serviceId = serviceId;
    }
}
