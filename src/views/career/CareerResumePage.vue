<template>
  <div>
    <CareerHeader />
    <v-container class="custom-container">
      <h2 class="page-title">지원서 작성하기</h2>

      <v-row>
        <!-- 건드 이러서 -->
        <v-col cols="12" md="6">
          <div class="resume-box">
            <p class="resume-placeholder">이력서 자리<br />(최신)</p>
          </div>
        </v-col>

        <!-- 자지소개서 -->
        <v-col cols="12" md="6">
          <h3 class="section-title">자기소개서</h3>

          <v-text-field
            label="자기소개서 제목"
            v-model="coverLetterTitle"
            variant="outlined"
            density="compact"
            class="mb-4"
          />

          <v-textarea
            label="당신의 강점은? *"
            v-model="strength"
            variant="outlined"
            rows="4"
            class="mb-4"
          />

          <v-textarea
            label="회사에 바라는 점은? *"
            v-model="hope"
            variant="outlined"
            rows="4"
            class="mb-4"
          />

          <div class="button-group">
            <v-btn variant="outlined" color="success" class="mr-2">취소</v-btn>
            <v-btn color="success" class="submit-btn">등록</v-btn>
          </div>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import CareerHeader from '@/components/career/CareerHeader.vue'

const coverLetterTitle = ref('신입 공채 기본 지원서 템플릿')
const strength = ref('')
const hope = ref('')
</script>

<style scoped>
.custom-container {
  max-width: 1240px;
  padding-top: 40px;
}
.page-title {
  font-size: 24px;
  font-weight: bold;
  margin-bottom: 32px;
}
.resume-box {
  width: 100%;
  height: 450px;
  background-color: #e0e0e0;
  display: flex;
  align-items: center;
  justify-content: center;
}
.resume-placeholder {
  text-align: center;
  color: #333;
  font-size: 14px;
}
.section-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 16px;
}
.button-group {
  display: flex;
  justify-content: flex-end;
}
.submit-btn {
  font-weight: bold;
  color: white;
}
</style>
