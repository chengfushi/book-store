package com.chengfu.usercenterapi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.chengfu.usercenterapi.model.domain.Cart;
import com.chengfu.usercenterapi.service.CartService;
import com.chengfu.usercenterapi.mapper.CartMapper;
import org.springframework.stereotype.Service;

/**
* @author Lenovo
* @description 针对表【cart】的数据库操作Service实现
* @createDate 2025-05-31 13:16:28
*/
@Service
public class CartServiceImpl extends ServiceImpl<CartMapper, Cart>
    implements CartService{

}




