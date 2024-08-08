<script setup>
import { ref, onMounted } from 'vue'
import { FilterMatchMode } from 'primevue/api'
import axios from '@/axios/axios'
import { useToast } from "primevue/usetoast"

const users = ref()
const roles = ref()
const selectedRole = ref()
const selectAction = ref()
const actions = ref([
	{ name: '增加' },
	{ name: '扣除' }
])
const balanceIn = ref()
const remarkIn = ref()
const userDialog = ref(false)
const deleteUserDialog = ref(false)
const pointDialog = ref(false)
const user = ref({})
const passwordIn = ref('')
const toast = useToast()

const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

onMounted(() => {
    getUserList()
	getRoleList()
})

//获取角色列表
const getRoleList = () => {
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
//获取用户列表
const getUserList = () => {
	axios.get('/user/list', {
        params: {
            page: 1,
            pageSize: 10
        }
    }).then(function(response) {
		users.value = response.data.data.records
    }).catch(function(error) {
        console.log(error)
    })
}
//修改用户信息
const edit = (id, password, roleId) => {
	axios.put('/user/update', {
		id: id,
		password: password,
		roleId: roleId
    }).then(function(response) {
		if (response.data.code === 200) {
			userDialog.value = false
			toast.add({severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000})
		} else {
			console.log(response.data)
			toast.add({severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000})
		}
		getUserList()
    }).catch(function(error) {
        console.log(error)
    })
}
//封禁
const lock = (id) => {
	axios.get('/user/lock', {
		params: {
			id: id
		}
	}).then(function(response) {
		if (response.data.code === 200)
			toast.add({severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000})
		else {
			console.log(response.data)
			toast.add({severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000})
		}
		getUserList()
    }).catch(function(error) {
        console.log(error)
    })
}
//注销
const remove = (id) => {
	axios.delete('/user/delete/' + id)
		.then(function(response) {
			if (response.data.code === 200){
	            deleteUserDialog.value = false
				toast.add({severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000})
	        } else {
				console.log(response.data)
				toast.add({severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000})
			}
			getUserList()
	    }).catch(function(error) {
			console.log(error)
	    })
}
//修改积分
const editPoint = (userId, action, balance, remark) => {
	axios.post('/point_record/edit', {
		userId: userId,
		action: action,
		balance: balance,
		remark: remark
	}).then(function(response) {
		if (response.data.code === 200) {
			pointDialog.value = false
			toast.add({severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000})
		} else {
			console.log(response.data)
			toast.add({severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000})
		}
		getUserList()
	}).catch(function(error) {
		console.log(error)
	})
}

//隐藏Dialog
const hideDialog = () => {
	user.value = {}
    userDialog.value = false
	pointDialog.value = false
}
//打开更新用户Dialog并传参
const editUser = (prod) => {
    user.value = prod
	selectedRole.value = user.value.roleId
    userDialog.value = true
}
//确认注销用户Dialog
const deleteUser = (prod) => {
    user.value = prod
    deleteUserDialog.value = true
}
//打开更新积分Dialog并传参
const editPointDialog = (prod) => {
	user.value = prod
	pointDialog.value = true
}
//提交确认注销用户请求
const confirmDeleteUser = () => {
	const id = user.value.id
	remove(id)
    user.value = {}
}
//提交确认更新积分请求
const confirmUpdatePoint = () => {
	const userId = user.value.id
	if (selectAction.value == null) {
		return toast.add({severity: 'error', summary: 'ERROR', detail: '积分操作不能为空', life: 3000})
	}
	const action = selectAction.value.name
	const balance = balanceIn.value
	const remark = remarkIn.value
	editPoint(userId, action, balance, remark)
	user.value = {}
}
//提交确认更新用户信息请求
const confirmUpdateUser = () => {
	const id = user.value.id
	const password = passwordIn.value
	if (selectedRole.value == null) {
		return toast.add({severity: 'error', summary: 'ERROR', detail: '角色不能为空', life: 3000})
	}
	const roleId = selectedRole.value.id
	edit(id, password, roleId)
}
//封禁用户
const lockUser = (prod) => {
	user.value = prod
	const id = user.value.id
	lock(id)
}
</script>

<template>
    <div>
        <div class="card">
            <Toolbar class="mb-4">
                <template #start>
	                <h4 class="m-0">用户列表</h4>
                </template>
            </Toolbar>

            <DataTable showGridlines :value="users" dataKey="id" columnResizeMode="fit" :paginator="true" :rows="10" :filters="filters"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" :rowsPerPageOptions="[5,10,25]"
                currentPageReportTemplate="第 {first} 页，本页 {last} 条记录，共 {totalRecords} 条记录">
                <template #header>
                    <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
						<span class="p-input-icon-left">
                            <i class="pi pi-search" />
                            <InputText v-model="filters['global'].value" placeholder="搜索" />
                        </span>
					</div>
                </template>
	            <Column field="id" header="ID" style="min-width: 8rem"></Column>
                <Column field="email" header="邮箱" style="min-width: 8rem"></Column>
	            <Column field="phone" header="手机号码" style="min-width: 8rem"></Column>
	            <Column field="point" header="积分" style="min-width: 8rem">
		            <template #body="slotProps">
			            <Button :label=String(slotProps.data.point) outlined style="width: 6rem" @click="editPointDialog(slotProps.data)" />
		            </template>
	            </Column>
	            <Column field="roleId" header="角色" style="min-width: 8rem"></Column>
	            <Column field="status" header="账号状态" style="min-width: 8rem">
		            <template #body="slotProps">
			            <Tag :severity="slotProps.data.status === 1 ? 'success' : 'danger'" :value="slotProps.data.status === 1 ? '正常' : '已封禁'" />
		            </template>
	            </Column>
	            <Column field="loginTime" header="最近登陆时间" style="min-width:12rem"></Column>
	            <Column field="createdTime" header="注册时间" style="min-width:12rem"></Column>
	            <Column field="updatedTime" header="更新时间" style="min-width:12rem"></Column>
                <Column :exportable="false" style="min-width:24rem">
                    <template #body="slotProps">
	                    <span class="p-buttonset">
		                    <Button label="编辑" icon="pi pi-pencil" @click="editUser(slotProps.data)" />
		                    <Button :label="slotProps.data.status === 1 ? '封禁' : '解除封禁'" icon="pi pi-ban" severity="warning" @click="lockUser(slotProps.data)" />
		                    <Button label="注销" icon="pi pi-trash" severity="danger" @click="deleteUser(slotProps.data)" />
	                    </span>
                    </template>
                </Column>
            </DataTable>
        </div>

	    <Toast />

        <Dialog v-model:visible="userDialog" :style="{width: '450px'}" header="更新用户" :modal="true" class="p-fluid">
	        <div class="field">
                <label for="password">密码</label>
                <InputText id="password" v-model.trim="passwordIn" />
            </div>
	        <div class="field">
                <label for="role">角色</label>
		        <Dropdown id="role" v-model="selectedRole" :options="roles" optionLabel="roleName" placeholder="选择角色" class="w-full" />
            </div>
	        <template #footer>
		        <Button text label="取消" icon="pi pi-times" @click="hideDialog" />
                <Button text type="submit" label="确认" icon="pi pi-check" @click="confirmUpdateUser" />
	        </template>
        </Dialog>

	    <Dialog v-model:visible="pointDialog" :style="{width: '450px'}" header="更新积分" :modal="true" class="p-fluid">
	        <div class="field">
                <label for="action">操作</label>
		        <Dropdown v-model="selectAction" :options="actions" optionLabel="name" placeholder="操作" class="w-full" autofocus />
            </div>
		    <div class="field">
                <label for="balance">积分</label>
                <InputText id="balance" v-model.trim="balanceIn" />
            </div>
		    <div class="field">
                <label for="remark">备注</label>
                <Textarea id="remark" v-model.trim="remarkIn" rows="5" cols="30" />
            </div>
	        <template #footer>
		        <Button text label="取消" icon="pi pi-times" @click="hideDialog" />
                <Button text label="确认" icon="pi pi-check" @click="confirmUpdatePoint" />
	        </template>
        </Dialog>

        <Dialog v-model:visible="deleteUserDialog" :style="{width: '500px'}" header="确认删除" :modal="true">
            <div class="confirmation-content">
                <i class="pi pi-exclamation-triangle mr-3" style="font-size: 2rem" />
                <span>该角色删除后，域名记录也将一起删除，请确认是否继续操作</span>
            </div>
            <template #footer>
                <Button label="取消" icon="pi pi-times" text @click="deleteUserDialog = false"/>
                <Button label="确认" icon="pi pi-check" text @click="confirmDeleteUser" />
            </template>
        </Dialog>
	</div>
</template>