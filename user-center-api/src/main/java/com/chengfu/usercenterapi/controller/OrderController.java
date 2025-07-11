package com.chengfu.usercenterapi.controller;



import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.chengfu.usercenterapi.common.BaseResponse;
import com.chengfu.usercenterapi.common.BusinessException;
import com.chengfu.usercenterapi.common.ErrorCode;
import com.chengfu.usercenterapi.model.domain.BookList;
import com.chengfu.usercenterapi.model.domain.OrderItem;
import com.chengfu.usercenterapi.model.domain.OrderMain;
import com.chengfu.usercenterapi.model.domain.User;
import com.chengfu.usercenterapi.model.request.CartAddRequest;
import com.chengfu.usercenterapi.model.request.OrderCreateRequest;
import com.chengfu.usercenterapi.service.BookListService;
import com.chengfu.usercenterapi.service.OrderItemService;
import com.chengfu.usercenterapi.service.OrderMainService;
import com.chengfu.usercenterapi.utils.OrderNoUtil;
import com.chengfu.usercenterapi.utils.ResultUtils;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

import static com.chengfu.usercenterapi.constant.UserConstant.USER_LOGIN_STATE;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderMainService orderMainService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private BookListService bookListService;

    @PostMapping("/create")
    @Transactional
    public BaseResponse<Boolean> createOrder(@RequestBody OrderCreateRequest orderCreateRequest,
                                             HttpServletRequest httpServletRequest) {
        // 获取用户信息
        User user = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        // 创建订单主表
        OrderMain orderMain = new OrderMain();
        String orderNo = OrderNoUtil.generateOrderNo();
        orderMain.setOrderNo(orderNo);
        orderMain.setUserId(user.getId());

        List<CartAddRequest> cartAddRequests = orderCreateRequest.getCartAddRequests();

        // 在保存主订单前先计算总价
        double totalPrice = cartAddRequests.stream()
                .mapToDouble(request -> {
                    BookList book = bookListService.getOne(new QueryWrapper<BookList>()
                            .eq("book_isbn", request.getBookIsbn()));
                    return book.getBookPrice() * request.getQuantity();
                })
                .sum();

        orderMain.setTotalAmount(totalPrice);
        orderMainService.save(orderMain);

        // 处理每个订单项
        for (CartAddRequest cartAddRequest : cartAddRequests) {
            // 查询书籍信息
            QueryWrapper<BookList> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("book_isbn", cartAddRequest.getBookIsbn());
            BookList bookList = bookListService.getOne(queryWrapper);
            if (bookList == null) {
                throw new BusinessException(ErrorCode.PARAMS_ERROR, "书籍不存在");
            }

            // 创建并保存订单项
            OrderItem orderItem = new OrderItem();  // 每次循环新建对象
            orderItem.setOrderId(orderMain.getId());  // 设置关联ID
            orderItem.setBookId(bookList.getId());
            orderItem.setBookIsbn(bookList.getBookIsbn());
            orderItem.setQuantity(cartAddRequest.getQuantity());
            orderItem.setPrice(bookList.getBookPrice());
            orderItem.setBookName(bookList.getBookName());
            orderItemService.save(orderItem);
        }

        // 更新订单总金额
        orderMain.setTotalAmount(totalPrice);
        orderMainService.updateById(orderMain);

        return ResultUtils.success(true);
    }

    @GetMapping("/list")  // 改为GET请求
    public BaseResponse<List<OrderMain>> listOrder(HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        //管理员返回所有订单
        if (user.getUserRole() == 1) {
            return ResultUtils.success(orderMainService.list());
        }
        else {
            // 添加排序条件
            List<OrderMain> orderMainList = orderMainService.list(
                    new QueryWrapper<OrderMain>()
                            .eq("user_id", user.getId())
                            .orderByDesc("create_time"));
            return ResultUtils.success(orderMainList);

        }
    }

    @GetMapping("/list/detail/{orderId}")
    public BaseResponse<List<OrderItem>> listOrderDetail(
            @PathVariable String orderId,
            HttpServletRequest httpServletRequest) {

        User user = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }


        // 查询订单明细
        List<OrderItem> orderItemList = orderItemService.list(
                new QueryWrapper<OrderItem>()
                        .eq("order_id", orderId));

        return ResultUtils.success(orderItemList);
    }

    //订单结算
    @PostMapping("/pay/{orderId}")
    public BaseResponse<Boolean> payOrder(@PathVariable String orderId,
                                           HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        // 先验证订单是否属于当前用户
        OrderMain order = orderMainService.getOne(
                new QueryWrapper<OrderMain>()
                        .eq("id", orderId)
                        .eq("user_id", user.getId()));

        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }

        // 修改订单状态
        order.setIsPaid(1);
        orderMainService.updateById(order);

        return ResultUtils.success(true);
    }

    //取消订单
    @PostMapping("/cancel/{orderId}")
    public BaseResponse<Boolean> cancelOrder(@PathVariable String orderId,
                                             HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null) {
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }

        // 先验证订单是否属于当前用户
        OrderMain order = orderMainService.getOne(
                new QueryWrapper<OrderMain>()
                        .eq("id", orderId)
                        .eq("user_id", user.getId()));

        if (order == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "订单不存在");
        }

        // 修改订单状态
        order.setIsPaid(0);
        // 1. 删除订单明细表中关联的子记录
        orderItemService.removeByMap(Collections.singletonMap("order_id", orderId));
        // 2. 删除订单主表中关联的记录
        orderMainService.removeById(orderId);

        return ResultUtils.success(true);
    }



}
