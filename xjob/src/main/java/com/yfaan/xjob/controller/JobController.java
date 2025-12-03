package com.yfaan.xjob.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.yfaan.xjob.dto.Result;
import com.yfaan.xjob.entity.Job;
import com.yfaan.xjob.service.IJobService;
import com.yfaan.xjob.utils.UserHolder;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/job")
public class JobController {
    @Resource
    private  IJobService jobService;
    //新增用户job
    @PostMapping("/add")
    public Result add(@RequestBody Job job) {
        return jobService.add(job);
    }
    //修改用户job
    @PostMapping("/update")
    public Result update(@RequestBody Job job) {
        Long userId = UserHolder.getUser().getId();
        return jobService.update(userId, job);
    }

    //根据用户id查询job信息
    @GetMapping("/Userlist")
    public Result Userlist() {
        Long userId = UserHolder.getUser().getId();
        return jobService.Userlist(userId);
    }

    //删除用户job
    @DeleteMapping("/delete")
    public Result delete(@RequestParam Long jobId) {
        return jobService.removeById(jobId);
    }

    //分页查询所有job信息
    @GetMapping("/page")
    public Result jobList(@RequestParam(value = "current", defaultValue = "1") Integer current,
                          @RequestParam(value = "size", defaultValue = "10") Integer size,
                          @RequestParam(value = "jobType", required = false) Integer jobType) {
        return jobService.jobList(current, size, jobType);
    }

}