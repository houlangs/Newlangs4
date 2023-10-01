<template>
  <Toast />
  <div class="grid">
    <div class="col-12 xl:col-6">
      <div class="card">
        <div class="flex align-items-center justify-content-between mb-4">
          <h4>Important Notice</h4>
        </div>
        <span class="block text-600 font-medium mb-3">🌕 Mid-Autumn Festival Greetings</span>
        <h6>
          🎉 Dear users, the Mid-Autumn Festival is approaching!<br />
          🏮 Let's celebrate this moment of reunion under the full moon together.<br />
          🥮 On this special day, we thank you for your continuous support and companionship.<br />
          🌟 Houlang wishes you a happy and joyous Mid-Autumn Festival!
        </h6>
        <br />
        <span class="block text-600 font-medium mb-3">🏆 Featured Functions</span>
        <h6>
          <Button label="📅 Daily Check-In: Sign in for rewards, double points for Mid-Autumn!" @click="signIn" />
          <br>
          <br>
          <Button
            onclick="window.open('http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=73T48TWm3nzVt5jSF6SyDVY2AoyZfMSu&authKey=As1WSDrOpm3nmJNkH6q1OQ3r%2FkyOD24hR7OBXDs0MkfQD5be1hTQo2HU8LRUNn57&noverify=0&group_code=230832864')">📢
            Join Our QQ Group: Connect and share your Mid-Autumn joy with us</Button>
          <br>
          <br>
          <Button onclick="window.open('https://forum.houlangs.com/d/6-hou-lang-geng-duo-lian-jie')">🔗 Links: Explore
            more interesting resource links</Button>
          <br>
        </h6>
        <br />
        <span class="block text-600 font-medium mb-3">🌝 Verification Method</span>
        <h6>Click the top right corner icon -> User Info -> Real Name Verification</h6>
      </div>
    </div>
  </div>
</template>

<script setup>
import axios from '@/axios/axios'
import { useToast } from "primevue/usetoast"

const toast = useToast()

const signIn = () => {
  axios.get('/user/sign_in')
    .then(function (response) {
      if (response.data.code === 200) {
        toast.add({ severity: 'Success', summary: 'Sign-In Successful', detail: 'You have successfully signed in!', life: 3000 })
      } else {
        console.log(response.data)
        toast.add({ severity: 'error', summary: 'Sign-In Failed', detail: 'You have already signed in today. Please try again tomorrow.', life: 3000 })
      }
    }).catch(function (error) {
      console.log(error)
    })
}
</script>
