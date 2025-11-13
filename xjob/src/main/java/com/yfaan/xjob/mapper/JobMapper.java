package com.yfaan.xjob.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yfaan.xjob.dto.JobWithUserDTO;
import com.yfaan.xjob.entity.Job;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface JobMapper extends BaseMapper<Job> {
    /**
     * 分页查询职位信息并关联用户昵称
     * @param page 分页参数
     * @return 职位信息列表
     */
    Page<JobWithUserDTO> selectJobWithUser(Page<JobWithUserDTO> page);
    
    /**
     * 查询职位总数
     * @return 总数
     */
    Long selectJobWithUserCount();
}