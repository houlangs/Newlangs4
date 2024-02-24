<script setup>
import axios from '@/axios/axios'
import { onMounted, ref } from 'vue'
import { useToast } from 'primevue/usetoast'

const typeIn = ref('')
const IP = ref('')
const device = ref('')
const toast = useToast()

onMounted(() => {
	checkType()
	getIP()
})

const checkType = () => {
	if (navigator.userAgent.toLowerCase().match(/MicroMessenger/i) == 'micromessenger') {
		device.value = 'wechat'
	} else if (navigator.userAgent.toLowerCase().match(/QQ/i) == 'qq') {
		device.value = 'qq'
	} else if (/Android|iPhone|iPad|iPod|webOS|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
		device.value = 'mobile'
	} else {
		device.value = 'pc'
	}
}

const getIP = () => {
	axios.get('http://ip-api.com/json', {
		params: {
			lang: 'zh-CN'
		}
	}).then(function(response) {
		IP.value = response.data.query
	}).catch(function(error) {
		console.log(error)
	})
}

const buy = (type, name, money, clientip, device) => {
	axios.post('/point_record/buy', {
		type: type,
		name: name,
		money: money,
		clientip: clientip,
		device: device
	}).then(function(response) {
		console.log(typeIn.value)
		if (response.data.code === 1) {
			if (response.data.payurl !== null || response.data.payurl !== '') {
				window.open(response.data.payurl, '_blank')
			} else if (response.data.qrcode !== null || response.data.qrcode !== '') {
				window.open(response.data.qrcode, '_blank')
			} else if (response.data.urlscheme !== null || response.data.urlscheme !== '') {
				window.open(response.data.urlscheme, '_blank')
			}
		}
	}).catch(function(error) {
		console.log(error)
	})
}
</script>

<template>
	<Toast />
	<div class="surface-ground px-4 py-8 md:px-6 lg:px-8">
		<div class="text-900 font-bold text-6xl mb-4 text-center">🛒购买解析</div>
	    <div class="grid">
	        <div class="col-12 lg:col-4">
	            <div class="p-3 h-full">
	                <div class="shadow-2 p-3 h-full flex flex-column surface-card" style="border-radius: 6px">
	                    <div class="text-900 font-medium text-xl mb-2">基础版</div>
	                    <div class="text-600">1积分</div>
	                    <hr class="my-3 mx-0 border-top-1 border-none surface-border" />
	                    <div class="flex align-items-center">
	                        <span class="font-bold text-2xl text-900">￥1</span>
	                        <span class="ml-2 font-medium text-600">永久</span>
	                    </div>
	                    <hr class="my-3 mx-0 border-top-1 border-none surface-border" />
	                    <ul class="list-none p-0 m-0 flex-grow-1">
	                        <li class="flex align-items-center mb-3">
	                            <i class="pi pi-check-circle text-green-500 mr-2"></i>
	                            <span>账户充值1积分</span>
	                        </li>
	                    </ul>
	                    <hr class="mb-3 mx-0 border-top-1 border-none surface-border mt-auto" />
		                <div class="flex justify-content-around my-4">
			                <div class="flex align-items-center">
						        <RadioButton v-model="typeIn" inputId="alipay" name="支付宝" value="alipay" />
						        <label for="alipay" class="ml-2">支付宝</label>
						    </div>
						    <div class="flex align-items-center">
						        <RadioButton v-model="typeIn" inputId="wxpay" name="微信" value="wxpay" />
						        <label for="wxpay" class="ml-2">微信</label>
						    </div>
			                <div class="flex align-items-center">
						        <RadioButton v-model="typeIn" inputId="qqpay" name="QQ" value="qqpay" />
						        <label for="qqpay" class="ml-2">QQ</label>
						    </div>
		                </div>
	                    <Button label="购买" class="p-3 w-full mt-auto p-button-outlined" @click="buy(typeIn, '基础版', '1.00', IP, device)"></Button>
	                </div>
	            </div>
	        </div>

	        <div class="col-12 lg:col-4">
	            <div class="p-3 h-full">
	                <div class="shadow-2 p-3 h-full flex flex-column surface-card" style="border-radius: 6px">
	                    <div class="text-900 font-medium text-xl mb-2">高级版</div>
	                    <div class="text-600">10积分</div>
	                    <hr class="my-3 mx-0 border-top-1 border-none surface-border" />
	                    <div class="flex align-items-center">
	                        <span class="font-bold text-2xl text-900">￥9</span>
	                        <span class="ml-2 font-medium text-600">永久</span>
	                    </div>
	                    <hr class="my-3 mx-0 border-top-1 border-none surface-border" />
	                    <ul class="list-none p-0 m-0 flex-grow-1">
	                        <li class="flex align-items-center mb-3">
	                            <i class="pi pi-check-circle text-green-500 mr-2"></i>
	                            <span>账户充值10积分</span>
	                        </li>
	                    </ul>
	                    <hr class="mb-3 mx-0 border-top-1 border-none surface-border" />
		                <div class="flex justify-content-around my-4">
			                <div class="flex align-items-center">
						        <RadioButton v-model="typeIn" inputId="alipay" name="支付宝" value="alipay" />
						        <label for="alipay" class="ml-2">支付宝</label>
						    </div>
						    <div class="flex align-items-center">
						        <RadioButton v-model="typeIn" inputId="wxpay" name="微信" value="wxpay" />
						        <label for="wxpay" class="ml-2">微信</label>
						    </div>
			                <div class="flex align-items-center">
						        <RadioButton v-model="typeIn" inputId="qqpay" name="QQ" value="qqpay" />
						        <label for="qqpay" class="ml-2">QQ</label>
						    </div>
		                </div>
	                    <Button label="购买" class="p-3 w-full mt-auto p-button-outlined" @click="buy(typeIn, '高级版', '9.00', IP, device)"></Button>
	                </div>
	            </div>
	        </div>

	        <div class="col-12 lg:col-4">
	            <div class="p-3 h-full">
	                <div class="shadow-2 p-3 flex flex-column surface-card" style="border-radius: 6px">
	                    <div class="text-900 font-medium text-xl mb-2">企业版</div>
	                    <div class="text-600">20积分</div>
	                    <hr class="my-3 mx-0 border-top-1 border-none surface-border" />
	                    <div class="flex align-items-center">
	                        <span class="font-bold text-2xl text-900">￥15</span>
	                        <span class="ml-2 font-medium text-600">永久</span>
	                    </div>
	                    <hr class="my-3 mx-0 border-top-1 border-none surface-border" />
	                    <ul class="list-none p-0 m-0 flex-grow-1">
	                        <li class="flex align-items-center mb-3">
	                            <i class="pi pi-check-circle text-green-500 mr-2"></i>
	                            <span>账户充值20积分</span>
	                        </li>
	                    </ul>
	                    <hr class="mb-3 mx-0 border-top-1 border-none surface-border" />
		                <div class="flex justify-content-around my-4">
			                <div class="flex align-items-center">
						        <RadioButton v-model="typeIn" inputId="alipay" name="支付宝" value="alipay" />
						        <label for="alipay" class="ml-2">支付宝</label>
						    </div>
						    <div class="flex align-items-center">
						        <RadioButton v-model="typeIn" inputId="wxpay" name="微信" value="wxpay" />
						        <label for="wxpay" class="ml-2">微信</label>
						    </div>
			                <div class="flex align-items-center">
						        <RadioButton v-model="typeIn" inputId="qqpay" name="QQ" value="qqpay" />
						        <label for="qqpay" class="ml-2">QQ</label>
						    </div>
		                </div>
	                    <Button label="购买" class="p-3 w-full mt-auto" @click="buy(typeIn, '企业版', '15.00', IP, device)"></Button>
	                </div>
	            </div>
	        </div>
	    </div>
	</div>
</template>