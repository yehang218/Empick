<template>
    <v-container fluid class="fill-height pa-0">
      <!-- Removed v-navigation-drawer here -->

      <v-main>
        <v-container fluid class="pa-6">
          <v-row align="center">
            <v-col cols="10">
              <h2 class="mb-4">내가 작성한 결재 문서 목록</h2>
            </v-col>
            <v-col cols="2" class="text-right">
              <v-btn color="primary" @click="createApprovalDocument">
                <v-icon left>mdi-plus</v-icon>
                결재 문서 작성하기
              </v-btn>
            </v-col>
          </v-row>
  
          <v-row class="mb-6">
            <v-col cols="3">
              <v-card class="pa-4 text-center">
                <v-card-title class="justify-center text-h5">30</v-card-title>
                <v-card-subtitle>총 결재 문서 수</v-card-subtitle>
              </v-card>
            </v-col>
            <v-col cols="3">
              <v-card class="pa-4 text-center">
                <v-card-title class="justify-center text-h5">23</v-card-title>
                <v-card-subtitle>승인 완료</v-card-subtitle>
              </v-card>
            </v-col>
            <v-col cols="3">
              <v-card class="pa-4 text-center">
                <v-card-title class="justify-center text-h5">4</v-card-title>
                <v-card-subtitle>진행 중</v-card-subtitle>
              </v-card>
            </v-col>
            <v-col cols="3">
              <v-card class="pa-4 text-center">
                <v-card-title class="justify-center text-h5">3</v-card-title>
                <v-card-subtitle>반려</v-card-subtitle>
              </v-card>
            </v-col>
          </v-row>
  
          <v-card>
            <v-card-title>
              <v-text-field
                v-model="search"
                append-icon="mdi-magnify"
                label="제목 검색"
                single-line
                hide-details
              ></v-text-field>
            </v-card-title>
            <v-data-table
              :headers="headers"
              :items="documents"
              :search="search"
              :items-per-page="10"
              class="elevation-1"
            >
              <template v-slot:item.status="{ item }">
                <v-chip :color="getStatusColor(item.status)" dark>{{ item.status }}</v-chip>
              </template>
              <template v-slot:item.progress="{ item }">
                <span>{{ item.currentStep }}/{{ item.totalSteps }}</span>
              </template>
            </v-data-table>
            <div class="text-center pt-2">
              <v-pagination v-model="page" :length="pageCount"></v-pagination>
            </div>
          </v-card>
        </v-container>
      </v-main>
    </v-container>
  </template>
  
  <script>
  export default {
    name: 'MyApprovalDocumentsPage',
    data() {
      return {
        darkMode: false,
        search: '',
        page: 1,
        pageCount: 0,
        headers: [
          { text: '제목', value: 'title' },
          { text: '결재 유형', value: 'type' },
          { text: '구분', value: 'category' },
          { text: '작성일', value: 'createdDate' },
          { text: '결재 완료일', value: 'completedDate' },
          { text: '현재 결재자', value: 'currentApprover' },
          { text: '상태', value: 'status' },
          { text: '결재 진행률', value: 'progress' },
        ],
        documents: [], // This will be populated from an API call
      };
    },
    created() {
      this.loadDocuments();
    },
    methods: {
      createApprovalDocument() {
        // Navigate to the document creation page
        console.log('결재 문서 작성하기 클릭');
        // Example: this.$router.push('/approval/create');
      },
      getStatusColor(status) {
        if (status === '승인') return 'green';
        if (status === '반려') return 'red';
        if (status === '진행 중') return 'orange';
        return 'grey';
      },
      loadDocuments() {
        // Mock data for now, replace with actual API call
        this.documents = [
          {
            id: 1,
            title: '휴가 신청서',
            type: '휴가',
            category: '연차',
            createdDate: '2025-05-20',
            completedDate: '2025-05-21',
            currentApprover: '-',
            status: '승인',
            currentStep: 4,
            totalSteps: 4,
          },
          {
            id: 2,
            title: '퇴사 신청서',
            type: '퇴사',
            category: '-',
            createdDate: '2025-05-18',
            completedDate: '2025-05-19',
            currentApprover: '-',
            status: '반려',
            currentStep: 2,
            totalSteps: 3,
          },
          {
            id: 3,
            title: '교육 신청서',
            type: '교육',
            category: '외부 교육',
            createdDate: '2025-05-22',
            completedDate: '-',
            currentApprover: '인사팀장',
            status: '진행 중',
            currentStep: 1,
            totalSteps: 3,
          },
          {
            id: 4,
            title: '출장 신청서',
            type: '출장',
            category: '-',
            createdDate: '2025-05-17',
            completedDate: '2025-05-18',
            currentApprover: '-',
            status: '승인',
            currentStep: 3,
            totalSteps: 3,
          },
          {
            id: 5,
            title: '회의 보고서',
            type: '회의',
            category: '-',
            createdDate: '2025-05-15',
            completedDate: '2025-05-16',
            currentApprover: '-',
            status: '승인',
            currentStep: 2,
            totalSteps: 2,
          },
          {
            id: 6,
            title: '공지 요청서',
            type: '공지',
            category: '사내 공지',
            createdDate: '2025-05-14',
            completedDate: '2025-05-15',
            currentApprover: '-',
            status: '승인',
            currentStep: 2,
            totalSteps: 2,
          },
          {
            id: 7,
            title: '휴가 신청서',
            type: '휴가',
            category: '반차',
            createdDate: '2025-05-23',
            completedDate: '-',
            currentApprover: '본부장',
            status: '진행 중',
            currentStep: 2,
            totalSteps: 4,
          },
          {
            id: 8,
            title: '교육 신청서',
            type: '교육',
            category: '외부 교육',
            createdDate: '2025-05-19',
            completedDate: '2025-05-21',
            currentApprover: '-',
            status: '승인',
            currentStep: 3,
            totalSteps: 3,
          },
        ];
        this.pageCount = Math.ceil(this.documents.length / 10); // Calculate page count
      },
    },
  };
  </script>
  
  <style scoped>
  .fill-height {
    height: 100vh;
  }
  </style> 