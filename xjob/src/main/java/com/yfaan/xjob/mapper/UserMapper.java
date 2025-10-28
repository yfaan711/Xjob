package com.yfaan.xjob.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yfaan.xjob.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
