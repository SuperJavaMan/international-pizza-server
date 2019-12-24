package com.jundevinc.internationalpizza.api.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

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
@Table(name = "orders")
public class Order implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private Customer customer;

    @NotBlank
    private String address;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "order_pizza",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "pizza_id")
    )
    private List<Pizza> pizzaList = new ArrayList<>();

    public Order(Customer customer, @NotBlank String address, List<Pizza> pizzaList) {
        this.customer = customer;
        this.address = address;
        this.pizzaList = pizzaList;
    }
}
