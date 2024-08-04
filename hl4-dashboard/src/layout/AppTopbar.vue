<script setup>
import { ref, computed, onMounted, onBeforeUnmount } from 'vue'
import { useLayout } from '@/layout/composables/layout'
import { useRouter } from 'vue-router'

const { layoutConfig, onMenuToggle } = useLayout()

const outsideClickListener = ref(null)
const topbarMenuActive = ref(false)
const router = useRouter()

const menu = ref()
const items = ref([])
const judgeLogin = () => {
    if (localStorage.getItem('accessToken') === undefined || localStorage.getItem('accessToken') === null)
        items.value = noLogin.value
    else
        items.value = login.value
}
const noLogin = ref([
    {
        label: '登录',
        icon: 'pi pi-sign-in',
        to: '/auth/login'
    },
    {
        label: '注册',
        icon: 'pi pi-user-plus',
        to: '/auth/register'
    },
])

const login = ref([
    {
        label: '用户信息',
        icon: 'pi pi-info-circle',
        to: '/user_info'
    },
    {
        label: '退出登录',
        icon: 'pi pi-sign-out',
        command: () => {
            localStorage.clear()
            router.push('/auth/login')
        }
    }
])



onMounted(() => {
    bindOutsideClickListener()
})

onBeforeUnmount(() => {
    unbindOutsideClickListener()
})

const logoUrl = computed(() => {
    return `layout/images/${layoutConfig.darkTheme.value ? 'logo-white' : 'logo-dark'}.svg`
})

const onTopBarMenuButton = () => {
    judgeLogin()
    menu.value.toggle(event)
    topbarMenuActive.value = !topbarMenuActive.value
}
const topbarMenuClasses = computed(() => {
    return {
        'layout-topbar-menu-mobile-active': topbarMenuActive.value
    }
})

const bindOutsideClickListener = () => {
    if (!outsideClickListener.value) {
        outsideClickListener.value = (event) => {
            if (isOutsideClicked(event)) {
                topbarMenuActive.value = false
            }
        }
        document.addEventListener('click', outsideClickListener.value)
    }
}
const unbindOutsideClickListener = () => {
    if (outsideClickListener.value) {
        document.removeEventListener('click', outsideClickListener)
        outsideClickListener.value = null;
    }
}
const isOutsideClicked = (event) => {
    if (!topbarMenuActive.value) return

    const sidebarEl = document.querySelector('.layout-topbar-menu')
    const topbarEl = document.querySelector('.layout-topbar-menu-button')

    return !(sidebarEl.isSameNode(event.target) || sidebarEl.contains(event.target) || topbarEl.isSameNode(event.target) || topbarEl.contains(event.target))
}
const toggle = (event) => {
    menu.value.toggle(event);
}
</script>

<template>
    <div class="layout-topbar">
        <router-link to="/" class="layout-topbar-logo">
            <img src="@/assets/logo.svg" alt="Logo" class="logo" />
        </router-link>

        <button class="p-link layout-menu-button layout-topbar-button" @click="onMenuToggle()">
            <i class="pi pi-bars"></i>
        </button>

        <button class="p-link layout-topbar-menu-button layout-topbar-button" @click="onTopBarMenuButton()">
            <i class="pi pi-ellipsis-v"></i>
        </button>

        <div class="layout-topbar-menu" :class="topbarMenuClasses">
            <button @click="onTopBarMenuButton()" class="p-link layout-topbar-button" aria-haspopup="true"
                aria-controls="overlay_menu">
                <i class="pi pi-user"></i>
                <span>Profile</span>
            </button>
            <Menu ref="menu" id="overlay_menu" :model="items" :popup="true" />
        </div>
    </div>
</template>

<style lang="scss" scoped></style>