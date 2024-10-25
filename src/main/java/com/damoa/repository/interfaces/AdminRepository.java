package com.damoa.repository.interfaces;

import com.damoa.repository.model.User;
import org.apache.ibatis.annotations.Mapper;

import com.damoa.repository.model.Admin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminRepository {

    public Admin findByUsername(String username);
    public List<User> getUserList(@Param("limit")int limit, @Param("offset") int offset);
    public List<User> getAllUser();
    public User getUserDetail(int id);


}
