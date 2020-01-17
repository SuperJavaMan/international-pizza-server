package com.jundevinc.internationalpizza.api.controller;

import com.jundevinc.internationalpizza.api.model.Pizza;
import com.jundevinc.internationalpizza.api.model.PizzaDTO;
import com.jundevinc.internationalpizza.api.repository.PizzaRepository;
import com.jundevinc.internationalpizza.api.service.IconStorageService;
import com.jundevinc.internationalpizza.api.utils.FileStorageUtils;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.NoSuchElementException;


@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping(value = "/api/pizza")
@Slf4j
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
        log.info("getAllPizza() invocation");
        return pizzaRepository.findAll();
    }

    @GetMapping("/{id}")
    public Pizza getPizzaById(@PathVariable Long id) {
        log.info("getPizzaById() invocation");
        return pizzaRepository.findById(id).orElseThrow(() -> {
            log.debug("Pizza with id = " + id + " did not exists");
            return new NoSuchElementException("Pizza with id = " + id + " did not exists");
        });
    }

    @GetMapping("/icon/{filename:.+}")
    public ResponseEntity<Resource> getIcon(@PathVariable String filename) {
        log.info("getIcon() invocation");
        log.debug("Filename -> " + filename);

        Resource icon = storageService.getIcon(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + icon.getFilename() + "\"")
                .body(icon);
    }

    @PostMapping
    public Pizza addPizza(@ModelAttribute PizzaDTO pizzaDTO) {
        log.info("addPizza() invocation");
        log.debug("PizzaDto -> " + pizzaDTO);

        Pizza pizza = new Pizza();
        MultipartFile icon = pizzaDTO.getMultipartFile();

        boolean isSavedOk = storageService.storeIcon(icon);
        String iconUrl = FileStorageUtils.getUrlForIcon(icon.getOriginalFilename());

        pizza.setName(pizzaDTO.getName());
        pizza.setSize(pizzaDTO.getSize());
        pizza.setPrice(pizzaDTO.getPrice());
        pizza.setIcon(iconUrl);
        if (isSavedOk) {
            log.debug("Pizza saved");
            return pizzaRepository.save(pizza);
        } else {
            log.debug("Pizza saving error");
            throw  new RuntimeException("Pizza saving error");
        }
    }

    @PutMapping("/{id}")
    public Pizza updatePizza(@PathVariable Long id,
                             @ModelAttribute PizzaDTO pizzaDTO) {
        log.info("updatePizza() invocation");
        log.debug("Id - > " + id + "; PizzaDto -> " + pizzaDTO);

        MultipartFile icon = pizzaDTO.getMultipartFile();
        Pizza pizzaUpdate = pizzaRepository.findById(id).orElseThrow(() -> {
                                        log.debug("Pizza with id = " + id + " did not exists");
                                        return new NoSuchElementException("Pizza with id = " + id + " did not exists");
                                    });
        boolean isDeletedOk;
        boolean isSavedOk;

        if (icon != null) {
            log.debug("Icon updating...");
            if (pizzaUpdate.getIcon() != null) {
                log.debug("Delete an old icon");
                isDeletedOk = storageService.deleteIcon(pizzaUpdate.getIcon());
            } else {
                log.debug("Pizza have no an icon");
                isDeletedOk = true;
            }
            isSavedOk = storageService.storeIcon(icon);
            String iconUrl = FileStorageUtils.getUrlForIcon(icon.getOriginalFilename());

            pizzaUpdate.setIcon(iconUrl);
        } else {
            log.debug("Icon no need to be updated");
            isDeletedOk = true;
            isSavedOk = true;
        }

        pizzaUpdate.setName(pizzaDTO.getName());
        pizzaUpdate.setSize(pizzaDTO.getSize());
        pizzaUpdate.setPrice(pizzaDTO.getPrice());

        if (isSavedOk && isDeletedOk) {
            log.debug("Icon is updated ok");
            return pizzaRepository.save(pizzaUpdate);
        } else {
            log.debug("Icon is not updated");
            throw  new RuntimeException("Pizza updating error");
        }
    }

    @DeleteMapping("/{id}")
    public void deletePizza(@PathVariable Long id) {
        log.info("deletePizza() invocation");
        log.debug("Pizza id -> " + id);
        Pizza pizza = pizzaRepository.findById(id).orElseThrow(() -> {
                                                log.debug("Pizza with id = " + id + " did not exists");
                                                return new NoSuchElementException("Pizza with id = " + id + " did not exists");
                                            });
        String iconUrl = pizza.getIcon();
        if (iconUrl != null) {
            storageService.deleteIcon(iconUrl);
        }
        pizzaRepository.delete(pizza);
    }
}
