package com.jundevinc.internationalpizza.security.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

/**
 * @author Oleg Pavlyukov
 * on 03.12.2019
 * cpabox777@gmail.com
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class LoginForm {

    @NotBlank
    private String username;
    @NotBlank
    private String password;
}
