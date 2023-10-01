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
	{ id: 1, name: 'DDoS Protected CDN' },
	{ id: 0, name: 'Default' }
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
// Get user information
const getUserInfo = () => {
	axios.get('/user/get_user')
		.then(function (response) {
			UserId.value = response.data.data.id
		}).catch(function (error) {
			console.log(error)
		})
}
// Get domain list
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
// Get domain record list
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
// Update domain record information
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
// Add domain record
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
// Remove domain record
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

// Hide Dialog
const hideDialog = () => {
	updateDomainRecordDialog.value = false
	addDomainRecordDialog.value = false
	deleteDomainRecordDialog.value = false
	domainRecord.value = null
}
// Open and pass parameters to update domain record Dialog
const updateDomainRecord = (prod) => {
	domainRecord.value = prod
	prefixIn.value = domainRecord.value.prefix
	selectedDomain.value = domainRecord.value.did
	valueIn.value = domainRecord.value.value
	selectLine.value = domainRecord.value.lineId
	commentIn.value = domainRecord.value.comment
	updateDomainRecordDialog.value = true
}
// Confirm delete domain Dialog
const deleteDomainRecord = (prod) => {
	domainRecord.value = prod
	deleteDomainRecordDialog.value = true
}
// Submit confirmation to update domain request
const confirmUpdateDomainRecord = () => {
	disabled.value = true
	const id = domainRecord.value.id
	const userId = UserId.value
	const prefix = prefixIn.value
	if (selectedDomain.value == null) {
		disabled.value = false
		return toast.add({ severity: 'error', summary: 'ERROR', detail: 'Domain cannot be empty', life: 3000 })
	}
	const did = selectedDomain.value.id
	if (selectedType.value == null) {
		disabled.value = false
		return toast.add({ severity: 'error', summary: 'ERROR', detail: 'Record type cannot be empty', life: 3000 })
	}
	const type = selectedType.value.name
	const value = valueIn.value
	if (selectLine.value == null) {
		disabled.value = false
		return toast.add({ severity: 'error', summary: 'ERROR', detail: 'Line cannot be empty', life: 3000 })
	}
	const lineId = selectLine.value.id
	const line = selectLine.value.name
	const comment = commentIn.value
	update(userId, id, did, prefix, type, value, lineId, line, comment)
}
// Submit confirmation to add domain request
const confirmAddDomainRecord = () => {
	disabled.value = true
	const userId = UserId.value
	const prefix = prefixIn.value
	if (selectedDomain.value == null) {
		disabled.value = false
		return toast.add({ severity: 'error', summary: 'ERROR', detail: 'Domain cannot be empty', life: 3000 })
	}
	const did = selectedDomain.value.id
	if (selectedType.value == null) {
		disabled.value = false
		return toast.add({ severity: 'error', summary: 'ERROR', detail: 'Record type cannot be empty', life: 3000 })
	}
	const type = selectedType.value.name
	const value = valueIn.value
	if (selectLine.value == null) {
		disabled.value = false
		return toast.add({ severity: 'error', summary: 'ERROR', detail: 'Line cannot be empty', life: 3000 })
	}
	const lineId = selectLine.value.id
	const line = selectLine.value.name
	const comment = commentIn.value
	add(userId, did, prefix, type, value, lineId, line, comment)
}
// Submit confirmation to delete domain request
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

// Get domain remarks and price
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
	<div class="card">
		<h3>⚠️ Parsing Notice</h3>
		<p>It is forbidden to set up any profit-making websites involving payments, such as card issuance, easy payment, stores, etc.
			<br>
			Prohibited from setting up illegal and irregular websites such as gambling, pornography, gambling, fraud, etc.
			<br>
			Please be sure to display the record number at the bottom of the website when using the record domain.
			<br>
			Prohibited from using domains for any illegal activities.
			<br>
			Violators will have their parsing deleted and their accounts banned, and will not be unblocked.
		</p>
		<h4>Please complete the authentication before parsing <a href="/#/user_info">Click here to authenticate</a></h4>
	</div>
	<div>
		<div class="card">
			<Toolbar class="mb-4">
				<template #start>
					<h4 class="m-0">Record List</h4>
				</template>
			</Toolbar>
			<DataTable showGridlines :value="domainList" dataKey="id" columnResizeMode="fit" :paginator="true" :rows="10"
				:filters="filters"
				paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
				:rowsPerPageOptions="[5, 10, 25]"
				currentPageReportTemplate="Page {first} of {last}, displaying {totalRecords} records">
				<template #header>
					<div class="flex flex-wrap gap-2 align-items-center justify-content-between">
						<span class="p-input-icon-left">
							<i class="pi pi-search" />
							<InputText v-model="filters['global'].value" placeholder="Search" />
						</span>
						<Button label="Add" icon="pi pi-plus" severity="success" class="mr-2"
							@click="addDomainRecordDialog = true" />
					</div>
				</template>

				<Column field="id" header="ID"></Column>
				<Column field="userId" header="User ID"></Column>
				<Column field="domain" header="Domain">
					<template #body="slotProps">
						<a :href="'http://' + slotProps.data.domain" target="_blank">{{ slotProps.data.domain }}</a>
					</template>
				</Column>
				<Column field="type" header="Record Type" style="min-width: 8rem"></Column>
				<Column field="value" header="Record Value" style="min-width: 8rem"></Column>
				<Column field="line" header="Line" style="min-width: 8rem"></Column>
				<Column field="createdTime" header="Add Time" style="min-width:12rem"></Column>
				<Column :exportable="false" style="min-width:24rem">
					<template #body="slotProps">
						<span class="p-buttonset">
							<Button label="Edit" icon="pi pi-pencil" @click="updateDomainRecord(slotProps.data)" />
							<Button label="Delete" icon="pi pi-trash" severity="danger"
								@click="deleteDomainRecord(slotProps.data)" />
						</span>
					</template>
				</Column>
			</DataTable>
		</div>

		<Toast />

		<Dialog v-model:visible="updateDomainRecordDialog" :style="{ width: '450px' }" header="Update Record" :modal="true"
			class="p-fluid">
				<!-- Your form inputs and code here -->
		</Dialog>

		<Dialog v-model:visible="addDomainRecordDialog" :style="{ width: '450px' }" header="Add Record" :modal="true"
			class="p-fluid">
				<!-- Your form inputs and code here -->
		</Dialog>

		<Dialog v-model:visible="deleteDomainRecordDialog" modal header="Delete Record" :style="{ width: '450px' }">
			<p>
				Once the domain record is deleted, it cannot be restored. Are you sure you want to delete this domain record?
			</p>
			<template #footer>
				<Button label="Cancel" icon="pi pi-times" @click="hideDialog" text />
				<Button label="Confirm" icon="pi pi-check" @click="confirmDeleteDomainRecord" autofocus />
			</template>
		</Dialog>
	</div>
</template>
