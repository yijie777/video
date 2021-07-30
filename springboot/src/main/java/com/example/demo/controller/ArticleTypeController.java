package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.ArticleType;
import com.example.demo.mapper.ArticleTypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("articletype")
@CrossOrigin
public class ArticleTypeController {

    @Autowired
    ArticleTypeMapper articleMapper;

    @GetMapping
    public Result<?> getAllTypes() {
        LambdaQueryWrapper<ArticleType> lqw = Wrappers.<ArticleType>lambdaQuery();
        List<ArticleType> types = articleMapper.selectList(lqw);
        return Result.success(types);
    }
    @GetMapping("/findPage")
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {

        LambdaQueryWrapper<ArticleType> lwq = Wrappers.<ArticleType>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            lwq.like(ArticleType::getTypeName, search);
        }
        Page<ArticleType> videoPage = articleMapper.selectPage(new Page<>(pageNum, pageSize), lwq);
        return Result.success(videoPage);
    }
    @PutMapping
    public Result<?> update(@RequestBody ArticleType articleType) {
        articleMapper.updateById(articleType);
        return Result.success();
    }
    @PostMapping
    public Result<?> save(@RequestBody ArticleType articleType) {
        articleMapper.insert(articleType);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        articleMapper.deleteById(id);
        return Result.success();
    }

    @PostMapping("/delList")
    public Result<?> delList(@RequestBody List<Long> ids) {
        articleMapper.deleteBatchIds(ids);
        return Result.success();
    }
}
