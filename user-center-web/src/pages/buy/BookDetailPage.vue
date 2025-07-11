<template>
  <div class="book-detail" v-if="book">
    <div class="detail-container">
      <!-- 左侧图片 -->
      <div class="book-image">
        <img
            v-if="book.bookImageBase64"
            :src="`data:image/jpeg;base64,${book.bookImageBase64}`"
            alt="书籍封面"
        />
      </div>

      <!-- 右侧信息 -->
      <div class="book-info">
        <h1 class="book-title">{{ book.bookName }}</h1>
        <p class="book-isbn">ISBN: {{ book.bookIsbn }}</p>
        <p class="book-price">价格: ¥{{ book.bookPrice}}</p>
        <p class="publish-date">
          出版日期: {{ book.publishDate}}
        </p>

        <div class="quantity-control">
          <span>数量: </span>
          <a-input-number
              v-model:value="quantity"
              :min="1"
              :max="10"
              @change="updateTotalPrice"
          />          
          <div class="quantity-control">
            <a-button type="primary" @click="handleBuyNow">立即购买</a-button>
          <a-button type="primary" @click="addToCart">加入购物车</a-button>
        </div>
      </div>
        </div>
    </div>

    <!-- 书籍描述 -->
    <div class="book-description">
      <h2>书籍描述</h2>
      <p>{{ book.bookDescription || '暂无描述' }}</p>
    </div>
  </div>


</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import dayjs from 'dayjs';
import { message } from 'ant-design-vue';
import { getBookDetail } from '@/api/book';
import {addCart, clearCart} from "@/api/cart";
import {createOrder} from "@/api/order";


interface Book {
  bookIsbn: string;
  bookName: string;
  bookPrice?: number;
  publishDate: dayjs.Dayjs | null;
  bookImageBase64: string;
  bookDescription: string;
}

const route = useRoute();
const router = useRouter();
const book = ref<Book | null>(null);

const quantity = ref(1);

const addToCart = async () => {
  try {
    // 获取当前书籍ID，假设从路由或props中获取
    const bookIsbn = route.query.isbn as string; // 假设路由参数名为isbn

    if (!bookIsbn) {
      message.error('请选择有效的书籍');
      return;
    }

    // 调用API加入购物车
    const response = await addCart({
      bookIsbn: bookIsbn,
      quantity: quantity.value
    });

    if (response.data.code === 0) {
      message.success('成功加入购物车');
      // 可以在这里添加其他成功后的逻辑，如更新购物车数量显示等
    } else {
      message.error(response.data.message || '加入购物车失败');
    }
  } catch (error) {
    console.error('加入购物车出错:', error);
    message.error('加入购物车失败，请稍后重试');
  }
};
const fetchBookDetail = async (bookIsbn: string) => {
  try {
    const res = await getBookDetail(bookIsbn);
    console.log('API响应数据:', res.data); // 调试日志

    // 直接访问 res.data 作为书籍数据
    const bookData = res.data.data;

    // 验证数据有效性（检查必要字段是否存在）
    if (bookData && bookData.bookIsbn) {
      book.value = {
        ...bookData, // 展开书籍数据对象
        publishDate: bookData.publishDate
            ? dayjs(bookData.publishDate).format('YYYY-MM-DD') // 格式化日期显示
            : null
      };
      console.log('处理后的书籍数据:', book.value);
    } else {
      message.error("获取书籍详情失败: 数据格式错误");
      book.value = null;
    }
  } catch (error) {
    console.error("获取书籍详情出错:", error);
    message.error("获取书籍详情失败: 网络错误");
    book.value = null;
  }
}

const handleBuyNow = async () => {
  try {
    if (!book.value) {
      message.error('书籍信息加载失败');
      return;
    }

    const bookIsbn = book.value.bookIsbn; // 直接从当前书籍对象获取ISBN
    if (!bookIsbn) {
      message.error('缺少书籍ISBN参数');
      return;
    }

    // 构造单个购物车项（对象）
    const cartAddRequest = {
      bookIsbn: bookIsbn,
      quantity: quantity.value
    };

    // 将单个对象包装成数组（关键修复点）
    const orderCreateRequest = {
      cartAddRequests: [cartAddRequest] // 注意：这里添加数组包裹符[]
    };

    // 发送请求
    const response = await createOrder(orderCreateRequest);

    if (response.data.code === 0) {
      message.success('订单创建成功');
    } else {
      message.error(response.data.message || '订单创建失败');
    }
  } catch (error) {
    console.error('立即购买出错:', error);
    message.error('订单创建失败，请稍后重试');
  }
};

onMounted(() => {
  const isbn = route.query.isbn as string;
  console.log('路由参数isbn:', isbn); // 调试日志
  if (isbn) {
    fetchBookDetail(isbn);
  } else {
    message.error('缺少书籍ISBN参数');
    router.back();
  }
});



</script>

<style scoped>
.book-detail {
  max-width: 1200px;
  margin: 20px auto;
  padding: 20px;
}

.detail-container {
  display: flex;
  gap: 40px;
  margin-bottom: 40px;
}

.book-image {
  flex: 0 0 300px;
}

.book-image img {
  width: 100%;
  max-height: 400px;
  object-fit: contain;
}

.book-info {
  flex: 1;
}

.book-title {
  font-size: 24px;
  margin-bottom: 10px;
}

.book-isbn, .book-price, .publish-date {
  color: #666;
  margin-bottom: 8px;
}

.book-price {
  font-size: 20px;
  color: #f56c6c;
  font-weight: bold;
}

.quantity-control {
  margin: 20px 0;
  display: flex;
  align-items: center;
  gap: 10px;
}

.total-price {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 20px;
}

.actions {
  display: flex;
  gap: 15px;
}

.book-description {
  margin-top: 40px;
  padding: 20px;
  background-color: #f9f9f9;
  border-radius: 8px;
}
.shoppingcart{
  margin-top: 20px;
}
.cart{
  width: 40px; /* 必须指定宽度 */
  margin-left: auto;
}

.loading {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
}
</style>