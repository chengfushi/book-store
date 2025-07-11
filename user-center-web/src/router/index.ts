import {createRouter, createWebHistory, RouteRecordRaw} from 'vue-router'
import HomePage from "@/pages/HomePage.vue";
import UserLoginPage from "@/pages/user/UserLoginPage.vue";
import UserRegisterPage from "@/pages/user/UserRegisterPage.vue";
import UserManagerPage from "@/pages/admin/UserManagerPage.vue";
import MessageViewPage from "@/pages/message/MessageViewPage.vue";
import BuyPage from "@/pages/buy/BuyPage.vue";
import CarPage from "@/pages/buy/CarPage.vue";
import OrderPage from "@/pages/buy/OrderPage.vue";
import BookManagerPage from "@/pages/admin/BookManagerPage.vue";
import OrderManagerPage from "@/pages/admin/OrderManagerPage.vue";
import BookDetail from "@/views/book/BookDetail.vue";
import BookDetailPage from "@/pages/buy/BookDetailPage.vue";

const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'home',
        component: HomePage
    },
    {
        path: '/user/login',
        name: 'login',
        component: UserLoginPage,
    },
    {
        path: '/user/register',
        name: 'register',
        component: UserRegisterPage,
    },
    {
        path: '/admin/userManage',
        component: UserManagerPage,
    },
    {
        path:'/book/bookManage',
        component: BookManagerPage,
    },
    {
        path:'/message',
        component:MessageViewPage,
    },
    {
        path:'/buy',
        component:BuyPage,
    },
    {
        path:'/cart',
        component:CarPage,
    },
    {
        path:'/order',
        component:OrderPage,
    },
    {
        path:'/order/orderManage',
        component:OrderManagerPage,
    },
    {
        path: '/book/detail',
        name: 'BookDetail',
        component: () => import('@/pages/buy/BookDetailPage.vue'),
        props: (route) => ({ query: route.query })  // 将query参数作为props传递
    },

]

const router = createRouter({
    history: createWebHistory(process.env.BASE_URL),
    routes
})

export default router
