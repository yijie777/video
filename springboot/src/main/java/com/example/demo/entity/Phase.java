package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
@TableName("phase")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Phase {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String vId;
    private String videoUrl;
}
