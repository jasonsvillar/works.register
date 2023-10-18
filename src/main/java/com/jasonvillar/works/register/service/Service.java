package com.jasonvillar.works.register.service;

import com.jasonvillar.works.register.user_service.UserService;
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
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "service_id_seq")
    @SequenceGenerator(name = "service_id_seq", sequenceName = "service_id_seq", allocationSize = 1)
    Long id;

    String name;

    @Builder
    public Service(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "service")
    List<UserService> userServiceList;
}
