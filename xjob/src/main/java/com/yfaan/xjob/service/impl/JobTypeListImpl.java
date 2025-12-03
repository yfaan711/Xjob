package com.yfaan.xjob.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfaan.xjob.dto.Result;
import com.yfaan.xjob.entity.JobType;
import com.yfaan.xjob.mapper.JobTypeMapper;
import com.yfaan.xjob.service.IJobTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

import java.util.List;

import static com.yfaan.xjob.utils.RedisConstants.CACHE_JOB_TYPE_KEY;

@Service
//标记该类为Spring框架中的服务层Bean 配合组件扫描，自动将该类注册为Spring容器中的Bean
@Slf4j
public class JobTypeListImpl extends ServiceImpl<JobTypeMapper, JobType> implements IJobTypeService {

    @Resource
    private StringRedisTemplate stringRedisTemplate;
    //Spring Data Redis提供的操作Redis的模板类
    @Override
    public Result queryType(){
        //1.在redis中查询
        //在redis里以字符串形式存储
        String typeJson = stringRedisTemplate.opsForValue().get(CACHE_JOB_TYPE_KEY);
        //2.存在，返回
        if(!StrUtil.isBlank(typeJson)){
            //反序列化
            List<JobType> jobTypeList = JSONUtil.toList(typeJson, JobType.class);
            return Result.ok(jobTypeList);
        }
        //3.不存在，查询数据库
        //按照sort字段进行升序排序
        List<JobType> jobTypeList = query().orderByAsc("sort").list();
        //4.数据库也不存在，报错
        if(jobTypeList == null){
            return Result.fail("数据不存在");
        }
        //5.存在，写入redis
        //查询到的职位类型列表(jobTypeList)序列化为JSON格式的字符串，然后存储到Redis中
        stringRedisTemplate.opsForValue().set(CACHE_JOB_TYPE_KEY, JSONUtil.toJsonStr(jobTypeList));
        //6.返回类型数据
        return Result.ok(jobTypeList);
    }
}
