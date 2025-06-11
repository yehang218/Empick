<template>
    <v-app>
        <v-main>
            <v-container class="pa-6">
                <h2 class="text-h6 font-weight-bold mb-4">실무 테스트 문제 리스트</h2>

                <JobtestListView :headers="headers" :items="items" :showCheckbox="true" :showArrow="true" />

                <div class="d-flex justify-end mt-4">
                    <v-btn color="error" variant="outlined" class="mr-2">삭제하기</v-btn>
                    <v-btn color="success">문제 등록하기</v-btn>
                </div>
            </v-container>
        </v-main>
    </v-app>
</template>

<script setup>
import JobtestListView from '@/components/employment/JobtestListView.vue'

const headers = [
    { label: '문제 제목', key: 'title' },
    { label: '유형', key: 'type' },
    { label: '난이도', key: 'difficulty' },
    { label: '출제자', key: 'author', type: 'avatar' },
    { label: '채용 수정자', key: 'editor', type: 'avatar' },
]

const items = [
    {
        title: 'Big-O 표기법에 대한 설명으로 옳은것은?',
        type: '선택형',
        difficulty: '쉬움',
        author: { name: '김현식', avatar: 'https://randomuser.me/api/portraits/men/1.jpg' },
        editor: { name: '박수정', avatar: 'https://randomuser.me/api/portraits/women/1.jpg' },
        selected: false,
    },
    {
        title: 'TCP와 UDP의 설명으로 옳지 않은 것은?',
        type: '선택형',
        difficulty: '어려움',
        author: { name: '김현식', avatar: 'https://randomuser.me/api/portraits/men/1.jpg' },
        editor: { name: '박수정', avatar: 'https://randomuser.me/api/portraits/women/1.jpg' },
        selected: true,
    },
    {
        title: '다음 중 비선점형 스케줄링 알고리즘은?',
        type: '선택형',
        difficulty: '보통',
        author: { name: '김현식', avatar: 'https://randomuser.me/api/portraits/men/1.jpg' },
        editor: { name: '박수정', avatar: 'https://randomuser.me/api/portraits/women/1.jpg' },
        selected: false,
    },
    {
        title: '해시 테이블에서 충돌이 발생하는 이유를 설명하고, 이를 해결하는 방법은?',
        type: '서술형',
        difficulty: '쉬움',
        author: { name: '김현식', avatar: 'https://randomuser.me/api/portraits/men/1.jpg' },
        editor: { name: '박수정', avatar: 'https://randomuser.me/api/portraits/women/1.jpg' },
        selected: false,
    },
    {
        title: '스택(Stack) 자료구조의 연산 순서는 무엇인가요?',
        type: '서술형',
        difficulty: '쉬움',
        author: { name: '김현식', avatar: 'https://randomuser.me/api/portraits/men/1.jpg' },
        editor: { name: '박수정', avatar: 'https://randomuser.me/api/portraits/women/1.jpg' },
        selected: false,
    },
    {
        title: '프로세스와 스레드의 차이점을 설명하고, 스레드 기반 프로그램의 장단점은?',
        type: '단답형',
        difficulty: '쉬움',
        author: { name: '김현식', avatar: 'https://randomuser.me/api/portraits/men/1.jpg' },
        editor: { name: '박수정', avatar: 'https://randomuser.me/api/portraits/women/1.jpg' },
        selected: false,
    },
]
</script>

<style scoped></style>