package com.chengfu.usercenterapi.model.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 书籍列表
 * @TableName book_list
 */
@TableName(value ="book_list")
@Data
public class BookList {
    /**
     * 主键ID
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 书籍ISBN
     */
    private String bookIsbn;

    /**
     * 书籍名称
     */
    private String bookName;

    /**
     * 书籍价格
     */
    private Double bookPrice;

    /**
     * 出版日期
     */
    private Date publishDate;

    /**
     * 逻辑删除标识(0-未删除,1-已删除)
     */
    @TableLogic
    private Integer isDelete;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 书籍描述
     */
    private String bookDescription;

    /**
     * 书籍图片
     */
    private byte[] bookImage;
}