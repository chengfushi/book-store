package com.chengfu.usercenterapi.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName order_item
 */
@TableName(value ="order_item")
@Data
public class OrderItem {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 订单ID
     */
    private Long orderId;

    /**
     * 书籍ID
     */
    private Integer bookId;

    /**
     * 书籍ISBN
     */
    private String bookIsbn;

    /**
     * 书籍名称
     */
    private String bookName;

    /**
     * 购买数量
     */
    private Integer quantity;

    /**
     * 购买价格
     */
    private Double price;

    /**
     * 
     */
    private Date createTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDeleted;
}