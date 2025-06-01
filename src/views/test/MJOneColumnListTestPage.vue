<template>
    <v-btn color="primary" @click="goBack" class="mb-4">
      ← 뒤로가기
    </v-btn>
  <div class="page-wrapper">
    <OneColumnList 
    :title="listTitle" 
    :items="listItems" 
    @update:selectedItem="handleSelectedItem"
    />
  </div>
</template>

<script setup>
import OneColumnList from '@/components/OneColumnList.vue' // 경로는 실제 위치에 맞게 수정
import { useRouter } from 'vue-router'

const router = useRouter()

const goBack = () => {
  router.push('/test/mj')
}

// 이 페이지에서 사용할 데이터 정의
const listTitle = '직무 목록'
const listItems = ['프론트엔드 개발자', '백엔드 개발자', '디자이너', '기획자']

const handleSelectedItem = (item) => {
  console.log('선택된 항목:', item)
}
</script>

<style scoped>
.page-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 40px;
}
</style>
