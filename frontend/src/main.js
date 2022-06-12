import {createApp} from 'vue'
import App from './App.vue'
import './registerServiceWorker'
import router from './router'
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome'
import {Chart, registerables} from 'chart.js'
import '@/assets/global.css'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'

Chart.register(...registerables)
Chart.defaults.font = {
    size: 12,
    family: 'Gilroy Medium',
    style: 'normal',
    weight: 'normal'
}

createApp(App).use(router).use(ElementPlus).component('font-awesome-icon', FontAwesomeIcon).mount('#app')
