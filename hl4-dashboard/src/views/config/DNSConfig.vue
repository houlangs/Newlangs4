<script setup>
import { onMounted, ref } from 'vue'
import axios from '@/axios/axios'
import { useToast } from 'primevue/usetoast'
import { useConfirm } from 'primevue/useconfirm'

const dnsList = ref()
const dnsObj = ref()
const dnsIn = ref()
const configIn = ref()
const selectDNS = ref()
const dnsOptions = ref([
	{ name: 'CloudFlare' }
])
const account = ref()
const secret = ref()
const accountIn = ref()
const secretIn = ref()
const updateDNSConfigDialog = ref(false)
const addDNSConfigDialog = ref(false)
const deleteDNSConfigDialog = ref(false)
const toast = useToast()
const confirm = useConfirm()

onMounted(() => {
	getList()
})

//动态配置account和secret
const auto = () => {
	if (selectDNS.value === undefined) {
		return toast.add({severity: 'error', summary: 'ERROR', detail: 'DNS不能为空', life: 3000})
	}
	switch (selectDNS.value.name) {
		case 'CloudFlare':
			account.value = 'email'
			secret.value = 'key'
			break
	}
}

//获取DNS配置列表
const getList = () => {
	axios.get('/domain_config/list')
		.then(function(response) {
			if (response.data.code === 200) {
				dnsList.value = response.data.data.records
			} else {
				toast.add({severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000})
			}
		})
		.catch(function(error) {
			console.log(error)
		})
}

//添加DNS配置
const add = (data) => {
	axios.post('/domain_config/add', data)
		.then(function(response) {
			if (response.data.code === 200) {
				addDNSConfigDialog.value = false
				dnsObj.value = ''
				toast.add({severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000})
			} else {
				console.log(response.data)
				toast.add({severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000})
			}
			getList()
		}).catch(function(error) {
			getList()
			console.log(error)
		})
}

//更新DNS配置
const update = (data) => {
	axios.put('/domain_config/update', data).then(function(response) {
			if (response.data.code === 200) {
				updateDNSConfigDialog.value = false
				dnsObj.value = ''
				toast.add({severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000})
			} else {
				console.log(response.data)
				toast.add({severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000})
			}
			getList()
		}).catch(function(error) {
			getList()
			console.log(error)
		})
}

//删除DNS配置
const remove = (dns) => {
	axios.delete('/domain_config/delete/' + dns)
		.then(function(response) {
			if (response.data.code === 200) {
				deleteDNSConfigDialog.value = false
				dnsObj.value = ''
				toast.add({severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000})
			} else {
				console.log(response.data)
				toast.add({severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000})
			}
			getList()
		}).catch(function(error) {
			getList()
			console.log(error)
		})
}

//隐藏Dialog
const hideDialog = () => {
	updateDNSConfigDialog.value = false
	addDNSConfigDialog.value = false
	dnsObj.value = ''
}
//提交确认添加DNS配置请求
const confirmAddDNSConfig = () => {
	if (selectDNS.value === undefined) {
		return toast.add({severity: 'error', summary: 'ERROR', detail: 'DNS不能为空', life: 3000})
	}
	auto()
	const dns = selectDNS.value.name
	const config = JSON.stringify({
		[account.value]: accountIn.value,
		[secret.value]: secretIn.value
	})
	const data = {
		dns: dns,
		config: config
	}
	console.log(accountIn.value)
	console.log(secretIn.value)
	add(data)
}
//打开更新DNS配置Dialog并传参
const updateDNSConfig = (prod) => {
	dnsObj.value = prod
	dnsIn.value = dnsObj.value.dns
	selectDNS.value = { name: dnsIn.value }
	configIn.value = JSON.parse(dnsObj.value.config)
	updateDNSConfigDialog.value = true
}
//提交确认更新DNS配置请求
const confirmUpdateDNSConfig = () => {
	if (selectDNS.value === undefined) {
		return toast.add({severity: 'error', summary: 'ERROR', detail: 'DNS不能为空', life: 3000})
	}
	auto()
	const dns = selectDNS.value.name
	const config = JSON.stringify({
		[account.value]: accountIn.value,
		[secret.value]: secretIn.value
	})
	const data = {
		dns: dns,
		config: config
	}
	console.log(accountIn.value)
	console.log(secretIn.value)
	update(data)
}
//确认删除域名Dialog
const deleteDNSConfig = (prod) => {
	dnsObj.value = prod
	deleteDNSConfigDialog.value = true
}
//提交确认删除DNS配置请求
const confirmDeleteDNSConfig = () => {
	const dns = dnsObj.value.dns
	remove(dns)
}
</script>

<template>
	<div>
        <div class="card">
            <Toolbar class="mb-4">
                <template #start>
	                <h4 class="m-0">DNS配置列表</h4>
                </template>
            </Toolbar>
            <DataTable showGridlines :value="dnsList" dataKey="dns" columnResizeMode="fit"
                :paginator="true" :rows="10"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" :rowsPerPageOptions="[5,10,25]"
                currentPageReportTemplate="第 {first} 页，本页 {last} 条记录，共 {totalRecords} 条记录">
                <template #header>
                    <Button label="添加" icon="pi pi-plus" severity="success" class="mr-2" @click="addDNSConfigDialog = true" />
                </template>

                <Column field="dns" header="DNS"></Column>
                <Column field="config" header="配置"></Column>
	            <Column field="createdTime" header="添加时间" style="min-width:12rem"></Column>
	            <Column field="updatedTime" header="更新时间" style="min-width: 8rem"></Column>
                <Column :exportable="false" style="min-width:24rem">
                    <template #body="slotProps">
	                    <span class="p-buttonset">
		                    <Button label="编辑" icon="pi pi-pencil" @click="updateDNSConfig(slotProps.data)" />
		                    <Button label="删除" icon="pi pi-trash" severity="danger" @click="deleteDNSConfig(slotProps.data)" />
	                    </span>
                    </template>
                </Column>
            </DataTable>
        </div>

	    <Toast />

		<Dialog v-model:visible="addDNSConfigDialog" :style="{width: '450px'}" header="添加配置" :modal="true" class="p-fluid">
            <div class="field">
                <label for="dns">DNS</label>
	            <Dropdown id="dns" v-model="selectDNS" :options="dnsOptions" optionLabel="name" placeholder="请选择DNS" class="w-full" autofocus />
            </div>
		    <div class="field">
		        <InputText id="account" v-model.trim="accountIn" placeholder="请输入账户" />
            </div>
		    <div class="field">
		        <InputText id="secret" v-model.trim="secretIn" placeholder="请输入密钥" />
            </div>
            <template #footer>
	            <Button text label="取消" icon="pi pi-times" @click="hideDialog" />
                <Button text label="确认" icon="pi pi-check" @click="confirmAddDNSConfig" />
            </template>
        </Dialog>

        <Dialog v-model:visible="updateDNSConfigDialog" :style="{width: '450px'}" header="更新配置" :modal="true" class="p-fluid">
            <div class="field">
                <label for="dns">DNS</label>
	            <Dropdown id="dns" v-model="selectDNS" :options="dnsOptions" optionLabel="name" placeholder="请选择DNS" class="w-full" autofocus />
            </div>
		    <div class="field">
		        <InputText id="account" v-model.trim="accountIn" placeholder="请输入账户" />
            </div>
		    <div class="field">
		        <InputText id="secret" v-model.trim="secretIn" placeholder="请输入密钥" />
            </div>
            <template #footer>
	            <Button text label="取消" icon="pi pi-times" @click="hideDialog" />
                <Button text label="确认" icon="pi pi-check" @click="confirmUpdateDNSConfig" />
            </template>
        </Dialog>

	    <Dialog v-model:visible="deleteDNSConfigDialog" modal header="删除DNS配置" :style="{ width: '450px' }">
		    <p>
		        确认删除该DNS配置？
		    </p>
		    <template #footer>
		        <Button label="取消" icon="pi pi-times" @click="hideDialog" text />
		        <Button label="确认" icon="pi pi-check" @click="confirmDeleteDNSConfig" autofocus />
		    </template>
		</Dialog>
	</div>
</template>