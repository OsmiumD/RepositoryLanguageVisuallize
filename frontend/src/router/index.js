import {createRouter, createWebHashHistory} from 'vue-router'
import DashboardView from "@/views/DashboardView";

const routes = [
    {
        path: '/',
        name: 'dashboard',
        component: DashboardView
    },
    {
        path: '/language',
        name: 'language',
        component: ()=>import('../views/LanguageView')
    },
    {
        path: '/timeline',
        name: 'timeline',
        component: ()=>import('../views/TimelineView')
    },
    {
        path: '/list',
        name: 'list',
        component: ()=>import('../views/ListView')
    }
]

const router = createRouter({
    history: createWebHashHistory(),
    routes
})

export default router
