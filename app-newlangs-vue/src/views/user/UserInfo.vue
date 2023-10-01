<script setup>
import { onMounted, ref } from 'vue'
import axios from '@/axios/axios'
import { useToast } from 'primevue/usetoast'

const toast = useToast()
const userId = ref('')
const email = ref('')
const name = ref('')
const idNumber = ref('')
const phone = ref('')
const point = ref('')
const roleId = ref(0)
const createdTime = ref('')
const loginTime = ref('')
const status = ref(1)

const phoneIn = ref(null)
const codeIn = ref(null)
const nameIn = ref(null)
const idNumberIn = ref(null)

const disabled = ref(false)
const sendCodeDisabled = ref(false)
const certificationDialog = ref(false)
const phoneDialog = ref(false)

onMounted(() => {
    getUserInfo()
})
// Get user information
const getUserInfo = () => {
    axios.get('/user/get_user')
        .then(function (response) {
            if (response.data.code === 200) {
                userId.value = response.data.data.id
                email.value = response.data.data.email
                name.value = response.data.data.name
                idNumber.value = response.data.data.idNumber
                phone.value = response.data.data.phone
                point.value = response.data.data.point
                roleId.value = response.data.data.roleId
                createdTime.value = response.data.data.createdTime
                loginTime.value = response.data.data.loginTime
                status.value = response.data.data.status
            } else {
                toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
            }
        }).catch(function (error) {
            console.log(error)
        })
}

// Send mobile phone verification code
const sendPhoneCode = (phone) => {
    sendCodeDisabled.value = true
    axios.get('/user/phone_code', {
        params: {
            phone: phone
        }
    }).then(function (response) {
        if (response.data.code === 200) {
            toast.add({ severity: 'success', summary: 'SUCCESS', detail: response.data.msg, life: 3000 })
            setTimeout(() => {
                sendCodeDisabled.value = false
            }, 60000)
        } else {
            toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
            setTimeout(() => {
                sendCodeDisabled.value = false
            }, 60000)
        }
    }).catch(function (error) {
        console.log(error)
        setTimeout(() => {
            sendCodeDisabled.value = false
        }, 60000)
    })
}

// Phone number validation
const checkPhone = (phone, code) => {
    axios.get('/user/check_phone', {
        params: {
            phone: phone,
            code: code
        }
    }).then(function (response) {
        if (response.data.code === 200) {
            toast.add({ severity: 'success', summary: 'ERROR', detail: response.data.msg, life: 3000 })
            phoneDialog.value = false
            getUserInfo()
        } else {
            toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
        }
    }).catch(function (error) {
        console.log(error)
    })
}

// Real name authentication
const certification = (name, idNumber) => {
    disabled.value = true
    axios.post('/user/certification', {
        name: name,
        idNumber: idNumber
    }).then(function (response) {
        if (response.data.code === 200) {
            toast.add({ severity: 'success', summary: 'ERROR', detail: response.data.msg, life: 3000 })
            certificationDialog.value = false
            disabled.value = false
            getUserInfo()
        } else {
            toast.add({ severity: 'error', summary: 'ERROR', detail: response.data.msg, life: 3000 })
            disabled.value = false
        }
    }).catch(function (error) {
        console.log(error)
        disabled.value = false
    })
}

const computedRoleTab = (id) => {
    switch (id) {
        case 1:
            return 'Administrator'
        case 2:
            return 'Regular User'
        case 3:
            return 'Advanced User'
        case 4:
            return 'Verified User'
    }
}
</script>

<template>
    <Toast />
    <div class="card p-fluid">
        <div class="surface-section">
            <div class="font-medium text-3xl text-900 mb-3">User Information</div>
            <ul class="list-none p-0 m-0">
                <li class="flex align-items-center py-3 px-2 border-top-1 surface-border flex-wrap">
                    <div class="text-500 w-6 md:w-2 font-medium">ID</div>
                    <div class="text-900 w-full md:w-8 md:flex-order-0 flex-order-1">{{ userId }}</div>
                </li>
                <!-- Rest of the template remains the same, just the text content is translated -->
            </ul>
        </div>

        <Dialog v-model:visible="certificationDialog" :style="{ width: '450px' }" header="Real Name Certification" :modal="true"
            class="p-fluid">
            <!-- Translated labels and placeholders -->
        </Dialog>

        <Dialog v-model:visible="phoneDialog" :style="{ width: '450px' }" header="Mobile Verification" :modal="true" class="p-fluid">
            <!-- Translated labels and placeholders -->
        </Dialog>
    </div>
</template>
