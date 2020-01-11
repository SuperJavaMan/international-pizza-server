package com.jundevinc.internationalpizza.api.controller;

import com.jundevinc.internationalpizza.api.model.Customer;
import com.jundevinc.internationalpizza.api.model.Order;
import com.jundevinc.internationalpizza.api.model.Pizza;
import com.jundevinc.internationalpizza.api.repository.CustomerRepository;
import com.jundevinc.internationalpizza.api.repository.OrderRepository;
import com.jundevinc.internationalpizza.api.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("api/order")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class OrderController {

    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;
    private PizzaRepository pizzaRepository;

    @Autowired
    public OrderController(OrderRepository orderRepository,
                           CustomerRepository customerRepository,
                           PizzaRepository pizzaRepository) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.pizzaRepository = pizzaRepository;
    }

    @GetMapping("/{id}")
    public Order getOrderById(@PathVariable Long id) {
        return orderRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @GetMapping
    public List<Order> getAllCustomerOrders(Principal principal) {
        Customer customer = customerRepository.findByName(principal.getName())
                .orElseThrow(NoSuchElementException::new);
        return customer.getOrderList();
    }

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @PostMapping
    public Order addOrder(@RequestBody Order orderRequest, Principal principal) {
        Order order = new Order();
        Customer customer = customerRepository.findByName(principal.getName())
                                                .orElseThrow(NoSuchElementException::new);
        order.setCustomer(customer);
        order.setAddress(orderRequest.getAddress());
        order.setPizzaList(orderRequest.getPizzaList());

        Order savedOrder = orderRepository.save(order);
        customer.getOrderList().add(savedOrder);
        customerRepository.save(customer);
        return savedOrder;
    }

    @PutMapping("/{id}")
    public Order updateOrder(@PathVariable Long id,
                             @RequestBody Order requestOrder) {
        Order order = orderRepository.findById(id).orElseThrow(NoSuchElementException::new);
        order.setAddress(requestOrder.getAddress());
        order.setPizzaList(requestOrder.getPizzaList());
        return orderRepository.save(order);
    }

    @PutMapping("/addPizzaToOrder/{order_id}/{pizza_id}")
    public Order addToOrder(@PathVariable Long order_id,
                                  @PathVariable Long pizza_id,
                                  Principal principal) {
        Customer customer = customerRepository.findByName(principal.getName())
                .orElseThrow(NoSuchElementException::new);
        Pizza pizza = pizzaRepository.findById(pizza_id)
                .orElseThrow(NoSuchElementException::new);
        Order order = orderRepository.findById(order_id)
                .orElseThrow(NoSuchElementException::new);

        order.addPizzaToList(pizza);
        Order savedOrder = orderRepository.save(order);

        customer.getOrderList().add(savedOrder);
        customerRepository.save(customer);

        return savedOrder;
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        Order order = orderRepository.findById(id).orElseThrow(NoSuchElementException::new);
        orderRepository.delete(order);
    }
}
