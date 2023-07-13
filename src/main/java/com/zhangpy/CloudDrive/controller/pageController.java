package com.zhangpy.CloudDrive.controller;

import com.zhangpy.CloudDrive.service.FileService;
import com.zhangpy.CloudDrive.util.StringJudge;
import com.zhangpy.CloudDrive.bean.User;
import com.zhangpy.CloudDrive.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Controller
public class pageController {

    private static final Logger logger = LoggerFactory.getLogger(pageController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private FileService fileService;

    @RequestMapping("/index")
    public String index(){
        logger.info(System.getProperty("user.dir"));
        return "index";
    }

    @RequestMapping("/register")
    public String register(){
        return "register";
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(HttpServletRequest request,
                        @RequestParam(value = "username") String username,
                        @RequestParam(value = "password") String password,
                        Model model){
        logger.info("username:"+username+" password:"+password);
        if (!(StringJudge.isInRange(username) && StringJudge.isInRange(password))){
            logger.error("用户名或密码不合法");
            model.addAttribute("msg","用户名或密码不合法");
            return "LoginFail";
        }
        try {
            User user = userService.getUserByNameAndPassword(username,password);
            if(user!=null){
                if (!fileService.CreateUserDir(user)){
                    logger.error("创建用户文件夹失败");
                    model.addAttribute("msg","创建用户文件夹失败");
                    return "LoginFail";
                }
                model.addAttribute("user",user);
                HttpSession session = request.getSession();
                session.setAttribute("user",user);
                return "user";
        }
            else {
                // 密码错误
                logger.error("user为空");
                model.addAttribute("msg","用户名或密码错误");
                return "LoginFail";
            }
        } catch (Exception e) {
            e.printStackTrace();
            //
            logger.error("登录失败");
            model.addAttribute("msg","登录失败");
            return "LoginFail";
        }
    }

    @RequestMapping(value = "/register",method = RequestMethod.POST)
    public String register(@RequestParam(value = "username") String username,
                           @RequestParam(value = "password1") String password1,
                            @RequestParam(value = "password2") String password2,
                           @RequestParam(value = "email") String email,
                           Model model){
        logger.info("username:"+username+" password1:"+password1+" password2"+password2+" email:"+email);
        if(!password1.equals(password2)){
            logger.error("两次密码不一致");
            model.addAttribute("msg","两次密码不一致");
            return "RegisterFail";
        }
        if (!(StringJudge.isInRange(username) && StringJudge.isInRange(password1) && StringJudge.isInRange(password2) && StringJudge.isEmail(email))){
            logger.error("用户名或密码或邮箱不合法");
            model.addAttribute("msg","用户名或密码或邮箱不合法");
            return "RegisterFail";
        }
        else {
            try {
                userService.addUser(username,password1,email);
                User user = userService.getUserByNameAndPassword(username,password1);
                if (user==null || !user.getEmail().equals(email)){
                    logger.error("注册失败");
                    model.addAttribute("msg","用户名或邮箱已存在");
                    return "RegisterFail";
                }
                if (fileService.CreateUserDir(user)){
                    logger.info("注册成功");
                    model.addAttribute("user",user);
                    return "user";
                }
                else {
                    logger.error("注册失败");
                    model.addAttribute("msg","文件夹创建失败");
                    return "RegisterFail";
                }
            } catch (Exception e) {
                e.printStackTrace();
                logger.error("注册失败");
                model.addAttribute("msg","注册失败");
                return "RegisterFail";
            }
        }
    }

    @RequestMapping("/user")
    public String user(HttpServletRequest request,
                       Model model) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user==null){
            return "index";
        }
        else {
            int fileCount = fileService.GetFileList(user).length;
            model.addAttribute("user",user);
            model.addAttribute("fileCount",fileCount);
            return "user";
        }
    }

    @RequestMapping("/upload")
    public String upload(HttpServletRequest request,
                         Model model) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user==null){
            return "index";
        }
        else {
            int fileCount = fileService.GetFileList(user).length;
            model.addAttribute("user",user);
            model.addAttribute("fileCount",fileCount);
            return "upload";
        }
    }

    @RequestMapping("/uploadFile")
    public String uploadFile(HttpServletRequest request,
                             @RequestParam(value = "file") MultipartFile file,
                             Model model) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user==null) {
            model.addAttribute("msg","请先登录");
            return "LoginFail";
        }
        else {
            if (fileService.UploadFile(user,file)){
                model.addAttribute("user",user);
                return "UploadSuccess";
            }
            else {
                model.addAttribute("msg","上传失败");
                return "UploadFail";
            }
        }
    }

    @RequestMapping("/FileList")
    public String fileList(HttpServletRequest request,
                           Model model) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user==null){
            return "index";
        }
        else {
            File[] files = fileService.GetFileList(user);
            String[] fileNames = new String[files.length];
            for (int i=0;i<files.length;i++){
                fileNames[i] = files[i].getName();
            }
            int fileCount = files.length;
            model.addAttribute("user",user);
            model.addAttribute("fileNames",fileNames);
            return "FileList";
        }
    }

    @RequestMapping("/download/{fileName}")
    public String download(HttpServletRequest request,
                           HttpServletResponse response,
                           @PathVariable("fileName") String fileName,
                           Model model) throws Exception {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user==null){
            return "index";
        }
        else {
            if (fileService.DownloadFile(user,fileName,response)){
                model.addAttribute("user",user);
                model.addAttribute("fileName",fileName);
                return "DownloadSuccess";
            }
            else {
                model.addAttribute("msg","下载失败");
                return "DownloadFail";
            }
        }
    }
}
