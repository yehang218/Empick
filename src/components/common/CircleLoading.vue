<template>
    <v-overlay :model-value="visible" :persistent="persistent" class="modern-loading-overlay" :z-index="zIndex">
        <div class="loading-content">
            <!-- 로딩 스피너 -->
            <div class="spinner-container">
                <v-progress-circular :size="size" :width="width" :color="color" indeterminate class="modern-spinner" />
                <div class="spinner-glow"></div>
            </div>

            <!-- 로딩 메시지 -->
            <div v-if="message" class="message-container">
                <h3 class="main-message">{{ message }}</h3>
                <p v-if="subMessage" class="sub-message">{{ subMessage }}</p>
            </div>

            <!-- 진행률 표시 (옵션) -->
            <div v-if="showProgress && progress !== null" class="progress-container">
                <div class="progress-track">
                    <div class="progress-fill" :style="{ width: progress + '%' }"></div>
                </div>
                <span class="progress-percentage">{{ Math.round(progress) }}%</span>
            </div>
        </div>
    </v-overlay>
</template>

<script setup>
defineProps({
    visible: {
        type: Boolean,
        default: false
    },
    message: {
        type: String,
        default: ''
    },
    subMessage: {
        type: String,
        default: ''
    },
    size: {
        type: Number,
        default: 80
    },
    width: {
        type: Number,
        default: 6
    },
    color: {
        type: String,
        default: 'primary'
    },
    persistent: {
        type: Boolean,
        default: true
    },
    zIndex: {
        type: Number,
        default: 9999
    },
    showProgress: {
        type: Boolean,
        default: false
    },
    progress: {
        type: Number,
        default: null
    }
})
</script>

<style scoped>
/* 오버레이 배경 */
.modern-loading-overlay {
    background-color: rgba(0, 0, 0, 0.6);
    backdrop-filter: blur(8px);
    display: flex;
    align-items: center;
    justify-content: center;
}

/* 메인 컨테이너 */
.loading-content {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    text-align: center;
    animation: fadeInUp 0.4s cubic-bezier(0.4, 0, 0.2, 1);
}

/* 스피너 컨테이너 */
.spinner-container {
    position: relative;
    margin-bottom: 2rem;
    animation: pulse 2s ease-in-out infinite;
}

.modern-spinner {
    position: relative;
    z-index: 2;
    filter: drop-shadow(0 0 8px rgba(255, 255, 255, 0.3));
}

/* 스피너 글로우 효과 */
.spinner-glow {
    position: absolute;
    top: 50%;
    left: 50%;
    transform: translate(-50%, -50%);
    width: 120px;
    height: 120px;
    border-radius: 50%;
    background: radial-gradient(circle, rgba(33, 150, 243, 0.2) 0%, transparent 70%);
    animation: glow 1.5s ease-in-out infinite alternate;
    z-index: 1;
}

/* 메시지 컨테이너 */
.message-container {
    max-width: 400px;
    margin-bottom: 1.5rem;
}

.main-message {
    font-size: 1.5rem;
    font-weight: 600;
    margin-bottom: 0.75rem;
    color: #ffffff;
    text-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
    letter-spacing: -0.025em;
    line-height: 1.2;
}

.sub-message {
    font-size: 1rem;
    color: rgba(255, 255, 255, 0.85);
    margin: 0;
    line-height: 1.5;
    font-weight: 400;
    text-shadow: 0 1px 4px rgba(0, 0, 0, 0.2);
}

/* 진행률 컨테이너 */
.progress-container {
    width: 300px;
    margin-top: 1rem;
}

.progress-track {
    width: 100%;
    height: 6px;
    background-color: rgba(255, 255, 255, 0.2);
    border-radius: 3px;
    overflow: hidden;
    margin-bottom: 0.75rem;
}

.progress-fill {
    height: 100%;
    background: linear-gradient(90deg, #2196F3, #21CBF3);
    border-radius: 3px;
    transition: width 0.3s ease;
    box-shadow: 0 0 8px rgba(33, 150, 243, 0.6);
}

.progress-percentage {
    font-size: 0.9rem;
    font-weight: 500;
    color: rgba(255, 255, 255, 0.9);
    text-shadow: 0 1px 2px rgba(0, 0, 0, 0.3);
}

/* 애니메이션 */
@keyframes fadeInUp {
    from {
        opacity: 0;
        transform: translateY(20px);
    }

    to {
        opacity: 1;
        transform: translateY(0);
    }
}

@keyframes pulse {

    0%,
    100% {
        transform: scale(1);
    }

    50% {
        transform: scale(1.05);
    }
}

@keyframes glow {
    from {
        transform: translate(-50%, -50%) scale(0.8);
        opacity: 0.8;
    }

    to {
        transform: translate(-50%, -50%) scale(1.1);
        opacity: 0.4;
    }
}

/* 반응형 디자인 */
@media (max-width: 768px) {
    .loading-content {
        padding: 1rem;
    }

    .main-message {
        font-size: 1.3rem;
    }

    .sub-message {
        font-size: 0.9rem;
    }

    .progress-container {
        width: 250px;
    }

    .spinner-glow {
        width: 100px;
        height: 100px;
    }
}

@media (max-width: 480px) {
    .main-message {
        font-size: 1.2rem;
    }

    .sub-message {
        font-size: 0.85rem;
    }

    .progress-container {
        width: 200px;
    }

    .spinner-container {
        margin-bottom: 1.5rem;
    }
}
</style>