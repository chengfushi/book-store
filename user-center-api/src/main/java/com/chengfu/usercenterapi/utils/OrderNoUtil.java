package com.chengfu.usercenterapi.utils;

import java.util.Random;

public class OrderNoUtil {
    /**
     * 生成8位随机数字订单编号
     */
    public static String generateOrderNo() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }

    /**
     * 生成带前缀的订单编号
     * @param prefix 前缀，如"ORD"
     */
    public static String generateOrderNo(String prefix) {
        return prefix + generateOrderNo();
    }
}

