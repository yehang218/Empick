<template>
  <v-container>
    <h3 class="section-title">평가 기준</h3>
    <div class="criteria-list">
      <div
        v-for="(item, idx) in items"
        :key="item.id"
        class="criteria-card"
      >
        <div class="criteria-header">
          <span class="criteria-number">{{ idx + 1 }}.</span>
          <span class="criteria-title">{{ item.title }}</span>
          <v-btn
            icon
            size="x-small"
            color="grey"
            class="delete-btn"
            @click="removeItem(item.id)"
          >
            <v-icon>mdi-close</v-icon>
          </v-btn>
        </div>
      </div>
      <!-- 새 항목 입력 영역 -->
      <div class="criteria-card new-card">
        <v-text-field
          v-model="newTitle"
          label="평가 기준을 입력하세요."
          variant="plain"
          hide-details
          class="criteria-input"
        />
        <v-btn
          color="success"
          class="register-btn"
          @click="addItem"
          :disabled="!newTitle.trim()"
        >등록하기</v-btn>
      </div>
    </div>
    <v-btn
      class="add-btn"
      variant="outlined"
      block
      @click="focusInput"
    >+ 평가 기준 추가하기</v-btn>
  </v-container>
</template>

<script setup>
import { ref, onMounted, nextTick } from 'vue'
import { useRoute } from 'vue-router'
import { useIntroduceStore } from '@/stores/introduceStore'

const route = useRoute()
const templateId = Number(route.params.templateId)
const introduceStore = useIntroduceStore()
const items = introduceStore.items
const newTitle = ref('')

onMounted(() => {
  introduceStore.fetchItems(templateId)
})

const addItem = async () => {
  if (!newTitle.value.trim()) return
  await introduceStore.addItem({
    title: newTitle.value,
    member_id: 1, // 실제 로그인 유저 id로 대체
    introduce_template_id: templateId
  })
  newTitle.value = ''
  await nextTick()
  focusInput()
}

const removeItem = async (id) => {
  await introduceStore.removeItem(id)
}

function focusInput() {
  // 자동 포커스 (v-text-field에 ref 추가 필요시)
  const input = document.querySelector('.criteria-input input')
  if (input) input.focus()
}
</script>

<style scoped>
.section-title {
  font-size: 18px;
  font-weight: bold;
  margin-bottom: 16px;
}
.criteria-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}
.criteria-card {
  background: #fff;