package com.jundevinc.internationalpizza.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Oleg Pavlyukov
 * on 19.12.2019
 * cpabox777@gmail.com
 */
@Entity
@Data
@NoArgsConstructor
public class Customer implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String cardNumber;

    @JsonIgnore
    @OneToMany(mappedBy = "customer"
//            ,
//    cascade = CascadeType.ALL,
//    orphanRemoval = true
    )
    private List<Order> orderList = new ArrayList<>();

    public Customer(@NotBlank String name, @NotBlank String cardNumber) {
        this.name = name;
        this.cardNumber = cardNumber;
    }
}
