<script setup>
import RadioButton from 'primevue/radiobutton';
import Button from 'primevue/button';
import InputSwitch from 'primevue/inputswitch';
import Sidebar from 'primevue/sidebar';

import { ref } from 'vue';
import { useLayout } from '@/layout/composables/layout';

defineProps({
    simple: {
        type: Boolean,
        default: false
    }
});
const scales = ref([12, 13, 14, 15, 16]);
const visible = ref(false);

const { changeThemeSettings, setScale, layoutConfig } = useLayout();

const onConfigButtonClick = () => {
    visible.value = !visible.value;
};
const onChangeTheme = (theme, mode) => {
    const elementId = 'theme-css';
    const linkElement = document.getElementById(elementId);
    const cloneLinkElement = linkElement.cloneNode(true);
    const newThemeUrl = linkElement.getAttribute('href').replace(layoutConfig.theme.value, theme);
    cloneLinkElement.setAttribute('id', elementId + '-deepClone');
    cloneLinkElement.setAttribute('href', newThemeUrl);
    cloneLinkElement.addEventListener('load', () => {
        linkElement.remove();
        cloneLinkElement.setAttribute('id', elementId);
        changeThemeSettings(theme, mode === 'dark');
    });
    linkElement.parentNode.insertBefore(cloneLinkElement, linkElement.nextSibling);
};
const decrementScale = () => {
    setScale(layoutConfig.scale.value - 1);
    applyScale();
};
const incrementScale = () => {
    setScale(layoutConfig.scale.value + 1);
    applyScale();
};
const applyScale = () => {
    document.documentElement.style.fontSize = layoutConfig.scale.value + 'px';
};
</script>

<template>
    <button class="layout-config-button p-link" type="button" @click="onConfigButtonClick()">
        <i class="pi pi-cog"></i>
    </button>

    <Sidebar v-model:visible="visible" position="right" :transitionOptions="'.3s cubic-bezier(0, 0, 0.2, 1)'"
        class="layout-config-sidebar w-20rem">
        <h5>字体大小</h5>
        <div class="flex align-items-center">
            <Button icon="pi pi-minus" type="button" @click="decrementScale()"
                class="p-button-text p-button-rounded w-2rem h-2rem mr-2"
                :disabled="layoutConfig.scale.value === scales[0]"></Button>
            <div class="flex gap-2 align-items-center">
                <i class="pi pi-circle-fill text-300" v-for="s in scales" :key="s"
                    :class="{ 'text-primary-500': s === layoutConfig.scale.value }"></i>
            </div>
            <Button icon="pi pi-plus" type="button" pButton @click="incrementScale()"
                class="p-button-text p-button-rounded w-2rem h-2rem ml-2"
                :disabled="layoutConfig.scale.value === scales[scales.length - 1]"></Button>
        </div>

        <template v-if="!simple">
            <h5>菜单样式</h5>
            <div class="flex">
                <div class="field-radiobutton flex-1">
                    <RadioButton name="menuMode" value="static" v-model="layoutConfig.menuMode.value" inputId="mode1">
                    </RadioButton>
                    <label for="mode1">显示</label>
                </div>

                <div class="field-radiobutton flex-1">
                    <RadioButton name="menuMode" value="overlay" v-model="layoutConfig.menuMode.value" inputId="mode2">
                    </RadioButton>
                    <label for="mode2">隐藏</label>
                </div>
            </div>
        </template>

        <h5>模式选择</h5>
        <div class="grid">
            <div class="col-3">
                <button class="p-link w-2rem h-2rem" @click="onChangeTheme('lara-light-indigo', 'light')">
                    <i class="pi pi-sun" style="font-size: 30px"></i><p>浅色</p>
                </button>
            </div>
            <div class="col-3">
                <button class="p-link w-2rem h-2rem" @click="onChangeTheme('lara-dark-indigo', 'dark')">
                <i class="pi pi-moon" style="font-size: 30px"></i><p>深色</p>
            </button>
        </div>
    </div>
</Sidebar></template>
