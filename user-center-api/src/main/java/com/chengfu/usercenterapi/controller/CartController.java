package com.chengfu.usercenterapi.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chengfu.usercenterapi.common.BaseResponse;
import com.chengfu.usercenterapi.common.BusinessException;
import com.chengfu.usercenterapi.model.domain.BookList;
import com.chengfu.usercenterapi.model.domain.Cart;
import com.chengfu.usercenterapi.model.domain.User;
import com.chengfu.usercenterapi.model.request.CartAddRequest;
import com.chengfu.usercenterapi.service.BookListService;
import com.chengfu.usercenterapi.service.CartService;
import com.chengfu.usercenterapi.utils.ResultUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.chengfu.usercenterapi.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @Autowired
    private BookListService bookListService;

    @PostMapping("/add")
    // 修正参数声明，将 @RequestBody 注解正确放在参数类型前
    public BaseResponse<Boolean> addCart(@RequestBody CartAddRequest cartAddRequest,HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        Long userId = user.getId();
        QueryWrapper<BookList>  queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("book_isbn",cartAddRequest.getBookIsbn());
        BookList bookList = bookListService.getOne(queryWrapper);
        double totalPrice = bookList.getBookPrice() * cartAddRequest.getQuantity();
        Cart cart = new Cart();
        cart.setBookId(bookList.getId());
        cart.setUserId(userId);
        cart.setBookIsbn(cartAddRequest.getBookIsbn());
        cart.setQuantity(cartAddRequest.getQuantity());
        cart.setBookName(bookList.getBookName());
        cart.setTotalPrice(totalPrice);
        boolean result = cartService.save(cart);

        return ResultUtils.success(result);

    }
    @DeleteMapping("/delete")
    public BaseResponse<Boolean> deleteCart(@RequestBody CartAddRequest cartAddRequest,HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        Long userId = user.getId();
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.eq("book_isbn", cartAddRequest.getBookIsbn());
        queryWrapper.eq("quantity", cartAddRequest.getQuantity());
        boolean result = cartService.remove(queryWrapper);
        return ResultUtils.success(result);
    }

    @GetMapping("/get")
    public BaseResponse<List<Cart>> getCart(HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        Long userId = user.getId();
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        List<Cart> cartList = cartService.list(queryWrapper);
        return ResultUtils.success(cartList);
    }

    @DeleteMapping("/clear")
    public BaseResponse<Boolean> clearCart(HttpServletRequest httpServletRequest){
        User user = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        Long userId = user.getId();
        QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        boolean result = cartService.remove(queryWrapper);
        return ResultUtils.success(result);
    }
}
