<template>
    <div v-if="loading" class="loading-container">
        <p>로딩 중...</p>
    </div>
    <div v-else-if="error" class="error-container">
        <p>오류가 발생했습니다: {{ error.message }}</p>
    </div>
    <div v-else-if="!approvalDetail" class="no-data-container">
        <p>결재 문서 정보를 찾을 수 없습니다.</p>
    </div>

    <div v-else class="page-container">
        <div class="header">
            <h1 class="page-title">요청한 결재 문서 조회</h1>
        </div>

        <div class="content-section">
            <table class="info-table">
                <tbody>
                    <tr>
                        <th>결재 유형</th>
                        <td>{{ approvalDetail.categoryName }}</td>
                        <th>작성일</th>
                        <td>{{ formatDate(approvalDetail.createdAt, 'date') }}</td>
                        <th>결재 완료일</th>
                        <td>{{ getCompletionDate() }}</td>
                        <th>결재 상태</th>
                        <td>
                            <span class="status-badge" :class="getStatusClass(approvalDetail.status)">
                                {{ getStatusText(approvalDetail.status) }}
                            </span>
                        </td>
                    </tr>
                </tbody>
            </table>

            <div class="approval-line-section">
                <h3>결재선</h3>
                <div class="approvers-list">
                    <div v-for="approver in approvalDetail.approvers" :key="approver.memberId" class="approver-item"
                        :class="getApproverStatusClass(approver)">
                        <div class="signature-box">
                            <span v-if="approver.approvedAt && approver.approved" class="stamp approved">승인</span>
                            <span v-else-if="approver.approvedAt && !approver.approved" class="stamp rejected">반려</span>
                        </div>
                        <div class="approver-name">{{ approver.memberName }} {{ approver.positionName }}</div>
                        <div v-if="approver.approvedAt" class="approval-date">
                            {{ formatDate(approver.approvedAt, 'date') }}
                        </div>
                    </div>
                </div>
            </div>

            <div class="attachment-section">
                <h3>첨부 문서</h3>
                <p>첨부된 문서가 없습니다.</p>
            </div>

            <div class="main-content">
                <h2 class="content-title">{{ approvalDetail.categoryName }}</h2>
                <div class="form-layout">
                    <div class="form-row">
                        <label>신청자</label>
                        <span>{{ approvalDetail.writerName }} ({{ approvalDetail.writerDepartment }})</span>
                    </div>
                    <template v-for="item in approvalDetail.items" :key="item.itemName">
                        <div class="form-row">
                            <label>{{ item.itemName }}</label>
                            <span>{{ item.content }}</span>
                        </div>
                    </template>
                </div>
                <div class="disclaimer">
                    <p>※ 해당 문서는 결재 완료 후 수정이 불가능하니, 내용을 정확히 입력해 주세요.</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script setup>
import { onMounted, onUnmounted } from 'vue';
import { useRoute } from 'vue-router';
import { storeToRefs } from 'pinia';
import { useApprovalStore } from '../../stores/approvalStore';

defineProps({
    id: { type: String, required: true }
});

const route = useRoute();
const approvalStore = useApprovalStore();

const { approvalDetail, loading, error } = storeToRefs(approvalStore);
const { fetchRequestedApprovalDetail, clearApprovalDetail } = approvalStore;

const formatDate = (dateString, format = 'full') => {
    if (!dateString) return '';
    const date = new Date(dateString);
    const options = {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
    };
    if (format === 'date') {
        return date.toLocaleDateString('ko-KR', options).replace(/\. /g, '-').replace('.', '');
    }
    return date.toLocaleString('ko-KR');
};

const getCompletionDate = () => {
    if (!approvalDetail.value) return '';
    const status = approvalDetail.value.status;
    if (status === 'APPROVED' || status === 'REJECTED') {
        const lastApprover = [...approvalDetail.value.approvers]
            .filter(a => a.approvedAt)
            .sort((a, b) => new Date(b.approvedAt) - new Date(a.approvedAt))[0];
        return lastApprover ? formatDate(lastApprover.approvedAt, 'date') : '';
    }
    return '';
};

const getStatusText = (status) => {
    const statusMap = {
        'IN_PROGRESS': '진행중',
        'APPROVED': '승인',
        'REJECTED': '반려',
        'CANCELLED': '취소'
    };
    return statusMap[status] || '대기';
};

const getStatusClass = (status) => {
    return `status-${status.toLowerCase()}`;
};

const getApproverStatusClass = (approver) => {
    if (approver.approvedAt) {
        return approver.approved ? 'approved' : 'rejected';
    }
    return 'pending';
};

onMounted(async () => {
    const approvalId = parseInt(route.params.id);
    if (approvalId) {
        await fetchRequestedApprovalDetail(approvalId);
    }
});

onUnmounted(() => {
    clearApprovalDetail();
});
</script>

<style scoped>
.page-container {
    padding: 2rem;
    background-color: #f9f9f9;
}
.loading-container, .error-container, .no-data-container {
    text-align: center;
    padding: 4rem;
    font-size: 1.2rem;
}
.header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 1.5rem;
}
.page-title {
    font-size: 1.8rem;
    font-weight: 600;
}
.content-section {
    background-color: #fff;
    padding: 2rem;
    border-radius: 8px;
    box-shadow: 0 2px 10px rgba(0, 0, 0, 0.05);
}
.info-table {
    width: 100%;
    border-collapse: collapse;
    margin-bottom: 2rem;
}
.info-table th, .info-table td {
    padding: 0.8rem;
    text-align: left;
    border-bottom: 1px solid #eee;
}
.info-table th {
    font-weight: 600;
    color: #555;
    background-color: #fcfcfc;
    width: 10%;
}
.info-table td {
    width: 15%;
}
.status-badge {
    padding: 0.3rem 0.8rem;
    border-radius: 12px;
    font-weight: 500;
    font-size: 0.8rem;
}
.status-badge.status-in_progress { background-color: #fff3cd; color: #856404; }
.status-badge.status-approved { background-color: #d4edda; color: #155724; }
.status-badge.status-rejected { background-color: #f8d7da; color: #721c24; }
.status-badge.status-cancelled { background-color: #e2e3e5; color: #383d41;}
.approval-line-section, .attachment-section {
    margin-bottom: 2rem;
}
h3 {
    font-size: 1.2rem;
    font-weight: 600;
    margin-bottom: 1rem;
    border-bottom: 1px solid #eee;
    padding-bottom: 0.5rem;
}
.approvers-list {
    display: flex;
    gap: 2.5rem;
    padding: 1rem 0;
}
.approver-item {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 0.5rem;
    width: 120px;
    text-align: center;
    padding: 1rem;
    border-radius: 8px;
    transition: all 0.2s ease-in-out;
}
.approver-name {
    font-weight: 600;
    font-size: 1rem;
}
.signature-box {
    width: 80px;
    height: 80px;
    border: 2px solid #e0e0e0;
    border-radius: 50%;
    margin: 0.5rem 0;
    display: flex;
    align-items: center;
    justify-content: center;
}
.approver-item.pending .signature-box {
    border-style: dashed;
}
.approver-item.approved .signature-box {
    border-color: #2ecc71;
    border-style: solid;
}
.approver-item.rejected .signature-box {
    border-color: #e74c3c;
    border-style: solid;
}
.stamp {
    font-size: 1.5rem;
    font-weight: bold;
    font-family: 'Gungsuh', serif;
}
.stamp.approved { color: #2ecc71; }
.stamp.rejected { color: #e74c3c; }
.approval-date {
    font-size: 0.8rem;
    color: #888;
}
.attachment-section p {
    color: #777;
}
.main-content {
    border: 1px solid #eee;
    border-radius: 8px;
    padding: 2rem;
}
.content-title {
    font-size: 1.5rem;
    font-weight: 600;
    text-align: center;
    margin-bottom: 2rem;
}
.form-layout {
    display: grid;
    gap: 1rem;
    margin-bottom: 2rem;
}
.form-row {
    display: grid;
    grid-template-columns: 120px 1fr;
    align-items: center;
}
.form-row label {
    font-weight: 600;
    color: #333;
}
.form-row span {
    padding: 0.6rem 1rem;
    border: 1px solid #eee;
    border-radius: 4px;
    background-color: #fdfdfd;
}
.disclaimer {
    margin-top: 2rem;
    padding-top: 1.5rem;
    border-top: 1px solid #eee;
    font-size: 0.85rem;
    color: #888;
}
.disclaimer p {
    margin: 0.3rem 0;
}
</style>
