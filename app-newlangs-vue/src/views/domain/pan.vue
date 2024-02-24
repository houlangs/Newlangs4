<template>
    <div class="pan">
        <h1>浪·盘</h1>
        <p>由厚浪提供的不限速云存储服务<br>新用户注册即送<span class="blue">20GB</span>存储</p>
        <button @click="goToCloudDrive">立刻启动</button>
    </div>
</template>

<script>
export default {
    methods: {
        goToCloudDrive() {
            window.open('https://pan.chuangzhi.ink/')
        }
    }
}
</script>

<style scoped>
@font-face {
    font-family: 'CustomFont';
    src: url('/font/r0LsuCwvW7jX.woff') format('woff');
}

.pan {
    text-align: center;
    margin-top: 50px;
}

.blue {
    color: #2d5af6;
}

h1 {
    font-family: 'CustomFont', sans-serif;
    font-size: 40px;
    color: #2d5af6;
}

p {
    font-size: 25px;
    color: #666;
    margin-bottom: 20px;
    font-family: 'CustomFont', sans-serif;
}

button {
    padding: 10px 20px;
    font-size: 16px;
    color: #fff;
    background-color: #2d5af6;
    border: none;
    border-radius: 4px;
    cursor: pointer;
}

button:hover {
    background-color: #0056b3;
}
</style>
