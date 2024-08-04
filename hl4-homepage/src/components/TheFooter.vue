<template>
  <div class="box">
    <div>由<a href="https://langs.ink/article/houlangs/team.html" target="_blank">厚浪开发组</a>开发</div>
  </div>
</template>

<style scoped lang="scss">
.box {
  padding: 15px;
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

@media screen and (max-width: 768px) {
  .box {
    font-size: 10px;
    padding: 15px;
    height: 60px;
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    align-items: center;
  }
}
</style>
