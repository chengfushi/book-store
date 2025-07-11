package com.chengfu.usercenterapi.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName cart
 */
@TableName(value ="cart")
@Data
public class Cart {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 书籍ID
     */
    private Integer bookId;

    /**
     * 数量
     */
    private Integer quantity;


    /**
     * 
     */
    private Date createTime;

    /**
     * 
     */
    private Date updateTime;

    private String bookIsbn;

    private String bookName;

    private Double totalPrice;

    @TableLogic
    private Integer isDelete;
}