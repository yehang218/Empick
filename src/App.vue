<template>
  <button @click="show = true">모달 열기</button>

  <Modal
    v-if="show"
    :message="dynamicMessage"
    @confirm="handleYes"
    @cancel="handleNo"
  />
</template>

<script setup>
import { ref } from 'vue'
import Modal from './components/Modal.vue'

const show = ref(false)
const dynamicMessage = ref('템플릿을 사용하시겠습니까?')

function handleYes() {
  console.log('예 클릭됨')
  show.value = false
}

function handleNo() {
  console.log('아니오 클릭됨')
  show.value = false
}
</script>
