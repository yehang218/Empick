<template>
  <div class="message-box-wrapper">
    <v-card
      class="message-card"
      outlined
      rounded="xl"
    >
      <v-textarea
        v-model="inputMessage"
        placeholder="내용을 입력하세요."
        variant="outlined"
        rows="10"
        auto-grow
        hide-details
        class="message-input"
      />
    </v-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// 부모에서 사용할 수 있게 export 가능
const inputMessage = ref('')
defineExpose({ inputMessage })
</script>

<style scoped>
.message-box-wrapper {
  width: 600px;    /* 원하는 고정 너비 */
  height: 300px;   /* 원하는 고정 높이 */
  overflow: hidden;
}

.message-card {
  width: 100%;
  height: 100%;
  border: none;
  overflow: auto;
  padding: 12px;
  box-sizing: border-box;
}

.message-input {
  width: 100%;
  height: 100%;
  font-size: 16px;
  background-color: transparent;
  border: none;
  outline: none;
}

/* ✅ 항상 연한 회색 테두리 유지 */
.message-input >>> .v-input__control {
  border: 1px solid #e0e0e0 !important;
  border-radius: 12px;
  box-shadow: none !important;
}

/* ✅ 포커스(클릭) 상태에서도 테두리 유지 */
.message-input >>> .v-input.v-input--focused .v-input__control {
  border-color: #e0e0e0 !important;
  box-shadow: none !important;
}

/* ✅ 호버 상태에서도 테두리 유지 */
.message-input >>> .v-input:hover .v-input__control {
  border-color: #e0e0e0 !important;
}

</style>
