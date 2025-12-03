package com.yfaan.xjob.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfaan.xjob.dto.LoginFormDTO;
import com.yfaan.xjob.dto.Result;
import com.yfaan.xjob.dto.UserDTO;
import com.yfaan.xjob.entity.User;
import org.springframework.web.multipart.MultipartFile;

public interface IUserService extends IService<User> {
    //发送短信验证码
    Result sendCode(String phone);
    //登录功能
    Result login(LoginFormDTO loginForm);
    //登出功能
    Result logout(String token);
    //修改用户信息
    Result update(UserDTO userDTO);
    //修改头像
    Result updateAvatar(MultipartFile file);
}
