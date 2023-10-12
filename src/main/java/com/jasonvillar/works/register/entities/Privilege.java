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
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "privilege_id_seq")
    @SequenceGenerator(name = "privilege_id_seq", sequenceName = "privilege_id_seq", allocationSize = 1)
    Long id;

    String name;

    @Builder
    public Privilege(String name) {
        this.name = name;
    }

    @OneToMany(mappedBy = "privilege")
    List<RolePrivilege> rolePrivilegeList;
}
