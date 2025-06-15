<template>
  <v-container fluid>
    <v-row>
      <!-- 좌측: 지원자 정보 및 통계 -->
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
              <v-row class="info-line align-center">
                <v-col cols="8">
                  <strong>주소:</strong> {{ applicant.address }}
                </v-col>
                <v-col cols="4" class="d-flex justify-end">
                  <v-select
                    v-model="selectedStatus"
                    :items="statusOptions"
                    label="지원서 상태"
                    dense
                    outlined
                    hide-details
                    @update:modelValue="updateStatus"
                  />
                </v-col>
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
                  <v-btn size="x-small" variant="outlined" @click="selectEvaluation(row.type)">
                    상세 보기
                  </v-btn>
                </td>
              </tr>
            </tbody>
          </v-table>
        </v-card>
      </v-col>

      <!-- 우측: 평가 컴포넌트 -->
      <v-col cols="12" md="6">
        <component :is="evaluationComponent" :evaluationData="introduceEvaluationData" />
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import IntroduceResult from '@/components/employment/IntroduceEvaluationInput.vue'
import { useApplicationStore } from '@/stores/applicationStore'
import { useToast } from 'vue-toastification'
import apiClient from '@/apis/apiClient' // 직접 axios 호출을 위해 임포트

const route = useRoute()
const applicationStore = useApplicationStore()
const toast = useToast()
const applicationId = Number(route.params.id)

const evaluationComponent = ref(IntroduceResult)
const selectedStatus = ref('')
const statusOptions = [
  '서류합격', '실무테스트합격', '면접합격', '최종합격', '불합격'
]

// 상태 문자열과 정수 코드 매핑
const statusMap = {
  '서류합격': 1,
  '실무테스트합격': 2,
  '면접합격': 3,
  '최종합격': 4,
  '불합격': 5
}

const applicant = ref({
  name: '',
  birth: '',
  phone: '',
  email: '',
  address: '',
  pictureUrl: '',
  motivation: '',
  experience: '',
  skills: '',
  evaluationStats: [],
  status: '' // 현재 상태 문자열을 위한 필드
})

const introduceEvaluationData = ref(null) // IntroduceEvaluationInput.vue에 전달할 데이터

onMounted(async () => {
  try {
    const data = await applicationStore.fetchApplicationById(applicationId)
    if (data) {
      applicant.value = data
      selectedStatus.value = data.status

      // 자기소개서 평가 결과 ID가 있다면 상세 정보 로드
      if (data.introduceRatingResultId && data.introduceRatingResultId !== 0) {
        try {
          const response = await apiClient.get(`/api/v1/employment/introduce-rating-results/${data.introduceRatingResultId}`)
          if (response.data.success) {
            introduceEvaluationData.value = response.data.data
            console.log('자기소개서 평가 결과 데이터:', introduceEvaluationData.value)
          } else {
            toast.error('자기소개서 평가 결과 로드 실패: ' + response.data.message)
          }
        } catch (evalError) {
          console.error('자기소개서 평가 결과 API 호출 실패:', evalError)
          toast.error('자기소개서 평가 결과를 불러오는 데 실패했습니다.')
        }
      } else {
        console.log('연결된 자기소개서 평가 결과가 없습니다.')
        toast.info('연결된 자기소개서 평가 결과가 없습니다.')
      }
    }
  } catch (error) {
    console.error('지원서 상세 정보 로드 실패:', error)
    toast.error('지원서 정보를 불러오는 데 실패했습니다.')
  }
})

const updateStatus = async () => {
  try {
    const statusCode = statusMap[selectedStatus.value]
    if (statusCode === undefined) {
      toast.error('유효하지 않은 상태 값입니다.')
      return
    }

    await applicationStore.updateApplicationStatus(applicationId, { status: statusCode });
    toast.success(`지원서 상태가 '${selectedStatus.value}' (으)로 변경되었습니다.`) // 성공 토스트 메시지
    
    // 상태 변경 후 최신 정보 다시 불러오기
    const data = await applicationStore.fetchApplicationById(applicationId);
    if (data) {
      applicant.value = data;
      selectedStatus.value = data.status; // 최신 상태로 업데이트
    }
  } catch (error) {
    console.error('지원서 상태 변경 실패:', error);
    toast.error('지원서 상태 변경에 실패했습니다.');
  }
}

const selectEvaluation = (type) => {
  switch (type) {
    case '자기소개서':
      evaluationComponent.value = IntroduceResult
      break
    // case '실무 테스트':
    //   evaluationComponent.value = () => import('@/components/employment/TestResult.vue')
    //   break
    // case '면접':
    //   evaluationComponent.value = () => import('@/components/employment/InterviewResult.vue')
    //   break
    default:
      evaluationComponent.value = IntroduceResult
  }
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
