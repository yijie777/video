package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.Date;

@TableName("video")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Video {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String info;
    private String type;
    private String thumbnailUrl;
    private String enclosureUrl;
    private Integer userId;
    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date createTime;
}
