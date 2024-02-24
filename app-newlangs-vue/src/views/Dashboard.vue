<style scoped>
.button-group {
  display: flex;
  gap: 15px;
  /* 调整间距大小 */
}
</style>
<template>
  <Toast /> 
  <div class="grid">
    <div class="col-12 xl:col-6">
      <div class="card">
        <div class="flex align-items-center justify-content-between mb-4">
          <h4>温馨提示</h4>
        </div>
        <span class="block text-600 font-medium mb-3">🔔使用提示</span>
        <h5 class="text-600">
          🎉厚浪域名解析永久免费，欢迎使用~
          <br>
          🌟收藏首页 <a href="https://langs.ink" target="_blank">langs.ink</a> 不迷路！
          <br>
          🎉浪·盘 全新上线，注册即领存储空间 <a href="https://pan.chuangzhi.ink/" target="_blank">立即领取</a>!
          <br>
        </h5>
        <br />
        <span class="block text-600 font-medium mb-3">🏆常用功能</span>
        <h6 class="button-group">
          <Button label="📅每日签到" @click="signIn" />
          <Button
            onclick="window.open('http://qm.qq.com/cgi-bin/qm/qr?_wv=1027&k=2vThTeSkd03bFCVs9VzM5kfVKedfxTzv&authKey=oPyHIZDxhtYKegn317FSdKLWIBLVdrMkMkO7U3oJJY3fE%2BxcW9RBiIcjfEj%2BTaUn&noverify=0&group_code=605943548')">📢入群领福利</Button>
          <Button onclick="window.open('https://langsteam.feishu.cn/share/base/form/shrcn708g5ZIScs87gvWo0ms8yc')">新功能调研</Button>
          <br>
        </h6>
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
        toast.add({ severity: 'success', summary: '签到成功', detail: '积分已发放到账', life: 3000 })
      } else {
        console.log(response.data)
        toast.add({ severity: 'error', summary: '签到失败', detail: '今日已签到，明天再来吧~', life: 3000 })
      }
    }).catch(function (error) {
      console.log(error)
    })
}
</script>