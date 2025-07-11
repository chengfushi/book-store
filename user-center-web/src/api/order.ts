import myAxios from "@/request";

/**
 * @description: 创建订单
 * @param params
 */
export const createOrder = async (params: any) => {
    return myAxios.request({
        url: "/api/order/create",
        method: "POST",
        data: params,
    });
};

/**
 * @description: 获取订单列表
 */
export const listOrder = async () => {
    return myAxios.request({
        url: "/api/order/list",
        method: "GET",
    });
};

/**
 * @description: 获取订单详情
 * @param orderId
 */
export const listOrderDetail = async (orderId: string) => {
    return myAxios.request({
        url: `/api/order/list/detail/${orderId}`,
        method: "GET",
    });
};

/**
 * @description: 支付订单
 * @param orderId
 */
export const payOrder = async (orderId: string) => {
    return myAxios.request({
        url: `/api/order/pay/${orderId}`,
        method: "POST",
    });
};

/**
 * @description: 取消订单
 * @param orderId
 */
export const cancelOrder = async (orderId: string) => {
    return myAxios.request({
        url: `/api/order/cancel/${orderId}`,
        method: "POST",
    });
};