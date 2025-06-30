<template>
  <div class="selector-wrapper">
    <div class="title">{{ title }}</div>

    <v-card
      class="pa-3"
      outlined
      rounded="lg"
      style="height: 100%; overflow-y: auto"
    >
      <v-list density="compact">
        <v-list-item
          v-for="(item, index) in items"
          :key="index"
           @click="selectItem(index)"
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

// props 정의
const props = defineProps({
  title: {
    type: String,
    required: true
  },
  items: {
    type: Array,
    required: true
  }
})


// emit 선언
const emit = defineEmits(['update:selectedItem'])

const selected = ref(0)

// 선택 시 emit 호출
const selectItem = (index) => {
  selected.value = index
  emit('update:selectedItem', props.items[index])
}

</script>

<style scoped>
.selector-wrapper {
  position: relative;
  width: 100%; /* 또는 아예 제거 */
  height: 100%;
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