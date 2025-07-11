<template>
    <div id="userManage">
        <a-input-search
            style="max-width: 320px;margin-bottom: 20px"
            v-model:value="searchValue"
            placeholder="输入用户名搜索"
            enter-button="搜索"
            size="large"
            @search="onSearch"
        />
        <a-table :columns="columns" :data-source="data">

            <template #bodyCell="{ column, record }">
                <template v-if="column.dataIndex === 'avatarUrl'">
                    <a-image :src="record.avatarUrl" :width="120"/>
                </template>
                <template v-else-if="column.dataIndex === 'userRole'">
                    <a-tag color="blue">
                        {{ record.userRole === 0 ? '普通用户' : '管理员' }}
                    </a-tag>
                </template>
                <template v-else-if="column.dataIndex === 'gender'">
                    <a-tag color="blue">
                        {{ record.gender === 0 ? '女' : '男' }}
                    </a-tag>
                </template>
                <template v-else-if="column.dataIndex === 'createTime'">
                    {{ dayjs(record.createTime).format("YYYY-MM-DD HH:mm:ss") }}
                </template>

                <template v-else-if="column.dataIndex === 'action'">
                    <a-button danger @click="doDelete(record.id)">删除</a-button>
                </template>
            </template>
        </a-table>
    </div>
</template>

<script lang="ts" setup>
import {SmileOutlined, DownOutlined} from '@ant-design/icons-vue';
import {deleteUser, searchUsers} from "@/api/user";
import {reactive, ref} from "vue";
import {message} from "ant-design-vue";
import dayjs from "dayjs";

const searchValue = ref("");

//执行搜索获取数据
const onSearch = ()=>{
    fetchData(searchValue.value)
}


const doDelete = async (id:string) => {
    if (!id){
        return;
    }
    const res = await deleteUser(id);
    if (res.data.code === 0){
        message.success("删除成功");
    }
    else {
        message.error("删除失败");
    }
}

const columns = [
    {
        title: '用户名',
        dataIndex: 'userName',
    },
    {
        title: '账号',
        dataIndex: 'loginAccount',
    },
    {
        title: '性别',
        dataIndex: 'gender',
    },
    {
        title: '创建时间',
        dataIndex: 'createTime',
    },
    {
        title: '用户角色',
        dataIndex: 'userRole',
    },
    {
        title: '操作',
        dataIndex: 'action',
    }

];

const data = ref([]);

const fetchData = async (userName="") => {
    const res = await searchUsers(userName);
    if (res.data.data) {
        data.value = res.data.data;
    } else {
        message.error("获取用户列表失败");
    }
}
fetchData()
</script>

