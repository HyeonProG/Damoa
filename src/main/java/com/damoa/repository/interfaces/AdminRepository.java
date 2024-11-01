package com.damoa.repository.interfaces;

import com.damoa.dto.admin.AdDTO;
import com.damoa.repository.model.Ad;
import com.damoa.repository.model.User;
import org.apache.ibatis.annotations.Mapper;

import com.damoa.repository.model.Admin;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AdminRepository {

    public Admin findByUsername(String username);

    public List<User> getUserList(@Param("limit") int limit, @Param("offset") int offset);

    public List<User> getAllUser();

    public User getUserDetail(int id);

    public int insertAd(AdDTO ad);

    public List<Ad> getAdList();

    public Ad findById(int id);

    public int deleteAdById(int id);

    public int updateAdById(int id, String title);

    public List<Ad> getAdList(int pageSize, int offset);

    public int countAd();

    public List<AdDTO> activeAd();

}
