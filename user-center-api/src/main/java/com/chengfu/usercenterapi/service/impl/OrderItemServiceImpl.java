package com.chengfu.usercenterapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chengfu.usercenterapi.model.domain.OrderItem;
import com.chengfu.usercenterapi.service.OrderItemService;
import com.chengfu.usercenterapi.mapper.OrderItemMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【order_item】的数据库操作Service实现
* @createDate 2025-06-01 15:19:32
*/
@Service
public class OrderItemServiceImpl extends ServiceImpl<OrderItemMapper, OrderItem>
    implements OrderItemService{

}




