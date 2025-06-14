<template>
    <v-app>
        <v-main>
            <v-container class="pa-6">
                <h2 class="text-h6 font-weight-bold mb-4">실무 테스트 리스트</h2>

                <v-progress-circular v-if="store.loading" indeterminate color="primary" class="mx-auto my-4" />
                <v-alert v-if="store.error" type="error" class="mb-4" closable @click:close="store.error = null">
                    {{ store.error }}
                </v-alert>

                <ListView :headers="headers" :data="store.jobtests" :showCheckbox="true"
                    @item-click="handleItemClick" />

                <div class="d-flex justify-end mt-4">
                    <v-btn color="error" variant="outlined" class="mr-2" :disabled="!store.hasSelectedJobtests"
                        @click="handleDelete">삭제하기</v-btn>
                    <v-btn color="success" @click="handleCreate">실무 테스트 등록하기</v-btn>
                </div>
            </v-container>
        </v-main>
    </v-app>
</template>


<script setup>
import { onMounted } from 'vue';
import { useRouter } from 'vue-router';
import ListView from '@/components/common/ListView.vue';
import { useJobtestListStore } from '@/stores/jobtestListStore';

const router = useRouter();
const store = useJobtestListStore();

const headers = [
    { label: '제목', key: 'title' },
    { label: '난이도', key: 'difficulty' },
    { label: '출제자', key: 'createdName' },
    { label: '수정자', key: 'updatedName' }
];

const handleItemClick = (item) => {
    store.toggleSelection(item.id);
};

const handleCreate = () => {
    router.push({ name: 'JobtestCreate' });
};

const handleDelete = () => {
    console.log('선택된 ID:', store.selectedIds);
    // TODO: 삭제 연동
};

onMounted(async () => {
    await store.fetchJobtests();
});
</script>
