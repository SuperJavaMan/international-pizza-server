package com.jundevinc.internationalpizza.api.controller;

import com.jundevinc.internationalpizza.api.model.Customer;
import com.jundevinc.internationalpizza.api.repository.CustomerRepository;
import com.jundevinc.internationalpizza.security.model.User;
import com.jundevinc.internationalpizza.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.NoSuchElementException;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/customer")
@PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
public class CustomerController {

    private CustomerRepository customerRepository;
    private UserRepository userRepository;

    @Autowired
    public CustomerController(CustomerRepository customerRepository,
                              UserRepository userRepository) {
        this.customerRepository = customerRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/carent")
    public Customer getCarentCastomer(Principal principal){
        return customerRepository.findByName(principal.getName()).get();
    }




    @GetMapping("/{id}")
    public Customer getCustomer(@PathVariable Long id) {
        return customerRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @GetMapping
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @PostMapping
    public Customer addCustomer(@RequestBody Customer customer) {
        return customerRepository.save(customer);
    }

    @PutMapping("/{id}")
    public Customer updateCustomerInfo(@PathVariable Long id, @RequestBody Customer requestCustomer) {
        Customer customer = customerRepository.findById(id).orElseThrow(NoSuchElementException::new);
        customer.setName(requestCustomer.getName());
        customer.setCardNumber(requestCustomer.getCardNumber());

        User user = userRepository.findUserByUsername(requestCustomer.getName()).orElseThrow(NoSuchElementException::new);
        user.setUsername(requestCustomer.getName());
        userRepository.save(user);

        return customerRepository.save(customer);
    }

    @DeleteMapping("/{id}")
    public void deleteCustomer(@PathVariable Long id) {
        Customer customer = customerRepository.findById(id).orElseThrow(NoSuchElementException::new);
        customerRepository.delete(customer);
    }
}
