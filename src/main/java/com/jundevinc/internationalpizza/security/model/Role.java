package com.jundevinc.internationalpizza.security.model;

import lombok.Data;
import org.hibernate.annotations.NaturalId;

import javax.persistence.*;

/**
 * @author Oleg Pavlyukov
 * on 03.12.2019
 * cpabox777@gmail.com
 */
@Entity
@Data
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column
    @Enumerated(EnumType.STRING)
    @NaturalId
    private Roles userRole;

    public Role(Roles userRole) {
        this.userRole = userRole;
    }

    public Role() {
    }
}
