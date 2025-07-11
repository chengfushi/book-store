import { useLoginUserStore } from "@/store/useLoginUserStore";
import { message } from "ant-design-vue";
import router from "@/router";

/**
 * 全局权限校验
 * 未登录用户只能访问白名单中的路由
 */
router.beforeEach(async (to, from, next) => {
    const loginUserStore = useLoginUserStore();
    const loginUser = loginUserStore.loginUser;
    // 通过检查id属性判断是否真实登录
    const isLoggedIn = !!(loginUser && loginUser.id);
    const toPath = to.path; // 使用path而不是fullPath

    // 定义白名单路由
    const whiteList = ['/', '/home', '/user/login', '/user/register'];
    
    // 未登录用户只能访问白名单路由
    if (!isLoggedIn && !whiteList.includes(toPath)) {
        message.error('请先登录');
        next('/user/login'); // 重定向到登录页
        return;
    }



    // 已登录时的权限校验（原逻辑保留）
    if (toPath.startsWith('/admin')) {
        if (loginUserStore.loginUser.userRole !== 1) {
            message.error('没有权限');
            next(`/user/login?redirect=${to.fullPath}`);
            return;
        }
    }

    next(); // 其他情况正常跳转
});