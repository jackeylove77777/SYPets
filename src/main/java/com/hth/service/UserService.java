package com.hth.service;
import com.hth.dto.RegisterDto;

import java.util.Map;
public interface UserService {
    Map<String,String> register(RegisterDto registerDto);
    int exists(String username);
}
