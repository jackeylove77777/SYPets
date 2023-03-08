package com.hth.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 修改密码的校验
 */
@Data
public class UpdatePasswordDto implements Serializable {
    @Size(min = 6,max = 15)
    @NotBlank(message = "密码不能为空")
    private String lastPassword;

    @Size(min = 6,max = 15)
    @NotBlank(message = "密码不能为空")
    private String newPassword;

    @Size(min = 6,max = 15)
    @NotBlank(message = "密码不能为空")
    private String secondPassword;
}