package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Phase;
import com.example.demo.entity.Type;
import com.example.demo.entity.Video;
import com.example.demo.mapper.PhaseMapper;
import com.example.demo.mapper.TypeMapper;
import com.example.demo.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("type")
@CrossOrigin
public class TypeController {

    @Autowired
    TypeMapper typeMapper;

    @GetMapping
    public Result<?> getAllTypes() {
        LambdaQueryWrapper<Type> lqw = Wrappers.<Type>lambdaQuery();
        List<Type> types = typeMapper.selectList(lqw);
        return Result.success(types);
    }
}
