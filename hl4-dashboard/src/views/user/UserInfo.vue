<script setup>
import { onMounted, ref } from 'vue'
import axios from '@/axios/axios'
import { useToast } from 'primevue/usetoast'

const toast = useToast()
const userId = ref('')
const email = ref('')
const name = ref('')
const idNumber = ref('')
const phone = ref('')
const point = ref('')
const roleId = ref(0)
const createdTime = ref('')
const loginTime = ref('')
const status = ref(1)

const phoneIn = ref(null)
const codeIn = ref(null)
const nameIn = ref(null)
const idNumberIn = ref(null)

const disabled = ref(false)
const sendCodeDisabled = ref(false)
const certificationDialog = ref(false)
const phoneDialog = ref(false)

onMounted(() => {
	getUserInfo()
})
//获取用户信息
const getUserInfo = () => {
	axios.get('/user/get_user')
		.then(function (response) {
			if (response.data.code === 200) {
				userId.value = response.data.data.id
				email.value = response.data.data.email
				name.value = response.data.data.name
				idNumber.value = response.data.data.idNumber
				phone.value = response.data.data.phone
				point.value = response.data.data.point
				roleId.value = response.data.data.roleId
				createdTime.value = response.data.data.createdTime
				loginTime.value = response.data.data.loginTime
				status.value = response.data.data.status
			} else {
				toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
			}
		}).catch(function (error) {
			console.log(error)
		})
}

//发送手机验证码
const sendPhoneCode = (phone) => {
	sendCodeDisabled.value = true
	axios.get('/user/phone_code', {
		params: {
			phone: phone
		}
	}).then(function (response) {
		if (response.data.code === 200) {
			toast.add({ severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000 })
			setTimeout(() => {
				sendCodeDisabled.value = false
			}, 60000)
		} else {
			toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
			setTimeout(() => {
				sendCodeDisabled.value = false
			}, 60000)
		}
	}).catch(function (error) {
		console.log(error)
		setTimeout(() => {
			sendCodeDisabled.value = false
		}, 60000)
	})
}

//手机号校验
const checkPhone = (phone, code) => {
	axios.get('/user/check_phone', {
		params: {
			phone: phone,
			code: code
		}
	}).then(function (response) {
		if (response.data.code === 200) {
			toast.add({ severity: 'success', summary: 'ERROR', detail: response.data.msg, life: 3000 })
			phoneDialog.value = false
			getUserInfo()
		} else {
			toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
		}
	}).catch(function (error) {
		console.log(error)
	})
}

//实名认证
const certification = (name, idNumber) => {
	disabled.value = true
	axios.post('/user/certification', {
		name: name,
		idNumber: idNumber
	}).then(function (response) {
		if (response.data.code === 200) {
			toast.add({ severity: 'success', summary: 'ERROR', detail: response.data.msg, life: 3000 })
			certificationDialog.value = false
			disabled.value = false
			getUserInfo()
		} else {
			toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
			disabled.value = false
		}
	}).catch(function (error) {
		console.log(error)
		disabled.value = false
	})
}

const computedRoleTab = (id) => {
	switch (id) {
		case 1:
			return '管理员'
		case 2:
			return '普通用户'
		case 3:
			return '高级用户'
		case 4:
			return '认证用户'
	}
}
</script>

<template>
	<Toast />
	<div class="card p-fluid">
		<div class="surface-section">
			<div class="font-medium text-3xl text-900 mb-3">用户信息</div>
			<ul class="list-none p-0 m-0">
				<li class="flex align-items-center py-3 px-2 border-top-1 surface-border flex-wrap">
					<div class="text-500 w-6 md:w-2 font-medium">ID</div>
					<div class="text-900 w-full md:w-8 md:flex-order-0 flex-order-1">{{ userId }}</div>
				</li>
				<li class="flex align-items-center py-3 px-2 border-top-1 surface-border flex-wrap">
					<div class="text-500 w-6 md:w-2 font-medium">邮箱</div>
					<div class="text-900 w-full md:w-8 md:flex-order-0 flex-order-1">{{ email }}</div>
				</li>
				<li class="flex align-items-center py-3 px-2 border-top-1 surface-border flex-wrap">
					<div class="text-500 w-6 md:w-2 font-medium">手机号码</div>
					<div class="text-900 w-full md:w-8 md:flex-order-0 flex-order-1">
						<!-- 显示手机号码，如果没有则显示认证消息 -->
						<template v-if="phone">
							{{ phone }}
						</template>
						<template v-else>
							<p>请先完成手机认证，再进行实名认证</p>
						</template>
					</div>
					<div class="w-6 md:w-2 flex justify-content-end" v-if="!phone">
						<Button label="手机号验证" icon="pi pi-pencil" class="p-button-text"
							@click="phoneDialog = true"></Button>
					</div>
				</li>

				<li class="flex align-items-center py-3 px-2 border-top-1 surface-border flex-wrap">
					<div class="text-500 w-6 md:w-2 font-medium">实名信息</div>
					<div class="text-900 w-full md:w-8 md:flex-order-0 flex-order-1">{{ (name === null || name === undefined
						|| name === '') ? '实名完成后即可使用域名（只要这里显示实名信息解析即可）' : name }}</div>
					<div class="w-6 md:w-2 flex justify-content-end"
						v-if="name === null || name === undefined || name === ''">
						<Button label="实名认证" icon="pi pi-pencil" class="p-button-text"
							@click="certificationDialog = true"></Button>
					</div>
				</li>
				<li class="flex align-items-center py-3 px-2 border-top-1 surface-border flex-wrap">
					<div class="text-500 w-6 md:w-2 font-medium">积分</div>
					<div class="text-900 w-full md:w-8 md:flex-order-0 flex-order-1">{{ point }}</div>
				</li>
				<li class="flex align-items-center py-3 px-2 border-top-1 surface-border flex-wrap">
					<div class="text-500 w-6 md:w-2 font-medium">用户组</div>
					<div class="text-900 w-full md:w-8 md:flex-order-0 flex-order-1">{{ computedRoleTab(roleId) }}</div>
				</li>
				<li class="flex align-items-center py-3 px-2 border-top-1 surface-border flex-wrap">
					<div class="text-500 w-6 md:w-2 font-medium">注册时间</div>
					<div class="text-900 w-full md:w-8 md:flex-order-0 flex-order-1">{{ createdTime }}</div>
				</li>
				<li class="flex align-items-center py-3 px-2 border-top-1 surface-border flex-wrap">
					<div class="text-500 w-6 md:w-2 font-medium">最近登录时间</div>
					<div class="text-900 w-full md:w-8 md:flex-order-0 flex-order-1">{{ loginTime }}</div>
				</li>
			</ul>
		</div>

		<Dialog v-model:visible="certificationDialog" :style="{ width: '450px' }" header="实名认证" :modal="true"
			class="p-fluid">
			<div class="field">
				<label for="name"></label>
				<InputText id="name" v-model.trim="nameIn" placeholder="您的姓名" autofocus />
			</div>
			<div class="field">
				<InputText id="idNumber" v-model="idNumberIn" placeholder="您的身份证号码" />
			</div>
			<template #footer>
				<Button text label="取消" icon="pi pi-times" @click="certificationDialog = false" />
				<Button text label="验证" icon="pi pi-check" @click="certification(nameIn, idNumberIn)" :loading="disabled" />
			</template>
		</Dialog>

		<Dialog v-model:visible="phoneDialog" :style="{ width: '450px' }" header="手机验证" :modal="true" class="p-fluid">
			<div class="field">
				<InputText id="phone" v-model.trim="phoneIn" placeholder="您的大陆手机号码" autofocus />
			</div>
			<div class="field">
				<Button text label="发送短信验证码" icon="pi pi-send" @click="sendPhoneCode(phoneIn)"
					:loading="sendCodeDisabled" />
			</div>
			<div class="field">
				<InputText id="code" v-model="codeIn" placeholder="您收到的六位数字验证码" />
			</div>
			<template #footer>
				<Button text label="取消" icon="pi pi-times" @click="phoneDialog = false" />
				<Button text label="确认" icon="pi pi-check" @click="checkPhone(phoneIn, codeIn)" />
			</template>
		</Dialog>
	</div>
</template>