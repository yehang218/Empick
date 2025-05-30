<template>
  <v-card class="pa-4" elevation="2" :style="cardStyle">
    <div style="max-height: 100%; overflow-y: auto;">
      <v-list dense>
        <v-list-item
          v-for="(item, index) in items"
          :key="index"
          class="mb-2"
          style="border: 1px solid #e0e0e0; border-radius: 8px;"
        >
          <div>
            <div class="d-flex justify-space-between align-center">
              <!-- 제목 -->
              <div class="font-weight-bold text-base">
                {{ index + 1 }}. {{ item.title }}
              </div>

              <!-- 배점 -->
              <div class="font-weight-medium" style="color: #6b8e23;">
                {{ item.weight }}%
              </div>
            </div>

            <!-- 설명 -->
            <div class="text-body-2 mt-1 pl-2">
              {{ item.description }}
            </div>
            </div>
        </v-list-item>
      </v-list>
    </div>
  </v-card>
</template>

<script setup>
defineProps({
  items: {
    type: Array,
    required: true,
  },
})

const cardStyle = `
  width: 35vw;
  height: 20vw;
  min-width: 200px;
  min-height: 200px;
  overflow-y: auto;
`
</script>
