<script setup>
import { ref, onMounted } from 'vue'
import { FilterMatchMode } from 'primevue/api'
import axios from '@/axios/axios'
import { useToast } from "primevue/usetoast"

const pointRecords = ref()
const selectedUsers = ref()
const filters = ref({
    'global': {value: null, matchMode: FilterMatchMode.CONTAINS},
});
const toast = useToast()

onMounted(() => {
    getList()
})

// Get point records list
const getList = () => {
    axios.get('/point_record/list', {
        params: {
            page: 1,
            pageSize: 10
        }
    }).then(function(response) {
        pointRecords.value = response.data.data.records
        console.log(response.data)
    }).catch(function(error) {
        toast.add({severity: 'error', summary: 'ERROR', detail: error, life: 3000})
        console.log(error)
    })
}
</script>

<template>
    <div class="card">
        <Toolbar class="mb-4">
            <template #start>
                <h4 class="m-0">Point Records</h4>
            </template>
        </Toolbar>

        <DataTable showGridlines :value="pointRecords" v-model:selection="selectedUsers" dataKey="id" columnResizeMode="fit"
            :paginator="true" :rows="10" :filters="filters"
            paginatorTemplate="FirstPageLink PrevPageLink PageLinks NextPageLink LastPageLink CurrentPageReport RowsPerPageDropdown" :rowsPerPageOptions="[5,10,25]"
            currentPageReportTemplate="Page {first} of {last}, Total {totalRecords} records">
            <template #header>
                <div class="flex flex-wrap gap-2 align-items-center justify-content-between">
                    <span class="p-input-icon-left">
                        <i class="pi pi-search" />
                        <InputText v-model="filters['global'].value" placeholder="Search" />
                    </span>
                </div>
            </template>

            <Column field="userId" header="User ID"></Column>
            <Column field="action" header="Action" style="min-width: 5rem">
                <template #body="slotProps">
                    <Tag :severity="slotProps.data.action === 'Increase' ? 'success' : (slotProps.data.action === 'Consume' ? '' : 'danger')" :value="slotProps.data.action" />
                </template>
            </Column>
            <Column field="balance" header="Balance" style="min-width: 4rem"></Column>
            <Column field="rest" header="Remaining Quota" style="min-width: 8rem"></Column>
            <Column field="remark" header="Remark" style="min-width: 8rem"></Column>
            <Column field="createdTime" header="Creation Time" style="min-width:12rem"></Column>
        </DataTable>
    </div>
</template>
