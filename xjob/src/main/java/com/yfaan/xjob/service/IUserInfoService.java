package com.yfaan.xjob.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfaan.xjob.entity.UserInfo;

public interface IUserInfoService extends IService<UserInfo> {
    //继承了MyBatis-Plus的IService<UserInfo>接口，获得了通用的CRUD方法
}
