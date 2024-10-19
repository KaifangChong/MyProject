package com.kc.lesson.video.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

@Data
@TableName("t_video_tag")
public class VideoTag implements Serializable {
    @TableId(value = "id", type = IdType.AUTO)
    public Long id;
    @TableField
    public Long videoId;
    @TableField
    public Long tagId;

}
