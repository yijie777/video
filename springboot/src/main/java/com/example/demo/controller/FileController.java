package com.example.demo.controller;

import cn.hutool.core.util.IdUtil;
import com.example.demo.common.Result;
import com.example.demo.config.FilePathConfig;
import com.example.demo.entity.Phase;
import com.example.demo.entity.Video;
import com.example.demo.mapper.PhaseMapper;
import com.example.demo.mapper.VideoMapper;
import com.example.demo.utils.FileUtil;
import com.example.demo.utils.ThumbnailThread;
import com.example.demo.utils.TranscodingThread;
import com.example.demo.utils.ZipUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/files")
@CrossOrigin
public class FileController {


    @Value("${server.port}")
    private String port;

    @Value("${files.ip}")
    private String ip;

    @Autowired
    private FilePathConfig filePathConfig;

    @Autowired
    VideoMapper videoMapper;

    @Autowired
    PhaseMapper phaseMapper;


    @PostMapping("/uploadTh")
    public Result<?> upload(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();  // 获取源文件的名称
        // 定义文件的唯一标识（前缀）
        String flag = IdUtil.fastSimpleUUID();
        String thumbnailUrl = filePathConfig.getImagePath() + flag + "_" + originalFilename;
        String thumbnailRealPath = filePathConfig.getFileUrl() + thumbnailUrl;
        cn.hutool.core.io.FileUtil.writeBytes(file.getBytes(), thumbnailRealPath);  // 把文件写入到上传的路径
        return Result.success(thumbnailUrl);  // 返回结果 url
    }

    //下载附件
    @RequestMapping("/download/{fileUrlId}")
    public String download(@PathVariable String fileUrlId, HttpServletResponse response) throws UnsupportedEncodingException {
        Video video = videoMapper.selectById(fileUrlId);
        new FileUtil().downloadFile(filePathConfig.getFileUrl() + video.getEnclosureUrl(), response);
        return null;
    }

    //打包下载视频
    @RequestMapping("/downloadVideo/{name}")
    public String downloadByPath(@PathVariable String name, HttpServletResponse response) throws UnsupportedEncodingException {
        String packPath = filePathConfig.getFileUrl() + filePathConfig.getVideoPath() + name;
        new FileUtil().downloadFile(packPath, response);
        new FileUtil().deleteAllFilesOfDir(new File(packPath));
        return null;
    }

    @PostMapping("/packageZip")
    public Result<?> downloadVideo(@RequestBody List<Long> ids) throws FileNotFoundException {
        List<Phase> phases = phaseMapper.selectBatchIds(ids);
        List<File> videoPathList = new ArrayList<>();
        String uuid = IdUtil.fastSimpleUUID();
        String filePath = filePathConfig.getFileUrl() + filePathConfig.getVideoPath(); //视频存放的根目录

        for (Phase p : phases) {
            String videoPath = filePath + p.getVideoUrl().substring(p.getVideoUrl().lastIndexOf("/"));
            videoPathList.add(new File(videoPath));
        }
        String zipPath = filePath + uuid + ".zip";
        ZipUtils.toZip(videoPathList, new FileOutputStream(new File(zipPath)));
        return Result.success(uuid + ".zip");
    }

    @PostMapping("/upload")
    public Result<?> videoUpload(@RequestParam("files") MultipartFile[] videoFiles,
                                 @RequestParam(value = "enclosureList", required = false) MultipartFile[] enclosureList,
                                 @RequestParam String name,
                                 @RequestParam String describe,
                                 @RequestParam(required = false) Integer userId,
                                 @RequestParam String type) throws IOException {
        System.out.println("开始上传");
        long start = System.currentTimeMillis();
        //要保存的视频信息
        Video video = new Video();
        video.setName(name);
        video.setInfo(describe);
        video.setType(type);
        video.setUserId(userId);
        //定义视频章节列表
        List<Phase> phaseList = new ArrayList<>();

        //资源路径前缀
//        String ipPort = "http://" + ip + ":" + port;
        String ipPort = "";

        //上传附件
        if (enclosureList != null) {
            String flag1 = IdUtil.fastSimpleUUID();
            for (MultipartFile enclosure : enclosureList) {

                String originalFilename = enclosure.getOriginalFilename();//视频标题
                String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));//视频后缀

                String saveFileName = filePathConfig.getEnclosurePath() + flag1 + "/" + originalFilename + suffix;
                String fileRealPath = filePathConfig.getFileUrl() + saveFileName;
                File dest = new File(fileRealPath);
                if (!dest.getParentFile().exists()) {
                    // 判断文件父目录是否存在
                    dest.getParentFile().mkdirs();
                }
                // 保存文件
                enclosure.transferTo(dest);

            }
            //压缩文件夹
            String zipPath = filePathConfig.getFileUrl() + filePathConfig.getEnclosurePath() + flag1;
            FileOutputStream fos1 = new FileOutputStream(new File(zipPath + ".zip"));
            ZipUtils.toZip(zipPath, fos1, true);
            video.setEnclosureUrl(filePathConfig.getEnclosurePath() + flag1 + ".zip");
            //删除源文件
            new FileUtil().deleteAllFilesOfDir(new File(zipPath));
        }

        String lastVideoUrl = "";
        for (MultipartFile videoFile : videoFiles) {
            // 视频的保存路劲
            String flag = IdUtil.fastSimpleUUID();
            String originalFilename = videoFile.getOriginalFilename();//视频标题
            String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));//视频后缀
            String videoTitle = originalFilename.substring(0, originalFilename.lastIndexOf("."));//视频名字

            String saveFileName = filePathConfig.getVideoPath() + flag + suffix;
            String fileRealPath = filePathConfig.getFileUrl() + saveFileName;

            //要截图的视频位置
            lastVideoUrl = ipPort + saveFileName;

            File dest = new File(fileRealPath);
            if (!dest.getParentFile().exists()) {
                // 判断文件父目录是否存在
                dest.getParentFile().mkdirs();
            }
            // 保存文件
            videoFile.transferTo(dest);

            //是否需要转码
            if (!suffix.equals(".mp4")) {
                String transcodingUrl = fileRealPath.substring(0, fileRealPath.lastIndexOf(".")) + ".mp4";
                TranscodingThread thumbnailThread =
                        new TranscodingThread(fileRealPath, transcodingUrl);
                thumbnailThread.start();
                saveFileName = saveFileName.substring(0, saveFileName.lastIndexOf(".")) + ".mp4";
            }

            Phase phase = new Phase();
            phase.setName(videoTitle);
            phase.setVideoUrl(ipPort + saveFileName);
            phaseList.add(phase);
        }

        // 截图的保存路劲
        String thumbnailUrl = filePathConfig.getImagePath() + IdUtil.fastSimpleUUID() + ".jpg";
        String thumbnailRealPath = filePathConfig.getFileUrl() + thumbnailUrl;
        video.setThumbnailUrl(ipPort + thumbnailUrl);

        //截图并保存
        String firsFilePath = lastVideoUrl;
        String saveFileName = filePathConfig.getFileUrl() + firsFilePath.substring(firsFilePath.indexOf("/"));
        ThumbnailThread thumbnailThread =
                new ThumbnailThread(saveFileName, thumbnailRealPath);
        thumbnailThread.start();


        //数据库插入视频
        videoMapper.insert(video);
        for (Phase p : phaseList) {
            p.setVId(String.valueOf(video.getId()));
            phaseMapper.insert(p);
        }

        long end = System.currentTimeMillis();
        log.info("视频：" + video.getName() + "上传完成，耗时：" + (end - start) + " ms");
        return Result.success();
    }


}
