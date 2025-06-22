<template>
  <v-container fluid class="pa-6">
    <v-row>
      <!-- 좌측: 채용 공고 및 지원자 목록 -->
      <v-col cols="5">
        <v-row>
          <v-col cols="6">
            <v-card>
              <v-card-title class="text-h6">채용 공고</v-card-title>
              <v-list>
                <v-list-item v-for="(item, index) in jobPostings" :key="index" @click="selectedJob = item">
                  {{ item }}
                </v-list-item>
              </v-list>
            </v-card>
          </v-col>
          <v-col cols="4">
            <v-card>
                <v-card-title>지원자 목록</v-card-title>
                    <v-divider></v-divider>
                        <v-list>
                            <v-list-item
                                v-for="applicant in applicants"
                                :key="applicant.id"
                                @click="selectApplicant(applicant)"
                                :class="{ 'bg-grey-lighten-3': selectedApplicant?.id === applicant.id }"
                                style="cursor: pointer;"
                                >   
                            <v-list-item-title>{{ applicant.name }}</v-list-item-title>
                        </v-list-item>
                    </v-list>
                </v-card>
        </v-col>
        </v-row>

        <v-col cols="8">
        <v-card v-if="selectedApplicant">
          <v-card-title>상세 정보</v-card-title>
          <v-divider></v-divider>
          <v-card-text>
            <v-row>
              <v-col cols="4">
                <v-img
                  :src="selectedApplicant.profile_url"
                  aspect-ratio="1"
                  class="rounded"
                ></v-img>
              </v-col>
              <v-col cols="8">
                <v-list dense>
                  <v-list-item>
                    <v-list-item-title>이름: {{ selectedApplicant.name }}</v-list-item-title>
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-title>이메일: {{ selectedApplicant.email }}</v-list-item-title>
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-title>연락처: {{ selectedApplicant.phone }}</v-list-item-title>
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-title>생년월일: {{ selectedApplicant.birth }}</v-list-item-title>
                  </v-list-item>
                  <v-list-item>
                    <v-list-item-title>주소: {{ selectedApplicant.address }}</v-list-item-title>
                  </v-list-item>
                </v-list>
              </v-col>
            </v-row>
          </v-card-text>
        </v-card>
        <v-alert v-else type="info" class="mt-4">
          좌측 목록에서 지원자를 선택하세요.
        </v-alert>
      </v-col>

        <!-- 점수 통계 -->
        <v-card class="mt-4 pa-4">
          <v-card-title class="text-h6">진행 점수 및 통계</v-card-title>
          <v-row>
            <v-col cols="4" v-for="(score, index) in scores" :key="index">
              <v-card>
                <v-card-text class="text-center">
                  <div class="text-subtitle-1">{{ score.title }}</div>
                  <div class="text-h6">{{ score.value }}</div>
                  <v-btn text small color="primary">상세 보기</v-btn>
                </v-card-text>
              </v-card>
            </v-col>
          </v-row>
        </v-card>
      </v-col>

      <!-- 우측: 문자 작성 및 수신자 추가 -->
      <v-col cols="7">
        <v-card>
          <v-card-title class="text-h6">문자 내용 작성</v-card-title>
          <v-textarea label="이 곳에 메세지를 입력해 주세요." rows="10"></v-textarea>
        </v-card>

        <v-card class="mt-4">
          <v-card-title class="text-h6">수신자 추가</v-card-title>
          <v-row class="pa-4">
            <v-col cols="6">
              <v-text-field v-model="newRecipient.email" label="이메일"></v-text-field>
            </v-col>
            <v-col cols="6">
              <v-text-field v-model="newRecipient.name" label="이름"></v-text-field>
            </v-col>
            <v-col cols="12">
              <v-btn color="success" @click="addRecipient">추가하기</v-btn>
            </v-col>
            <v-col cols="12">
              <v-list>
                <v-list-item v-for="(recipient, index) in recipients" :key="index">
                  {{ recipient.name }} ({{ recipient.email }})
                  <v-btn icon small @click="removeRecipient(index)">
                    <v-icon>mdi-close</v-icon>
                  </v-btn>
                </v-list-item>
              </v-list>
            </v-col>
          </v-row>
        </v-card>

        <v-btn color="primary" class="mt-4">발표 발송</v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const jobPostings = ['채용 공고1', '채용 공고2', '채용 공고3', '채용 공고4'];
const applicants = ref([])
const selectedJob = ref(null);
const selectedApplicant = ref(null);

import axios from 'axios'

const fetchApplicants = async () => {
  try {
    const response = await axios.get('http://localhost:5001/api/v1/employment/applicant');
    console.log('📥 받은 지원자 데이터:', response.data); // 여기에 로그 추가
    applicants.value = response.data.data;
  } catch (error) {
    console.error('지원자 데이터를 불러오지 못했습니다.', error);
  }
};

const selectApplicant = (applicant) => {
  selectedApplicant.value = applicant
}

onMounted(fetchApplicants)

const scores = ref([
  { title: '자기소개서', value: 85 },
  { title: '실무 테스트', value: 90 },
  { title: '면접', value: 71 },
]);

const newRecipient = ref({ email: '', name: '' });
const recipients = ref([{ email: 'tjsehdtjf@naver.com', name: '서준영' }]);

function addRecipient() {
  if (newRecipient.value.email && newRecipient.value.name) {
    recipients.value.push({ ...newRecipient.value });
    newRecipient.value.email = '';
    newRecipient.value.name = '';
  }
}

function removeRecipient(index) {
  recipients.value.splice(index, 1);
}
</script>

<style scoped>
.text-h6 {
  font-weight: bold;
}
</style>
