package com.example.demo.controller;


import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpResponse;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.config.FilePathConfig;
import com.example.demo.entity.Phase;
import com.example.demo.entity.User;
import com.example.demo.entity.Video;
import com.example.demo.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {

    @Value("${server.port}")
    private String port;

    @Value("${files.ip}")
    private String ip;

    @Autowired
    UserMapper userMapper;
    @Autowired
    private FilePathConfig filePathConfig;

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        return Result.success(user);
    }
    @PutMapping
    public Result<?> update(@RequestBody User user) {
        userMapper.updateById(user);
        log.info("用户："+user.getUsername()+"个人信息更新成功");
        return Result.success();
    }

    @PostMapping("/login")
    public Result<?> login(@RequestBody User user) {
        User user1 = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()).eq(User::getPassword, user.getPassword()));
        if(user1==null){
            log.info("用户："+user.getUsername()+"登录失败");
            return  Result.error("-1","用户名或密码错误");
        }
        log.info("用户："+user.getUsername()+"成功登录");
        return Result.success(user1);
    }
    @PostMapping("/register")
    public Result<?> register(@RequestBody User user) {

        user.setImgUrl("\\default.jpg");
        user.setRoleId(2);
        User user1 = userMapper.selectOne(Wrappers.<User>lambdaQuery().eq(User::getUsername, user.getUsername()));
        if(user1!=null){
            return  Result.error("-1","用户名已存在");
        }
        userMapper.insert(user);
        log.info("用户："+user.getUsername()+"成功注册");
        return Result.success();
    }
    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search) {

        LambdaQueryWrapper<User> lwq = Wrappers.<User>lambdaQuery();
        if (StrUtil.isNotBlank(search)) {
            lwq.like(User::getUsername, search).or().eq(User::getNickName, search);
        }
        Page<User> videoPage = userMapper.selectPage(new Page<>(pageNum, pageSize), lwq);

        return Result.success(videoPage);
    }
    @PostMapping
    public Result<?> save(@RequestBody User user) {
        userMapper.insert(user);
        return Result.success();
    }



    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        User user = userMapper.selectById(id);
        if(user.getImgUrl().contains("\\")){
            String videoPath = filePathConfig.getFileUrl() + filePathConfig.getImagePath() + user.getImgUrl().substring(user.getImgUrl().lastIndexOf("\\")+1);
            File file = new File(videoPath);
            if (file.exists()) file.delete();
        }
        userMapper.deleteById(id);
        return Result.success();
    }
    @PostMapping("/delList")
    public Result<?> delList(@RequestBody List<Long> ids) {
        for(Long id:ids){
            User user = userMapper.selectById(id);
            if(user.getImgUrl().contains("\\")){
                String videoPath = filePathConfig.getFileUrl() + filePathConfig.getImagePath() + user.getImgUrl().substring(user.getImgUrl().lastIndexOf("\\")+1);
                File file = new File(videoPath);
                if (file.exists()) file.delete();
            }
            userMapper.deleteById(id);
        }
        return Result.success();
    }
}

