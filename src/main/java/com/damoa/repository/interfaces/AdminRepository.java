package com.damoa.repository.interfaces;

import org.apache.ibatis.annotations.Mapper;

import com.damoa.repository.model.Admin;

@Mapper
public interface AdminRepository {

    public Admin findByUsername(String username);

}
