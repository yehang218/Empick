<template>
  <v-container class="d-flex justify-center align-center" style="height: 100vh">
    <v-row class="pa-4" style="width: 400px; height: 400px" align="center" no-gutters>
      <!-- 왼쪽 버튼 -->
      <v-col cols="3" class="d-flex flex-column align-center" style="gap: 50px">
        <v-btn
          color="success"
          style="height: 40px; width: 140px"
          @click="notifySupporterAdd"
        >
          <v-icon left>mdi-plus</v-icon>
          지원자 추가하기
        </v-btn>
        <v-btn
          color="success"
          style="height: 40px; width: 140px"
          @click="bulkAdd"
        >
          일괄 추가하기
        </v-btn>
      </v-col>

        <!-- 오른쪽 사용자 리스트 박스 -->
        <v-col class="pl-2" style="max-width: 320px">
        <v-card
            class="pa-4 d-flex flex-column justify-between"
            height="100%"
            rounded="xl"
            elevation="4"
            style="width: 170%"
        >
            <div>
            <!-- 헤더 -->
            <v-row class="text-subtitle2 text-center pb-1">
                <v-col cols="7" class="d-flex justify-center">이메일</v-col>
                <v-col cols="3" class="d-flex justify-center">이름</v-col>
                <v-col cols="1"></v-col> <!-- X 아이콘 자리 빈 칸 -->
            </v-row>
            <v-divider class="mb-3"></v-divider>

            <!-- 사용자 입력 필드 -->
            <v-row
            v-for="(user, index) in users"
            :key="index"
            class="mb-3 align-center"
            no-gutters
            >
            <v-col cols="7">
                <v-text-field
                v-model="user.email"
                density="compact"
                placeholder="이메일"
                hide-details
                variant="outlined"
                />
            </v-col>
            <v-col cols="3">
                <v-text-field
                v-model="user.name"
                density="compact"
                placeholder="이름"
                hide-details
                variant="outlined"
                />
            </v-col>
            <v-col cols="auto" class="d-flex justify-end pr-1" style="min-width: 30px;">
                <v-icon
                @click="removeUser(index)"
                color="grey"
                size="20"
                class="me-0"
                >
                mdi-close
                </v-icon>
            </v-col>
            </v-row>
            </div>

            <!-- 아래 추가 버튼 -->
            <div class="mt-2 text-center">
            <v-btn
                variant="outlined"
                color="grey"
                style="border-color: #ccc; color: #555"
                @click="addUser"
            >
                <v-icon left>mdi-plus</v-icon>
                추가하기
            </v-btn>
            </div>
        </v-card>
        </v-col>
    </v-row>
  </v-container>
</template>

<script setup>
import { ref } from 'vue';

const users = ref([
  { email: 'tjalswhd1@naver.com', name: '서민종' },
  { email: '', name: '' },
]);

const addUser = () => {
  users.value.push({ email: '', name: '' });
};

const removeUser = (index) => {
  users.value.splice(index, 1);
};

const bulkAdd = () => {
  alert('일괄 추가하기 모달 열기');
};

const notifySupporterAdd = () => {
  alert('지원자 정보 추가');
};
</script>

<style scoped>
.v-card {
  border: 1px solid #e0e0e0;
}
</style>
