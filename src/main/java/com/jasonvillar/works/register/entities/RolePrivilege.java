package com.jasonvillar.works.register.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(uniqueConstraints = { @UniqueConstraint(name = "UniqueRoleAndPrivilege", columnNames = { "role_id", "privilege_id" }) })
public class RolePrivilege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "role_privilege_id_seq")
    @SequenceGenerator(name = "role_privilege_id_seq", sequenceName = "role_privilege_id_seq", allocationSize = 1)
    Long id;

    @ManyToOne
    @JoinColumn(name = "role_id")
    Role role;

    @ManyToOne
    @JoinColumn(name = "privilege_id")
    Privilege privilege;
}
