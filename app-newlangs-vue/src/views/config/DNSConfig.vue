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

// Dynamic configuration of account and secret
const auto = () => {
	if (selectDNS.value === undefined) {
		return toast.add({ severity: 'error', summary: 'ERROR', detail: 'DNS cannot be empty', life: 3000 })
	}
	switch (selectDNS.value.name) {
		case 'CloudFlare':
			account.value = 'email'
			secret.value = 'key'
			break
	}
}

// Get DNS configuration list
const getList = () => {
	axios.get('/domain_config/list')
		.then(function (response) {
			if (response.data.code === 200) {
				dnsList.value = response.data.data.records
			} else {
				toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
			}
		})
		.catch(function (error) {
			console.log(error)
		})
}

// Add DNS configuration
const add = (data) => {
	axios.post('/domain_config/add', data)
		.then(function (response) {
			if (response.data.code === 200) {
				addDNSConfigDialog.value = false
				dnsObj.value = ''
				toast.add({ severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000 })
			} else {
				console.log(response.data)
				toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
			}
			getList()
		}).catch(function (error) {
			getList()
			console.log(error)
		})
}

// Update DNS configuration
const update = (data) => {
	axios.put('/domain_config/update', data).then(function (response) {
		if (response.data.code === 200) {
			updateDNSConfigDialog.value = false
			dnsObj.value = ''
			toast.add({ severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000 })
		} else {
			console.log(response.data)
			toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
		}
		getList()
	}).catch(function (error) {
		getList()
		console.log(error)
	})
}

// Remove DNS configuration
const remove = (dns) => {
	axios.delete('/domain_config/delete/' + dns)
		.then(function (response) {
			if (response.data.code === 200) {
				deleteDNSConfigDialog.value = false
				dnsObj.value = ''
				toast.add({ severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000 })
			} else {
				console.log(response.data)
				toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
			}
			getList()
		}).catch(function (error) {
			getList()
			console.log(error)
		})
}

// Hide Dialog
const hideDialog = () => {
	updateDNSConfigDialog.value = false
	addDNSConfigDialog.value = false
	dnsObj.value = ''
}

// Submit confirmation to add DNS configuration
const confirmAddDNSConfig = () => {
	if (selectDNS.value === undefined) {
		return toast.add({ severity: 'error', summary: 'ERROR', detail: 'DNS cannot be empty', life: 3000 })
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

// Open the update DNS configuration Dialog and pass parameters
const updateDNSConfig = (prod) => {
	dnsObj.value = prod
	dnsIn.value = dnsObj.value.dns
	selectDNS.value = { name: dnsIn.value }
	configIn.value = JSON.parse(dnsObj.value.config)
	updateDNSConfigDialog.value = true
}

// Submit confirmation to update DNS configuration
const confirmUpdateDNSConfig = () => {
	if (selectDNS.value === undefined) {
		return toast.add({ severity: 'error', summary: 'ERROR', detail: 'DNS cannot be empty', life: 3000 })
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

// Confirm DNS configuration deletion Dialog
const deleteDNSConfig = (prod) => {
	dnsObj.value = prod
	deleteDNSConfigDialog.value = true
}

// Submit confirmation to delete DNS configuration
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
					<h4 class="m-0">DNS Configuration List</h4>
				</template>
			</Toolbar>
			<DataTable showGridlines :value="dnsList" dataKey="dns" columnResizeMode="fit" :paginator="true" :rows="10"
				paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
				:rowsPerPageOptions="[5, 10, 25]"
				currentPageReportTemplate="Page {first} of {last}, displaying {totalRecords} records">
				<template #header>
					<Button label="Add" icon="pi pi-plus" severity="success" class="mr-2"
						@click="addDNSConfigDialog = true" />
				</template>

				<Column field="dns" header="DNS"></Column>
				<Column field="config" header="Configuration"></Column>
				<Column field="createdTime" header="Added Time" style="min-width:12rem"></Column>
				<Column field="updatedTime" header="Updated Time" style="min-width: 8rem"></Column>
				<Column :exportable="false" style="min-width:24rem">
					<template #body="slotProps">
						<span class="p-buttonset">
							<Button label="Edit" icon="pi pi-pencil" @click="updateDNSConfig(slotProps.data)" />
							<Button label="Delete" icon="pi pi-trash" severity="danger"
								@click="deleteDNSConfig(slotProps.data)" />
						</span>
					</template>
				</Column>
			</DataTable>
		</div>

		<Toast />

		<Dialog v-model:visible="addDNSConfigDialog" :style="{ width: '450px' }" header="Add Configuration" :modal="true"
			class="p-fluid">
			<div class="field">
				<label for="dns">DNS</label>
				<Dropdown id="dns" v-model="selectDNS" :options="dnsOptions" optionLabel="name" placeholder="Select DNS"
					class="w-full" autofocus />
			</div>
			<div class="field">
				<InputText id="account" v-model.trim="accountIn" placeholder="Enter Account" />
			</div>
			<div class="field">
				<InputText id="secret" v-model.trim="secretIn" placeholder="Enter Secret Key" />
			</div>
			<template #footer>
				<Button text label="Cancel" icon="pi pi-times" @click="hideDialog" />
				<Button text label="Confirm" icon="pi pi-check" @click="confirmAddDNSConfig" />
			</template>
		</Dialog>

		<Dialog v-model:visible="updateDNSConfigDialog" :style="{ width: '450px' }" header="Update Configuration"
			:modal="true" class="p-fluid">
			<div class="field">
				<label for="dns">DNS</label>
				<Dropdown id="dns" v-model="selectDNS" :options="dnsOptions" optionLabel="name" placeholder="Select DNS"
					class="w-full" autofocus />
			</div>
			<div class="field">
				<InputText id="account" v-model.trim="accountIn" placeholder="Enter Account" />
			</div>
			<div class="field">
				<InputText id="secret" v-model.trim="secretIn" placeholder="Enter Secret Key" />
			</div>
			<template #footer>
				<Button text label="Cancel" icon="pi pi-times" @click="hideDialog" />
				<Button text label="Confirm" icon="pi pi-check" @click="confirmUpdateDNSConfig" />
			</template>
		</Dialog>

		<Dialog v-model:visible="deleteDNSConfigDialog" modal header="Delete DNS Configuration" :style="{ width: '450px' }">
			<p>
				Confirm the deletion of this DNS configuration?
			</p>
			<template #footer>
				<Button label="Cancel" icon="pi pi-times" @click="hideDialog" text />
				<Button label="Confirm" icon="pi pi-check" @click="confirmDeleteDNSConfig" autofocus />
		</template>
	</Dialog>
</div></template>
