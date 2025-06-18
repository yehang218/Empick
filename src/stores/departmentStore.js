import { defineStore } from 'pinia';
import { ref } from 'vue';
import { fetchDepartmentList } from '@/services/departmentService';

export const useDepartmentStore = defineStore('department', () => {
    const departmentList = ref([]);

    const loadDepartmentList = async () => {
        try {
            const result = await fetchDepartmentList();
            departmentList.value = result;
        } catch (err) {
            console.error('부서 목록 조회 실패:', err);
        }
    };

    return {
        departmentList,
        loadDepartmentList,
    };
}); 