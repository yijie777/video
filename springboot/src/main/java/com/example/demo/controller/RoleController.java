package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.common.Result;
import com.example.demo.entity.Role;
import com.example.demo.entity.Type;
import com.example.demo.mapper.RoleMapper;
import com.example.demo.mapper.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("role")
@CrossOrigin
public class RoleController {

    @Autowired
    RoleMapper roleMapper;

    @GetMapping
    public Result<?> getAllRoles() {
        LambdaQueryWrapper<Role> lqw = Wrappers.<Role>lambdaQuery();
        List<Role> types = roleMapper.selectList(lqw);
        return Result.success(types);
    }
}
