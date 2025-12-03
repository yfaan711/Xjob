package com.yfaan.xjob.controller;

import com.yfaan.xjob.dto.Result;
import com.yfaan.xjob.service.IJobTypeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@Slf4j
@RestController
@RequestMapping("/jobType")
public class JobTypeController {
    @Resource
    private IJobTypeService jobTypeService;

    @GetMapping("/list")
    public Result queryTypeList() {
        return jobTypeService.queryType();
    }
}
