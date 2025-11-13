package com.yfaan.xjob.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfaan.xjob.dto.Result;
import com.yfaan.xjob.entity.Job;

public interface IJobService extends IService<Job> {
    Result add(Job job);

    Result Userlist(Long userId);

    Result update(Long userId, Job job);

    Result removeById(Long jobId);
    
    Result jobList(Integer current, Integer size);
}