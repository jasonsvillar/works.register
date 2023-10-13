package com.jasonvillar.works.register.entities;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

@Entity
@Table(name = "`user`")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "user_id_seq")
    @SequenceGenerator(name = "user_id_seq", sequenceName = "user_id_seq", allocationSize = 1)
    private Long id;

    private String name;

    private String email;

    private String password;

    @Builder
    public User(String name, String email, String password) {
        this.name = name;
        this.email = email;
        this.password = password;
    }

    @ManyToMany(mappedBy = "inUserList")
    Set<Role> hasRoleList;

    public void addRole(Role role) {
        this.hasRoleList.add(role);
        role.getInUserList().add(this);
    }

    public void removeRole(Role role) {
        this.hasRoleList.remove(role);
        role.getInUserList().remove(this);
    }

    @Transient
    List<SimpleGrantedAuthority> authorityList;
}
