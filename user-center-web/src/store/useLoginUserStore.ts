import { defineStore } from 'pinia'
import {ref} from "vue";
import {getCurrentUser} from "@/api/user";

export const useLoginUserStore = defineStore('loginUser', () => {
    // 从localStorage初始化登录状态
    const loginUser = ref<any>(JSON.parse(localStorage.getItem('loginUser') || '{"userName":"未登录"}'));

    // 远程获取用户登录信息
    async function fetchLoginUser(){
        const res = await getCurrentUser();
        if (res.data.code === 0 && res.data.data){
            setLoginUser(res.data.data);
        }
    }

    // 设置登录用户信息
    function setLoginUser(newloginUser:any ):void{
        loginUser.value = newloginUser;
        localStorage.setItem('loginUser', JSON.stringify(newloginUser));
    }

    // 清除登录状态
    function clearLoginUser(): void {
        loginUser.value = { userName: "未登录" };
        localStorage.removeItem('loginUser');
    }

    return {loginUser, fetchLoginUser, setLoginUser, clearLoginUser}
})