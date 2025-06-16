<template>
  <div>
    <CareerHeader />
    <v-container class="custom-container">
      <v-row>
        <v-col cols="12" md="8">
          <h2 class="title">{{ detail.title }}</h2>
          <p class="mb-4 intro-text">{{ detail.intro }}</p>

          <h3 class="subtitle">주요 업무</h3>
          <ul>
            <li v-for="(task, idx) in detail.tasks" :key="idx">{{ task }}</li>
          </ul>

          <h3 class="subtitle">자격 요건</h3>
          <ul>
            <li v-for="(qual, idx) in detail.qualifications" :key="idx">{{ qual }}</li>
          </ul>

          <h3 class="subtitle">우대 사항</h3>
          <ul>
            <li v-for="(plus, idx) in detail.preferred" :key="idx">{{ plus }}</li>
          </ul>

          <h3 class="subtitle">전형 절차</h3>
          <p>{{ detail.process }}</p>
        </v-col>

        <v-col cols="12" md="4">
          <v-card class="pa-4 summary-card">
            <div class="info-box">
              <div><strong>직군</strong><br />{{ detail.job }}</div>
              <v-divider class="my-2" />
              <div><strong>경력사항</strong><br />{{ detail.career }}</div>
              <v-divider class="my-2" />
              <div><strong>고용형태</strong><br />{{ detail.employmentType }}</div>
              <v-divider class="my-2" />
              <div><strong>근무지</strong><br />{{ detail.location }}</div>
            </div>
            <v-btn block class="mt-4 apply-btn" color="success" @click="goToResumePage">
              지원하기
            </v-btn>
          </v-card>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router' // ✅ useRouter 추가
import CareerHeader from '@/components/career/CareerHeader.vue'

const route = useRoute()
const router = useRouter() // ✅ 선언 추가
const id = Number(route.params.id)

const goToResumePage = () => {
  router.push(`/career/recruitments/resume/${id}`) // ✅ 라우터에 등록된 경로로 이동
}

const baseRecruitment = {
  intro: '안녕하세요, [회사명] 팀입니다. 우리는 "진짜 가족 같은 동료"를 찾고 있습니다.',
  tasks: [
    '업무 기획 및 일정 조율',
    '입사자 온보딩 및 관리',
    '관련 제도 기획 및 운영'
  ],
  qualifications: [
    '경력 3년 이상',
    '소통 능력과 협업 마인드',
    '문서 작성 능력'
  ],
  preferred: [
    '관련 업무 경험자',
    'IT 환경 이해도',
    '자기주도적인 분'
  ],
  process: '서류 → 면접 → 입사',
  location: '[회사명] 본사\n서울특별시 [구/주소] 또는 재택근무 가능'
}

const recruitmentMap = {
  1: {
    title: '인사팀에서 가족같이 지낼 인재를 찾습니다.',
    job: '인사(HR)',
    career: '경력 7년 이상',
    employmentType: '정규직',
    ...baseRecruitment
  },
  2: {
    title: '경영팀 백엔드 개발자 채용',
    job: '백엔드 개발자',
    career: '경력무관',
    employmentType: '계약직',
    ...baseRecruitment
  },
  // ... 3 ~ 10번도 동일하게 작성
}

const detail = recruitmentMap[id] || {}
</script>

<style scoped>
.custom-container {
  max-width: 1240px;
}
.title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 24px;
}
.subtitle {
  font-size: 20px;
  font-weight: 600;
  margin-top: 28px;
  margin-bottom: 14px;
}
.intro-text {
  font-size: 16px;
  color: #444;
  margin-bottom: 20px;
}
.info-box > div {
  margin-bottom: 10px;
  font-size: 15px;
  line-height: 1.6;
}
.summary-card {
  background-color: #f3f9f3;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0,0,0,0.08);
}
.apply-btn {
  font-weight: bold;
  border-radius: 999px;
  font-size: 15px;
  height: 44px;
}
ul {
  padding-left: 18px;
  line-height: 1.8;
  font-size: 16px;
  color: #333;
}
</style>
