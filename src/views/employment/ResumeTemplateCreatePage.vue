<template>
  <v-container class="resume-template-container">
    <div class="header-row">
      <h2 class="page-title">자기소개서 템플릿 작성</h2>
      <v-btn class="add-btn" color="primary" variant="outlined" @click="addItem">
        + 항목 추가하기
      </v-btn>
    </div>
    <div class="template-title-wrap">
      <span class="template-title-label">템플릿 제목</span>
      <v-text-field
        v-model="templateTitle"
        class="template-title-input"
        variant="outlined"
        hide-details
        dense
      />
    </div>
    <div class="item-list">
      <div v-for="(item, idx) in items" :key="item.id || idx" class="item-row">
        <span class="item-number">{{ idx + 1 }}.</span>
        <v-text-field
          v-model="item.title"
          class="item-input"
          variant="outlined"
          hide-details
          dense
        />
        <v-btn icon color="grey" class="delete-btn" @click="removeItem(idx)">
          <v-icon size="22">mdi-close</v-icon>
        </v-btn>
      </div>
    </div>
    <div class="action-btns">
      <v-btn class="cancel-btn" color="grey" variant="outlined">취소</v-btn>
      <v-btn class="submit-btn" color="success" variant="flat">등록</v-btn>
    </div>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'

const templateTitle = ref('')
const items = ref([
  { title: '' },
])

function addItem() {
  items.value.push({ title: '' })
}
function removeItem(idx) {
  if (items.value.length > 1) {
    items.value.splice(idx, 1)
  }
}
</script>

<style scoped>
.resume-template-container {
  max-width: 800px;
  margin: 0 auto;
  background: #fff;
  border-radius: 12px;
  padding: 32px 40px 40px 40px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}
.header-row {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}
.page-title {
  font-size: 22px;
  font-weight: bold;
  margin-bottom: 0;
  text-align: left;
}
.template-title-wrap {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}
.template-title-label {
  font-weight: bold;
  margin-right: 16px;
  font-size: 16px;
}
.template-title-input {
  flex: 1;
}
.item-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: 16px;
}
.item-row {
  display: flex;
  align-items: center;
  gap: 12px;
}
.item-number {
  font-weight: bold;
  font-size: 16px;
  width: 24px;
  text-align: right;
}
.item-input {
  flex: 1;
}
.delete-btn {
  margin-left: 4px;
}
.add-btn {
  font-weight: bold;
  margin-bottom: 0;
  min-width: 140px;
}
.action-btns {
  display: flex;
  justify-content: flex-end;
  gap: 16px;
  margin-top: 24px;
}
.cancel-btn {
  min-width: 100px;
}
.submit-btn {
  min-width: 100px;
  font-weight: bold;
}
</style> 