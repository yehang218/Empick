<template>
    <div>
        <ListView :headers="headers" :data="tableData" />
    </div>
</template>

<script setup>
import ListView from '@/components/common/ListView.vue';

const headers = [
    { key: 'avatarName', label: '이름' },
    { key: 'id', label: '사번' },
    { key: 'email', label: '이메일' },
    { key: 'phone', label: '연락처' },
    { key: 'department', label: '부서' },
    { key: 'status', label: '처리상태' }
];

const tableData = [
    {
        name: '김석희',
        position: '대리',
        id: '87364523',
        email: 'brooklyns@mail.com',
        phone: '(603) 555-0123',
        department: '인사',
        departmentPosition: '팀장',
        status: '처리완료',
        avatar: 'https://cdn.vuetifyjs.com/images/john.jpg'
    },
];
</script>

<style lang="scss" scoped></style>