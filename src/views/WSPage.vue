<!-- src/views/WSPage.vue -->
<template>
  <div>
    <h2>우석이 공통 컴포넌트 모음ㅎ_ㅎ</h2>

    <button @click="show = true">모달 열기</button>

    <!-- ✅ 조건부 렌더링 -->
    <Modal
      v-if="show"
      :message="dynamicMessage"
      @confirm="handleYes"
      @cancel="handleNo"
    />
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Modal from '@/components/Modal.vue' // 경로 확인 필요

const show = ref(false)
const dynamicMessage = ref('템플릿을 사용하시겠습니까?')

function handleYes() {
  console.log('확인 눌림')
  show.value = false
}

function handleNo() {
  console.log('취소 눌림')
  show.value = false
}
</script>
