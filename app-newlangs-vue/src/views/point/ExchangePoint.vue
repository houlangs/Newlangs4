<script setup>
import axios from '@/axios/axios'
import { ref } from 'vue'
import { useToast } from 'primevue/usetoast'

const toast = useToast()
const pointCodeIn = ref('')

const exchangePoint = () => {
    if (pointCodeIn.value === '') {
        toast.add({ severity: 'error', summary: 'Input Error', detail: 'Please enter a valid redemption code', life: 3000 })
        return
    }

    axios.get('/point_record/exchangePoint', {
        params: {
            pointCode: pointCodeIn.value
        }
    }).then(function (response) {
        if (response.data.code === 200) {
            toast.add({ severity: 'success', summary: 'Redemption Successful', detail: response.data.msg, life: 3000 })
        } else {
            console.log(response.data)
            toast.add({ severity: 'error', summary: 'Redemption Failed', detail: 'You have already redeemed this code. Please wait for the next opportunity.', life: 3000 })
        }
    }).catch(function (error) {
        console.log(error)
    })
}
</script>

<template>
    <Toast />
    <div class="card p-fluid">
        <div class="field grid">
            <label for="from" class="col-12 mb-2 md:col-2 md:mb-0">Redemption Code</label>
            <div class="col-12 md:col-10">
                <InputText id="from" type="text" v-model.trim="pointCodeIn" />
            </div>
            <Button label="Redeem Now" class="mt-4" @click="exchangePoint" />
        </div>
    </div>
    <div>
        <div class="card">
            <div class="flex align-items-center justify-content-between mb-4">
                <h4>🤔 How to Get Redemption Codes/Redeem Points?</h4>
            </div>
            <p>
                🌟 Join Q Group: 230832864, the Q Group manager will provide you with redemption codes!<br><br>
                🌦️ Visit Rainyun Redemption: Go to <a href="https://rainyun.ink" target="_blank">Rainyun</a>, register an account -> Points Center -> Points Mall to redeem!<br><br>
                🥳 Promote the Website: Post a promotional video on video platforms and mention @administrators in the group to receive points!<br><br>
                📢 Note: Redemption codes obtained from Rainyun have a validity period of 5 minutes, so please use them promptly!
            </p>
        </div>
    </div>
</template>
