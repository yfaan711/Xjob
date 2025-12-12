package com.yfaan.xjob.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfaan.xjob.entity.JobType;
import com.yfaan.xjob.mapper.JobTypeMapper;
import com.yfaan.xjob.service.IJobTypeService;
import org.springframework.stereotype.Service;

@Service
public class JobTypeServiceImpl extends ServiceImpl<JobTypeMapper, JobType> implements IJobTypeService {
}
