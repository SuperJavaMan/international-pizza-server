package com.jundevinc.internationalpizza.api.controller;

import com.jundevinc.internationalpizza.api.model.Pizza;
import com.jundevinc.internationalpizza.api.model.PizzaDTO;
import com.jundevinc.internationalpizza.api.repository.PizzaRepository;
import com.jundevinc.internationalpizza.api.service.IconStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;


@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping(value = "/api/pizza/all")
public class PizzaController {

    private PizzaRepository pizzaRepository;
    private IconStorageService storageService;

    @Autowired
    public PizzaController(PizzaRepository pizzaRepository) {
        this.pizzaRepository = pizzaRepository;
    }

    @GetMapping
    public List<Pizza> getAllPizza(){
        return pizzaRepository.findAll();
    }

    @GetMapping("{id}")
    public Pizza getPizzaById(@PathVariable Long id) {
        return pizzaRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @PostMapping
    public Pizza addPizza(@ModelAttribute PizzaDTO pizzaDTO) {
        Pizza pizza = new Pizza();
        MultipartFile icon = pizzaDTO.getMultipartFile();

        return null;
    }
}
