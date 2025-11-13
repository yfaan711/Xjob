package com.yfaan.xjob.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.lang.UUID;
import cn.hutool.core.util.RandomUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfaan.xjob.dto.LoginFormDTO;
import com.yfaan.xjob.dto.Result;
import com.yfaan.xjob.dto.UserDTO;
import com.yfaan.xjob.entity.User;
import com.yfaan.xjob.mapper.UserMapper;
import com.yfaan.xjob.service.IUserService;
import com.yfaan.xjob.utils.RegexUtils;
import com.yfaan.xjob.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.yfaan.xjob.utils.RedisConstants.*;
import static com.yfaan.xjob.utils.SystemConstants.USER_NICK_NAME_PREFIX;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    //发送短信验证码
    public Result sendCode(String phone) {
        //1,校验手机号
        //2.如果不符合，返回错误信息
        if(RegexUtils.isPhoneInvalid(phone)){
            return Result.fail("手机号格式错误");
        }
        //3.如果符合，生成验证码
        String code = RandomUtil.randomNumbers(6);
/*      //4.保存验证码到session
        session.setAttribute("code",code);*/
        //4.保存验证码到redis
        stringRedisTemplate.opsForValue().set(LOGIN_CODE_KEY+phone,code, LOGIN_CODE_TTL, TimeUnit.MINUTES);
        //5.返回验证码
        log.debug("发送验证码成功，验证码：{}",code);
        return Result.ok();
    }

    @Override
    //登录功能（验证码或密码）
    public Result login(LoginFormDTO loginForm) {
        //1.校验手机号
        if(RegexUtils.isPhoneInvalid(loginForm.getPhone())){
            return Result.fail("手机号格式错误");
        }

        if(loginForm.getCode() == null && loginForm.getPassword() != null){
            //密码登录
            //判断是否存在账号
            User user = query().eq("phone",loginForm.getPhone()).one();
            if(user == null){
                return Result.fail("用户不存在，请使用验证码登录注册账号");
            }
            //如果存在账号，判断密码是否正确
            if(!loginForm.getPassword().equals(user.getPassword())){
                return Result.fail("密码错误");
            }
            else{
                return Result.ok(saveUserInRedis( user));
            }
        }

        //2.校验验证码
        /*Object cachecode = session.getAttribute("code");*/
        //获取redis中的验证码
        String cachecode = stringRedisTemplate.opsForValue().get(LOGIN_CODE_KEY+loginForm.getPhone());
        if(cachecode == null || !cachecode.equals(loginForm.getCode())){
            return Result.fail("验证码错误");
        }
        //3.根据手机号查询用户 mybatis+的查询功能
        User user = query().eq("phone",loginForm.getPhone()).one();
        //4.判断用户是否存在
        //5.不存在，创建新用户
        if(user == null){
            user = createUserWithPhone(loginForm.getPhone());
        }
        //6.保存用户信息到session
      /*  session.setAttribute("user", BeanUtil.copyProperties(user, UserDTO.class));*/

        //6.返回用户token
        return Result.ok(saveUserInRedis( user));
    }

    private User createUserWithPhone(String phone) {
        //1.创建用户
        User user = new User();
        user.setPhone(phone);
        user.setNickName(USER_NICK_NAME_PREFIX+RandomUtil.randomString(10));
        //2.保存用户
        save(user);//mybatis plus的保存功能
        return user;
    }
    //继承自Mybatie plus 的ServiceImpl类，实现IUserService接口


    private String saveUserInRedis(User user) {
        //6.保存用户信息到redis
        String token = UUID.randomUUID().toString(true);
        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
        Map<String, Object> userMap = BeanUtil.beanToMap(userDTO, new HashMap<>(),
                CopyOptions.create()
                        .setIgnoreNullValue(true)
                        .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString()));
        stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY + token, userMap);
        stringRedisTemplate.expire(LOGIN_USER_KEY + token, LOGIN_USER_TTL, TimeUnit.SECONDS);//设置登录有效期
        return token;
    }

    @Override
    public Result logout(String token) {
        //删除redis中的用户信息
        if (token != null) {
            stringRedisTemplate.delete(LOGIN_USER_KEY + token);
            log.debug("已退出登录");
            return Result.ok("已退出登录");
        }
        return Result.fail("无效的token");
    }

    @Override
    public Result update(UserDTO userDTO) {
        // 1. 获取当前登录用户
        UserDTO currentUser = UserHolder.getUser();
        if (currentUser == null) {
            return Result.fail("请先登录");
        }
        // 2. 获取数据库中的用户实体
        User user = getById(currentUser.getId());
        if (user == null) {
            return Result.fail("用户不存在");
        }
        // 3. 更新用户信息
        user.setNickName(userDTO.getNickName());
        //user.setIcon(userDTO.getIcon());
        updateById(user); // 写入数据库
        
        // 4. 构造更新后的 DTO
        UserDTO updatedUserDTO = BeanUtil.copyProperties(user, UserDTO.class);
        
        // 5. 更新 Redis 中的用户信息
        String token = UserHolder.getToken();
        if (token != null) {
            Map<String, Object> userMap = BeanUtil.beanToMap(
                    updatedUserDTO, new HashMap<>(),
                    CopyOptions.create()
                            .setIgnoreNullValue(true)
                            .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString())
            );
            stringRedisTemplate.opsForHash().putAll(LOGIN_USER_KEY + token, userMap);
        }

        // 6. 返回更新后的 DTO
        return Result.ok(updatedUserDTO);
    }
}