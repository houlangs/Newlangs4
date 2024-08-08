<template>
  <div class="home_content">
    <a href="https://app.houlangs.com"><img src="@/assets/logo.svg" alt="Logo" class="logo" /></a>
    <br>
    <br>
    <div class="description">The Best Free Subdomain Service!</div>
    <SearchDomain />
    <br>
  </div>
</template>

<script setup>
import SearchDomain from '@/components/SearchDomain.vue'
</script>

<style scoped lang="scss">
.home_content {
  min-width: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  margin-top: 160px;
}

.logo {
  width: 500px;
  height: auto;
  /* Adjust the width as needed */
}

.description {
  font-size: 20px;
  font-weight: 500;
  padding-bottom: 30px;
}

/* Media queries for responsive design */
@media screen and (max-width: 1024px) {
  .logo {
    width: 150px;
    /* Adjust the width for smaller screens */
  }

  .description {
    font-size: 18px;
  }
}

@media screen and (max-width: 640px) {
  .logo {
    width: 100px;
    /* Adjust the width for even smaller screens */
  }

  .description {
    font-size: 14px;
  }
}

@media screen and (max-width: 420px) {
  .logo {
    width: 80px;
    /* Adjust the width for the smallest screens */
  }

  .description {
    font-size: 10px;
  }
}
</style>
