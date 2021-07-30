package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Phase;
import com.example.demo.entity.Type;
import com.example.demo.entity.User;
import com.example.demo.entity.Video;
import com.example.demo.mapper.PhaseMapper;
import com.example.demo.mapper.TypeMapper;
import com.example.demo.mapper.VideoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
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

    @GetMapping("/findPage")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {

        LambdaQueryWrapper<Type> lwq = Wrappers.<Type>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            lwq.like(Type::getTypeName, search);
        }
        Page<Type> videoPage = typeMapper.selectPage(new Page<>(pageNum, pageSize), lwq);
        return Result.success(videoPage);
    }
    @PutMapping
    public Result<?> update(@RequestBody Type type) {
        typeMapper.updateById(type);
        return Result.success();
    }
    @PostMapping
    public Result<?> save(@RequestBody Type type) {
        typeMapper.insert(type);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        typeMapper.deleteById(id);
        return Result.success();
    }

    @PostMapping("/delList")
    public Result<?> delList(@RequestBody List<Long> ids) {
        typeMapper.deleteBatchIds(ids);
        return Result.success();
    }

}
