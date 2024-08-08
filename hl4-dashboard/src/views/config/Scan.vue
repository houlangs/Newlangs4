<script setup>
import { onMounted, ref } from 'vue'
import axios from '@/axios/axios'
import { useToast } from "primevue/usetoast"
import ProgressSpinner from 'primevue/progressspinner'

const toast = useToast()
const isScan = ref(false)

const startScan = () => {
	isScan.value = true
	axios.get('/config/scan', { timeout: 100 })
		.then(function (response) {
			if (response.data.code === 200)
				toast.add({ severity: 'success', summary: '操作成功', detail: response.data.msg, life: 3000 })
			else {
				console.log(response.data)
				toast.add({ severity: 'error', summary: '操作失败', detail: response.data.msg, life: 3000 })
			}
			isScan.value = false
		}).catch(function (error) {
			console.log(error)
		})
}
</script>

<template>
	<Toast />
	<div class="card p-fluid">
		<h3>🤔 什么是违规扫描？</h3>
		<p>为了保证域名长期可用，我们定期扫描所有域名，如果发现违规情况，将会删除域名。</p>
		<br>
		<h3>😤 为何我的域名被删除？</h3>
		<p>无法访问或违规的域名将被删除。如果您的域名用于Minecraft服务器，请在解析时添加备注。</p>
	</div>
	<div class="card p-fluid">
		<Button label="开始扫描" class="mr-2 mb-2 gradient" :disabled="isScan" @click="startScan"></Button>
	</div>
	<div class="card flex justify-content-center" v-if="isScan">
		<ProgressSpinner />
	</div>
</template>

<style scoped lang="scss"></style>
