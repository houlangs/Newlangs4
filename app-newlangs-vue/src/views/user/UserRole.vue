<script setup>
import { ref, onMounted } from 'vue'
import { FilterMatchMode } from 'primevue/api'
import axios from '@/axios/axios'
import { useToast } from "primevue/usetoast"

const roles = ref()
const roleDialog = ref(false)
const deleteRoleDialog = ref(false)
let role = ref()
const roleTab = ref()
const roleName = ref()
const selectedRoles = ref()
const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
})
const roleOfUsers = ref()
const inputNewRoleId = ref()
const toast = useToast()

onMounted(() => {
    getList()
})

// Get the list of roles
const getList = () => {
	axios.get('/role/list', {
        params: {
            page: 1,
            pageSize: 10
        }
    }).then(function(response) {
		roles.value = response.data.data.records
    }).catch(function(error) {
        console.log(error)
    })
}
// Edit
const edit = (id, tab, name) => {
	axios.post('/role/edit', {
		id: id,
        roleTab: tab,
		roleName: name
    }).then(function(response) {
		if (response.data.code === 200) {
			roleDialog.value = false
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
// Add
const add = (tab, name) => {
	axios.post('/role/add', {
        roleTab: tab,
		roleName: name
    }).then(function(response) {
		if (response.data.code === 200) {
			roleDialog.value = false
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
// Delete
const remove = (id, newRoleId) => {
	axios.post('/role/delete', {
        id: id,
		newRoleId: newRoleId
    }).then(function(response) {
		if (response.data.code === 200) {
			roleDialog.value = false
			deleteRoleDialog.value = false
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
// Get the number of users belonging to a role
const count = (id) => {
	axios.get('/role/get_count', {
		params: {
			id: id
		}
	}).then(function(response) {
		roleOfUsers.value = response.data.data
    }).catch(function(error) {
        console.log(error)
    })
}

// Open Dialog
// Initialize variables
const openNew = () => {
    role.value = {}
    roleDialog.value = true
}
// Hide Dialog
const hideDialog = () => {
    roleDialog.value = false
	role.value = {}
}
// Open the update role Dialog and pass parameters
const editRole = (prod) => {
    role.value = prod
	roleTab.value = role.value.roleTab
	roleName.value = role.value.roleName
    roleDialog.value = true
}
// Confirm Delete Role Dialog
const confirmDeleteRole = (prod) => {
    role.value = prod
	count(role.value.id)
    deleteRoleDialog.value = true
}
// Submit the request to delete a role
const deleteRole = () => {
	const id = role.value.id
    const newRoleId = inputNewRoleId.value

	if (roleOfUsers.value == 0) {
		remove(id)
	} else {
		remove(id, newRoleId)
	}
    role.value = {}
}
// Submit the request to update or add role information
const addSubmit = () => {
	const id = role.value.id
    const tab = roleTab.value
    const name = roleName.value
	// If there is an id, then edit
    if (role.value.id != null) {
	    edit(id, tab, name)
    } else {
		// Otherwise, add
	    add(tab, name)
    }
}
</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-4">
                <template #start>
	                <h4 class="m-0">Role List</h4>
                </template>
            </Toolbar>

            <DataTable showGridlines :value="roles" v-model:selection="selectedRoles" dataKey="id" columnResizeMode="fit"
                :paginator="true" :rows="10" :filters="filters"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" :rowsPerPageOptions="[5,10,25]"
                currentPageReportTemplate="Page {first} of {last}, Total {totalRecords} records">
                <template #header>
                    <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
						<span class="p-input-icon-left">
                            <i class="pi pi-search" />
                            <InputText v-model="filters['global'].value" placeholder="Search" />
                        </span>
	                    <Button label="Add" icon="pi pi-plus" severity="success" class="mr-2" @click="openNew" />
					</div>
                </template>

                <Column field="id" header="ID" style="min-width: 2rem"></Column>
                <Column field="roleName" header="Role Name" style="min-width: 8rem"></Column>
                <Column :exportable="false" style="min-width: 8rem">
                    <template #body="slotProps">
	                    <span class="p-buttonset">
		                    <Button label="Edit" icon="pi pi-pencil" @click="editRole(slotProps.data)" />
                            <Button label="Delete" icon="pi pi-trash" severity="danger" @click="confirmDeleteRole(slotProps.data)" />
	                    </span>
                    </template>
                </Column>
            </DataTable>
        </div>

	    <Toast />

        <Dialog v-model:visible="roleDialog" :style="{width: '450px'}" header="Edit Role" :modal="true" class="p-fluid">
            <div class="field">
                <label for="name">Role Name</label>
                <InputText id="name"
                           v-model.trim="roleName"
                           autofocus
                           placeholder="Regular User"
                />
            </div>
            <template #footer>
	            <Button text label="Cancel" icon="pi pi-times" @click="hideDialog" />
                <Button text label="Confirm" icon="pi pi-check" @click="addSubmit" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteRoleDialog" :style="{width: '450px'}" header="Confirm Deletion" :modal="true">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
                <span>This role is associated with <b>{{ roleOfUsers }}</b> users. Please reassign them to a new role.</span>
				<span class="p-float-label mt-4">
				    <InputNumber id="number-input" v-model="inputNewRoleId" :disabled="roleOfUsers == 0" />
				    <label for="number-input">Role ID</label>
				</span>
            </div>
            <template #footer>
                <Button label="Cancel" icon="pi pi-times" @click="deleteRoleDialog = false"/>
                <Button label="Confirm" icon="pi pi-check" @click="deleteRole" />
            </template>
        </Dialog>
	</div>
</template>
