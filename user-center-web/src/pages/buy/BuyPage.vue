<template>
  <div class="buy-page">
    <!-- 搜索框 -->
    <div class="search-bar">
      <a-input-search
          v-model:value="searchValue"
          placeholder="输入书名搜索"
          enter-button="搜索"
          @search="onSearch"
      />
    </div>

    <!-- 商品列表 -->
    <div class="book-list">
      <div
          v-for="book in data"
          :key="book.bookIsbn"
          class="book-card"
          @click="goToDetail(book.bookIsbn)"
      >
        <div class="book-image">
          <img
              v-if="book.bookImageBase64"
              :src="`data:image/jpeg;base64,${book.bookImageBase64}`"
              alt="书籍封面"
          />
        </div>
        <div class="book-info">
          <h3 class="book-title">{{ book.bookName }}</h3>
          <p class="book-price">¥{{ book.bookPrice.toFixed(2) }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import dayjs from 'dayjs';
import { message } from 'ant-design-vue';
import { searchBook} from '@/api/book';

interface Book {
  bookIsbn: string;
  bookName: string;
  bookPrice: number;
  publishDate: dayjs.Dayjs | null;
  bookImageBase64: string;
  bookDescription: string;
  purchaseQuantity?: number;
  totalPrice?: number;
}

const router = useRouter();
const data = ref<Book[]>([]);
const searchValue = ref("");

const fetchData = async (bookName = "") => {
  try {
    const res = await searchBook(bookName);
    if (res.data.data) {
      data.value = res.data.data.map((item: any) => ({
        ...item,
        publishDate: item.publishDate ? dayjs(item.publishDate) : null,
        purchaseQuantity: 1,
        totalPrice: item.bookPrice
      }));
    } else {
      message.error("获取书籍列表失败");
      data.value = [];
    }
  } catch (error) {
    console.error("获取书籍列表出错:", error);
    message.error("获取书籍列表失败: 网络错误");
    data.value = [];
  }
}

const onSearch = () => {
  fetchData(searchValue.value);
}


const goToDetail = (bookIsbn: string) => {
  router.push({
    path: '/book/detail',
    query: { isbn: bookIsbn }  // 使用isbn作为参数名更清晰
  });
}

// 初始化加载数据
fetchData();
</script>

<style scoped>
.buy-page {
  padding: 20px;
  max-width: 1200px;
  margin: 0 auto;
}

.search-bar {
  margin-bottom: 20px;
}

.book-list {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(200px, 1fr));
  gap: 20px;
}

.book-card {
  border: 1px solid #f0f0f0;
  border-radius: 8px;
  padding: 15px;
  cursor: pointer;
  transition: all 0.3s;
}

.book-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.book-image img {
  width: 100%;
  height: 180px;
  object-fit: contain;
  margin-bottom: 10px;
}

.book-info {
  text-align: center;
}

.book-title {
  font-size: 16px;
  margin-bottom: 8px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.book-price {
  color: #f56c6c;
  font-weight: bold;
  font-size: 18px;
}
</style>
