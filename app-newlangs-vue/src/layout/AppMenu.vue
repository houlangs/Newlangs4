<script setup>
import { ref } from 'vue';
import AppMenuItem from './AppMenuItem.vue'

const auth = localStorage.getItem('auth')

const userModel = ref([
    {
        label: 'Overview',
        items: [
            { label: 'Home', icon: 'pi pi-fw pi-home', to: '/' },
        ]
    },
    {
        label: 'Domain Services',
        items: [
            { label: 'Domain Resolution', icon: 'pi pi-fw pi-at', to: '/domain_record' },
            { label: 'Expense Records', icon: 'ri-coins-line', to: '/point_record' },
            { label: 'Get Resolution Quota', icon: 'pi pi-fw pi-gift', to: '/exchange_point' }
        ]
    },
    {
        label: 'Cloud Services',
        items: [
            { label: 'CDN', icon: 'pi pi-fw pi-cloud-upload', to: '/cdn' },
            { label: 'Cloud Servers', icon: 'pi pi-fw pi-globe', to: '/service' },
            { label: 'Virtual Hosting', icon: 'pi pi-fw pi-database', to: '/rvh' }
        ]
    },
    {
        label: 'Other',
        items: [
            { label: 'Violation Scan', icon: 'pi pi-fw pi-search', to: '/scan' },
            { label: 'Feedback', icon: 'pi pi-fw pi-question-circle', to: '/feedback' },
            { label: 'Blue Charity', icon: 'pi pi-fw pi-thumbs-up', to: '/philanthropy' }
        ]
    }
])

const adminModel = ref([
    {
        label: 'Overview',
        items: [{ label: 'Home', icon: 'pi pi-fw pi-home', to: '/' }]
    },
    {
        label: 'User Management',
        items: [
            { label: 'User Groups', icon: 'pi pi-fw pi-sitemap', to: '/user_role' },
            { label: 'User List', icon: 'pi pi-fw pi-users', to: '/user_list' },
        ]
    },
    {
        label: 'Domain Services',
        items: [
            { label: 'Domain List', icon: 'pi pi-fw pi-globe', to: '/domain_list' },
            { label: 'Domain Resolution', icon: 'pi pi-fw pi-at', to: '/domain_record' },
            { label: 'Expense Records', icon: 'ri-coins-line', to: '/point_record' },
            { label: 'Get Resolution Quota', icon: 'pi pi-fw pi-gift', to: '/exchange_point' }
        ]
    },
    {
        label: 'Cloud Services',
        items: [
            { label: 'CDN', icon: 'pi pi-fw pi-cloud-upload', to: '/cdn' },
            { label: 'Cloud Servers', icon: 'pi pi-fw pi-globe', to: '/service' },
            { label: 'Virtual Hosting', icon: 'pi pi-fw pi-database', to: '/rvh' }
        ]
    },
    {
        label: 'Settings',
        items: [
            { label: 'General Configuration', icon: 'pi pi-fw pi-cog', to: '/common_config' },
            { label: 'DNS Configuration', icon: 'pi pi-fw pi-wrench', to: '/dns_config' },
            { label: 'Violation Detection', icon: 'pi pi-fw pi-search', to: '/scan' }
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
