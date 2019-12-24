package com.jundevinc.internationalpizza.api.controller;

import com.jundevinc.internationalpizza.api.model.Pizza;
import com.jundevinc.internationalpizza.api.model.PizzaDTO;
import com.jundevinc.internationalpizza.api.repository.PizzaRepository;
import com.jundevinc.internationalpizza.api.service.IconStorageService;
import com.jundevinc.internationalpizza.api.utils.FileStorageUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/api/pizza")
public class PizzaController {

    private PizzaRepository pizzaRepository;
    private IconStorageService storageService;

    @Autowired
    public PizzaController(PizzaRepository pizzaRepository,
                           IconStorageService storageService) {
        this.pizzaRepository = pizzaRepository;
        this.storageService = storageService;
    }

    @GetMapping("/all")
    public List<Pizza> getAllPizza(){
        return pizzaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Pizza getPizzaById(@PathVariable Long id) {
        return pizzaRepository.findById(id).orElseThrow(NoSuchElementException::new);
    }

    @GetMapping("/icon/{filename:.+}")
    public ResponseEntity<Resource> getIcon(@PathVariable String filename) {
        Resource icon = storageService.getIcon(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + icon.getFilename() + "\"")
                .body(icon);
    }

    @PostMapping
    public Pizza addPizza(@ModelAttribute PizzaDTO pizzaDTO) {
        Pizza pizza = new Pizza();
        MultipartFile icon = pizzaDTO.getMultipartFile();

        boolean isSavedOk = storageService.storeIcon(icon);
        String iconUrl = FileStorageUtils.getUrlForIcon(icon.getOriginalFilename());

        pizza.setName(pizzaDTO.getName());
        pizza.setSize(pizzaDTO.getSize());
        pizza.setPrice(pizzaDTO.getPrice());
        pizza.setIcon(iconUrl);
        if (isSavedOk) {
            return pizzaRepository.save(pizza);
        } else {
            throw  new RuntimeException("Pizza saving error");
        }
    }

    @PutMapping("/{id}")
    public Pizza updatePizza(@PathVariable Long id,
                             @ModelAttribute PizzaDTO pizzaDTO) {
        MultipartFile icon = pizzaDTO.getMultipartFile();
        Pizza pizzaUpdate = pizzaRepository.findById(id).orElseThrow(NoSuchElementException::new);
        boolean isDeletedOk;
        boolean isSavedOk;

        if (icon != null) {
            if (pizzaUpdate.getIcon() != null) {
                isDeletedOk = storageService.deleteIcon(pizzaUpdate.getIcon());
            } else isDeletedOk = true;
            isSavedOk = storageService.storeIcon(icon);
            String iconUrl = FileStorageUtils.getUrlForIcon(icon.getOriginalFilename());

            pizzaUpdate.setIcon(iconUrl);
        } else {
            isDeletedOk = true;
            isSavedOk = true;
        }

        pizzaUpdate.setName(pizzaDTO.getName());
        pizzaUpdate.setSize(pizzaDTO.getSize());
        pizzaUpdate.setPrice(pizzaDTO.getPrice());

        if (isSavedOk && isDeletedOk) {
            return pizzaRepository.save(pizzaUpdate);
        } else {
            throw  new RuntimeException("Pizza updating error");
        }
    }

    @DeleteMapping("/{id}")
    public void deletePizza(@PathVariable Long id) {
        Pizza pizza = pizzaRepository.findById(id).orElseThrow(NoSuchElementException::new);
        String iconUrl = pizza.getIcon();
        if (iconUrl != null) {
            storageService.deleteIcon(iconUrl);
        }
        pizzaRepository.delete(pizza);
    }
}
