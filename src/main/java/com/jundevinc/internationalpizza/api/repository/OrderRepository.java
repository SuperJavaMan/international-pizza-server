package com.jundevinc.internationalpizza.api.repository;

import com.jundevinc.internationalpizza.api.model.Customer;
import com.jundevinc.internationalpizza.api.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author Oleg Pavlyukov
 * on 19.12.2019
 * cpabox777@gmail.com
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    Optional<List<Order>> findAllByCustomer(Customer customer);
}
