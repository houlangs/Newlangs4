<template>
  <div class="center example-nav">
    <div class="square">
      <router-view />
    </div>
    <TheFooter />
  </div>
</template>

<script setup>
import TheFooter from '@/components/TheFooter.vue'
import { useNavStore } from '@/stores/nav'

const navStore = useNavStore()
const active = navStore.active
</script>

<style lang="scss" scoped>
.square {
  height: calc(100vh - 60px);
  position: relative;
  overflow: auto;
  width: 100%;
}

.example-nav {
  background: #f0f0f0 !important;
  padding: 0 !important;
  position: relative;
}
</style>
