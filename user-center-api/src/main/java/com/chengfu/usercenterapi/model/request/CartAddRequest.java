package com.chengfu.usercenterapi.model.request;

import lombok.Data;

@Data
public class CartAddRequest {
    /**
     * 书籍ISBN
     */
    private String bookIsbn;

    /**
     * 数量
     */
    private Integer quantity;

}
