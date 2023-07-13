package com.zhangpy.CloudDrive.service;

import com.zhangpy.CloudDrive.bean.User;
import com.zhangpy.CloudDrive.dao.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public void addUser(String username,String password,String email){
        userMapper.addUser(username,password,email);
    }

    public void deleteUser(User user){
        userMapper.deleteUser(user);
    }

    public void updateUser(User user){
        userMapper.updateUser(user);
    }

    public String getPassword(String username){
        return userMapper.getPassword(username);
    }

    public String getEmail(String username,String password){
        return userMapper.getEmail(username,password);
    }

    public User getUserByNameAndPassword(String username,String password){
        return userMapper.getUserByNameAndPassword(username,password);
    }
}
