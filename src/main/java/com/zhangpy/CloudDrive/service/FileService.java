package com.zhangpy.CloudDrive.service;

import com.zhangpy.CloudDrive.bean.User;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
public class FileService {
    private static final String fileRootPath = System.getProperty("user.dir") + "\\file\\";

    public String getUserDirPath(User user) {
        return fileRootPath + user.getUsername() + "\\";
    }

    public boolean CreateUserDir(User user) {
        String userDirPath = getUserDirPath(user);
        File userDir = new File(userDirPath);
        if (!userDir.exists()) {
            return userDir.mkdir();
        }
        return true;
    }

    public File[] GetFileList(User user) throws Exception{
        String userDirPath = getUserDirPath(user);
        File userDir = new File(userDirPath);
        if (!userDir.exists()) {
            throw new Exception("user dir not exist");
        }
        return userDir.listFiles();
    }

    public boolean UploadFile(User user, MultipartFile file) {
        String userDirPath = getUserDirPath(user);
        File userDir = new File(userDirPath);
        if (!userDir.exists()) {
            return false;
        }
        String fileName = file.getOriginalFilename();
        File newFile = new File(userDirPath + fileName);
        try {
            FileOutputStream fos = new FileOutputStream(newFile);
            BufferedInputStream bis = new BufferedInputStream(file.getInputStream());
            byte[] buffer = new byte[1024];
            int len;
            while ((len = bis.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            bis.close();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean DownloadFile(User user, String fileName, HttpServletResponse response) {
        String userDirPath = getUserDirPath(user);
        File userDir = new File(userDirPath);
        if (!userDir.exists()) {
            throw new RuntimeException("user dir not exist");
        }
        File file = new File(userDirPath + fileName);
        if (!file.exists()) {
            throw new RuntimeException("file not exist");
        }
        FileInputStream fileInputStream = null;
        try {
            fileInputStream = new FileInputStream(file);
            byte[] buffer = new byte[1024];
            int len;
            response.reset();
            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(fileName, StandardCharsets.UTF_8));
            response.addHeader("Content-Length", String.valueOf(file.length()));
            while ((len = fileInputStream.read(buffer)) > 0) {
                response.getOutputStream().write(buffer, 0, len);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fileInputStream != null) {
                    fileInputStream.close();
                }
                response.getOutputStream().close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}
