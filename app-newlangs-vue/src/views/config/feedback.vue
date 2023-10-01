<template>
    <div>
        <div class="iframe-container">
            <iframe ref="myIframe" src="https://langsteam.feishu.cn/share/base/form/shrcnfx5BilO3R6mLpnxRUf01ig" width="100%" frameborder="0"></iframe>
        </div>
    </div>
</template>

<script>
export default {
    mounted() {
        // 获取窗口的高度
        const windowHeight = window.innerHeight;

        // 计算 iframe 的高度，比窗口高度小 20px
        const iframeHeight = windowHeight - 130;

        // 设置 iframe 的高度
        this.$refs.myIframe.style.height = `${iframeHeight}px`;

        // 监听窗口大小变化，以便在窗口大小变化时更新 iframe 高度
        window.addEventListener('resize', () => {
            const newWindowHeight = window.innerHeight;
            const newIframeHeight = newWindowHeight - 20;
            this.$refs.myIframe.style.height = `${newIframeHeight}px`;
        });
    }
};
</script>

<style>
.iframe-container {
    border-radius: 10px; /* 设置圆角半径 */
    border: 1px solid #ccc; /* 设置边框样式，可以根据需要调整 */
    overflow: hidden; /* 隐藏超出边框的部分内容 */
}
</style>
