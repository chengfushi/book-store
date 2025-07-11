<template>
    <div id="messageViewPage" style="margin-bottom: 20px">
        <div class="message-list">
            <a-table :columns="columns" :data-source="data">
                <template #bodyCell="{ column, record }">
                    <template v-if="column.dataIndex === 'action'">
                        <a-button danger @click="doDelete(record.id)">删除</a-button>
                    </template>
                </template>
            </a-table>
        </div>

        <!-- 添加留言表单 -->
        <div class="message-form bg-white rounded-xl shadow-sm p-6 border border-gray-100">
            <h3 class="text-lg font-semibold text-gray-800 mb-4">
                <SmileOutlined class="mr-2 text-primary" />
                留下你的留言
            </h3>

            <a-form :model="formState">
                <a-form-item label="留言内容" class="mb-6">
                    <a-textarea
                        v-model:value="formState.messageContent"
                        placeholder="请输入留言内容..."
                        class="w-full rounded-lg border-gray-200 focus:border-primary focus:ring-2 focus:ring-primary/20 transition-all duration-200"
                        :rows="5"
                    />
                </a-form-item>

                <a-form-item class="text-right" >
                    <a-button
                        type="primary"
                        :disabled="!formState.messageContent.trim()"
                        @click="handleSubmit"
                        class="bg-primary hover:scale-105 transition-all duration-200 transform rounded-lg px-6 py-3 text-white"

                        style="margin: 0 auto"
                    >
                        提交留言
                    </a-button>
                </a-form-item>
            </a-form>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { SmileOutlined, DownOutlined } from '@ant-design/icons-vue';
import { reactive, ref, h } from "vue"; // 导入 h 函数
import { message } from "ant-design-vue";
import dayjs from "dayjs";
import { deleteMessage, getMessageList, addMessage } from "@/api/message";
import { useLoginUserStore } from "@/store/useLoginUserStore";

//获取全局登录状态
const loginUserStore = useLoginUserStore();

// 定义formState用于表单数据
const formState = reactive({
    messageContent: ''
});

const doDelete = async (id: string) => {
    if (!id) {
        return;
    }
    const res = await deleteMessage(id);
    if (res.data.code === 0) {
        message.success("删除成功");
        fetchData(); // 删除成功后刷新列表
    } else {
        message.error("删除失败");
    }
}

const handleSubmit = async () => {
    console.log('messageContent:', formState.messageContent);
    // 修复1: 正确的空值检查逻辑
    if (!formState.messageContent.trim()) { // 修复空值检查逻辑
        message.error("留言不能为空");
        return;
    }

    // 修复2: 直接传递字符串而不是对象
    const res = await addMessage({ messageContent: formState.messageContent }); // 确保传递的是字符串
    if (res.data.code === 0) {
        message.success("留言成功");
        formState.messageContent = ''; // 清空输入框
        fetchData(); // 刷新列表
    } else {
        message.error("留言失败: " + res.data.message || "未知错误");
    }
}

const columns = [
    {
        title: '用户名',
        dataIndex: 'userName',
    },
    {
        title: '留言内容',
        dataIndex: 'messageContent',
    },
    {
        title: '创建时间',
        dataIndex: 'createTime',
        customRender: ({ text }) => dayjs(text).format('YYYY-MM-DD HH:mm:ss')
    },
    // {
    //     // 只有管理员才显示操作列
    //     ...(loginUserStore.loginUser.userRole === 1 ? [{
    //         title: '操作',
    //         dataIndex: 'action',
    //     }] : [])
    // }
    ...(loginUserStore.loginUser.userRole === 1 ? [
        {
            title: '操作',
            dataIndex: 'action',
            // 使用标准的render函数语法
            render: (text, record) => {
                return h('a-button', {
                    danger: true,
                    onClick: () => doDelete(record.id)
                }, '删除');
            }
        }
    ] : [])
];

const data = ref([]);

const fetchData = async () => {
    try {
        const res = await getMessageList();
        if (res.data.code === 0 && res.data.data) {
            data.value = res.data.data;
        } else {
            message.error("获取留言列表失败");
        }
    } catch (error) {
        console.error("获取留言列表异常:", error);
        message.error("获取留言列表异常");
    }
}

// 初始化加载数据
fetchData();
</script>

<style scoped>
.container {
    margin: 0 auto;
    padding: 0 1rem;
}

.text-primary {
    color: #1677ff;
}

.bg-primary {
    background-color: #1677ff;
}

.rounded-lg {
    border-radius: 0.5rem;
}

.rounded-xl {
    border-radius: 0.75rem;
}

.shadow-sm {
    box-shadow: 0 1px 3px rgba(0, 0, 0, 0.12);
}

.transition-all {
    transition-property: all;
    transition-timing-function: cubic-bezier(0.4, 0, 0.2, 1);
    transition-duration: 150ms;
}

.duration-200 {
    transition-duration: 200ms;
}

.transform {
    transform: translateZ(0);
}

.hover\:scale-105:hover {
    transform: scale(1.05);
}
</style>