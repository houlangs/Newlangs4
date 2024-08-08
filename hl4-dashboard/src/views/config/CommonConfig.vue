<script setup>
import { onMounted, ref } from 'vue'
import axios from '@/axios/axios'
import { useToast } from "primevue/usetoast"
import { deepClone } from '@/utils/deepClone'

const sslOptions = ref(['false', 'true'])
const starttlsOptions = ref(['false', 'true'])

const config = ref([])
const hostIn = ref('')
const portIn = ref('')
const fromIn = ref('')
const userIn = ref('')
const passIn = ref('')
const sslIn = ref(true)
const starttlsIn = ref(true)
const timeoutIn = ref('')
const connectionTimeoutIn = ref('')
const keyWordsIn = ref('')
const defaultPointIn = ref('')
const noPrefixIn = ref('')
const signPointIn = ref('')
const commonCodeIn = ref('')
const commonPointIn = ref('')

const toast = useToast()

onMounted(() => {
	list()
})

//获取配置列表
const list = () => {
	axios.get('/config/list')
		.then(function(response) {
			config.value = response.data
			for (let i = 0; i < config.value.length; i++) {
				switch (config.value[i].k){
					case 'mail_host':
						hostIn.value = config.value[i].v
						break
					case 'mail_port':
						portIn.value = config.value[i].v
						break
					case 'mail_from':
						fromIn.value = config.value[i].v
						break
					case 'mail_user':
						userIn.value = config.value[i].v
						break
					case 'mail_pass':
						passIn.value = config.value[i].v
						break
					case 'mail_sslEnable':
						sslIn.value = config.value[i].v
						break
					case 'mail_starttlsEnable':
						starttlsIn.value = config.value[i].v
						break
					case 'mail_timeout':
						timeoutIn.value = config.value[i].v
						break
					case 'mail_connectionTimeout':
						connectionTimeoutIn.value = config.value[i].v
						break
					case 'scan_keyWords':
						keyWordsIn.value = config.value[i].v
						break
					case 'default_point':
						defaultPointIn.value = config.value[i].v
						break
					case 'no_prefix':
						noPrefixIn.value = config.value[i].v
						break
					case 'sign_point':
						signPointIn.value = config.value[i].v
						break
					case 'common_point':
						commonPointIn.value = config.value[i].v
						break
					case 'common_code':
						commonCodeIn.value = config.value[i].v
						break
				}
			}
		}).catch(function(error) {
			console.log(error)
		})
}

//更新配置
const update = (entityList) => {
	axios.put('/config/update', entityList)
		.then(function(response) {
			if (response.data.code === 200) {
				toast.add({severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000})
			} else {
				console.log(response.data)
				toast.add({severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000})
			}
			list()
		}).catch(function(error) {
			console.log(error)
			list()
		})
}

//确认发起更新请求
const confirmUpdate = () => {
	let entityList = deepClone(config.value)
	for (let i = 0; i < entityList.length; i++) {
		switch (config.value[i].k){
			case 'mail_host':
				entityList[i].v = hostIn.value
				break
			case 'mail_port':
				entityList[i].v = portIn.value
				break
			case 'mail_from':
				entityList[i].v = fromIn.value
				break
			case 'mail_user':
				entityList[i].v = userIn.value
				break
			case 'mail_pass':
				entityList[i].v = passIn.value
				break
			case 'mail_sslEnable':
				entityList[i].v = sslIn.value
				break
			case 'mail_starttlsEnable':
				entityList[i].v = starttlsIn.value
				break
			case 'mail_timeout':
				entityList[i].v = timeoutIn.value
				break
			case 'mail_connectionTimeout':
				entityList[i].v = connectionTimeoutIn.value
				break
			case 'scan_keyWords':
				entityList[i].v = keyWordsIn.value
				break
			case 'default_point':
				entityList[i].v = defaultPointIn.value
				break
			case 'no_prefix':
				entityList[i].v = noPrefixIn.value
				break
			case 'sign_point':
				entityList[i].v = signPointIn.value
				break
			case 'common_point':
				entityList[i].v = commonPointIn.value
				break
			case 'common_code':
				entityList[i].v = commonCodeIn.value
				break
		}
	}
	update(entityList)
}
</script>

<template>
	<Toast />
	<div class="card p-fluid">
        <div class="field grid">
            <label for="host" class="col-12 mb-2 md:col-2 md:mb-0">SMTP地址</label>
            <div class="col-12 md:col-10">
                <InputText id="host" type="text" v-model.trim="hostIn" />
	            <small>邮件服务器的SMTP地址</small>
            </div>
        </div>
        <div class="field grid">
            <label for="port" class="col-12 mb-2 md:col-2 md:mb-0">SMTP端口</label>
            <div class="col-12 md:col-10">
                <InputNumber id="port" v-model.trim="portIn" />
	            <small>邮件服务器的SMTP端口，可选，默认25</small>
            </div>
        </div>
		<div class="field grid">
            <label for="from" class="col-12 mb-2 md:col-2 md:mb-0">发件地址</label>
            <div class="col-12 md:col-10">
                <InputText id="from" type="text"  v-model.trim="fromIn" />
	            <small>必须正确，否则发送失败</small>
            </div>
        </div>
		<div class="field grid">
            <label for="user" class="col-12 mb-2 md:col-2 md:mb-0">用户名</label>
            <div class="col-12 md:col-10">
                <InputText id="user" type="text" v-model.trim="userIn" />
	            <small>SMTP服务商的用户名</small>
            </div>
        </div>
		<div class="field grid">
            <label for="pass" class="col-12 mb-2 md:col-2 md:mb-0">密码</label>
            <div class="col-12 md:col-10">
                <InputText id="pass" type="text" v-model.trim="passIn" />
	            <small>SMTP用户授权码</small>
            </div>
        </div>
		<div class="field grid">
            <label for="ssl" class="col-12 mb-2 md:col-2 md:mb-0">SSL</label>
            <div class="col-12 md:col-2">
                <SelectButton v-model="sslIn" :options="sslOptions" aria-labelledby="basic" v-model.trim="sslIn" />
	            <small>使用SSL安全连接</small>
            </div>
        </div>
		<div class="field grid">
            <label for="starttls" class="col-12 mb-2 md:col-2 md:mb-0">STARTTLS</label>
            <div class="col-12 md:col-2">
                <SelectButton v-model="starttlsIn" :options="starttlsOptions" aria-labelledby="basic" v-model.trim="starttlsIn" />
	            <small>使用STARTTLS安全连接，STARTTLS是对纯文本通信协议的扩展</small>
            </div>
        </div>
		<div class="field grid">
            <label for="timeout" class="col-12 mb-2 md:col-2 md:mb-0">SMTP超时</label>
            <div class="col-12 md:col-10">
                <InputNumber id="timeout" v-model.trim="timeoutIn" />
	            <small>SMTP超时时长，单位毫秒，缺省值不超时，默认值为0</small>
            </div>
        </div>
		<div class="field grid">
            <label for="connectionTimeout" class="col-12 mb-2 md:col-2 md:mb-0">Socket连接超时</label>
            <div class="col-12 md:col-10">
                <InputNumber id="connectionTimeout" v-model.trim="connectionTimeoutIn" />
	            <small>Socket连接超时值，单位毫秒，缺省值不超时，默认值为0</small>
            </div>
        </div>
		<div class="field grid">
            <label for="keyWords" class="col-12 mb-2 md:col-2 md:mb-0">违规关键词</label>
            <div class="col-12 md:col-10">
                <Textarea id="keyWords" type="text" v-model.trim="keyWordsIn" rows="4" />
	            <small>网站违规扫描关键词，使用英文逗号分隔</small>
            </div>
        </div>
		<div class="field grid">
            <label for="noPrefix" class="col-12 mb-2 md:col-2 md:mb-0">保留前缀</label>
            <div class="col-12 md:col-10">
                <Textarea id="noPrefix" type="text" v-model.trim="noPrefixIn" rows="4" />
	            <small>保留前缀，禁止用户解析，使用英文逗号分隔</small>
            </div>
        </div>
		<div class="field grid">
            <label for="point" class="col-12 mb-2 md:col-2 md:mb-0">注册用户初始积分</label>
            <div class="col-12 md:col-10">
                <InputNumber id="point" v-model.trim="defaultPointIn" />
            </div>
        </div>
		<div class="field grid">
            <label for="sign" class="col-12 mb-2 md:col-2 md:mb-0">每日签到积分</label>
            <div class="col-12 md:col-10">
                <InputNumber id="sign" v-model.trim="signPointIn" />
            </div>
        </div>
		<div class="field grid">
            <label for="exchange" class="col-12 mb-2 md:col-2 md:mb-0">通用兑换码</label>
            <div class="col-12 md:col-10">
                <InputText id="exchange" type="text" v-model.trim="commonCodeIn" />
            </div>
        </div>
		<div class="field grid">
            <label for="commonPoint" class="col-12 mb-2 md:col-2 md:mb-0">通用兑换码兑换积分</label>
            <div class="col-12 md:col-10">
                <InputNumber id="commonPoint" v-model.trim="commonPointIn" />
            </div>
        </div>
		<div class="flex align-items-center justify-content-end">
			<Button label="保存" icon="pi pi-save" @click="confirmUpdate" class="w-7rem" />
		</div>
    </div>
</template>