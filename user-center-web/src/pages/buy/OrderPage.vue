<template>
  <a-table
      :columns="columns"
      :data-source="orderList"
      class="components-table-demo-nested"
      :loading="loading"
      :row-key="record => record.id"
      @expand="handleExpand"
  >
    <!-- 展开行模板：订单详情表格 -->
    <template #expandedRowRender="{ record }">
      <a-table
          :columns="orderItemColumns"
          :data-source="record.items"
          :pagination="false"
          :loading="detailLoading[record.id]"
      >
        <template #bodyCell="{ column, record: item }">
          <!-- 小计列计算 -->
          <template v-if="column.key === 'total'">
            ¥{{ (item.quantity * item.price).toFixed(2) }}
          </template>
        </template>
      </a-table>
    </template>

    <!-- 表格主体单元格模板 -->
    <template #bodyCell="{ column, record }">
      <!-- 订单编号列 -->
      <template v-if="column.key === 'orderNo'">
        <a @click="handleOrderDetail(record)">{{ record.orderNo }}</a>
      </template>

      <!-- 状态列 -->
      <template v-if="column.key === 'status'">
        <a-badge :status="getStatusBadge(record.status)" :text="getStatusText(record.status)" />
      </template>

      <!-- 操作列 -->
      <template v-if="column.key === 'operation'">
        <span class="table-operation">
          <a @click="handlePayOrder(record)">支付</a>
        <a @click="handleDeleteOrder(record)" style="color: red;" :disabled="record.status !== 'pending'">
            取消
          </a>
        </span>
      </template>
    </template>
  </a-table>
</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted } from 'vue';
import { useRouter } from 'vue-router';
import { message, Modal } from 'ant-design-vue';
import { listOrder, listOrderDetail, cancelOrder,payOrder } from '@/api/order'; // 假设cancelOrder为删除接口

// 组件状态
const router = useRouter();
const loading = ref(false); // 列表加载状态
const detailLoading = ref<Record<string, boolean>>({}); // 详情加载状态
const isMounted = ref(true); // 组件挂载状态标记

// 类型定义
interface OrderItem {
  id: number;
  bookName: string;
  quantity: number;
  price: number;
}

interface FrontendOrder {
  id: number;
  key: number;
  orderNo: string;
  createTime: string;
  userName: string;
  totalAmount: number;
  status: string;
  items?: OrderItem[];
}

interface BackendOrder {
  id: number;
  orderNo: string;
  createTime: string;
  userId: number;
  totalAmount: number;
  isPaid: number;
}

interface BackendOrderItem {
  id: number;
  bookName: string;
  quantity: number;
  price: number;
}

// 表格列配置
const columns = [
  { title: '订单编号', dataIndex: 'orderNo', key: 'orderNo' },
  { title: '创建时间', dataIndex: 'createTime', key: 'createTime' },
  { title: '用户ID', dataIndex: 'userId', key: 'userId' },
  {
    title: '总价',
    dataIndex: 'totalAmount',
    key: 'totalAmount',
    render: (text: number) => `¥${text.toFixed(2)}`,
  },
  { title: '状态', dataIndex: 'status', key: 'status' },
  { title: '操作', key: 'operation' },
];

const orderItemColumns = [
  { title: '商品名称', dataIndex: 'bookName', key: 'bookName' },
  { title: '数量', dataIndex: 'quantity', key: 'quantity' },
  { title: '单价', dataIndex: 'price', key: 'price', render: (text: number) => `¥${text.toFixed(2)}` },
  { title: '小计', key: 'total' },
];

const orderList = ref<FrontendOrder[]>([]); // 订单列表数据

// 状态映射（使用字符串键，类型安全）
const statusMap = {
  '0': { text: '待支付', badge: 'warning', status: 'pending' },
  '1': { text: '已支付', badge: 'success', status: 'paid' },
} as const;

// 状态转换工具函数
const getStatusText = (status: string): string => {
  return Object.values(statusMap).find(item => item.status === status)?.text || '未知状态';
};

const getStatusBadge = (status: string): string => {
  return Object.values(statusMap).find(item => item.status === status)?.badge || 'default';
};

// 获取订单列表
const fetchOrderList = async () => {
  loading.value = true;
  try {
    const response = await listOrder();
    const backendData: BackendOrder[] = response.data.data;

    // 数据转换（类型安全映射）
    const transformedData = backendData.map(order => ({
      key: order.id,
      id: order.id,
      orderNo: order.orderNo,
      createTime: new Date(order.createTime).toLocaleString(),
      userId: order.userId,
      userName: `用户${order.userId}`,
      totalAmount: order.totalAmount,
      status: statusMap[order.isPaid.toString()]?.status || 'pending',
      items: [], // 初始为空，展开时加载
    }));

    if (isMounted.value) orderList.value = transformedData;
  } catch (error) {
    message.error('获取订单列表失败');
  } finally {
    if (isMounted.value) loading.value = false;
  }
};

// 展开行处理（加载订单详情）
const handleExpand = async (expanded: boolean, record: FrontendOrder) => {
  if (expanded && (!record.items || record.items.length === 0)) {
    detailLoading.value = { ...detailLoading.value, [record.id]: true };
    try {
      const response = await listOrderDetail(record.id.toString());
      const backendItems: BackendOrderItem[] = response.data.data;

      if (isMounted.value) {
        record.items = backendItems.map(item => ({
          id: item.id,
          bookName: item.bookName,
          quantity: item.quantity,
          price: item.price,
        }));
      }
    } catch (error) {
      message.error('获取订单详情失败');
    } finally {
      if (isMounted.value) detailLoading.value[record.id] = false;
    }
  }
};


// 删除订单逻辑（无对话框）
const handleDeleteOrder = async (order: FrontendOrder) => {
  // 状态校验：仅待支付订单可删除
  if (order.status !== 'pending') {
    message.warn('仅待支付订单可取消');
    return;
  }

  try {
    // 调用删除接口
    await cancelOrder(order.id.toString());

    // 更新订单列表
    if (isMounted.value) {
      orderList.value = orderList.value.filter(item => item.id !== order.id);
      message.success('订单取消成功');
    }
  } catch (error: any) {
    const errorMsg = error.response?.data?.message || '取消订单失败，请重试';
    message.error(errorMsg);
    console.error('取消订单失败:', error);
  }
};

const handlePayOrder = async (order: FrontendOrder) => {
  // 状态校验：仅待支付订单可支付
  if (order.status !== 'pending') {
    message.warning('仅待支付订单可支付');
    return;
  }

  try {
    // 显示加载状态
    const hideLoading = message.loading('支付处理中...', 0);

    // 调用支付接口
    const response = await payOrder(order.id.toString());

    // 隐藏加载状态
    hideLoading();

    // 检查支付结果
    if (response.data.code === 0) {
      message.success('支付成功');

      // 更新本地订单状态（避免重新获取整个列表）
      const index = orderList.value.findIndex(item => item.id === order.id);
      if (index !== -1) {
        orderList.value[index].status = 'paid';
      }

      // 或者直接刷新订单列表（根据业务需求选择）
      // await fetchOrderList();
    } else {
      message.error(response.data.message || '支付失败');
    }
  } catch (error) {
    console.error('支付处理出错:', error);
    message.error('支付处理失败，请稍后重试');
  }
};


// 生命周期钩子
onMounted(() => fetchOrderList());
onUnmounted(() => (isMounted.value = false));
</script>

<style scoped>
.table-operation a {
  margin-right: 10px;
}

/* 删除按钮红色样式 */
.table-operation a[style*="color: red"] {
  color: #ff4d4f !important;
}
</style>