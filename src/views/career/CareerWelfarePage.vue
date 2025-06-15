<template>
  <div>
    <CareerHeader />
    <div class="welfare-container">
      <h2 class="section-title">복지 제도</h2>
      <v-divider class="my-4" />

      <v-row class="mt-6" dense>
        <v-col
          v-for="(item, index) in welfares"
          :key="index"
          cols="12" sm="6" md="4" lg="3"
          class="welfare-card"
        >
          <div class="icon-box">
            <img :src="item.icon" alt="" class="icon" />
          </div>
          <div class="title">{{ item.title }}</div>
          <div class="desc">{{ item.description }}</div>
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
  max-width: 1800px;
  margin: 0 auto;
  padding: 40px 32px;
}

.section-title {
  font-size: 28px;
  font-weight: bold;
  margin-bottom: 24px;
}

.welfare-card {
  text-align: center;
  padding: 24px;
}

.icon-box {
  height: 100px;
  margin-bottom: 16px;
}

.icon {
  height: 64px;
}

.title {
  font-weight: 700;
  font-size: 18px;
  margin-bottom: 10px;
}

.desc {
  font-size: 15px;
  color: #444;
  line-height: 1.5;
}
</style>
