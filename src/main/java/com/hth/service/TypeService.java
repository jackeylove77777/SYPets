package com.hth.service;

import com.hth.entity.Type;

import java.util.List;

public interface TypeService {
    List<Type> findTypeList();

    List<Type> findTypeListNums();

    Integer findTypeIdByName(String name);
}
