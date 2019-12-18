package com.jundevinc.internationalpizza.security.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author Oleg Pavlyukov
 * on 03.12.2019
 * cpabox777@gmail.com
 */
@Data
public class RegForm {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotNull
    private MultipartFile file;
}
