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
				toast.add({ severity: 'success', summary: 'Operation Successful', detail: response.data.msg, life: 3000 })
			else {
				console.log(response.data)
				toast.add({ severity: 'error', summary: 'Operation Failed', detail: response.data.msg, life: 3000 })
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
		<h3>🤔 What is Violation Scan?</h3>
		<p>To ensure the long-term availability of domains, we regularly scan all domains. If violations are detected, the
			domains will be deleted.</p>
		<br>
		<h3>😤 Why Was My Domain Deleted?</h3>
		<p>Domains that are inaccessible or in violation of our policies will be deleted. If your domain is used for a
			Minecraft server, please add a note during DNS configuration.</p>
	</div>
	<div class="card p-fluid">
		<Button label="Start Scan" class="mr-2 mb-2 gradient" :disabled="isScan" @click="startScan"></Button>
	</div>
	<div class="card flex justify-content-center" v-if="isScan">
		<ProgressSpinner />
	</div>
</template>

<style scoped lang="scss"></style>
