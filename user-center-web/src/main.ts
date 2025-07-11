import { createApp } from 'vue'
import { createPinia } from 'pinia'
import App from './App.vue'
import router from './router'
import Antd from 'ant-design-vue';
import 'ant-design-vue/dist/reset.css';

const pinia = createPinia()

const app = createApp(App).use(pinia).use(Antd).use(router);


app.mount('#app');