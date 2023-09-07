package com.example.demo.controller;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.demo.common.Result;
import com.example.demo.config.FilePathConfig;
import com.example.demo.entity.Blog;
import com.example.demo.entity.Phase;
import com.example.demo.entity.Type;
import com.example.demo.entity.Video;
import com.example.demo.mapper.PhaseMapper;
import com.example.demo.mapper.TypeMapper;
import com.example.demo.mapper.VideoMapper;
import jdk.nashorn.internal.codegen.TypeMap;
import lombok.extern.log4j.Log4j;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("video")
@CrossOrigin
public class VideoController {

    @Autowired
    VideoMapper videoMapper;
    @Autowired
    PhaseMapper phaseMapper;

    @Autowired
    TypeMapper typeMapper;
    @Autowired
    private FilePathConfig filePathConfig;

    @GetMapping
    public Result<?> findPage(@RequestParam(defaultValue = "1") Integer pageNum,
                              @RequestParam(defaultValue = "10") Integer pageSize,
                              @RequestParam(defaultValue = "") String search,
                              @RequestParam(defaultValue = "") String type) {

        LambdaQueryWrapper<Video> lwq = Wrappers.<Video>lambdaQuery();
        if (StrUtil.isNotBlank(type)) {
            lwq.like(Video::getType, type);
        }
        if (StrUtil.isNotBlank(search)) {
            lwq.like(Video::getName, search).or().eq(Video::getType, search);
        }
        Page<Video> videoPage = videoMapper.selectPage(new Page<>(pageNum, pageSize), lwq);

        return Result.success(videoPage);
    }

    @GetMapping("/findByHome")
    public Result<?> findByHome() {
        LambdaQueryWrapper<Type> lqw = Wrappers.<Type>lambdaQuery();
        List<Type> types = typeMapper.selectList(lqw);
        Map<Object,Object> map = new HashMap<>();

        for (Type t : types) {
            LambdaQueryWrapper<Video> vlqw = Wrappers.<Video>lambdaQuery();
            vlqw.like(Video::getType, t);
            Page<Video> videoPage = videoMapper.selectPage(new Page<>(1, 8), vlqw);
            map.put(t,videoPage);
        }
        return Result.success(map);
    }

    @GetMapping("/getAllPhase/{id}")
    public Result<?> getAllPhase(@PathVariable String id) {
        LambdaQueryWrapper<Phase> lwq = Wrappers.<Phase>lambdaQuery().eq(Phase::getVId, id);
        List<Phase> phases = phaseMapper.selectList(lwq);
        return Result.success(phases);
    }

    @GetMapping("/getOneVideo/{id}")
    public Result<?> getOneVideo(@PathVariable String id) {
        Video video = videoMapper.selectById(id);
        return Result.success(video);
    }

    @PostMapping
    public Result<?> save(@RequestBody Video video) {
        videoMapper.insert(video);
        return Result.success();
    }

    @PutMapping
    public Result<?> update(@RequestBody Video video) {
        videoMapper.updateById(video);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        LambdaQueryWrapper<Phase> lwq = Wrappers.<Phase>lambdaQuery().eq(Phase::getVId, id);
        Video video = videoMapper.selectById(id);
        List<Phase> phases = phaseMapper.selectList(lwq);

        String videoPath = filePathConfig.getFileUrl() + filePathConfig.getImagePath() + video.getThumbnailUrl().substring(video.getThumbnailUrl().lastIndexOf("\\") + 1);
        File file = new File(videoPath);
        if (file.exists()) file.delete();

        if (video.getEnclosureUrl() != null) {
            if (!video.getEnclosureUrl().equals("")) {
                String enclosure = filePathConfig.getFileUrl() + filePathConfig.getEnclosurePath() + video.getEnclosureUrl().substring(video.getEnclosureUrl().lastIndexOf("\\") + 1);
                file = new File(enclosure);
                if (file.exists()) file.delete();
                file = new File(enclosure + ".zip");
                if (file.exists()) file.delete();
            }
        }

        for (Phase p : phases) {
            String pPath = filePathConfig.getFileUrl() + filePathConfig.getVideoPath() + p.getVideoUrl().substring(p.getVideoUrl().lastIndexOf("\\") + 1);
            file = new File(pPath);
            if (file.exists()) file.delete();
        }
        videoMapper.deleteById(id);
        phaseMapper.delete(lwq);
        log.info("视频" + video.getName() + "已删除");
        return Result.success();
    }

    @PostMapping("/delList")
    public Result<?> delList(@RequestBody List<Long> ids) {

        for (Long id : ids) {
            LambdaQueryWrapper<Phase> lwq = Wrappers.<Phase>lambdaQuery().eq(Phase::getVId, id);
            Video video = videoMapper.selectById(id);
            List<Phase> phases = phaseMapper.selectList(lwq);

            String videoPath = filePathConfig.getFileUrl() + filePathConfig.getImagePath() + video.getThumbnailUrl().substring(video.getThumbnailUrl().lastIndexOf("\\") + 1);
            File file = new File(videoPath);
            if (file.exists()) file.delete();

            if (video.getEnclosureUrl() != null) {
                if (!video.getEnclosureUrl().equals("")) {
                    String enclosure = filePathConfig.getFileUrl() + filePathConfig.getEnclosurePath() + video.getEnclosureUrl().substring(video.getEnclosureUrl().lastIndexOf("\\") + 1);
                    file = new File(enclosure);
                    if (file.exists()) file.delete();
                    file = new File(enclosure + ".zip");
                    if (file.exists()) file.delete();
                }
            }

            for (Phase p : phases) {
                String pPath = filePathConfig.getFileUrl() + filePathConfig.getVideoPath() + p.getVideoUrl().substring(p.getVideoUrl().lastIndexOf("\\") + 1);
                file = new File(pPath);
                if (file.exists()) file.delete();
            }
            videoMapper.deleteById(id);
            phaseMapper.delete(lwq);
            log.info("视频" + video.getName() + "已删除");
        }
        return Result.success();
    }


}
