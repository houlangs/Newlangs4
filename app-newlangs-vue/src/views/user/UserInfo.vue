<script setup>
import { onMounted, ref } from 'vue'
import axios from '@/axios/axios'
import { useToast } from 'primevue/usetoast'

const toast = useToast()
const userId = ref('')
const email = ref('')
const idNumber = ref('')
const point = ref('')
const roleId = ref(0)
const createdTime = ref('')
const loginTime = ref('')
const status = ref(1)

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
				<li class="flex align-items-center py-3 px-2 border-top-1 surface-border flex-wrap">
					<div class="text-500 w-6 md:w-2 font-medium">账号状态</div>
					<div class="text-900 w-full md:w-8 md:flex-order-0 flex-order-1">{{ status === 1 ? '正常' : '已封禁' }}</div>
				</li>
			</ul>
		</div>
	</div>
</template>