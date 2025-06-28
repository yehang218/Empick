<template>
    <div class="standard-item-page">
        <div class="content-wrapper">
            <div class="page-header">
                <h1 class="page-title">자기소개서 표준항목 관리</h1>
                <p class="page-subtitle">채용에 사용할 표준항목을 관리합니다</p>
            </div>

            <div class="coming-soon-section">
                <div class="coming-soon-content">
                    <v-icon size="x-large" color="primary" class="feature-icon">mdi-construction</v-icon>
                    <h2 class="coming-soon-title">기능 개발 중</h2>
                    <p class="coming-soon-description">
                        자기소개서 표준항목 관리 기능이 현재 개발 중입니다.<br>
                        곧 더 나은 서비스를 제공해드리겠습니다.
                    </p>
                    <v-btn color="primary" variant="outlined" size="large" class="back-btn">
                        <v-icon left>mdi-arrow-left</v-icon>
                        이전 페이지로
                    </v-btn>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
export default {
    name: 'IntroduceStandardItemPage',
    data() {
        return {
            // TODO: 데이터 추가
        }
    }
}
</script>

<style scoped>
.standard-item-page {
    max-width: none;
    margin: 0;
    padding: 40px 0;
    background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
    min-height: 100vh;
}

.content-wrapper {
    background: white;
    border-radius: 0;
    padding: 40px 60px;
    box-shadow: none;
    margin: 0;
    max-width: none;
}

.page-header {
    text-align: center;
    margin-bottom: 40px;
    padding-bottom: 32px;
    border-bottom: 2px solid #f1f5f9;
}

.page-title {
    font-size: 32px;
    font-weight: 800;
    color: #1e293b;
    margin-bottom: 16px;
    letter-spacing: -0.025em;
}

.page-subtitle {
    font-size: 16px;
    color: #64748b;
    line-height: 1.6;
    max-width: 500px;
    margin: 0 auto;
}

.coming-soon-section {
    padding: 60px 24px;
}

.coming-soon-content {
    text-align: center;
    max-width: 500px;
    margin: 0 auto;
}

.feature-icon {
    margin-bottom: 24px;
    opacity: 0.7;
}

.coming-soon-title {
    font-size: 24px;
    font-weight: 700;
    color: #1e293b;
    margin-bottom: 16px;
}

.coming-soon-description {
    font-size: 16px;
    color: #64748b;
    line-height: 1.6;
    margin-bottom: 32px;
}

.back-btn {
    border-radius: 12px;
    font-weight: 600;
    height: 48px;
    min-width: 160px;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
    .standard-item-page {
        padding: 32px 0;
    }
    
    .content-wrapper {
        margin: 0;
        padding: 32px 40px;
    }
    
    .page-title {
        font-size: 28px;
    }
    
    .page-subtitle {
        font-size: 15px;
    }
    
    .coming-soon-section {
        padding: 40px 16px;
    }
    
    .coming-soon-title {
        font-size: 20px;
    }
    
    .coming-soon-description {
        font-size: 15px;
    }
}

@media (max-width: 480px) {
    .standard-item-page {
        padding: 24px 0;
    }
    
    .content-wrapper {
        margin: 0;
        padding: 24px 20px;
    }
    
    .page-title {
        font-size: 24px;
    }
    
    .coming-soon-section {
        padding: 32px 12px;
    }
    
    .coming-soon-title {
        font-size: 18px;
    }
}
</style>
