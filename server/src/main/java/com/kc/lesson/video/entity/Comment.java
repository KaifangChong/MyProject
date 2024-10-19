package com.kc.lesson.video.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_comment")
public class Comment implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    public Long id;
    @TableField
    public String content;
    @TableField
    public String commentTime;
    @TableField
    public String likeCount;
    @TableField
    public String userId;
    @TableField(exist = false)
    public String username; // 用户名
    @TableField
    public String videoId;
    @TableField(exist = false)
    public String title; // 视频名称
    @TableField(exist = false)
    public String cover; // 视频封面

    @TableField(exist = false)
    public String avatar; // 用户头像
}
