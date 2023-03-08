package com.hth.entity;

import java.util.Date;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * (Syslogger)表实体类
 *
 * @author makejava
 * @since 2023-02-23 13:02:34
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("syslogger")
public class Syslogger  {
    //记录ID@TableId
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    
    private String description;
    private Date createTime;
    
    private String ip;
    
    private Integer uid;
    //修改时间
    private String url;
    //执行的方法-类全命名.方法
    private String requestType;
    
    private String classMethod;
    //传入的参数
    private String params;
    //主机
    private String response;
    
    private Integer excTime;
    
    private Integer logType;



}
