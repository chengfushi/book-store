import myAxios from "@/request";

export const getMessageList = async () => {
    return myAxios.request({
        url: "/api/message/search",
        method: "POST",
    });
}

export const addMessage = async (messageContent:any) => {
    return myAxios.request({
        url: "/api/message/add",
        method: "POST",
        data: messageContent,

    });
}

export const deleteMessage = async (id: any) => {
    return myAxios.request({
        url: "/api/message/delete",
        method: "DELETE",
        data: id,

        // 关键点：要传递 JSON 格式的值
        headers: {
            "Content-Type": "application/json",
        },
    })
}