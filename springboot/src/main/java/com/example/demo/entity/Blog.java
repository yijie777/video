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

@TableName("blog")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Blog {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private Long articleId;
    private String author;
    private String articleUrl;
    private Integer commentCount;
    private Integer collectCount;
    private Integer readCount;
    private String articleTitle;
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date publishDate;
    private String articleTags;
    private String articleType;
    private String articleDesc;
    private String articleContext;
}
