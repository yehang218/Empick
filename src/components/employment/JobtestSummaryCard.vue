<template>
    <v-card variant="outlined" class="summary-card pa-6 mb-4">
        <div class="card-header">
            <div class="title-section">
                <h3 class="card-title">{{ jobtest.title }}</h3>
                <div class="difficulty-section">
                    <span class="difficulty-label">난이도:</span>
                    <span class="difficulty-tag" :style="getDifficultyStyle(jobtest.difficulty)">
                        {{ getDifficultyLabel(jobtest.difficulty) }}
                    </span>
                </div>
            </div>
            <div class="test-time-badge">
                <v-icon class="time-icon">mdi-clock-outline</v-icon>
                <span class="time-text">{{ jobtest.testTime }}분</span>
            </div>
        </div>

        <v-divider class="my-4"></v-divider>

        <div class="card-content">
            <div class="info-grid">
                <div class="info-item">
                    <div class="info-label">
                        <v-icon class="info-icon">mdi-calendar-start</v-icon>
                        시작일시
                    </div>
                    <div class="info-value">{{ formatDate(jobtest.startedAt) }}</div>
                </div>
                <div class="info-item">
                    <div class="info-label">
                        <v-icon class="info-icon">mdi-calendar-end</v-icon>
                        종료일시
                    </div>
                    <div class="info-value">{{ formatDate(jobtest.endedAt) }}</div>
                </div>
                <div class="info-item">
                    <div class="info-label">
                        <v-icon class="info-icon">mdi-account-plus</v-icon>
                        생성자
                    </div>
                    <div class="info-value">{{ jobtest.createdName }} ({{ formatDate(jobtest.createdAt) }})</div>
                </div>
                <div class="info-item">
                    <div class="info-label">
                        <v-icon class="info-icon">mdi-account-edit</v-icon>
                        수정자
                    </div>
                    <div class="info-value">{{ jobtest.updatedName }} ({{ formatDate(jobtest.updatedAt) }})</div>
                </div>
            </div>
        </div>
    </v-card>
</template>

<script setup>
import { computed } from 'vue'
import { getDifficultyLabel, getDifficultyColors } from '@/constants/employment/difficulty.js'

const props = defineProps({
    jobtest: Object
})

const getDifficultyStyle = (difficulty) => {
    const colors = getDifficultyColors(difficulty);
    return {
        backgroundColor: colors.background,
        color: colors.text,
        padding: '6px 12px',
        borderRadius: '20px',
        fontSize: '0.75rem',
        fontWeight: '600',
        textTransform: 'uppercase',
        letterSpacing: '0.5px',
        boxShadow: '0 2px 4px rgba(0, 0, 0, 0.1)',
        transition: 'all 0.2s ease',
        display: 'inline-flex',
        alignItems: 'center',
        border: '1px solid rgba(255, 255, 255, 0.2)'
    };
};

const formatDate = (dateStr) => {
    if (!dateStr) return '-'
    const date = new Date(dateStr)
    return `${date.toLocaleDateString()} ${date.toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}`
}
</script>

<style scoped>
.summary-card {
    background: white;
    border-radius: 16px;
    box-shadow: 0 2px 12px rgba(0, 0, 0, 0.08);
    transition: all 0.3s ease;
    border: 1px solid rgba(0, 0, 0, 0.05);
    overflow: hidden;
    position: relative;
}

.summary-card::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, #1976d2, #42a5f5);
    border-radius: 16px 16px 0 0;
}

.summary-card:hover {
    transform: translateY(-4px);
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.12);
}

.card-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    gap: 16px;
}

.title-section {
    flex: 1;
}

.card-title {
    color: #1a237e;
    font-size: 1.5rem;
    font-weight: 700;
    margin-bottom: 12px;
    line-height: 1.3;
}

.difficulty-section {
    display: flex;
    align-items: center;
    gap: 8px;
}

.difficulty-label {
    color: #666;
    font-size: 0.875rem;
    font-weight: 500;
}

.difficulty-tag:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.15);
}

.test-time-badge {
    display: flex;
    align-items: center;
    gap: 6px;
    background: linear-gradient(135deg, #4caf50, #66bb6a);
    color: white;
    padding: 8px 16px;
    border-radius: 20px;
    font-weight: 600;
    font-size: 0.875rem;
    box-shadow: 0 2px 8px rgba(76, 175, 80, 0.3);
    transition: all 0.2s ease;
}

.test-time-badge:hover {
    transform: translateY(-1px);
    box-shadow: 0 4px 12px rgba(76, 175, 80, 0.4);
}

.time-icon {
    font-size: 1rem;
}

.time-text {
    font-weight: 600;
}

.card-content {
    margin-top: 8px;
}

.info-grid {
    display: grid;
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 20px;
}

.info-item {
    display: flex;
    flex-direction: column;
    gap: 6px;
}

.info-label {
    display: flex;
    align-items: center;
    gap: 8px;
    color: #666;
    font-size: 0.875rem;
    font-weight: 500;
}

.info-icon {
    color: #1976d2;
    font-size: 1rem;
}

.info-value {
    color: #333;
    font-weight: 500;
    font-size: 0.9rem;
    padding-left: 24px;
    line-height: 1.4;
}

/* 반응형 디자인 */
@media (max-width: 960px) {
    .summary-card {
        border-radius: 12px;
        padding: 20px;
    }
    
    .card-header {
        flex-direction: column;
        align-items: stretch;
        gap: 12px;
    }
    
    .test-time-badge {
        align-self: flex-start;
    }
    
    .info-grid {
        grid-template-columns: 1fr;
        gap: 16px;
    }
    
    .card-title {
        font-size: 1.3rem;
    }
}

@media (max-width: 600px) {
    .summary-card {
        border-radius: 8px;
        padding: 16px;
    }
    
    .card-title {
        font-size: 1.2rem;
        margin-bottom: 8px;
    }
    
    .difficulty-section {
        flex-direction: column;
        align-items: flex-start;
        gap: 4px;
    }
    
    .info-item {
        gap: 4px;
    }
    
    .info-value {
        padding-left: 20px;
        font-size: 0.85rem;
    }
}
</style>
