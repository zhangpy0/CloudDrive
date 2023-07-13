package com.zhangpy.CloudDrive;

import com.zhangpy.CloudDrive.bean.User;
import com.zhangpy.CloudDrive.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class CloudDriveApplicationTests {

    @Autowired
    private UserService UserService;

    @Test
    void contextLoads() {
        User user = UserService.getUserByNameAndPassword("admin","admin");
        System.out.println(user.getUsername());
        System.out.println(user.getPassword());
    }
}
