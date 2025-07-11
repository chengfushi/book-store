import myAxios from "@/request";

/*
* 添加购物车
*/
export const addCart = async (params: any) => {
    return myAxios.request({
        url: "/api/cart/add",
        method: "POST",
        data: params,
    });
};

export const getCart = async ()=>{
    return myAxios.request({
        url: "/api/cart/get",
        method: "GET",
    });
}

export const deleteCart = async (params:any)=>{
    return myAxios.request({
        url: "/api/cart/delete",
        method: "DELETE",
        data: params,
    });
}

/*
* 清空购物车
* */
export const clearCart = async ()=>{
    return myAxios.request({
        url: "/api/cart/clear",
        method: "DELETE",
    });
}

