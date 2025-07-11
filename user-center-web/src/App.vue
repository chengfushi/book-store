<template>
    <div id="app">
        <BasicLayOut>
            <router-view></router-view>

        </BasicLayOut>

    </div>

</template>

<style>

</style>
<script setup lang="ts">
import BasicLayOut from './layouts/BasicLayOut.vue';
import {useLoginUserStore} from "@/store/useLoginUserStore";
import "./access"

const loginUserStore = useLoginUserStore();
loginUserStore.fetchLoginUser();

</script>
