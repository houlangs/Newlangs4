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

//获取角色列表
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
//修改
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
//新增
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
//删除
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
//获取属于该角色的用户数
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

//打开Dialog
//变量初始化
const openNew = () => {
    role.value = {}
    roleDialog.value = true
}
//隐藏Dialog
const hideDialog = () => {
    roleDialog.value = false
	role.value = {}
}
//打开更新角色Dialog并传参
const editRole = (prod) => {
    role.value = prod
	roleTab.value = role.value.roleTab
	roleName.value = role.value.roleName
    roleDialog.value = true
}
//确认删除角色Dialog
const confirmDeleteRole = (prod) => {
    role.value = prod
	count(role.value.id)
    deleteRoleDialog.value = true
}
//提交确认删除角色请求
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
//提交确认更新or添加角色信息请求
const addSubmit = () => {
	const id = role.value.id
    const tab = roleTab.value
    const name = roleName.value
	//如果有id就修改
    if (role.value.id != null) {
	    edit(id, tab, name)
    } else {
		//否则新增
	    add(tab, name)
    }
}
</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-4">
                <template #start>
	                <h4 class="m-0">角色列表</h4>
                </template>
            </Toolbar>

            <DataTable showGridlines :value="roles" v-model:selection="selectedRoles" dataKey="id" columnResizeMode="fit"
                :paginator="true" :rows="10" :filters="filters"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" :rowsPerPageOptions="[5,10,25]"
                currentPageReportTemplate="第 {first} 页，本页 {last} 条记录，共 {totalRecords} 条记录">
                <template #header>
                    <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
						<span class="p-input-icon-left">
                            <i class="pi pi-search" />
                            <InputText v-model="filters['global'].value" placeholder="搜索" />
                        </span>
	                    <Button label="添加" icon="pi pi-plus" severity="success" class="mr-2" @click="openNew" />
					</div>
                </template>

                <Column field="id" header="ID" style="min-width: 2rem"></Column>
                <Column field="roleName" header="角色名称" style="min-width: 8rem"></Column>
                <Column :exportable="false" style="min-width: 8rem">
                    <template #body="slotProps">
	                    <span class="p-buttonset">
		                    <Button label="编辑" icon="pi pi-pencil" @click="editRole(slotProps.data)" />
                            <Button label="删除" icon="pi pi-trash" severity="danger" @click="confirmDeleteRole(slotProps.data)" />
	                    </span>
                    </template>
                </Column>
            </DataTable>
        </div>

	    <Toast />

        <Dialog v-model:visible="roleDialog" :style="{width: '450px'}" header="编辑角色" :modal="true" class="p-fluid">
            <div class="field">
                <label for="name">角色名称</label>
                <InputText id="name"
                           v-model.trim="roleName"
                           autofocus
                           placeholder="普通用户"
                />
            </div>
            <template #footer>
	            <Button text label="取消" icon="pi pi-times" @click="hideDialog" />
                <Button text label="确认" icon="pi pi-check" @click="addSubmit" />
            </template>
        </Dialog>

        <Dialog v-model:visible="deleteRoleDialog" :style="{width: '450px'}" header="确认删除" :modal="true">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
                <span>该角色隶属于<b>{{ roleOfUsers }}</b>位用户，请重新为他们分配新的角色</span>
				<span class="p-float-label mt-4">
				    <InputNumber id="number-input" v-model="inputNewRoleId" :disabled="roleOfUsers == 0" />
				    <label for="number-input">角色ID</label>
				</span>
            </div>
            <template #footer>
                <Button label="取消" icon="pi pi-times" @click="deleteRoleDialog = false"/>
                <Button label="确认" icon="pi pi-check" @click="deleteRole" />
            </template>
        </Dialog>
	</div>
</template>