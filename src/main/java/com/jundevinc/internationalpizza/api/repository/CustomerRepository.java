package com.jundevinc.internationalpizza.api.repository;

import com.jundevinc.internationalpizza.api.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Oleg Pavlyukov
 * on 19.12.2019
 * cpabox777@gmail.com
 */
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Optional<Customer> findByName(String name);
}
