package com.yfaan.xjob.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.yfaan.xjob.dto.Result;
import com.yfaan.xjob.dto.UserInfoDTO;
import com.yfaan.xjob.entity.UserInfo;
import com.yfaan.xjob.mapper.UserInfoMapper;
import com.yfaan.xjob.service.IUserInfoService;
import com.yfaan.xjob.utils.UserHolder;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {
    //UserInfoServiceImpl继承了MyBatis-Plus的ServiceImpl<UserInfoMapper, UserInfo>，这个类提供了IService接口的所有实现
    
    @Override
    public Result update(UserInfoDTO userInfoDTO) {
        // 1. 获取当前登录用户
        Long userId = UserHolder.getUser().getId();
        if (userId == null) {
            return Result.fail("请先登录");
        }
        
        // 2. 获取数据库中的用户详细信息实体
        UserInfo userInfo = getById(userId);
        if (userInfo == null) {
            // 如果不存在，则创建新的用户详细信息记录
            userInfo = new UserInfo();
            userInfo.setUserId(userId); // 设置用户ID
        }
        
        // 3. 更新用户详细信息
        userInfo.setCity(userInfoDTO.getCity());
        userInfo.setIntroduce(userInfoDTO.getIntroduce());
        userInfo.setGender(userInfoDTO.getGender());
        userInfo.setBirthday(userInfoDTO.getBirthday());
        
        // 4. 保存到数据库：如果用户详细信息记录不存在，则创建新的记录；否则，更新已有的记录
        boolean success = saveOrUpdate(userInfo);
        
        if (success) {
            // 5. 返回更新后的用户详细信息
            return Result.ok(userInfo);
        } else {
            return Result.fail("更新失败");
        }
    }
}