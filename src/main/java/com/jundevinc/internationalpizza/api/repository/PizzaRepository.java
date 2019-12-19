package com.jundevinc.internationalpizza.api.repository;

import com.jundevinc.internationalpizza.api.model.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PizzaRepository  extends JpaRepository<Pizza, Long> {
    Pizza getByName(String name);
}
