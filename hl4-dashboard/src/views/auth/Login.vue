<script setup>
localStorage.clear()
import { ref } from 'vue'
import AppConfig from '@/layout/AppConfig.vue'
import axios from '@/axios/axios'
import { useToast } from 'primevue/usetoast'
import { useRouter } from 'vue-router'

const email = ref('')
const password = ref('')
const checked = ref(false)
const disabled = ref(false)
const toast = useToast()
const router = useRouter()

const login = () => {
    disabled.value = true
    axios.post('/user/login', {
        email: email.value,
        password: password.value,
        rememberMe: checked.value
    }).then(function (response) {
        if (response.data.code === 200) {
            localStorage.setItem('accessToken', response.data.data.accessToken)
            localStorage.setItem('auth', response.data.data.userData.roleId)
            toast.add({ severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000 })
            router.replace('/')
        } else {
            console.log(response.data)
            toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
        }
        disabled.value = false
    }).catch(function (error) {
        console.log(error)
        disabled.value = false
    })
}
</script>
<style>
.logo {
    width: 270px;
    height: auto;
}
</style>

<template>
    <Toast />

    <div class="surface-ground flex align-items-center justify-content-center min-h-screen min-w-screen overflow-hidden">
        <div class="flex flex-column align-items-center justify-content-center">
            <a href="/"><img src="@/assets/logo.svg" alt="Logo" class="logo" /></a><br>
            <div
                style="border-radius: 56px; padding: 0.3rem; background: linear-gradient(180deg, var(--primary-color) 10%, rgba(33, 150, 243, 0) 30%)">
                <div class="w-full surface-card py-8 px-5 sm:px-8" style="border-radius: 53px">
                    <div class="text-center mb-5">
                        <div class="text-900 text-3xl font-medium mb-3">欢迎使用二级域名</div>
                        <span class="text-600 font-medium">登录您的账号</span>
                    </div>

                    <div>
                        <label for="email1" class="block text-900 text-xl font-medium mb-2">邮箱</label>
                        <InputText id="email1" type="text" placeholder="您的注册邮箱" class="w-full md:w-30rem mb-5"
                            style="padding: 1rem" v-model="email" />

                        <label for="password" class="block text-900 font-medium text-xl mb-2">密码</label>
                        <Password id="password" v-model="password" placeholder="您的密码" :toggleMask="true" class="w-full mb-3"
                            inputClass="w-full" inputStyle="padding:1rem" :feedback="false"></Password>

                        <div class="flex align-items-center justify-content-between mb-5 gap-5">
                            <div class="flex align-items-center">
                                <Checkbox v-model="checked" id="rememberme" binary class="mr-2"></Checkbox>
                                <label for="rememberme">记住登录</label>
                            </div>
                            <router-link to="/auth/register" class="font-medium no-underline ml-2 text-right cursor-pointer"
                                style="color: var(--primary-color)">新用户？去注册</router-link>
                        </div>
                        <Button label="登录" class="w-full p-3 text-xl" @click="login" :disabled="disabled"></Button>
                    </div>
                </div>
            </div>
        </div>
    </div>
    <AppConfig simple />
</template>

<style scoped>
.pi-eye {
    transform: scale(1.6);
    margin-right: 1rem;
}

.pi-eye-slash {
    transform: scale(1.6);
    margin-right: 1rem;
}
</style>
