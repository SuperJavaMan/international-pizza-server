package com.jundevinc.internationalpizza.api.controller;

import com.jundevinc.internationalpizza.api.model.Pizza;
import com.jundevinc.internationalpizza.api.repository.PizzaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;


@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "/api/pizza")
public class PizzaController {

    @Autowired
    private PizzaRepository pizzaRepository;

//    @PreAuthorize("permitAll()")  //можно не авторизиронным
//    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')") //только авторизиронным
//    писать RequestEntity не нужно так как у нас аннотация не @Controller а @RestController


    @GetMapping
    public List<Pizza> getAllPizza(){
        return pizzaRepository.findAll();
    }




}
