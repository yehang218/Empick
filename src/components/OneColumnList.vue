<template>
  <div class="selector-wrapper">
    <div class="title">{{ title }}</div>

    <v-card
      class="pa-3"
      outlined
      rounded="lg"
      style="max-height: 250px; overflow-y: auto"
    >
      <v-list density="compact">
        <v-list-item
          v-for="(item, index) in items"
          :key="index"
          @click="selected = index"
          :class="{ 'selected-item': selected === index }"
        >
          <v-list-item-title>{{ item }}</v-list-item-title>
        </v-list-item>
      </v-list>
    </v-card>
  </div>
</template>

<script setup>
import { ref } from 'vue'

// Props 받아오기
defineProps({
  title: {
    type: String,
    required: true
  },
  items: {
    type: Array,
    required: true
  }
})

const selected = ref(0)
</script>

<style scoped>
.selector-wrapper {
  position: relative;
  width: 200px;
  margin-top: 20px;
}
.title {
  position: absolute;
  top: -18px;
  left: 50%;
  transform: translateX(-50%);
  background: white;
  padding: 0 8px;
  color: grey;
  font-size: 14px;
}
.selected-item {
  font-weight: bold;
  color: #2e7d32 !important;
}
.v-list-item {
  cursor: pointer;
}
.v-list-item:hover {
  background-color: #f5f5f5;
}
.v-card {
  border: 1px solid #e0e0e0;
}
</style>