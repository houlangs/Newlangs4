<script setup>
import { onMounted, ref } from 'vue'
import { FilterMatchMode } from 'primevue/api'
import axios from '@/axios/axios'
import { useToast } from 'primevue/usetoast'
import { useConfirm } from 'primevue/useconfirm'

const UserId = ref('')
const domainRecords = ref([])
const domainRecord = ref('')
const domains = ref([])
const selectedDomain = ref('')
const prefixIn = ref('')
const types = ref([
	{ name: 'A' },
	{ name: 'CNAME' },
	{ name: 'TXT' }
])
const selectedType = ref('')
const valueIn = ref('')
const lines = ref([
	{ id: 1, name: '高防CDN' },
	{ id: 0, name: '默认' }
])
const selectLine = ref('')
const commentIn = ref('')
const price = ref('')
const domainComment = ref('')

const updateDomainRecordDialog = ref(false)
const addDomainRecordDialog = ref(false)
const deleteDomainRecordDialog = ref(false)
const toast = useToast()
const confirm = useConfirm()
const filters = ref({
	'global': { value: null, matchMode: FilterMatchMode.CONTAINS },
})
let domainItem = ref()
let domainList = ref([])
const disabled = ref(false)

onMounted(() => {
	getUserInfo()
	getDomainRecordList()
})
//获取用户信息
const getUserInfo = () => {
	axios.get('/user/get_user')
		.then(function (response) {
			UserId.value = response.data.data.id
		}).catch(function (error) {
			console.log(error)
		})
}
//获取域名列表
const getDomainList = () => {
	axios.get('/domain/list', {
		params: {
			page: 1,
			pageSize: 10
		}
	}).then(function (response) {
		if (response.data.code === 200) {
			domains.value = response.data.data.records
			computedDomainItem()
		} else {
			toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
		}
	}).catch(function (error) {
		console.log(error)
	})
}
//获取域名记录列表
const getDomainRecordList = () => {
	axios.get('/domain_record/list', {
		params: {
			page: 1,
			pageSize: 10
		}
	}).then(function (response) {
		if (response.data.code === 200) {
			domainRecords.value = response.data.data.records
			getDomainList()
		} else {
			toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
		}
	}).catch(function (error) {
		console.log(error)
	})
}
//更新域名记录信息
const update = (userId, id, did, prefix, type, value, lineId, line, comment) => {
	axios.put('/domain_record/update', {
		userId: userId,
		id: id,
		did: did,
		prefix: prefix,
		type: type,
		value: value,
		lineId: lineId,
		line: line,
		comment: comment
	}).then(function (response) {
		if (response.data.code === 200) {
			updateDomainRecordDialog.value = false
			domainRecord.value = null
			disabled.value = false
			toast.add({ severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000 })
		} else {
			console.log(response.data)
			disabled.value = false
			toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
		}
		getDomainRecordList()
	}).catch(function (error) {
		getDomainRecordList()
		console.log(error)
	})
}
//添加域名记录
const add = (userId, did, prefix, type, value, lineId, line, comment) => {
	axios.post('/domain_record/add', {
		userId: userId,
		did: did,
		prefix: prefix,
		type: type,
		value: value,
		lineId: lineId,
		line: line,
		comment: comment
	}).then(function (response) {
		if (response.data.code === 200) {
			addDomainRecordDialog.value = false
			domainRecord.value = null
			toast.add({ severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000 })
		} else {
			console.log(response.data)
			toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
		}
		disabled.value = false
		getDomainRecordList()
	}).catch(function (error) {
		getDomainRecordList()
		console.log(error)
	})
}
//删除域名记录
const remove = (id) => {
	axios.delete('/domain_record/delete/' + id)
		.then(function (response) {
			if (response.data.code === 200) {
				deleteDomainRecordDialog.value = false
				domainRecord.value = null
				toast.add({ severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000 })
			} else {
				console.log(response.data)
				toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
			}
			getDomainRecordList()
		}).catch(function (error) {
			getDomainRecordList()
			console.log(error)
		})
}

//隐藏Dialog
const hideDialog = () => {
	updateDomainRecordDialog.value = false
	addDomainRecordDialog.value = false
	deleteDomainRecordDialog.value = false
	domainRecord.value = null
}
//打开更新域名记录Dialog并传参
const updateDomainRecord = (prod) => {
	domainRecord.value = prod
	prefixIn.value = domainRecord.value.prefix
	selectedDomain.value = domainRecord.value.did
	valueIn.value = domainRecord.value.value
	selectLine.value = domainRecord.value.lineId
	commentIn.value = domainRecord.value.comment
	updateDomainRecordDialog.value = true
}
//确认删除域名Dialog
const deleteDomainRecord = (prod) => {
	domainRecord.value = prod
	deleteDomainRecordDialog.value = true
}
//提交确认更新域名请求
const confirmUpdateDomainRecord = () => {
	disabled.value = true
	const id = domainRecord.value.id
	const userId = UserId.value
	const prefix = prefixIn.value
	if (selectedDomain.value == null) {
		disabled.value = false
		return toast.add({ severity: 'error', summary: 'ERROR', detail: '域名不能为空', life: 3000 })
	}
	const did = selectedDomain.value.id
	if (selectedType.value == null) {
		disabled.value = false
		return toast.add({ severity: 'error', summary: 'ERROR', detail: '记录类型不能为空', life: 3000 })
	}
	const type = selectedType.value.name
	const value = valueIn.value
	if (selectLine.value == null) {
		disabled.value = false
		return toast.add({ severity: 'error', summary: 'ERROR', detail: '线路不能为空', life: 3000 })
	}
	const lineId = selectLine.value.id
	const line = selectLine.value.name
	const comment = commentIn.value
	update(userId, id, did, prefix, type, value, lineId, line, comment)
}
//提交确认添加域名请求
const confirmAddDomainRecord = () => {
	disabled.value = true
	const userId = UserId.value
	const prefix = prefixIn.value
	if (selectedDomain.value == null) {
		disabled.value = false
		return toast.add({ severity: 'error', summary: 'ERROR', detail: '域名不能为空', life: 3000 })
	}
	const did = selectedDomain.value.id
	if (selectedType.value == null) {
		disabled.value = false
		return toast.add({ severity: 'error', summary: 'ERROR', detail: '记录类型不能为空', life: 3000 })
	}
	const type = selectedType.value.name
	const value = valueIn.value
	if (selectLine.value == null) {
		disabled.value = false
		return toast.add({ severity: 'error', summary: 'ERROR', detail: '线路不能为空', life: 3000 })
	}
	const lineId = selectLine.value.id
	const line = selectLine.value.name
	const comment = commentIn.value
	add(userId, did, prefix, type, value, lineId, line, comment)
}
//提交确认删除域名请求
const confirmDeleteDomainRecord = () => {
	const id = domainRecord.value.id
	remove(id)
}
const computedDomainItem = () => {
	domainList.value = []
	for (let i = 0; i < domainRecords.value.length; i++) {
		for (let j = 0; j < domains.value.length; j++) {
			let obj = {
				id: '',
				userId: '',
				domain: '',
				type: '',
				value: '',
				line: '',
				createdTime: ''
			}

			if (domainRecords.value[i].did === domains.value[j].id) {
				domainItem.value = domains.value[j].name
				obj.id = domainRecords.value[i].id
				obj.userId = domainRecords.value[i].userId
				obj.domain = domainRecords.value[i].prefix + '.' + domainItem.value
				obj.type = domainRecords.value[i].type
				obj.value = domainRecords.value[i].value
				obj.line = domainRecords.value[i].line
				obj.createdTime = domainRecords.value[i].createdTime
				domainList.value.push(obj)
			}
		}
	}
}

//获取域名备注和价格
// onBeforeUpdate(() => {
// 	axios.get('/domain/detail/' + selectedDomain.value.id)
// 		.then(function(response) {
// 			if (response.data.code === 200) {
// 				console.log(response.data.data)
// 				price.value = response.data.data.price
// 				domainComment.value = response.data.data.comment
// 			} else {
// 				console.log(response.data)
// 				toast.add({severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000})
// 			}
// 		}).catch(function(error) {
// 			console.log(error)
// 		})
// })

//获取域名备注和价格
const computedDomain = () => {
	axios.get('/domain/detail/' + selectedDomain.value.id)
		.then(function (response) {
			if (response.data.code === 200) {
				console.log(response.data.data)
				price.value = response.data.data.point
				domainComment.value = response.data.data.comment
			} else {
				console.log(response.data)
				toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
			}
		}).catch(function (error) {
			console.log(error)
		})
}
</script>

<template>
	<div>
		<div class="card">
			<Toolbar class="mb-4">
				<template #start>
					<h4 class="m-0">记录列表</h4>
				</template>
			</Toolbar>
			<DataTable showGridlines :value="domainList" dataKey="id" columnResizeMode="fit" :paginator="true" :rows="10"
				:filters="filters"
				paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
				:rowsPerPageOptions="[5, 10, 25]"
				currentPageReportTemplate="第 {first} 页，本页 {last} 条记录，共 {totalRecords} 条记录">
				<template #header>
					<div class="flex flex-wrap gap-2 align-items-center justify-content-between">
						<span class="p-input-icon-left">
							<i class="pi pi-search" />
							<InputText v-model="filters['global'].value" placeholder="搜索" />
						</span>
						<Button label="添加" icon="pi pi-plus" severity="success" class="mr-2"
							@click="addDomainRecordDialog = true" />
					</div>
				</template>

				<Column field="id" header="ID"></Column>
				<Column field="userId" header="用户ID"></Column>
				<Column field="domain" header="域名">
					<template #body="slotProps">
						<a :href="'http://' + slotProps.data.domain" target="_blank">{{ slotProps.data.domain }}</a>
					</template>
				</Column>
				<Column field="type" header="记录类型" style="min-width: 8rem"></Column>
				<Column field="value" header="记录值" style="min-width: 8rem"></Column>
				<Column field="line" header="线路" style="min-width: 8rem"></Column>
				<Column field="createdTime" header="添加时间" style="min-width:12rem"></Column>
				<Column :exportable="false" style="min-width:24rem">
					<template #body="slotProps">
						<span class="p-buttonset">
							<Button label="编辑" icon="pi pi-pencil" @click="updateDomainRecord(slotProps.data)" />
							<Button label="删除" icon="pi pi-trash" severity="danger"
								@click="deleteDomainRecord(slotProps.data)" />
						</span>
					</template>
				</Column>
			</DataTable>
		</div>

		<Toast />

		<Dialog v-model:visible="updateDomainRecordDialog" :style="{ width: '450px' }" header="更新记录" :modal="true"
			class="p-fluid">
			<div class="field">
				<label for="prefix">域名前缀</label>
				<InputText id="prefix" v-model.trim="prefixIn" placeholder="请输入域名前缀" autofocus />
			</div>
			<div class="field">
				<label for="domain">选择域名</label>
				<Dropdown id="dns" v-model="selectedDomain" :options="domains" optionLabel="name" placeholder="请选择域名"
					class="w-full" />
			</div>
			<div class="field">
				<label for="type">记录类型</label>
				<Dropdown id="type" v-model="selectedType" :options="types" optionLabel="name" placeholder="请选择记录类型"
					class="w-full" />
			</div>
			<div class="field">
				<label for="value">记录值</label>
				<InputText id="value" v-model.trim="valueIn" placeholder="请输入记录值" />
			</div>
			<div class="field">
				<label for="line">线路</label>
				<Dropdown id="line" v-model="selectLine" :options="lines" optionLabel="name" placeholder="请选择解析线路"
					class="w-full" />
			</div>
			<div class="field">
				<label for="comment">备注</label>
				<Textarea id="comment" v-model.trim="commentIn" rows="3" cols="30" />
			</div>
			<template #footer>
				<Button text label="取消" icon="pi pi-times" @click="hideDialog" />
				<Button text label="确认" icon="pi pi-check" @click="confirmUpdateDomainRecord" :loading="disabled" />
			</template>
		</Dialog>

		<Dialog v-model:visible="addDomainRecordDialog" :style="{ width: '450px' }" header="添加记录" :modal="true"
			class="p-fluid">
			<div class="field">
				<label for="prefix">域名前缀</label>
				<InputText id="prefix" v-model.trim="prefixIn" placeholder="请输入域名前缀" autofocus />
			</div>
			<div class="field">
				<label for="domain">选择域名</label>
				<Dropdown id="dns" v-model="selectedDomain" :options="domains" optionLabel="name" placeholder="请选择域名"
					class="w-full" @change="computedDomain" />
			</div>
			<div class="card p-fluid"
				v-if="selectedDomain !== null || selectedDomain !== undefined || selectedDomain !== ''">
				消耗积分: {{ price }}<br><br>
				域名介绍: <span v-html="domainComment"></span>
			</div>

			<div class="field">
				<label for="type">记录类型</label>
				<Dropdown id="type" v-model="selectedType" :options="types" optionLabel="name" placeholder="请选择记录类型"
					class="w-full" />
			</div>
			<div class="field">
				<label for="value">记录值</label>
				<InputText id="value" v-model.trim="valueIn" placeholder="请输入记录值" />
			</div>
			<div class="field">
				<label for="line">线路</label>
				<Dropdown id="line" v-model="selectLine" :options="lines" optionLabel="name" placeholder="请选择解析线路"
					class="w-full" />
			</div>
			<div class="field">
				<label for="comment">备注</label>
				<Textarea id="comment" v-model.trim="commentIn" rows="3" cols="30" />
			</div>
			<template #footer>
				<Button text label="取消" icon="pi pi-times" @click="hideDialog" />
				<Button text label="确认" icon="pi pi-check" @click="confirmAddDomainRecord" :loading="disabled" />
			</template>
		</Dialog>

		<Dialog v-model:visible="deleteDomainRecordDialog" modal header="删除记录" :style="{ width: '450px' }">
			<p>
				域名记录删除后不可恢复，确认删除该域名记录？
			</p>
			<template #footer>
				<Button label="取消" icon="pi pi-times" @click="hideDialog" text />
				<Button label="确认" icon="pi pi-check" @click="confirmDeleteDomainRecord" autofocus />
			</template>
		</Dialog>
	</div>
</template>