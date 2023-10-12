package com.jasonvillar.works.register.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "role_id_seq")
    @SequenceGenerator(name = "role_id_seq", sequenceName = "role_id_seq", allocationSize = 1)
    Integer id;

    String name;

    @Builder
    public Role(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "role")
    List<RolePrivilege> rolePrivilegeList;
}
