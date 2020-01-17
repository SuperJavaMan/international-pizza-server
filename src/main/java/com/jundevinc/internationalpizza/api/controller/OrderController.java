package com.jundevinc.internationalpizza.api.controller;

import com.jundevinc.internationalpizza.api.model.Customer;
import com.jundevinc.internationalpizza.api.model.Order;
import com.jundevinc.internationalpizza.api.model.Pizza;
import com.jundevinc.internationalpizza.api.repository.CustomerRepository;
import com.jundevinc.internationalpizza.api.repository.OrderRepository;
import com.jundevinc.internationalpizza.api.repository.PizzaRepository;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
        log.info("getOrderById() invoked");
        log.debug("Order id -> " + id);

        return orderRepository.findById(id).orElseThrow(() -> {
                                                log.debug("Order with id -> " + id + " not found");
                                                return new NoSuchElementException("Order with id -> " + id + " not found");
                                            });
    }

    @GetMapping
    public List<Order> getAllCustomerOrders(Principal principal) {
        log.info("getAllCustomerOrders() invoked");
        log.debug("Principal name -> " + principal.getName());

        Customer customer = customerRepository.findByName(principal.getName())
                                                .orElseThrow(NoSuchElementException::new);
        return customer.getOrderList();
    }

    @GetMapping("/all")
    public List<Order> getAllOrders() {
        log.info("getAllOrders() invoked");

        return orderRepository.findAll();
    }

    @PostMapping
    public Order addOrder(@RequestBody Order orderRequest,
                                        Principal principal) {
        log.info("addOrder() invoked");
        log.debug("Request order -> " + orderRequest);

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
        log.info("updateOrder() invoked");
        log.debug("Request order -> " + requestOrder);

        Order order = orderRepository.findById(id).orElseThrow(() -> {
                                                log.debug("Order with id -> " + id + " not found");
                                                return new NoSuchElementException("Order with id -> " + id + " not found");
                                            });
        order.setAddress(requestOrder.getAddress());
        order.setPizzaList(requestOrder.getPizzaList());
        return orderRepository.save(order);
    }

    @PutMapping("/addPizzaToOrder/{order_id}/{pizza_id}")
    public Order addToOrder(@PathVariable Long order_id,
                                  @PathVariable Long pizza_id,
                                  Principal principal) {
        log.info("addToOrder() invoked");
        log.debug("Order id -> " + order_id + "; Pizza id -> " + pizza_id);
        Customer customer = customerRepository.findByName(principal.getName())
                                                .orElseThrow(NoSuchElementException::new);
        Pizza pizza = pizzaRepository.findById(pizza_id)
                                        .orElseThrow(() -> {
                                            log.debug("Pizza with id = " + pizza_id + " did not exists");
                                            return new NoSuchElementException("Pizza with id = " + pizza_id + " did not exists");
                                        });
        Order order = orderRepository.findById(order_id)
                                        .orElseThrow(() -> {
                                            log.debug("Order with id -> " + order_id + " not found");
                                            return new NoSuchElementException("Order with id -> " + order_id + " not found");
                                        });

        order.addPizzaToList(pizza);
        Order savedOrder = orderRepository.save(order);

        customer.getOrderList().add(savedOrder);
        customerRepository.save(customer);

        return savedOrder;
    }

    @DeleteMapping("/{id}")
    public void deleteOrder(@PathVariable Long id) {
        log.info("deleteOrder() invoked");
        Order order = orderRepository.findById(id)
                                        .orElseThrow(() -> {
                                            log.debug("Order with id -> " + id + " not found");
                                            return new NoSuchElementException("Order with id -> " + id + " not found");
                                        });
        orderRepository.delete(order);
    }
}
