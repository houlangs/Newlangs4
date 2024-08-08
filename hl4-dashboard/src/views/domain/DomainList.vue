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
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});

onMounted(() => {
    getDomainList()
	getRoleList()
	getDNS()
})

//获取角色列表
const getRoleList = () => {
	axios.get('/role/list', {
        params: {
            page: 1,
            pageSize: 10
        }
    }).then(function(response) {
		roleList.value = response.data.data.records
    }).catch(function(error) {
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
    }).then(function(response) {
		domains.value = response.data.data.records
    }).catch(function(error) {
        console.log(error)
    })
}
//更新域名信息
const update = (id, roleIds, point, comment) => {
	axios.put('/domain/update', {
		id: id,
        roleIds: roleIds,
		point: point,
		comment: comment
    }).then(function(response) {
		if (response.data.code === 200) {
			updateDomainDialog.value = false
			domain.value = {}
			toast.add({severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000})
		} else {
			console.log(response.data)
			toast.add({severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000})
		}
		getDomainList()
    }).catch(function(error) {
		getDomainList()
        console.log(error)
    })
}
//添加域名
const add = (dns, domainId, name, roleIds, point, comment) => {
	axios.post('/domain/add', {
		dns: dns,
		domainId: domainId,
		name: name,
		roleIds: roleIds,
		point: point,
		comment: comment
	}).then(function(response) {
		if (response.data.code === 200) {
			addDomainDialog.value = false
			domain.value = {}
			toast.add({severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000})
		} else {
			console.log(response.data)
			toast.add({severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000})
		}
		getDomainList()
    }).catch(function(error) {
		getDomainList()
        console.log(error)
    })
}
//删除域名
const remove = (id) => {
	axios.delete('/domain/delete/' + id)
		.then(function(response) {
			if (response.data.code === 200) {
				deleteDomainDialog.value = false
				domain.value = {}
				toast.add({severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000})
			} else {
				console.log(response.data)
				toast.add({severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000})
			}
			getDomainList()
	    }).catch(function(error) {
			getDomainList()
	        console.log(error)
	    })
}
//从DNS平台获取域名列表
const getDomainListFromDNS = (dns) => {
	axios.get('/domain/list_from_dns', {
		params: {
			dns: dns
		}
	}).then(function(response) {
		if (response.data.code === 200) {
			domainsFromDNS.value = response.data.data
			toast.add({severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000})
		} else {
			console.log(response.data)
			toast.add({severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000})
		}
    }).catch(function(error) {
        console.log(error)
    })
}
//获取DNS平台列表
const getDNS = () => {
	axios.get('/domain_config/list', {
		params: {
			page: 1,
			pageSize: 10
		}
	}).then(function(response) {
		if (response.data.code === 200) {
			DNS.value = response.data.data.records
		} else {
			console.log(response.data)
			toast.add({severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000})
		}
    }).catch(function(error) {
        console.log(error)
    })
}

//隐藏Dialog
const hideDialog = () => {
    updateDomainDialog.value = false
	addDomainDialog.value = false
	deleteDomainDialog.value = false
	domain.value = {}
}
//打开更新域名Dialog并传参
const updateDomain = (prod) => {
    domain.value = prod
	// checkedRole.value = domain.value.roleIds
	pointIn.value = domain.value.point
	commentIn.value = domain.value.comment
    updateDomainDialog.value = true
}
//确认删除域名Dialog
const deleteDomain = (prod) => {
    domain.value = prod
    deleteDomainDialog.value = true
}
//提交确认更新域名请求
const confirmUpdateDomain = () => {
	const id = domain.value.id
	let roleIds = null
	if (checkedRole.value == null) {
		return toast.add({severity: 'error', summary: 'ERROR', detail: '域名所属角色不能为空', life: 3000})
	} else {
		roleIds = String(checkedRole.value)
	}
	const point = pointIn.value
	const comment = commentIn.value
	update(id, roleIds, point, comment)
}
//提交确认添加域名请求
const confirmAddDomain = () => {
	const dns = selectedDNS.value.dns
	const domainId = selectedDomain.value.domainId
	const name = selectedDomain.value.name
	let roleIds
	if (checkedRole.value == null) {
		return toast.add({severity: 'error', summary: 'ERROR', detail: '域名所属角色不能为空', life: 3000})
	} else {
		roleIds = String(checkedRole.value)
	}
	const point = pointIn.value
	const comment = commentIn.value
	add(dns, domainId, name, roleIds, point, comment)
}
//提交确认删除域名请求
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
	                <h4 class="m-0">域名列表</h4>
                </template>
            </Toolbar>

            <DataTable showGridlines :value="domains" dataKey="id" columnResizeMode="fit"
                :paginator="true" :rows="10" :filters="filters"
                paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" :rowsPerPageOptions="[5,10,25]"
                currentPageReportTemplate="第 {first} 页，本页 {last} 条记录，共 {totalRecords} 条记录">
                <template #header>
                    <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
						<span class="p-input-icon-left">
                            <i class="pi pi-search" />
                            <InputText v-model="filters['global'].value" placeholder="搜索" />
                        </span>
	                    <Button label="添加" icon="pi pi-plus" severity="success" class="mr-2" @click="addDomainDialog = true" />
					</div>
                </template>

                <Column field="id" header="ID"></Column>
                <Column field="dns" header="DNS"></Column>
                <Column field="domainId" header="domainId"></Column>
	            <Column field="name" header="域名"></Column>
	            <Column field="roleIds" header="所属角色" style="min-width: 8rem"></Column>
	            <Column field="point" header="积分" style="min-width: 8rem"></Column>
	            <Column field="comment" header="介绍" style="min-width: 8rem"></Column>
	            <Column field="createdTime" header="创建时间" style="min-width:12rem"></Column>
	            <Column field="updatedTime" header="更新时间" style="min-width:12rem"></Column>
                <Column :exportable="false" style="min-width:24rem">
                    <template #body="slotProps">
	                    <span class="p-buttonset">
		                    <Button label="编辑" icon="pi pi-pencil" @click="updateDomain(slotProps.data)" />
		                    <Button label="删除" icon="pi pi-trash" severity="danger" @click="deleteDomain(slotProps.data)" />
	                    </span>
                    </template>
                </Column>
            </DataTable>
        </div>

	    <Toast />

        <Dialog v-model:visible="updateDomainDialog" :style="{width: '450px'}" header="更新用户" :modal="true" class="p-fluid">
            <div class="card flex flex-wrap justify-content-center gap-3">
	            <div v-for="role of roleList" :key="role.id" class="flex align-items-center">
	                <Checkbox v-model="checkedRole" :inputId="String(role.id)" name="role" :value="role.id" />
	                <label :for="role.id">{{ role.roleName }}</label>
	            </div>
            </div>
            <div class="field">
                <label for="point">积分</label>
                <InputText id="point" v-model.trim="pointIn" />
            </div>
	        <div class="field">
                <label for="comment">域名介绍</label>
                <Textarea id="comment" v-model.trim="commentIn" rows="5" cols="30" />
            </div>
            <template #footer>
	            <Button text label="取消" icon="pi pi-times" @click="hideDialog" />
                <Button text label="确认" icon="pi pi-check" @click="confirmUpdateDomain" />
            </template>
        </Dialog>

	    <Dialog v-model:visible="addDomainDialog" :style="{width: '450px'}" header="更新用户" :modal="true" class="p-fluid">
		    <div class="field">
                <label for="dns">DNS解析平台</label>
		        <Dropdown id="dns" v-model="selectedDNS" :options="DNS" optionLabel="dns" placeholder="请选择选择DNS解析平台" class="w-full" autofocus />
			    <Button label="解析配置" icon="pi pi-search" @click="getDomainListFromDNS(selectedDNS.dns)" class="mt-2" />
            </div>
		    <div class="field">
                <label for="domain">选择域名</label>
		        <Dropdown id="domain" v-model="selectedDomain" :options="domainsFromDNS" optionLabel="name" placeholder="请选择域名" class="w-full" />
            </div>
		    <div class="field">
                <label for="domain">分配角色</label>
		        <div class="card flex flex-wrap justify-content-center gap-3">
		            <div v-for="role of roleList" :key="role.id" class="flex align-items-center">
		                <Checkbox v-model="checkedRole" :inputId="String(role.id)" name="role" :value="role.id" />
		                <label :for="role.id">{{ role.roleName }}</label>
		            </div>
	            </div>
            </div>
            <div class="field">
                <label for="point">积分</label>
                <InputText id="point" v-model.trim="pointIn" />
            </div>
	        <div class="field">
                <label for="comment">域名介绍</label>
                <Textarea id="comment" v-model.trim="commentIn" rows="5" cols="30" />
            </div>
		    <template #footer>
	            <Button text label="取消" icon="pi pi-times" @click="hideDialog" />
                <Button text label="确认" icon="pi pi-check" @click="confirmAddDomain" />
            </template>
        </Dialog>

	    <Dialog v-model:visible="deleteDomainDialog" modal header="删除域名" :style="{ width: '450px' }">
		    <p>
		        确认后，该域名的所有记录都将被删除，确认删除该域名？
		    </p>
		    <template #footer>
		        <Button label="取消" icon="pi pi-times" @click="hideDialog" text />
		        <Button label="确认" icon="pi pi-check" @click="confirmDeleteDomain" autofocus />
		    </template>
		</Dialog>
	</div>
</template>