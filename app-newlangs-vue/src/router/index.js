import { createRouter, createWebHashHistory } from 'vue-router';
import AppLayout from '@/layout/AppLayout.vue';

const router = createRouter({
    history: createWebHashHistory(),
    routes: [
        {
            path: '/',
            component: AppLayout,
            children: [
                {
                    path: '/',
                    name: 'dashboard',
                    component: () => import('@/views/Dashboard.vue')
                },
                {
                    path: '/user_role',
                    name: 'userRole',
                    component: () => import('@/views/user/UserRole.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/user_list',
                    name: 'userList',
                    component: () => import('@/views/user/UserList.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/point_record',
                    name: 'pointRecord',
                    component: () => import('@/views/point/PointRecord.vue')
                },
                {
                    path: '/domain_list',
                    name: 'domainList',
                    component: () => import('@/views/domain/DomainList.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/domain_record',
                    name: 'domainRecord',
                    component: () => import('@/views/domain/DomainRecord.vue')
                },
                {
                    path: '/common_config',
                    name: 'commonConfig',
                    component: () => import('@/views/config/CommonConfig.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/dns_config',
                    name: 'DNSConfig',
                    component: () => import('@/views/config/DNSConfig.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/scan',
                    name: 'scan',
                    component: () => import('@/views/config/Scan.vue'),
                    meta: { requiresAuth: true }
                },
                {
                    path: '/user_info',
                    name: 'userInfo',
                    component: () => import('@/views/user/UserInfo.vue'),
                },
                {
                    path: '/cdn',
                    name: 'CDN',
                    component: () => import('@/views/domain/CDN.vue'),
                },
                {
                    path: '/service',
                    name: 'service',
                    component: () => import('@/views/domain/service.vue'),
                },
                {
                    path: '/rvh',
                    name: 'rvh',
                    component: () => import('@/views/domain/rvh.vue'),
                },
                {
                    path: '/donate',
                    name: 'donate',
                    component: () => import('@/views/config/donate.vue'),
                },
                // {
                //     path: '/buy_point',
                //     name: 'BuyPoint',
                //     component: () => import('@/views/point/BuyPoint.vue'),
                // },
                {
                    path: '/exchange_point',
                    name: 'ExchangePoint',
                    component: () => import('@/views/point/ExchangePoint.vue'),
                },
                {
                    path: '/feedback',
                    name: 'feedback',
                    component: () => import('@/views/config/feedback.vue'),
                },
                {
                    path: '/pan',
                    name: 'pan',
                    component: () => import('@/views/domain/pan.vue'),
                },
            ]
        },
        {
            path: '/auth/login',
            name: 'login',
            component: () => import('@/views/auth/Login.vue')
        },
        {
            path: '/auth/register',
            name: 'register',
            component: () => import('@/views/auth/Register.vue')
        }
    ]
});

export default router;
