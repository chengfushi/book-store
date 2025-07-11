package com.chengfu.usercenterapi.model.request;

import lombok.Data;

import java.util.List;

@Data
public class OrderCreateRequest {
    private List<CartAddRequest> cartAddRequests;
}
