package com.zhangpy.CloudDrive.dao;

import com.zhangpy.CloudDrive.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserMapper {
    void addUser(String username,String password,String email);

    void deleteUser(User user);

    void updateUser(User user);

    String getPassword(String username);

    String getEmail(String username,String password);

    User getUserByNameAndPassword(String username,String password);
}
