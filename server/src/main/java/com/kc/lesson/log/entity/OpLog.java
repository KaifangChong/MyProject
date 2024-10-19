package com.kc.lesson.log.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_op_log")
public class OpLog implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    public Long id;
    @TableField
    public String ip;
    @TableField
    public String time;
    @TableField
    public String ua;
    @TableField
    public String url;
    @TableField
    public String method;
    @TableField
    public String content;
    @TableField
    public String accessTime;

}
