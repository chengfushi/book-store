<template>
    <div id="userRegister">
        <h2 class="title">用户注册</h2>
        <a-form
            style="max-width: 480px;margin: 0 auto"
            :model="formState"
            name="basic"
            :label-align="left"
            :label-col="{ span: 4 }"
            :wrapper-col="{ span: 20 }"
            autocomplete="off"
            @finish="handleSubmit"
        >
            <a-form-item
                label="账号"
                name="userAccount"
                :rules="[{ required: true, message: '请输入账号' }]"
            >
                <a-input v-model:value="formState.userAccount" placeholder="请输入账号"/>
            </a-form-item>

            <a-form-item
                label="密码"
                name="userPassword"
                :rules="[{ required: true, message: '请输入密码' },{min:8,message:'密码长度至少为8位'}]"
            >
                <a-input-password v-model:value="formState.userPassword" placeholder="请输入密码"/>
            </a-form-item>
            <a-form-item
                label=" 确认密码"
                name="checkPassword"
                :rules="[{ required: true, message: '请输入确认密码' },{min:8,message:'密码长度至少为8位'}]"
            >
                <a-input-password v-model:value="formState.checkPassword" placeholder="请输入确认密码"/>
            </a-form-item>

            <a-form-item :wrapper-col="{ offset: 8, span: 16 }">
                <a-button type="primary" html-type="submit">注册</a-button>
            </a-form-item>
        </a-form>
    </div>
</template>
<script lang="ts" setup>
import {reactive} from 'vue';
import {userLogin, userRegister} from "@/api/user";
import {useLoginUserStore} from "@/store/useLoginUserStore";
import {message} from "ant-design-vue";
import router from "@/router";

interface FormState {
    userAccount: string;
    userPassword: string;
    checkPassword:string;
}

const formState = reactive<FormState>({
    userAccount:"",
    userPassword: "",
    checkPassword:"",
});
const onFinish = (values: any) => {
    console.log('Success:', values);
};

const onFinishFailed = (errorInfo: any) => {
    console.log('Failed:', errorInfo);
};

//获取全局登录状态
const loginUserStore = useLoginUserStore();
/**
 * @description: 提交表单
 * @author Chrng Fu
 */
const handleSubmit = async (formState:any)=>{
    //判断两次密码是否一致
    if (formState.userPassword !== formState.checkPassword){
        message.error("两次密码不一致");
        return;
    }
    const res = await userRegister(formState);

    if (res.data.code === 0 && res.data.data){
        message.success("注册成功");
        router.push({
            path:"/user/login",
            replace:true,
        })
    }else {
        message.error("注册失败" + res.data.description);
    }
}
</script>

<style scoped>
#userRegister .title{
    text-align: center;
    margin-bottom: 16px;
}
</style>