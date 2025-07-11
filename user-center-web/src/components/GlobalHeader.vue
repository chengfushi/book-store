<template>
    <div id="globalHeader">
        <a-row :wrap="false">
            <!--左边网站logo-->
            <a-col flex="200px">
                <div class="title-bar">
                    <img class="logo" src="../assets/logo.png" alt="logo">
                    <div class="title">耄耋书籍商城</div>
                </div>
            </a-col>

            <!--中部导航栏-->
            <a-col flex="auto">
                <a-menu
                    v-model:selectedKeys="current"
                    mode="horizontal" :items="items"
                    @click="doMenueClick"
                />
            </a-col>

            <!--用户登录状态-->
            <a-col flex="100px">
                <div class="user-login-status">
                    <!--<div v-if="loginUserStore.loginUser.id">-->
                    <!--    {{ loginUserStore.loginUser.userName ?? "耄耋一号" }}-->
                    <!--</div>-->
                        <div>
                            <a-button type="primary" href="/user/login" ghost>登录</a-button>
                        </div>
                    </div>
            </a-col>
        </a-row>
    </div>
</template>

<script lang="ts" setup>
import {h, ref,computed,watch} from "vue";
import {CrownOutlined, HomeOutlined,ShoppingCartOutlined,OrderedListOutlined} from "@ant-design/icons-vue";
import {MenuProps} from "ant-design-vue";
import {useRouter} from "vue-router";
import router from "@/router";
import { useLoginUserStore } from "@/store/useLoginUserStore";

const loginUserStore = useLoginUserStore();
const loginUser = loginUserStore.loginUser;


// 路由跳转实现
const doMenueClick = ({key}: { key: string }) => {
    router.push(
        {
            path: key,
        }
    )
}




//导航栏当前选中项
const items = computed<MenuProps["items"]>(()=>{
  const baseItems=[
    {
      key: "/",
      icon: () => h(HomeOutlined),
      label: "主页",
      title: "主页",
    },
];
  if (!loginUser.id) {
    return baseItems;
  }
  // 普通用户菜单
  if (loginUser.userRole === 0) {
    return [
      ...baseItems,
      {
        key:"/buy",
        icon: () => h(ShoppingCartOutlined),
        label: "图书购买",
        title: "图书购买"
      },

      {
        key: "/cart",
        icon: () => h(ShoppingCartOutlined),
        label: "购物车",
        title: "查看购物车",
      },
      {
        key: "/order",
        icon: () => h(OrderedListOutlined),
        label: "我的订单",
        title: "查看订单历史",
      },
      {
        key:"/message",
        label: "留言板",
        title: "留言板",
      },
      {
        key: "others",
        label: h(
            "a",
            {href: "https://chengfushi.blog.csdn.net/", target: "_blank"},
            "个人博客"
        ),
        title: "个人博客",
      },
    ];
  }
  return [
    ...baseItems,

    {
      key: "/admin/userManage",
      icon: () => h(CrownOutlined),
      label: "用户管理",
      title: "用户管理",
    },
    {
      key: "/book/bookManage",
      icon: () => h(CrownOutlined),
      label: "书籍管理",
      title: "书籍管理",
    },
    {
      key: "/order/orderManage",
      icon: () => h(CrownOutlined),
      label: "订单管理",
      title: "订单管理",
    },
    {
      key:"/message",
      label: "留言板",
      title: "留言板",
    },
    {
      key: "others",
      label: h(
          "a",
          {href: "https://chengfushi.blog.csdn.net/", target: "_blank"},
          "个人博客"
      ),
      title: "个人博客",
    },
  ];
});

// 监听 loginUser 的变化
watch(
    loginUser,
    (newUser) => {
      if (newUser?.id) {
        window.location.reload(); // 强制刷新页面
      }
    },
    { deep: true }
);
</script>

<style scoped>
.title-bar {
    display: flex;
    align-items: center;
}

.title {
    color: black;
    font-size: 18px;
    margin-left: 16px
}

.logo {
    height: 48px;
    width: 48px;
}
</style>
