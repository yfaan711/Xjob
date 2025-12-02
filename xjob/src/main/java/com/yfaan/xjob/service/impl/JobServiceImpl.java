package com.yfaan.xjob.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yfaan.xjob.dto.JobWithUserDTO;
import com.yfaan.xjob.dto.Result;
import com.yfaan.xjob.entity.Job;
import com.yfaan.xjob.mapper.JobMapper;
import com.yfaan.xjob.service.IJobService;
import com.yfaan.xjob.utils.UserHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JobServiceImpl extends ServiceImpl<JobMapper, Job> implements IJobService {
    @Override
    public Result add(Job job){
        //1.获取userid
        Long userId = UserHolder.getUser().getId();
        //2.判断该用户是否已有 job ，一个人最多创建3个job
        if(query().eq("user_id",userId).count() >= 3){
            log.debug("一个用户最多创建3个job");
            return Result.fail("一个用户最多创建3个job");
        }
        //3.检查必填字段
        if (job.getJob() == null || job.getJob().trim().isEmpty()) {
            return Result.fail("职位名称不能为空");
        }
        //4.设置默认值
        if (job.getWorkDuration() == null) {
            job.setWorkDuration(0);
        }
        //5.保存job
        job.setUserId(userId.intValue()); // 修改为Integer类型
        save(job);
        return Result.ok(job);
    }

    @Override
    public Result Userlist(Long userId) {
        // 根据用户ID查询职位列表
        return Result.ok(query().eq("user_id", userId).list());
    }

    @Override
    public Result update(Long userId, Job job) {
        //1.查找 job 是否存在
        Job job1 = getById(job.getId());
        if(job1 == null){
            return Result.fail("该 job 不存在");
        }
        //2.判断该用户是否是该 job 的创建者
        if(!job1.getUserId().equals(userId.intValue())){ // 修改为Integer类型
            return Result.fail("该 job 不属于该用户");
        }
        //3.设置默认值
        if (job.getWorkDuration() == null) {
            job.setWorkDuration(0);
        }
        //4.更新 job
        updateById(job);
        return Result.ok(getById(job.getId()));
    }

    @Override
    public Result removeById(Long jobId) {
        //1.查找 job 是否存在
        Job job = getById(jobId);
        if(job == null){
            return Result.fail("该 job 不存在");
        }
        //2.判断该用户是否是该 job 的创建者
        Long userId = UserHolder.getUser().getId();
        if(!job.getUserId().equals(userId.intValue())){ // 修改为Integer类型
            return Result.fail("该 job 不属于该用户");
        }
        //3.删除 job
        super.removeById(jobId);
        return Result.ok("已删除job");
    }
    
    @Override
    public Result jobList(Integer current, Integer size) {
        // 使用自定义查询关联用户信息
        Page<JobWithUserDTO> page = new Page<>(current, size);
        Page<JobWithUserDTO> resultPage = getBaseMapper().selectJobWithUser(page);
        
        // 查询总数
        Long total = getBaseMapper().selectJobWithUserCount();
        resultPage.setTotal(total);
        
        return Result.ok(resultPage.getRecords(), resultPage.getTotal());
    }

    //JobServiceImpl 继承了 ServiceImpl<JobMapper, Job>，其中指定了实体类 Job 和 Mapper 接口 JobMapper
    //JobMapper 接口上有 @Mapper 注解，并且继承了 BaseMapper<Job>，这建立了与 Job 实体类的关联
    //Job 实体类上有 @TableName 注解，指定了对应的数据库表名
    //MyBatis-Plus 框架:通过 ServiceImpl 提供的 query() 方法，自动识别对应的表结构
    //生成类似 SELECT * FROM tb_job WHERE user_id = ? 的 SQL 语句
}