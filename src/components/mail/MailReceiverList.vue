<template>
  <v-container class="d-flex justify-center align-center pa-0" style="width: 100%;">
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
        <v-col class="pl-2" style="max-width: 320px;">
          <v-card
            class="pa-4 d-flex flex-column justify-between"
            rounded="xl"
            elevation="4"
            style="width: 570px; height: 370px;" 
          >
          <!-- ✅ 정사각형 고정 -->
            <!-- 헤더 -->
            <v-row
              class="header-row"
              no-gutters
            >
              <v-col cols="7" class="d-flex justify-center align-center">이메일</v-col>
              <v-col cols="3" class="d-flex justify-center align-center">이름</v-col>
              <v-col cols="1"></v-col>
            </v-row>

            <v-divider style="margin-bottom: 4px; margin-top: 1px;" />

            <!-- ✅ 리스트 영역: 크기 고정 + 내부 스크롤 -->
            <div style="flex: 1; overflow-y: auto; min-height: 240px;">
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

            <!-- 하단 추가 버튼 -->
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

// ⬇️ 이 부분이 중요! users를 부모에 노출
defineExpose({ users });
</script>

<style scoped>
.v-card {
  border: 1px solid #e0e0e0;
}

.header-row {
  font-size: 14px;
}
</style>
