<template>
  <div class="search_box">
    <div class="con-input">
      <vs-input v-model="inputValue" color="primary" placeholder="Desired Domain Prefix" />
    </div>
    <div class="con-select">
      <vs-select v-model="selectValue" placeholder="Main Domain Name to Use" color="primary">
        <vs-option label="669.ink" value="4"> ⭐️669.ink (Already Registered) </vs-option>
        <vs-option label="Domain List" value="1"> Domain List </vs-option>
        <vs-option label="Domain List" value="1"> Domain List </vs-option>
        <vs-option label="Domain List" value="1"> Domain List </vs-option>
        <vs-option label="Domain List" value="1"> Domain List </vs-option>
        <vs-option label="Domain List" value="1"> Domain List </vs-option>
        <vs-option label="Domain List" value="1"> Domain List </vs-option>
        <a href="https://app.houlangs.com/#/auth/register">
          <vs-option label="What's More" value="8"> 30+ More Available Domains····🥳</vs-option></a>
      </vs-select>
    </div>
  </div>
  <br>
  <br>
  <br>
  <div class="btn">
    <a href="https://app.houlangs.com/#/auth/register">
      <button class="btn">
        <svg height="10" width="10" fill="#FFFFFF" viewBox="0 0 24 24" data-name="Layer 1" id="Layer_1" class="sparkle">
          <path
            d="M10,21.236,6.755,14.745.264,11.5,6.755,8.255,10,1.764l3.245,6.491L19.736,11.5l-6.491,3.245ZM18,21l1.5,3L21,21l3-1.5L21,18l-1.5-3L18,18l-3,1.5ZM19.333,4.667,20.5,7l1.167-2.333L24,3.5,21.667,2.333,20.5,0,19.333,2.333,17,3.5Z">
          </path>
        </svg>
        <span class="text">Parse Now!</span>
      </button>
    </a>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const inputValue = ref('')
const selectValue = ref('')
</script>

<style lang="scss">
.search_box {
  width: 80%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.con-input {
  padding: 0 8px;
  min-width: calc(80% - 270px);
}

.con-select {
  padding: 0 8px;

  .vs-select {
    min-width: 200px;
  }
}

.btn {
  min-width: 70px;
}

@media screen and (max-width: 640px) {
  .search_box {
    width: 80%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .con-input {
    padding: 8px 0;
    min-width: 360px;
  }

  .con-select {
    padding: 8px 0;

    .vs-select {
      min-width: 360px;
    }
  }
}

@media screen and (max-width: 420px) {
  .search_box {
    width: 80%;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
  }

  .con-input {
    padding: 8px 0;
    min-width: 340px;
  }

  .con-select {
    padding: 8px 0;

    .vs-select {
      min-width: 340px;
    }
  }
}
</style>
