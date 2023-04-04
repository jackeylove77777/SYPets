package com.hth.dto;

import com.hth.entity.Foster;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
public class FosterDetail extends Foster {
    private String username;
    private String avatar;
}
