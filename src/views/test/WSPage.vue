<template>
  <div>
    <h2>우석이 공통 컴포넌트 모음ㅎ_ㅎ</h2>

    <!-- 일반 확인/취소 모달 -->
    <button @click="showConfirmModal = true">모달 열기</button>

    <!-- 단순 알림 모달 -->
    <button @click="showAlertModal = true">알림 모달 열기</button>

    <!-- ✅ 확인/취소 모달 -->
    <Modal
      v-if="showConfirmModal"
      :message="dynamicMessage"
      @confirm="handleYes"
      @cancel="handleNo"
    />

    <!-- ✅ 확인만 있는 알림 모달 -->
    <AlertModal
      v-if="showAlertModal"
      :message="alertMessage"
      @confirm="handleAlertConfirm"
    />

    <!-- 다른 공통 컴포넌트 -->
    <Search />
    <EvaluationInput />
    <IntroduceResult />
    <IntroduceEvaluationInput />
  </div>
</template>

<script setup>
import { ref } from 'vue'

import Modal from '@/components/common/Modal.vue'
import AlertModal from '@/components/common/AlertModal.vue'
import Search from '@/components/common/Search.vue'
import EvaluationInput from '@/components/employment/EvaluationInput.vue'
import IntroduceEvaluationInput from '@/components/employment/IntroduceEvaluationInput.vue'
import IntroduceResult from '@/components/employment/IntroduceResult.vue'

const showConfirmModal = ref(false)
const dynamicMessage = ref('템플릿을 사용하시겠습니까?')

const showAlertModal = ref(false)
const alertMessage = ref('이 템플릿은 사용이 불가능합니다.')

function handleYes() {
  console.log('확인 눌림')
  showConfirmModal.value = false
}

function handleNo() {
  console.log('취소 눌림')
  showConfirmModal.value = false
}

function handleAlertConfirm() {
  console.log('알림 확인됨')
  showAlertModal.value = false
}
</script>

<style scoped>
h2 {
  margin-bottom: 1rem;
}
</style>
