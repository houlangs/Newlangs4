<script setup>
import axios from '@/axios/axios'
import { ref } from 'vue'
import { useToast } from 'primevue/usetoast'

const toast = useToast()
const pointCodeIn = ref('')

const exchangePoint = () => {
	if (pointCodeIn.value === '') {
		toast.add({ severity: 'error', summary: '输入错误', detail: '请输入有效的兑换码', life: 3000 })
		return
	}

	axios.get('/point_record/exchangePoint', {
		params: {
			pointCode: pointCodeIn.value
		}
	}).then(function (response) {
		if (response.data.code === 200) {
			toast.add({ severity: 'success', summary: '兑换成功', detail: response.data.msg, life: 3000 })
		} else {
			console.log(response.data)
			toast.add({ severity: 'error', summary: '兑换失败', detail: '您已经兑换过了，请等待下一次机会', life: 3000 })
		}
	}).catch(function (error) {
		console.log(error)
	})
}
</script>

<template>
	<Toast />
	<div class="card p-fluid">
		<div class="field grid">
			<label for="from" class="col-12 mb-2 md:col-2 md:mb-0">兑换码</label>
			<div class="col-12 md:col-10">
				<InputText id="from" type="text" v-model.trim="pointCodeIn" />
			</div>
			<Button label="立即兑换" class="mt-4" @click="exchangePoint" />
		</div>
	</div>
	<div>
		<div class="card">
			<div class="flex align-items-center justify-content-between mb-4">
				<h4>🤔 如何获取兑换码/解析额度？</h4>
			</div>
			<p>
				🌟 加入 Q 群<br><br>
				🥳 宣传本站：在视频平台发布宣传视频，在群内@管理员即可领取额度！<br><br>
				📢 注意：从雨云兑换的兑换码，有效期为 5 分钟，请及时使用哦！
			</p>
		</div>
	</div>
</template>