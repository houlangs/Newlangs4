<template>
  <div class="box">
    <div>Developed by <a href="https://forum.houlangs.com/d/14-newlangs-ming-xie-ming-dan" target="_blank">Houlangs Development Team</a></div>
    <div>Newlangs 4 - Open Source</div>
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
