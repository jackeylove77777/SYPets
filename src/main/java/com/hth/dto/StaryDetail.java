package com.hth.dto;

import com.hth.entity.Stray;
import lombok.Data;

@Data
public class StaryDetail extends Stray {
    private String username;
    private String avatar;
}
