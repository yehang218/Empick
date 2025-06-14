<template>
  <v-container fluid style="margin-top: 70px">
    <v-row>
      <!-- 왼쪽: 지원자 정보 및 통계 -->
      <v-col cols="12" md="6">
        <!-- 지원자 기본 정보 카드 -->
        <v-card class="mb-4" elevation="1">
          <v-card-title class="font-weight-bold">지원자 정보</v-card-title>
          <v-card-text>
            <v-row align="center">
              <v-col cols="4" class="text-center">
                <v-avatar size="100">
                  <img :src="applicant.picture" alt="지원자 사진" />
                </v-avatar>
              </v-col>
              <v-col cols="8">
                <div><strong>이름:</strong> {{ applicant.name }}</div>
                <div><strong>연락처:</strong> {{ applicant.phone }}</div>
                <div><strong>주소:</strong> {{ applicant.address }}</div>
                <div><strong>생년월일:</strong> {{ applicant.birth }}</div>
                <div><strong>이메일:</strong> {{ applicant.email }}</div>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>

        <!-- 이력서 정보 카드 -->
        <v-card class="mb-4" elevation="1">
          <v-card-text>
            <div><strong>입사 동기</strong><br />{{ resume.motivation }}</div>
            <div class="mt-4"><strong>주요 경력</strong><br />{{ resume.experience }}</div>
            <div class="mt-4"><strong>기술 스택</strong><br />{{ resume.skills }}</div>
          </v-card-text>
        </v-card>

        <!-- 통계 테이블 -->
        <v-card elevation="1">
          <v-card-title class="font-weight-bold">전형 점수 및 통계</v-card-title>
          <v-card-text>
            <v-table>
              <thead>
                <tr>
                  <th>구분</th>
                  <th>전형 점수</th>
                  <th>채용 공고 지원자 통계</th>
                  <th>전형 결과</th>
                  <th>평가 보기</th>
                </tr>
              </thead>
              <tbody>
                <tr v-for="row in statistics" :key="row.label">
                  <td>{{ row.label }}</td>
                  <td>{{ row.score }}</td>
                  <td>{{ row.avg }}</td>
                  <td>
                    <span :class="row.pass ? 'text-success' : 'text-error'">
                      {{ row.pass ? '합격' : '불합격' }}
                    </span>
                  </td>
                  <td>
                    <v-btn size="x-small" variant="outlined">상세 보기</v-btn>
                  </td>
                </tr>
              </tbody>
            </v-table>
          </v-card-text>
        </v-card>
      </v-col>

      <!-- 오른쪽: 평가 상세 영역 -->
      <v-col cols="12" md="6">
        <component :is="currentEvaluationComponent" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref } from 'vue'

// 샘플 데이터
const applicant = ref({
  name: '박지민',
  birth: '1994-11-23',
  phone: '010-5678-1234',
  email: 'jimin@example.com',
  address: '서울시 강남구 역삼동',
  picture: 'https://randomuser.me/api/portraits/women/1.jpg'
})

const resume = ref({
  motivation: '성장할 수 있는 환경을 찾아 지원했습니다.',
  experience: '백엔드 개발 3년 경력, Spring Boot 사용',
  skills: 'Java, Spring, JPA, Vue.js'
})

const statistics = ref([
  { label: '자기소개서', score: 85, avg: 76, pass: true },
  { label: '실무 테스트', score: 90, avg: 65, pass: true },
  { label: '면접', score: 71, avg: 78, pass: false },
])

// 향후 실제 컴포넌트 연결용
const currentEvaluationComponent = ref('div')
</script>

<style scoped>
.text-success {
  color: green;
}
.text-error {
  color: red;
}
</style>
