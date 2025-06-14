<template>
  <v-container fluid>
    <v-row>
      <!-- 좌측: 지원자 정보 및 통계 (6/12) -->
      <v-col cols="12" md="6">
        <!-- 🧍‍♂️ 지원자 정보 -->
        <v-card class="mb-4 pa-4 elevated-card">
          <h3 class="text-h6 font-weight-bold mb-4">지원자 정보</h3>
          <v-row>
            <v-col cols="4">
              <v-avatar size="100">
                <img :src="applicant.pictureUrl" alt="avatar" />
              </v-avatar>
            </v-col>
            <v-col cols="8">
              <v-row class="info-line">
                <v-col cols="6"><strong>이름:</strong> {{ applicant.name }}</v-col>
                <v-col cols="6"><strong>생년월일:</strong> {{ applicant.birth }}</v-col>
              </v-row>
              <v-divider class="my-1" />
              <v-row class="info-line">
                <v-col cols="6"><strong>연락처:</strong> {{ applicant.phone }}</v-col>
                <v-col cols="6"><strong>이메일:</strong> {{ applicant.email }}</v-col>
              </v-row>
              <v-divider class="my-1" />
              <v-row class="info-line">
                <v-col cols="12"><strong>주소:</strong> {{ applicant.address }}</v-col>
              </v-row>
            </v-col>
          </v-row>
        </v-card>

        <!-- 📝 이력서 요약 -->
        <v-card class="mb-4 pa-4 elevated-card">
          <div class="resume-section">
            <strong>입사 동기</strong>
            <v-divider class="my-2" />
            {{ applicant.motivation }}
          </div>
          <div class="resume-section">
            <strong>주요 경력</strong>
            <v-divider class="my-2" />
            {{ applicant.experience }}
          </div>
          <div>
            <strong>기술 스택</strong>
            <v-divider class="my-2" />
            {{ applicant.skills }}
          </div>
        </v-card>

        <!-- 📊 통계 영역 -->
        <v-divider class="my-6" />
        <v-card class="pa-4 elevated-card">
          <h3 class="text-h6 font-weight-bold mb-4">전형 점수 및 통계</h3>
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
              <tr v-for="row in applicant.evaluationStats" :key="row.type">
                <td>{{ row.type }}</td>
                <td>{{ row.score }}</td>
                <td>{{ row.average }}</td>
                <td :class="{ 'text-success': row.result === '합격', 'text-error': row.result === '불합격' }">
                  {{ row.result }}
                </td>
                <td>
                  <v-btn size="x-small" variant="outlined">상세 보기</v-btn>
                </td>
              </tr>
            </tbody>
          </v-table>
        </v-card>
      </v-col>

      <!-- 우측: 평가 결과 영역 (6/12) -->
      <v-col cols="12" md="6">
        <slot name="evaluation" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
const applicant = {
  name: '박지민',
  birth: '1994-11-23',
  phone: '010-5678-1234',
  email: 'jimin@example.com',
  address: '서울시 강남구 역삼동',
  pictureUrl: 'https://randomuser.me/api/portraits/women/1.jpg',
  motivation: '성장할 수 있는 환경을 찾아 지원했습니다.',
  experience: '백엔드 개발 3년 경력, Spring Boot 사용',
  skills: 'Java, Spring, JPA, Vue.js',
  evaluationStats: [
    { type: '자기소개서', score: 85, average: 76, result: '합격' },
    { type: '실무 테스트', score: 90, average: 65, result: '합격' },
    { type: '면접', score: 71, average: 78, result: '불합격' }
  ]
}
</script>

<style scoped>
.v-card.elevated-card {
  border-radius: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border: 1px solid #ccc;
}

.info-line {
  margin-bottom: 8px;
}
.resume-section {
  margin-bottom: 12px;
}
.text-success {
  color: #4CAF50;
  font-weight: bold;
}
.text-error {
  color: #F44336;
  font-weight: bold;
}
</style>
