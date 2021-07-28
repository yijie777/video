package com.example.demo.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@TableName("type")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Type {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String typeName;
}