package com.yfaan.xjob.service.impl;

import com.yfaan.xjob.entity.UserInfo;
import com.yfaan.xjob.mapper.UserInfoMapper;
import com.yfaan.xjob.service.IUserInfoService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

@Service
public class UserInfoServiceImpl extends ServiceImpl<UserInfoMapper, UserInfo> implements IUserInfoService {
    //UserInfoServiceImpl继承了MyBatis-Plus的ServiceImpl<UserInfoMapper, UserInfo>，这个类提供了IService接口的所有实现
}
