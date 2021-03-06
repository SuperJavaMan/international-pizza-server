package com.jundevinc.internationalpizza.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.lang.Nullable;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Oleg Pavlyukov
 * on 22.12.2019
 * cpabox777@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class PizzaDTO {
    private Long id;
    private String name;
    private String size;
    private int price;
    private MultipartFile multipartFile;
}
