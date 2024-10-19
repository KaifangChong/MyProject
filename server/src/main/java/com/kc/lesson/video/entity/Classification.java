package com.kc.lesson.video.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_classification")
public class Classification implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    public Long id;
    @TableField
    public String title;
    @TableField
    public String createTime;

}
