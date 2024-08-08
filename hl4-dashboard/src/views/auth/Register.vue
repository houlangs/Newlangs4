<script setup>
import { ref } from 'vue'
import AppConfig from '@/layout/AppConfig.vue'
import axios from '@/axios/axios'
import { useRouter } from 'vue-router'
import { useToast } from 'primevue/usetoast'

const email = ref('')
const password = ref('')
const rePassword = ref('')
const code = ref('')
const disabled = ref(false)
const regDisabled = ref(false)
const router = useRouter()
const toast = useToast()

const agree = ref(false)

const register = () => {
	if (password.value !== rePassword.value) {
		return toast.add({ severity: 'error', summary: 'ERROR', detail: '密码不一致！', life: 3000 })
	}
	regDisabled.value = true
	if (agree.value === false) {
		return toast.add({ severity: 'error', summary: 'ERROR', detail: '请勾选表示同意使用政策', life: 3000 })
	}

	axios.post('/user/register', {
		email: email.value,
		password: password.value,
		code: code.value
	}).then(function (response) {
		if (response.data.code === 200) {
			toast.add({ severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000 })
			router.replace('/auth/login')
		} else {
			console.log(response.data)
			toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
		}
		regDisabled.value = false
	}).catch(function (error) {
		regDisabled.value = false
		console.log(error)
	})
}

const sendCode = () => {
	if (email.value === '')
		return toast.add({ severity: 'error', summary: 'ERROR', detail: '请填写邮箱', life: 3000 })
	disabled.value = true
	axios.get('/user/email_code', {
		params: {
			email: email.value
		}
	})
		.then(function (response) {
			if (response.data.code === 200) {
				toast.add({ severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000 })
			} else {
				console.log(response.data)
				toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
			}
			disabled.value = false
		}).catch(function (error) {
			disabled.value = false
			console.log(error)
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
						<span class="text-600 font-medium">注册您的账号</span>
					</div>
					<div> 
						<label for="email1" class="block text-900 text-xl font-medium mb-2">邮箱</label>
						<InputText id="email1" type="text" placeholder="您的邮件地址" class="w-full md:w-30rem mb-5"
							style="padding: 1rem" v-model="email" />

						<label for="password1" class="block text-900 font-medium text-xl mb-2">密码</label>
						<Password id="password1" v-model="password" placeholder="您的密码" :toggleMask="true"
							class="w-full mb-3" inputClass="w-full" inputStyle="padding:1rem"></Password>

						<label for="rePassword" class="block text-900 font-medium text-xl mb-2">重复密码</label>
						<Password id="rePassword" v-model="rePassword" placeholder="重复密码" :toggleMask="true"
							class="w-full mb-3" inputClass="w-full" inputStyle="padding:1rem"></Password>

						<label for="email1" class="block text-900 text-xl font-medium mb-2">验证码</label>
						<InputText id="email1" type="text" placeholder="邮件验证码" class="w-full md:w-30rem mb-5"
							style="padding: 1rem" v-model="code" />
						<div class="flex align-items-center justify-content-between mb-5 gap-5">
							<router-link to="/auth/login" class="font-medium no-underline ml-2 text-right cursor-pointer"
								style="color: var(--primary-color)">已有账号？去登录</router-link>
							<div>
								<Checkbox v-model="agree" :binary="true" />
								<label class="ml-2">
									<a href="使用政策链接" target="_blank"
										class="font-medium no-underline ml-2 text-right cursor-pointer">同意全部使用政策</a>
								</label>
							</div>
						</div>
						<Button text label="获取验证码" class="w-full p-3 text-xl mb-2" @click="sendCode"
							:disabled="disabled"></Button>
						<Button label="注册" class="w-full p-3 text-xl" @click="register"></Button>
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
