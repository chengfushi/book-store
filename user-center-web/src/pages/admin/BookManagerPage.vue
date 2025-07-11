<template>
    <div id="bookManage" style="margin-bottom: 30px">
        <div class="search">
            <a-input-search
                style="max-width: 320px;margin-bottom: 20px"
                v-model:value="searchValue"
                placeholder="输入书名搜索"
                enter-button="搜索"
                size="large"
                @search="onSearch"
            />
        </div>

        <!-- 管理员才显示添加书籍表单 -->
        <div class="add" style="margin-bottom: 20px" v-if="isAdmin">
            <a-form
                :model="formState"
                name="add_book_form"
                layout="inline"
                autocomplete="off"
                @finish="onFinish"
                @finishFailed="onFinishFailed"
            >
                <a-form-item
                    label="ISBN"
                    name="bookIsbn"
                    :rules="[{ required: true, message: '请输入ISBN!' }]"
                >
                    <a-input v-model:value="formState.bookIsbn"/>
                </a-form-item>

                <a-form-item
                    label="书名"
                    name="bookName"
                    :rules="[{ required: true, message: '请输入书名!' }]"
                >
                    <a-input v-model:value="formState.bookName"/>
                </a-form-item>

                <a-form-item
                    label="价格"
                    name="bookPrice"
                    :rules="[{ required: true, message: '请输入价格!' }]"
                >
                    <a-input-number v-model:value="formState.bookPrice" :min="0"/>
                </a-form-item>
              <a-form-item
                  label="描述"
                  name="bookDescription"
                  :rules="[{ required: true, message: '请输入书籍描述!' }]"
              >
                <a-input v-model:value="formState.bookDescription"/>
              </a-form-item>


                <a-form-item
                    label="图片"
                    name="bookImageBase64"
                >
                    <a-upload
                        name="avatar"
                        list-type="picture-card"
                        class="avatar-uploader"
                        :show-upload-list="false"
                        :before-upload="beforeUpload"
                        :customRequest="handleCustomUpload"
                    >
                        <img v-if="imageUrl" :src="imageUrl" alt="avatar" style="width: 100%" />
                        <div v-else>
                            <loading-outlined v-if="loading"></loading-outlined>
                            <plus-outlined v-else></plus-outlined>
                            <div class="ant-upload-text">上传</div>
                        </div>
                    </a-upload>
                </a-form-item>

                <a-form-item
                    label="出版日期"
                    name="publishDate"
                    :rules="[{ required: true, message: '请选择出版日期!' }]"
                >
                    <a-date-picker v-model:value="formState.publishDate"/>
                </a-form-item>

                <a-form-item>
                    <a-button :disabled="disabled" type="primary" html-type="submit">添加书籍</a-button>
                </a-form-item>
            </a-form>
        </div>

        <div class="table">
            <a-table :columns="isAdmin ? columns : userColumns" :data-source="data">
                <template #bodyCell="{ column, record }">
                    <!-- 管理员操作列 -->
                    <template v-if="column.dataIndex === 'action' && isAdmin">
                        <a-button @click="openEditModal(record)">编辑</a-button>
                        <a-button danger @click="doDelete(record.bookIsbn)">删除</a-button>
                    </template>

                    <template v-else-if="column.dataIndex === 'photoUrl'">
                        <img :src="'data:image/jpeg;base64,' + record.bookImageBase64" :alt="record.bookName" style="max-width: 50px; max-height: 50px;" />
                    </template>
                    <template v-else-if="column.dataIndex === 'publishDate'">
                        {{ record.publishDate ? record.publishDate.format("YYYY-MM-DD") : '' }}
                    </template>
                </template>
            </a-table>
        </div>

        <!-- 编辑模态框 -->
        <a-modal
            :visible="editModalVisible"
            title="编辑书籍"
            @cancel="closeEditModal"
            @ok="saveEditBook"
        >
            <a-form
                :model="editFormState"
                name="edit_book_form"
                :layout="formLayout"
                autocomplete="off"
            >
                <a-form-item
                    label="ISBN"
                    name="bookIsbn"
                    :rules="[{ required: true, message: '请输入ISBN!' }]"
                >
                    <a-input v-model:value="editFormState.bookIsbn" disabled/>
                </a-form-item>

                <a-form-item
                    label="书名"
                    name="bookName"
                    :rules="[{ required: true, message: '请输入书名!' }]"
                >
                    <a-input v-model:value="editFormState.bookName"/>
                </a-form-item>

                <a-form-item
                    label="价格"
                    name="bookPrice"
                    :rules="[{ required: true, message: '请输入价格!' }]"
                >
                    <a-input-number v-model:value="editFormState.bookPrice" :min="0"/>
                </a-form-item>
              <a-form-item
                  label="描述"
                  name="bookDescription"
                  :rules="[{ required: true, message: '请输入书籍描述!' }]"
              >
                <a-input v-model:value="editFormState.bookDescription"/>
              </a-form-item>

                <a-form-item
                    label="出版日期"
                    name="publishDate"
                    :rules="[{ required: true, message: '请选择出版日期!' }]"
                >
                    <a-date-picker v-model:value="editFormState.publishDate"/>
                </a-form-item>

                <a-form-item
                    label="图片"
                    name="bookImageBase64"
                >
                    <a-upload
                        name="avatar"
                        list-type="picture-card"
                        class="avatar-uploader"
                        :show-upload-list="false"
                        :before-upload="beforeUpload"
                        :customRequest="handleCustomUpload"
                    >
                        <img v-if="imageUrl" :src="imageUrl" alt="avatar" style="width: 100%" />
                        <div v-else>
                            <loading-outlined v-if="loading"></loading-outlined>
                            <plus-outlined v-else></plus-outlined>
                            <div class="ant-upload-text">上传</div>
                        </div>
                    </a-upload>
                </a-form-item>
            </a-form>
        </a-modal>
    </div>
</template>

<script lang="ts" setup>
import {SmileOutlined, DownOutlined, LoadingOutlined, PlusOutlined} from '@ant-design/icons-vue';
import { ref as vRef } from 'vue';
import {addBook, searchBook, updateBook, deleteBook} from "@/api/book";
import {reactive, ref, onMounted, computed} from "vue";
import {message} from "ant-design-vue";
import dayjs from "dayjs";
import {useLoginUserStore} from "@/store/useLoginUserStore";

interface FormState {
    bookIsbn: string;
    bookName: string;
    bookPrice: number;
    publishDate: dayjs.Dayjs | null;
    bookImageBase64: string;
    bookDescription:string;
}

// 添加书籍表单
const formState = reactive<FormState>({
    bookIsbn: '',
    bookName: '',
    bookPrice: 0,
    publishDate: null,
    bookImageBase64: '',
    bookDescription:''
});

// 编辑书籍表单
const editFormState = reactive<FormState>({
    bookIsbn: '',
    bookName: '',
    bookPrice: 0,
    publishDate: null,
    bookImageBase64: '',
    bookDescription:'',
});

// 图片上传相关
const imageUrl = vRef<string>('');
const loading = vRef<boolean>(false);

/**
 * 处理图片上传前的操作
 * @param file 待上传的文件
 * @returns 是否允许上传
 */
const beforeUpload = (file: File) => {
    const isJpgOrPng = file.type === 'image/jpeg' || file.type === 'image/png';
    if (!isJpgOrPng) {
        message.error('只能上传JPG/PNG格式的图片！');
    }
    const isLt2M = file.size / 1024 / 1024 < 2;
    if (!isLt2M) {
        message.error('图片大小不能超过2MB！');
    }
    return isJpgOrPng && isLt2M;
};

/**
 * 自定义上传处理函数，直接在本地处理文件转base64
 * @param options 上传选项
 */
const handleCustomUpload = (options: any) => {
    const { file, onSuccess, onError } = options;
    try {
        const reader = new FileReader();
        reader.readAsDataURL(file);
        reader.onload = (e) => {
            if (e.target) {
                const base64String = e.target.result as string;
                // 移除base64字符串中的前缀部分
                const base64Data = base64String.split(',')[1];
                // 更新表单数据
                if (editModalVisible.value) {
                    editFormState.bookImageBase64 = base64Data;
                } else {
                    formState.bookImageBase64 = base64Data;
                }
                imageUrl.value = base64String; // 保留完整base64用于显示
                loading.value = false;
                onSuccess();
            }
        };
        reader.onerror = (error) => {
            loading.value = false;
            message.error('图片读取失败');
            onError(error);
        };
    } catch (error) {
        loading.value = false;
        message.error('图片处理失败');
        onError(error);
    }
};

const editModalVisible = ref(false);

const formLayout = {
    labelCol: {span: 6},
    wrapperCol: {span: 14},
};

const onFinish = async (values: any) => {
    try {
        // 转换日期格式
        const formattedValues = {
            ...values,
            publishDate: values.publishDate ? values.publishDate.format('YYYY-MM-DD') : '',
        };

        const res = await addBook(formattedValues);
        if (res.data.code === 0) {
            message.success("添加成功");
            // 重置表单
            Object.keys(formState).forEach(key => {
                formState[key as keyof FormState] =
                    key === 'bookPrice' ? 0 :
                        key === 'publishDate' ? null : '';
            });
            // 刷新数据
            fetchData();
        } else {
            message.error("添加失败: " + res.data.message);
        }
    } catch (error) {
        console.error("添加书籍出错:", error);
        message.error("添加失败");
    }
};

const onFinishFailed = (errorInfo: any) => {
    console.log('表单验证失败:', errorInfo);
};

const disabled = computed(() => {
    return !(formState.bookIsbn && formState.bookName && formState.bookPrice && formState.publishDate);
});

const searchValue = ref("");

// 执行搜索获取数据
const onSearch = () => {
    fetchData(searchValue.value)
}

const doDelete = async (bookIsbn: string) => {
    if (!bookIsbn) {
        return;
    }
    const res = await deleteBook(bookIsbn);
    if (res.data.code === 0) {
        message.success("删除成功");
        fetchData(); // 刷新数据
    } else {
        message.error("删除失败");
    }
}

// 打开编辑模态框
const openEditModal = (record: any) => {
    // 复制数据到编辑表单
    editFormState.bookIsbn = record.bookIsbn;
    editFormState.bookName = record.bookName;
    editFormState.bookPrice = record.bookPrice;
    editFormState.publishDate = record.publishDate;
    editFormState.bookImageBase64 = record.bookImageBase64;
    // 编辑模式下不显示已有图片
    imageUrl.value = '';

    editModalVisible.value = true;
}

// 关闭编辑模态框
const closeEditModal = () => {
    editModalVisible.value = false;
}

// 保存编辑的书籍
const saveEditBook = async () => {
    try {
        // 转换日期格式
        const formattedValues = {
            ...editFormState,
            publishDate: editFormState.publishDate ? editFormState.publishDate.format('YYYY-MM-DD') : '',
        };

        const res = await updateBook(formattedValues);
        if (res.data.code === 0) {
            message.success("更新成功");
            closeEditModal();
            fetchData(); // 刷新数据
        } else {
            message.error("更新失败: " + res.data.message);
        }
    } catch (error) {
        console.error("更新书籍出错:", error);
        message.error("更新失败: 网络错误");
    }
}

// 获取用户信息
const loginUserStore = useLoginUserStore();
const isAdmin = computed(() => loginUserStore.loginUser.userRole === 1);

// 管理员表格列配置
const columns = [
    {
        title: "ISBN",
        dataIndex: "bookIsbn",
    },
    {
        title: '图片',
        dataIndex: 'photoUrl',
    },
    {
        title: '书名',
        dataIndex: 'bookName',
    },
    {
        title: '价格',
        dataIndex: 'bookPrice',
    },
  {
    title: '描述',
        dataIndex: 'bookDescription',
  },
    {
        title: '出版时间',
        dataIndex: 'publishDate',
    },
    {
        title: '操作',
        dataIndex: 'action',
    }
];


const data = ref([]);

const fetchData = async (bookName = "") => {
    try {
        const res = await searchBook(bookName);
        if (res.data.data) {
            data.value = res.data.data.map((item: any) => ({
                ...item,
                publishDate: item.publishDate ? dayjs(item.publishDate) : null,
                purchaseQuantity: 1, // 初始购买数量为1
                totalPrice: item.bookPrice // 初始总价等于单价
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



// 初始化加载数据
fetchData();
</script>

<style scoped>
/* 添加上传组件样式 */
.avatar-uploader > .ant-upload {
    width: 128px;
    height: 128px;
}
.ant-upload-select-picture-card i {
    font-size: 32px;
    color: #999;
}
.ant-upload-select-picture-card .ant-upload-text {
    margin-top: 8px;
    color: #666;
}

.purchase-controls {
    display: flex;
    flex-direction: row; /* 改为行布局实现横向排列 */
    align-items: center; /* 垂直方向居中对齐元素，可按需调整 */
    gap: 10px; /* 调整元素间的间距，可按需修改数值 */
}

.total-price {
    font-weight: bold;
    color: #ff4d4f;
    margin: 5px 0;
}

.cart-container {
    margin-top: 20px;
    padding: 16px;
    border: 1px solid #e8e8e8;
    border-radius: 4px;
    background-color: #fafafa;
}

.cart-items {
    margin-bottom: 10px;
}

.cart-item {
    display: flex;
    justify-content: space-between;
    margin-bottom: 8px;
}

.cart-item-price {
    font-weight: bold;
}

.cart-total {
    display: flex;
    justify-content: space-between;
    font-weight: bold;
    margin-bottom: 16px;
}

.cart-total-price {
    color: #ff4d4f;
}
</style>
