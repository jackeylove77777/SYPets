package com.hth.dto;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * 用来接收email，并且做校验工作
 */
@Data
public class EmailDto implements Serializable {
    @NotBlank
    @Email(message = "邮箱格式不正确")
    private String email;
}
