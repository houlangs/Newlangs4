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
				<h4>🤔 如何获取兑换码/积分？</h4>
			</div>
			<p>
				🌦️ 前往雨云兑换：进入 <a href="https://www.rainyun.com/ins_?s=app-langs-score" target="_blank">雨云</a> 注册账号 -> 积分中心 -> 积分商城 免费兑换！<br><br>
				📢 注意：从雨云兑换的兑换码，有效期为 5 分钟，请及时使用哦！<br><br>
				🌟 加入Q群：<a href="http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=2vThTeSkd03bFCVs9VzM5kfVKedfxTzv&authKey=oPyHIZDxhtYKegn317FSdKLWIBLVdrMkMkO7U3oJJY3fE%2BxcW9RBiIcjfEj%2BTaUn&noverify=0&group_code=605943548" target="_blank">605943548</a>，Q群管家会告诉你兑换码！
			</p>
		</div>
	</div>
</template>