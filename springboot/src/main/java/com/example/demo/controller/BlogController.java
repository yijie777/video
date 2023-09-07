package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.entity.Blog;
import com.example.demo.mapper.BlogMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("blog")
@CrossOrigin
public class BlogController {

    @Autowired
    BlogMapper blogMapper;

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "") String type,
                              @RequestParam(required = false) Integer sort) {

        LambdaQueryWrapper<Blog> lwq = Wrappers.<Blog>lambdaQuery();

        if(StrUtil.isNotBlank(type)&&StrUtil.isNotBlank(search)){
           lwq.like(Blog::getArticleType, type).and(wrapper ->wrapper.like(Blog::getArticleTitle, search)
                   .or().like(Blog::getArticleTags, search))
                   .or().like(Blog::getArticleType, search);
        }else{
            if (StrUtil.isNotBlank(type)) {
                lwq.like(Blog::getArticleType, type);
            }
            if (StrUtil.isNotBlank(search)) {
                lwq.like(Blog::getArticleTitle, search)
                        .or().like(Blog::getArticleTags, search)
                        .or().like(Blog::getArticleType, search);
        }

        }
        if (sort != null) {
            if (sort == -1) {
                lwq.orderByDesc(Blog::getReadCount);
            } else if (sort == 1) {
                lwq.orderByAsc(Blog::getReadCount);
            }
        }
        Page<Blog> videoPage = blogMapper.selectPage(new Page<>(pageNum, pageSize), lwq);
//        if (videoPage.getTotal() == 0) {
//            videoPage = blogMapper.selectPage(new Page<>(pageNum, pageSize), Wrappers.<Blog>lambdaQuery().like(Blog::getArticleDesc, search));
//        }
        return Result.success(videoPage);
    }

    @PutMapping
    public Result<?> update(@RequestBody Blog blog) {
        blogMapper.updateById(blog);
        return Result.success();
    }

    @PostMapping
    public Result<?> save(@RequestBody Blog blog) {
        blogMapper.insert(blog);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        blogMapper.deleteById(id);
        return Result.success();
    }

    @PostMapping("/delList")
    public Result<?> delList(@RequestBody List<Long> ids) {
        blogMapper.deleteBatchIds(ids);
        return Result.success();
    }
}
