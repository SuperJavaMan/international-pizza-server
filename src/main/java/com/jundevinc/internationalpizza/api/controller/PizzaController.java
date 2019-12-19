package com.jundevinc.internationalpizza.api.controller;

import com.jundevinc.internationalpizza.api.model.Pizza;
import com.jundevinc.internationalpizza.api.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/api/pizza")
public class PizzaController {

    @Autowired
    private PizzaRepository pizzaRepository;

//    @PreAuthorize("permitAll()")  //можно не авторизиронным
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") //только авторизиронным
//    писать RequestEntity не нужно так как у нас не @Controller а @RestController

    @PreAuthorize("permitAll()")
    @GetMapping
    public List<Pizza> getAllPizza(){
        return pizzaRepository.findAll();
    }




}
