<template>
    <v-container>
        <h2 class="text-h6 font-weight-bold mb-4">지원자 목록 (테스트용)</h2>

        <v-data-table :headers="headers" :items="dummyApplications" item-value="id" class="mb-4">
            <!-- 체크 아이콘 버튼 -->
            <template #item.actions="{ item }">
                <v-btn size="small" icon :color="selectedIds.includes(item.id) ? 'primary' : 'grey-lighten-1'"
                    variant="tonal" @click="toggleSelection(item.id)">
                    <v-icon>
                        {{ selectedIds.includes(item.id) ? 'mdi-checkbox-marked' : 'mdi-checkbox-blank-outline' }}
                    </v-icon>
                </v-btn>
            </template>
        </v-data-table>

        <div class="d-flex justify-end mb-4">
            <v-btn color="primary" @click="handleAssignClick" :disabled="!selectedIds.length">
                실무 테스트 할당
            </v-btn>
        </div>

        <!-- 실무 테스트 선택 모달 -->
        <JobtestSelectModal v-model="jobtestModal" :jobtests="jobtestListStore.jobtests"
            @select="handleJobtestSelected" />
    </v-container>
</template>

<script setup>
import { ref } from 'vue';
import { useToast } from 'vue-toastification';
import { useJobtestListStore } from '@/stores/jobtestListStore';
import { useApplicationJobtestStore } from '@/stores/applicationJobtestStore';
import ApplicationJobtestDTO from '@/dto/employment/jobtest/applicationJobtestDTO';
import JobtestSelectModal from '@/components/employment/JobtestSelectModal.vue';

const selectedIds = ref([]);
const jobtestModal = ref(false);
const toast = useToast();

const dummyApplications = [
    { id: 1, name: '김지원 지원서', email: 'kim@example.com' },
    { id: 3, name: '이승훈 지원서', email: 'lee@example.com' },
    { id: 4, name: '박하늘 지원서', email: 'park@example.com' }
];

const headers = [
    { text: '', value: 'actions', sortable: false, width: 48 }, // ⬅ 체크 아이콘용
    { text: '이름', value: 'name' },
    { text: '이메일', value: 'email' }
];

const jobtestListStore = useJobtestListStore();
const applicationJobtestStore = useApplicationJobtestStore();

const toggleSelection = (id) => {
    const idx = selectedIds.value.indexOf(id);
    if (idx > -1) {
        selectedIds.value.splice(idx, 1);
    } else {
        selectedIds.value.push(id);
    }
};

const handleAssignClick = async () => {
    try {
        await jobtestListStore.fetchJobtests();
        jobtestModal.value = true;
    } catch (e) {
        toast.error('실무 테스트 목록을 불러오는 데 실패했습니다.');
    }
};

const handleJobtestSelected = async (jobtest) => {
    jobtestModal.value = false;

    const dtoList = selectedIds.value.map(appId => {
        return new ApplicationJobtestDTO(appId, jobtest.id);
    });

    try {
        await applicationJobtestStore.assignJobtest(dtoList);
        toast.success('선택한 지원서에 문제를 성공적으로 할당했습니다.');
    } catch (e) {
        toast.error(applicationJobtestStore.errorMessage);
    }
};
</script>
