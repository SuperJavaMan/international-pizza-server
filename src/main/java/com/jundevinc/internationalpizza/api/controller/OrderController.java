package com.jundevinc.internationalpizza.api.controller;

import com.jundevinc.internationalpizza.api.model.Customer;
import com.jundevinc.internationalpizza.api.model.Order;
import com.jundevinc.internationalpizza.api.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("api/order")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class OrderController {

    private OrderRepository orderRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @GetMapping("/{id}")
    public Order getOrders(@PathVariable Long id) {
        return orderRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @GetMapping
    public List<Order> getAllCustomerOrders(@RequestBody Customer customer) {
        return orderRepository.findAllByCustomer(customer).orElseThrow(NoSuchElementException::new);
    }

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PostMapping
    public Order addOrder(@RequestBody Order order) {
        return orderRepository.save(order);
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id,
                             @RequestBody Order requestOrder) {
        Order order = orderRepository.findById(id).orElseThrow(NoSuchElementException::new);
        order.setAddress(requestOrder.getAddress());
        order.setPizzaList(requestOrder.getPizzaList());
        return orderRepository.save(order);
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow(NoSuchElementException::new);
        orderRepository.delete(order);
    }
}
