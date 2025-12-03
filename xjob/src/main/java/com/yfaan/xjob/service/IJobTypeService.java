package com.yfaan.xjob.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.yfaan.xjob.dto.Result;
import com.yfaan.xjob.entity.JobType;

public interface IJobTypeService extends IService<JobType> {

    Result queryType();
}
