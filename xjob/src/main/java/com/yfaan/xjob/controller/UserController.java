package com.yfaan.xjob.controller;

import com.yfaan.xjob.dto.LoginFormDTO;
import com.yfaan.xjob.dto.Result;
import com.yfaan.xjob.dto.UserDTO;
import com.yfaan.xjob.dto.UserInfoDTO;
import com.yfaan.xjob.entity.User;
import com.yfaan.xjob.entity.UserInfo;
import com.yfaan.xjob.service.IUserInfoService;
import com.yfaan.xjob.service.IUserService;
import com.yfaan.xjob.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
@Slf4j //日志
@RestController
@RequestMapping("/user")
public class UserController {
    @Resource
    private IUserService userService;
    @Resource
    private IUserInfoService userInfoService;

    /**
     * 发送手机验证码
     */

    //多台Tomcat不共享session存储空间，需要使用redis
    @PostMapping("/code")
    //RequestParam用于从 HTTP 请求的 URL 参数里获取值
    //session 负责在服务器端存储用户的会话信息，cookie 则在客户端（浏览器）存储相关标识
    public Result sendCode(@RequestParam("phone") String phone) {
        //通过 @RequestParam 从 URL 参数中获取手机号码
        log.info("收到验证码请求，手机号: {}", phone);  // 添加这行
        return userService.sendCode(phone);
    }

    /**
     * 登录功能
     * @param loginForm 登录参数，包含手机号、验证码；或者手机号、密码
     */
    @PostMapping("/login")
    //@RequestBody用于从 HTTP 请求体中获取数据
    public Result login(@RequestBody LoginFormDTO loginForm){
        //通过 @RequestBody 从请求体中获取登录表单数据
        return userService.login(loginForm);
    }
    /**
     * 登出功能
     * @return 无
     */
    @PostMapping("/logout")
    public Result logout(){
        String token = UserHolder.getToken();
        Result result = userService.logout(token);
        // 登出后清除ThreadLocal中的用户信息和token
        UserHolder.removeUser();
        UserHolder.removeToken();
        log.info("用户已登出");
        return result;
    }
    //通过 @PathVariable 从 URL 路径中获取 token
    /**
     * 获取当前登录用户信息
     * */
    @GetMapping("/me")
    public Result me(){
        UserDTO user = UserHolder.getUser();
        return Result.ok(user);
    }

    /**
     * 获取当前登录用户并查询其详细信息
     * */
    @GetMapping("/info/{id}")
    //@PathVariable用于从 URL 中获取变量
    public Result info(@PathVariable("id") Long userId){
        // 查询详情
        UserInfo info = userInfoService.getById(userId);
        if (info == null) {
            // 没有详情，应该是第一次查看详情
            return Result.ok();
        }
        info.setCreateTime(null);
        info.setUpdateTime(null);
        // 返回
        return Result.ok(info);
    }

    /**
     * 更新当前登录用户基本信息
     * */
    @PostMapping("/update")
    public Result update(@RequestBody UserDTO userDTO){
        return userService.update(userDTO);
    }

    /**
     * 更新当前登录用户详细信息
     * */
    @PostMapping("/info/update")
    public Result updateInfo(@RequestBody UserInfoDTO userInfoDTO) {
        return userInfoService.update(userInfoDTO);
    }

    @PostMapping("/avatar")
    public Result uploadAvatar(@RequestPart("file") MultipartFile file) {
        return userService.updateAvatar(file);
    }
}