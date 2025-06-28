<template>
  <div>
    <CareerHeader />
    <div class="welfare-container">
      <div class="page-header">
        <h1 class="page-title">복지 제도</h1>
        <p class="page-subtitle">EMPICK과 함께 성장하는 당신을 위한 다양한 복지 혜택을 제공합니다</p>
      </div>

      <v-row class="welfare-grid" dense>
        <v-col v-for="(item, index) in welfares" :key="index" cols="12" sm="6" md="4" lg="4" class="welfare-card-col">
          <div class="welfare-card">
            <div class="icon-container">
              <img :src="item.icon" alt="" class="welfare-icon" />
            </div>
            <div class="welfare-content">
              <h3 class="welfare-title">{{ item.title }}</h3>
              <p class="welfare-description">{{ item.description }}</p>
            </div>
          </div>
        </v-col>
      </v-row>
    </div>
  </div>
</template>

<script setup>
import CareerHeader from '@/components/career/CareerHeader.vue'
import insuranceIcon from '@/assets/career/insurances.png'
import vacationIcon from '@/assets/career/vacation.png'
import mealSupportIcon from '@/assets/career/mealSupport.png'
import healthIcon from '@/assets/career/health.png'
import transportationIcon from '@/assets/career/transportation.png'
import educationIcon from '@/assets/career/education.png'

const welfares = [
  {
    title: '4대보험',
    description: '모든 구성원이 든든하게 일할 수 있도록 국민연금, 건강보험, 고용보험, 산재보험을 지원합니다.',
    icon: insuranceIcon
  },
  {
    title: '보상 휴가 제공',
    description: '야근, 주말 근무? 걱정 마세요! 합당한 휴식은 반드시 보장됩니다.',
    icon: vacationIcon
  },
  {
    title: '식대 지원',
    description: '점심값 걱정은 NO! 맛있게 먹고 힘내세요.',
    icon: mealSupportIcon
  },
  {
    title: '정기 건강검진',
    description: '당신의 건강이 우리의 자산! 매년 정기 검진을 통해 꼼꼼히 챙겨드립니다.',
    icon: healthIcon
  },
  {
    title: '교통비 지원',
    description: '지하철이든 자가용이든, 출퇴근 부담 덜어드릴게요.',
    icon: transportationIcon
  },
  {
    title: '교육 및 자기계발비 지원',
    description: '성장하는 당신을 응원합니다. 외부 강의, 온라인 강좌도 OK!',
    icon: educationIcon
  }
]
</script>

<style scoped>
.welfare-container {
  max-width: none;
  margin: 0;
  padding: 60px 0;
  background: linear-gradient(135deg, #f8fafc 0%, #e2e8f0 100%);
  min-height: 100vh;
}

.page-header {
  text-align: center;
  margin-bottom: 60px;
  padding: 0 24px;
}

.page-title {
  font-size: 42px;
  font-weight: 800;
  color: #1e293b;
  margin-bottom: 16px;
  letter-spacing: -0.025em;
}

.page-subtitle {
  font-size: 18px;
  color: #64748b;
  line-height: 1.6;
  max-width: 600px;
  margin: 0 auto;
}

.welfare-grid {
  margin-top: 0;
  padding: 0 24px;
}

.welfare-card-col {
  padding: 16px;
}

.welfare-card {
  background: white;
  border-radius: 16px;
  padding: 32px 24px;
  height: 100%;
  box-shadow: 0 4px 6px -1px rgba(0, 0, 0, 0.1), 0 2px 4px -1px rgba(0, 0, 0, 0.06);
  transition: all 0.3s cubic-bezier(0.4, 0, 0.2, 1);
  border: 1px solid #e2e8f0;
  position: relative;
  overflow: hidden;
}

.welfare-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  height: 4px;
  background: linear-gradient(90deg, #3b82f6, #8b5cf6);
  transform: scaleX(0);
  transition: transform 0.3s ease;
}

.welfare-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 20px 25px -5px rgba(0, 0, 0, 0.1), 0 10px 10px -5px rgba(0, 0, 0, 0.04);
}

.welfare-card:hover::before {
  transform: scaleX(1);
}

.icon-container {
  display: flex;
  justify-content: center;
  align-items: center;
  margin-bottom: 24px;
  height: 80px;
}

.welfare-icon {
  height: 64px;
  width: auto;
  filter: drop-shadow(0 4px 6px rgba(0, 0, 0, 0.1));
  transition: transform 0.3s ease;
}

.welfare-card:hover .welfare-icon {
  transform: scale(1.1);
}

.welfare-content {
  text-align: center;
}

.welfare-title {
  font-size: 20px;
  font-weight: 700;
  color: #1e293b;
  margin-bottom: 12px;
  line-height: 1.3;
}

.welfare-description {
  font-size: 15px;
  color: #64748b;
  line-height: 1.6;
  margin: 0;
  font-weight: 400;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
  .welfare-container {
    padding: 40px 0;
  }

  .page-header {
    padding: 0 16px;
  }

  .welfare-grid {
    padding: 0 16px;
  }

  .page-title {
    font-size: 32px;
  }

  .page-subtitle {
    font-size: 16px;
  }

  .welfare-card {
    padding: 24px 20px;
  }

  .welfare-title {
    font-size: 18px;
  }

  .welfare-description {
    font-size: 14px;
  }
}

@media (max-width: 480px) {
  .welfare-container {
    padding: 32px 0;
  }

  .page-header {
    padding: 0 12px;
  }

  .welfare-grid {
    padding: 0 12px;
  }

  .page-title {
    font-size: 28px;
  }

  .welfare-card-col {
    padding: 12px;
  }
}
</style>
