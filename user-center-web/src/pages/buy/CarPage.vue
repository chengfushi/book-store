<template>
  <div id="car" class="shopping-cart">
    <h2 class="page-title">购物车</h2>

    <!-- 加载状态 -->
    <div v-if="loading" class="loading-status">
      <i class="el-icon-loading"></i>
      <span>加载中...</span>
    </div>

    <!-- 空状态 -->
    <div v-else-if="cartItems.length === 0" class="empty-state">
      <div class="empty-content">
        <p class="empty-tip">购物车还是空的，快去挑选喜欢的商品吧！</p>
        <router-link to="/buy" class="go-shopping-btn">去逛逛</router-link>
      </div>
    </div>

    <!-- 商品列表 -->
    <div v-else class="cart-list">
      <div v-for="item in cartItems" :key="item.id" class="cart-item">
        <div class="item-info">
          <h3 class="item-name">{{ item.bookName }}</h3>
          <div class="item-attrs">
            <span>ISBN：{{ item.bookIsbn }}</span>
            <span>数量：{{ item.quantity }}</span>
          </div>
        </div>

        <div class="item-price">￥{{ item.totalPrice.toFixed(2) }}</div>

        <div class="item-actions">
          <button
              @click="handleDeleteCart(item)"
              class="delete-btn"
          >
            <i class="el-icon-delete"></i>
            删除
          </button>
        </div>
      </div>

      <!-- 底部结算栏 -->
      <div class="cart-footer">
        <div class="total-info">
          <span>合计：￥{{ calculateTotal() }}</span>
        </div>
        <!-- 结算按钮 -->
        <div class="cart-footer">
          <a-button type="primary" size="large" @click="handleCheckout" :loading="checkoutLoading">
            去结算
          </a-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
// 逻辑代码与之前完全一致，仅移除图片相关代码
import { ref, onMounted } from 'vue';
import {getCart, deleteCart, clearCart} from "@/api/cart";

const cartItems = ref<any[]>([]);
const loading = ref(false);

// 计算总价方法
const calculateTotal = () => {
  return cartItems.value.reduce((sum, item) => sum + (item.totalPrice || 0), 0).toFixed(2);
};

// 删除逻辑和数据获取逻辑不变
const handleDeleteCart = async (item: any) => {
  try {
    const res = await deleteCart({
      bookIsbn: item.bookIsbn,
      quantity: item.quantity
    });
    if (res.data.code === 0) {
      fetchCart();
    } else {
      console.error('删除购物车失败:', res.data.message);
    }
  } catch (error) {
    console.error('删除购物车失败:', error);
  }
};

const fetchCart = async () => {
  try {
    loading.value = true;
    const res = await getCart();
    if (res.data.code === 0) {
      cartItems.value = res.data.data || [];
    } else {
      console.error('获取购物车失败:', res.data.message);
      cartItems.value = [];
    }
  } catch (error) {
    console.error('获取购物车失败:', error);
    cartItems.value = [];
  } finally {
    loading.value = false;
  }
};
import { createOrder } from '@/api/order';
import {message} from "ant-design-vue";

const checkoutLoading = ref(false);

const handleCheckout = async () => {
  try {
    checkoutLoading.value = true;

    // 1. 构造CartAddRequest数组（与之前逻辑相同）
    const cartAddRequests = cartItems.value.map(item => ({
      bookIsbn: item.bookIsbn,
      quantity: item.quantity
    }));

    // 2. 将数组包装成OrderCreateRequest对象
    const orderCreateRequest = {
      cartAddRequests: cartAddRequests // 关键：添加cartAddRequests字段
    };

    // 3. 传递对象给后端接口
    const res = await createOrder(orderCreateRequest); // 注意：参数改为对象

    if (res.data.code === 0) {
      // 订单创建成功,清空购物车
      await clearCart();
      await fetchCart();

      message.success('下单成功');
    } else {
      message.error(`下单失败: ${res.data.message}`);
    }
  } catch (error) {
    console.error('下单失败:', error);
    message.error('下单失败,请稍后重试');
  } finally {
    checkoutLoading.value = false;
  }
};


onMounted(() => {
  fetchCart();
});
</script>

<style scoped>
.shopping-cart {
  max-width: 800px;
  margin: 20px auto;
  padding: 30px 25px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 24px rgba(0, 0, 0, 0.06);
}

.page-title {
  font-size: 22px;
  color: #2c3e50;
  margin-bottom: 25px;
  padding-bottom: 12px;
  border-bottom: 2px solid #f0f2f5;
}

.loading-status {
  text-align: center;
  padding: 50px 0;
  color: #7f8c8d;
  font-size: 16px;
}

.empty-state {
  text-align: center;
  padding: 80px 0;
}

.empty-content {
  max-width: 500px;
  margin: 0 auto;
}

.empty-tip {
  font-size: 18px;
  color: #6c757d;
  margin-bottom: 20px;
}

.go-shopping-btn {
  display: inline-block;
  padding: 10px 28px;
  background-color: #409eff;
  color: #fff;
  border-radius: 6px;
  text-decoration: none;
  font-size: 16px;
  transition: background-color 0.3s;
}

.go-shopping-btn:hover {
  background-color: #66b1ff;
}

.cart-list {
  width: 100%;
}

.cart-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 22px 20px;
  margin-bottom: 20px;
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  transition: all 0.2s ease;
}

.cart-item:hover {
  transform: translateX(8px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
}

.item-info {
  flex-grow: 1;
  margin-right: 30px;
}

.item-name {
  font-size: 18px;
  color: #34495e;
  margin-bottom: 8px;
}

.item-attrs {
  font-size: 14px;
  color: #7f8c8d;
}

.item-price {
  font-size: 18px;
  color: #e74c3c;
  font-weight: 500;
}

.item-actions {
  flex-shrink: 0;
}

.delete-btn {
  padding: 8px 18px;
  background-color: #e74c3c;
  color: #fff;
  border: none;
  border-radius: 4px;
  font-size: 14px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.delete-btn:hover {
  background-color: #c0392b;
}

.cart-footer {
  margin-top: 40px;
  padding-top: 25px;
  border-top: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 18px;
}

.total-info {
  color: #2c3e50;
  font-weight: 600;
}

.checkout-btn {
  padding: 12px 32px;
  background-color: #409eff;
  color: #fff;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  cursor: pointer;
  transition: transform 0.2s;
}

.checkout-btn:hover {
  transform: scale(1.03);
}
</style>