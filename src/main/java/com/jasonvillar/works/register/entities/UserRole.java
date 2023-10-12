package com.jasonvillar.works.register.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(name = "UniqueUserAndRole", columnNames = { "user_id", "role_id" }) })
public class UserRole {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "role_privilege_id_seq")
    @SequenceGenerator(name = "role_privilege_id_seq", sequenceName = "role_privilege_id_seq", allocationSize = 1)
    Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    User user;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;
}
