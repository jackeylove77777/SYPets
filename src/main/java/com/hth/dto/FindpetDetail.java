package com.hth.dto;

import com.hth.entity.Findpet;
import lombok.Data;

@Data
public class FindpetDetail extends Findpet {
    private String username;
    private String avatar;
}
