<script setup>
import { ref, onMounted } from 'vue'
import { FilterMatchMode } from 'primevue/api'
import axios from '@/axios/axios'
import { useToast } from "primevue/usetoast"
import { useConfirm } from "primevue/useconfirm"

const domains = ref()
const domain = ref()
const DNS = ref([])
const selectedDNS = ref()
const domainsFromDNS = ref([])
const selectedDomain = ref()
const roleList = ref()
const checkedRole = ref([])
const pointIn = ref()
const commentIn = ref()
const updateDomainDialog = ref(false)
const addDomainDialog = ref(false)
const deleteDomainDialog = ref(false)
const toast = useToast()
const confirm = useConfirm()
const filters = ref({
	'global': { value: null, matchMode: FilterMatchMode.CONTAINS },
});

onMounted(() => {
	getDomainList()
	getRoleList()
	getDNS()
})

// Get the list of roles
const getRoleList = () => {
	axios.get('/role/list', {
		params: {
			page: 1,
			pageSize: 10
		}
	}).then(function (response) {
		roleList.value = response.data.data.records
	}).catch(function (error) {
		console.log(error)
	})
}
// Get the list of domains
const getDomainList = () => {
	axios.get('/domain/list', {
		params: {
			page: 1,
			pageSize: 10
		}
	}).then(function (response) {
		domains.value = response.data.data.records
	}).catch(function (error) {
		console.log(error)
	})
}
// Update domain information
const update = (id, roleIds, point, comment) => {
	axios.put('/domain/update', {
		id: id,
		roleIds: roleIds,
		point: point,
		comment: comment
	}).then(function (response) {
		if (response.data.code === 200) {
			updateDomainDialog.value = false
			domain.value = {}
			toast.add({ severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000 })
		} else {
			console.log(response.data)
			toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
		}
		getDomainList()
	}).catch(function (error) {
		getDomainList()
		console.log(error)
	})
}
// Add a domain
const add = (dns, domainId, name, roleIds, point, comment) => {
	axios.post('/domain/add', {
		dns: dns,
		domainId: domainId,
		name: name,
		roleIds: roleIds,
		point: point,
		comment: comment
	}).then(function (response) {
		if (response.data.code === 200) {
			addDomainDialog.value = false
			domain.value = {}
			toast.add({ severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000 })
		} else {
			console.log(response.data)
			toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
		}
		getDomainList()
	}).catch(function (error) {
		getDomainList()
		console.log(error)
	})
}
// Delete a domain
const remove = (id) => {
	axios.delete('/domain/delete/' + id)
		.then(function (response) {
			if (response.data.code === 200) {
				deleteDomainDialog.value = false
				domain.value = {}
				toast.add({ severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000 })
			} else {
				console.log(response.data)
				toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
			}
			getDomainList()
		}).catch(function (error) {
			getDomainList()
			console.log(error)
		})
}
// Get domain list from DNS platform
const getDomainListFromDNS = (dns) => {
	axios.get('/domain/list_from_dns', {
		params: {
			dns: dns
		}
	}).then(function (response) {
		if (response.data.code === 200) {
			domainsFromDNS.value = response.data.data
			toast.add({ severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000 })
		} else {
			console.log(response.data)
			toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
		}
	}).catch(function (error) {
		console.log(error)
	})
}
// Get DNS platform list
const getDNS = () => {
	axios.get('/domain_config/list', {
		params: {
			page: 1,
			pageSize: 10
		}
	}).then(function (response) {
		if (response.data.code === 200) {
			DNS.value = response.data.data.records
		} else {
			console.log(response.data)
			toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
		}
	}).catch(function (error) {
		console.log(error)
	})
}

// Hide Dialog
const hideDialog = () => {
	updateDomainDialog.value = false
	addDomainDialog.value = false
	deleteDomainDialog.value = false
	domain.value = {}
}
// Open the update domain Dialog and pass parameters
const updateDomain = (prod) => {
	domain.value = prod
	// checkedRole.value = domain.value.roleIds
	pointIn.value = domain.value.point
	commentIn.value = domain.value.comment
	updateDomainDialog.value = true
}
// Confirm Delete Domain Dialog
const deleteDomain = (prod) => {
	domain.value = prod
	deleteDomainDialog.value = true
}
// Submit the request to update domain information
const confirmUpdateDomain = () => {
	const id = domain.value.id
	let roleIds = null
	if (checkedRole.value == null) {
		return toast.add({ severity: 'error', summary: 'ERROR', detail: 'The domain must have a role assigned', life: 3000 })
	} else {
		roleIds = String(checkedRole.value)
	}
	const point = pointIn.value
	const comment = commentIn.value
	update(id, roleIds, point, comment)
}
// Submit the request to add a new domain
const confirmAddDomain = () => {
	const dns = selectedDNS.value.dns
	const domainId = selectedDomain.value.domainId
	const name = selectedDomain.value.name
	let roleIds
	if (checkedRole.value == null) {
		return toast.add({ severity: 'error', summary: 'ERROR', detail: 'The domain must have a role assigned', life: 3000 })
	} else {
		roleIds = String(checkedRole.value)
	}
	const point = pointIn.value
	const comment = commentIn.value
	add(dns, domainId, name, roleIds, point, comment)
}
// Submit the request to delete a domain
const confirmDeleteDomain = () => {
	const id = domain.value.id
	remove(id)
}
</script>

<template>
	<div>
		<div class="card">
			<Toolbar class="mb-4">
				<template #start>
					<h4 class="m-0">Domain List</h4>
				</template>
			</Toolbar>

			<DataTable showGridlines :value="domains" dataKey="id" columnResizeMode="fit" :paginator="true" :rows="10"
				:filters="filters"
				paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown"
				:rowsPerPageOptions="[5, 10, 25]"
				currentPageReportTemplate="Page {currentPage} of {totalPages}, Rows {first} - {last} of {totalRecords}">
				<template #header>
					<div class="flex flex-wrap gap-2 align-items-center justify-content-between">
						<span class="p-input-icon-left">
							<i class="pi pi-search" />
							<InputText v-model="filters['global'].value" placeholder="Search" />
						</span>
						<Button label="Add" icon="pi pi-plus" severity="success" class="mr-2"
							@click="addDomainDialog = true" />
					</div>
				</template>

				<Column field="id" header="ID"></Column>
				<Column field="dns" header="DNS"></Column>
				<Column field="domainId" header="domainId"></Column>
				<Column field="name" header="Domain Name"></Column>
				<Column field="roleIds" header="Assigned Roles" style="min-width: 8rem"></Column>
				<Column field="point" header="Points" style="min-width: 8rem"></Column>
				<Column field="comment" header="Introduction" style="min-width: 8rem"></Column>
				<Column field="createdTime" header="Created Time" style="min-width:12rem"></Column>
				<Column field="updatedTime" header="Updated Time" style="min-width:12rem"></Column>
				<Column :exportable="false" style="min-width:24rem">
					<template #body="slotProps">
						<span class="p-buttonset">
							<Button label="Edit" icon="pi pi-pencil" @click="updateDomain(slotProps.data)" />
							<Button label="Delete" icon="pi pi-trash" severity="danger"
								@click="deleteDomain(slotProps.data)" />
						</span>
					</template>
				</Column>
			</DataTable>
		</div>

		<Toast />

		<Dialog v-model:visible="updateDomainDialog" :style="{ width: '450px' }" header="Update Domain" :modal="true"
			class="p-fluid">
			<div class="card flex flex-wrap justify-content-center gap-3">
				<div v-for="role of roleList" :key="role.id" class="flex align-items-center">
					<Checkbox v-model="checkedRole" :inputId="String(role.id)" name="role" :value="role.id" />
					<label :for="role.id">{{ role.roleName }}</label>
				</div>
			</div>
			<div class="field">
				<label for="point">Points</label>
				<InputText id="point" v-model.trim="pointIn" />
			</div>
			<div class="field">
				<label for="comment">Domain Introduction</label>
				<Textarea id="comment" v-model.trim="commentIn" rows="5" cols="30" />
			</div>
			<template #footer>
				<Button text label="Cancel" icon="pi pi-times" @click="hideDialog" />
				<Button text label="Confirm" icon="pi pi-check" @click="confirmUpdateDomain" />
			</template>
		</Dialog>

		<Dialog v-model:visible="addDomainDialog" :style="{ width: '450px' }" header="Add Domain" :modal="true"
			class="p-fluid">
			<div class="field">
				<label for="dns">DNS Resolution Platform</label>
				<Dropdown id="dns" v-model="selectedDNS" :options="DNS" optionLabel="dns"
					placeholder="Select DNS Resolution Platform" class="w-full" autofocus />
				<Button label="DNS Configuration" icon="pi pi-search" @click="getDomainListFromDNS(selectedDNS.dns)"
					class="mt-2" />
			</div>
			<div class="field">
				<label for="domain">Select Domain</label>
				<Dropdown id="domain" v-model="selectedDomain" :options="domainsFromDNS" optionLabel="name"
					placeholder="Select Domain" class="w-full" />
			</div>
			<div class="field">
				<label for="domain">Assign Roles</label>
				<div class="card flex flex-wrap justify-content-center gap-3">
					<div v-for="role of roleList" :key="role.id" class="flex align-items-center">
						<Checkbox v-model="checkedRole" :inputId="String(role.id)" name="role" :value="role.id" />
						<label :for="role.id">{{ role.roleName }}</label>
					</div>
				</div>
			</div>
			<div class="field">
				<label for="point">Points</label>
				<InputText id="point" v-model.trim="pointIn" />
			</div>
			<div class="field">
				<label for="comment">Domain Introduction</label>
				<Textarea id="comment" v-model.trim="commentIn" rows="5" cols="30" />
			</div>
			<template #footer>
				<Button text label="Cancel" icon="pi pi-times" @click="hideDialog" />
				<Button text label="Confirm" icon="pi pi-check" @click="confirmAddDomain" />
			</template>
		</Dialog>

		<Dialog v-model:visible="deleteDomainDialog" modal header="Delete Domain" :style="{ width: '450px' }">
			<p>
				Once confirmed, all records of this domain will be deleted. Are you sure you want to delete this domain?
			</p>
			<template #footer>
			<Button label="Cancel" icon="pi pi-times" @click="hideDialog" text />
			<Button label="Confirm" icon="pi pi-check" @click="confirmDeleteDomain" autofocus />
		</template>
	</Dialog>
</div></template>
