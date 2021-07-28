package com.example.demo.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.example.demo.common.Result;
import com.example.demo.entity.ArticleType;
import com.example.demo.entity.Type;
import com.example.demo.mapper.ArticleMapper;
import com.example.demo.mapper.TypeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("articletype")
@CrossOrigin
public class ArticleController {

    @Autowired
    ArticleMapper articleMapper;

    @GetMapping
    public Result<?> getAllTypes() {
        LambdaQueryWrapper<ArticleType> lqw = Wrappers.<ArticleType>lambdaQuery();
        List<ArticleType> types = articleMapper.selectList(lqw);
        return Result.success(types);
    }
}
