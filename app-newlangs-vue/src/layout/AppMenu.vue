<script setup>
import { ref } from 'vue';
import AppMenuItem from './AppMenuItem.vue'

const auth = localStorage.getItem('auth')

const userModel = ref([
    {
        label: '总览',
        items: [
            { label: '首页', icon: 'pi pi-fw pi-home', to: '/' },
        ]
    },
    {
        label: '域名服务',
        items: [
            { label: '解析域名', icon: 'pi pi-fw pi-at', to: '/domain_record' },
            { label: '积分记录', icon: 'ri-coins-line', to: '/point_record' },
            { label: '获取积分', icon: 'pi pi-fw pi-gift', to: '/exchange_point' }
        ]
    },
    {
        label: '云服务',
        items: [
            { label: '不限速云盘', icon: 'pi pi-fw pi-send', to: '/pan' },
            { label: '云服务器', icon: 'pi pi-fw pi-globe', to: '/service' },
        ]
    },
    {
        label: '其它',
        items: [
            //{ label: '违规扫描', icon: 'pi pi-fw pi-search', to: '/scan' },
            { label: '问题反馈', icon: 'pi pi-fw pi-question-circle', to: '/feedback' },
        ]
    }
])

const adminModel = ref([
    {
        label: '总览',
        items: [{ label: '首页', icon: 'pi pi-fw pi-home', to: '/' }]
    },
    {
        label: '用户管理',
        items: [
            { label: '用户组', icon: 'pi pi-fw pi-sitemap', to: '/user_role' },
            { label: '用户列表', icon: 'pi pi-fw pi-users', to: '/user_list' },
        ]
    },
    {
        label: '域名服务',
        items: [
            { label: '域名列表', icon: 'pi pi-fw pi-globe', to: '/domain_list' },
            { label: '解析域名', icon: 'pi pi-fw pi-at', to: '/domain_record' },
            { label: '积分记录', icon: 'ri-coins-line', to: '/point_record' },
            { label: '获取积分', icon: 'pi pi-fw pi-gift', to: '/exchange_point' }
        ]
    },
    {
        label: '管理员',
        items: [
            { label: '通用配置', icon: 'pi pi-fw pi-cog', to: '/common_config' },
            { label: 'DNS配置', icon: 'pi pi-fw pi-wrench', to: '/dns_config' },
            { label: '违规检测', icon: 'pi pi-fw pi-search', to: '/scan' }
        ]
    }
])
</script>

<template>
    <ul class="layout-menu" v-if="auth === '1'">
        <template v-for="(item, i) in adminModel" :key="item">
            <app-menu-item v-if="!item.separator" :item="item" :index="i"></app-menu-item>
            <li v-if="item.separator" class="menu-separator"></li>
        </template>
    </ul>
    <ul class="layout-menu" v-else>
        <template v-for="(item, i) in userModel" :key="item">
            <app-menu-item v-if="!item.separator" :item="item" :index="i"></app-menu-item>
            <li v-if="item.separator" class="menu-separator"></li>
        </template>
    </ul>
</template>

<style lang="scss" scoped></style>
