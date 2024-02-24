<template>
    <div>
        <!-- 其他Vue组件内容 -->
        <div class="iframe-container">
            <iframe id="myIframe" src="https://www.rainyun.com/rvh" width="100%" height="4000"
                frameborder="0"></iframe>
            <div class="overlay" @click="redirectToRainyun"></div>
        </div>
    </div>
</template>
  
<script>
export default {
    methods: {
        redirectToRainyun() {
            // 在用户点击时跳转到https://www.rainyun.com/?ref=ins/
            window.location.href = 'https://www.rainyun.com/ins_?s=app-langs-rvh';
        },
    },
};
</script>
  
<style>
.iframe-container {
    position: relative;
}

.overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: transparent;
    cursor: pointer;
    z-index: 1;
    /* 确保透明层在iframe上层 */
    opacity: 0;
    /* 使透明层不可见，可以根据需要调整透明度 */
    pointer-events: auto;
    /* 允许鼠标事件透传到iframe内部 */
}
</style>
  