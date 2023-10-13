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
    Long id;

    String name;

    @Builder
    public Role(String name, List<Privilege> hasPrivilegeList) {
        this.name = name;
        this.hasPrivilegeList = hasPrivilegeList;
    }

    @ManyToMany(mappedBy = "inRoleList")
    List<Privilege> hasPrivilegeList;
}
