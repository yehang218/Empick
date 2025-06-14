<template>
  <v-app>
    <v-main>
      <v-container class="pa-6">
        <h2 class="text-h6 font-weight-bold mb-4">지원자 목록</h2>

        <!-- 로딩 스피너 -->
        <v-progress-circular v-if="store.loading" indeterminate color="primary" class="mx-auto my-4" />

        <!-- 에러 메시지 -->
        <v-alert
          v-if="store.error"
          type="error"
          class="mb-4"
          closable
          @click:close="store.error = null"
        >
          {{ store.error }}
        </v-alert>

        <!-- 지원자 리스트 -->
        <ListView
          :headers="headers"
          :data="store.applicantList"
          :showCheckbox="true"
          @item-click="handleItemClick"
          @update-status="handleUpdateStatus"
        />

        <!-- 액션 버튼 -->
        <div class="d-flex justify-end mt-4">
          <v-btn color="primary" class="mr-2" :disabled="!store.selectedApplicantIds.length" @click="handleSendMail">
            이메일 발송
          </v-btn>
          <v-btn color="success" :disabled="!store.selectedApplicantIds.length" @click="handleAssignTest">
            테스트 할당
          </v-btn>
        </div>
      </v-container>
    </v-main>
  </v-app>
</template>

<script setup>
import { onMounted } from 'vue'
import { useToast } from 'vue-toastification'
import ListView from '@/components/common/ListView.vue'
import { useApplicantListStore } from '@/stores/applicantListStore'

const store = useApplicantListStore()
const toast = useToast()

const headers = [
  { label: '이름', key: 'name' },
  { label: '이메일', key: 'email' },
  { label: '연락처', key: 'phone' },
  { label: '생년월일', key: 'birth' },
  { label: '주소', key: 'address' },
  { label: '상태', key: 'status' },
  { label: '채용공고', key: 'recruitment' },
  { label: '상세보기', key: 'viewButton' }
]

const handleItemClick = (id) => {
  store.toggleSelection(id)
}

const handleSendMail = () => {
  if (!store.selectedApplicantIds.length) {
    toast.error('선택된 지원자가 없습니다.')
    return
  }
  toast.success('메일 모달 열기') // TODO: 실제 모달 연결
}

const handleAssignTest = async () => {
  if (!store.selectedApplicantIds.length) {
    toast.error('선택된 지원자가 없습니다.')
    return
  }

  await store.assignTest(store.selectedApplicantIds)
  toast.success('테스트가 할당되었습니다.')
}

const handleUpdateStatus = ({ id, newStatus }) => {
  store.updateStatus(id, newStatus)
}

onMounted(() => {
  store.fetchAllApplicants()
})
</script>
