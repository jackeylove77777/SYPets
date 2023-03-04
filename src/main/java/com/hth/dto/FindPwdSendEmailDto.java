package com.hth.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * 在找回密码的接收json数据
 * 并且校验
 */
@NoArgsConstructor
@Data
public class FindPwdSendEmailDto implements Serializable {
    @Size(min = 3,max = 20)
    @NotBlank(message = "昵称不能为空")
    private String username;


    @NotBlank
    @Email
    private String email;
}
