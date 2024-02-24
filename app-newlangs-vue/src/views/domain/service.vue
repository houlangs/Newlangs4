<template>
    <div>
        <!-- 其他Vue组件内容 -->
        <div class="iframe-container">
            <iframe id="myIframe" src="https://www.rainyun.com/rcs" width="100%" height="5000"
                frameborder="0"></iframe>
            <div class="overlay" @click="redirectToRainyun"></div>
        </div>
    </div>
</template>
  
<script>
export default {
  mounted() {
    this.redirectToRainyun();
  },
  methods: {
    redirectToRainyun() {
      // 打开页面时跳转https://www.rainyun.com/?ref=ins/
      window.open('https://www.rainyun.com/ins_?s=app-langs-rcs', '_blank');
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
  