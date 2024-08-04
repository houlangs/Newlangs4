<template>
    <div class="donate-container">
        <div class="donate-item">
            <h1>支付宝捐助</h1>
            <div class="donate-image-container" @mouseover="addHoverEffect('alipay')"
                @mouseleave="removeHoverEffect('alipay')">
                <img :src="alipayQRCode" alt="支付宝二维码" width="300" height="auto" />
            </div>
            <h2>
                <a href="https://qr.alipay.com/fkx16450mmskjdlf9pdx540" target="_blank">点击捐助</a> | <a
                    href="https://669.ink/wp-content/uploads/2023/09/alipay.jpeg">保存图片</a>
            </h2>
        </div>
        <div class="donate-item">
            <h1>微信捐助</h1>
            <div class="donate-image-container" @mouseover="addHoverEffect('wechat')"
                @mouseleave="removeHoverEffect('wechat')">
                <img :src="wechatQRCode" alt="微信二维码" width="300" height=auto />
            </div>
            <h2>
                <a href="https://669.ink/wp-content/uploads/2023/09/wechatpay-1.jpeg">保存图片</a>
            </h2>
        </div>
    </div>
</template>
  
<script>
export default {
    name: "Donate",
    data() {
        return {
            alipayQRCode: "https://669.ink/wp-content/uploads/2023/09/alipay.jpeg",
            wechatQRCode: "https://669.ink/wp-content/uploads/2023/09/wechatpay-1.jpeg",
        };
    },
    methods: {
        addHoverEffect(paymentMethod) {
            const container = document.querySelector(`.${paymentMethod}-container`);
            container.classList.add("hovered");
        },
        removeHoverEffect(paymentMethod) {
            const container = document.querySelector(`.${paymentMethod}-container`);
            container.classList.remove("hovered");
        },
    },
};
</script>
  
<style scoped>
.donate-container {
    display: flex;
    justify-content: space-around;
    align-items: center;
}

.donate-item {
    text-align: center;
}

.donate-item img {
    max-width: 100%;
    height: auto;
}

.donate-image-container {
    position: relative;
    overflow: hidden;
    cursor: pointer;
    transition: transform 0.3s, filter 0.3s;
}

.donate-image-container img {
    transition: transform 0.3s, filter 0.3s;
}

.donate-image-container.hovered {
    transform: scale(1.1);
    filter: brightness(1.2);
    transition: transform 0.3s, filter 0.3s;
}

h2 {
    font-size: 18px;
    margin-bottom: 10px;
}
</style>
  