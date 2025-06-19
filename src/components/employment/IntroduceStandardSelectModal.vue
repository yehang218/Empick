<template>
  <v-dialog v-model="visible" max-width="600">
    <v-card>
      <v-card-title class="d-flex justify-space-between align-center">
        <span>기준표 선택</span>
        <v-btn icon @click="close">
          <v-icon>mdi-close</v-icon>
        </v-btn>
      </v-card-title>
      <v-divider />
      <v-card-text>
        <v-radio-group v-model="selectedStandardId">
          <v-list lines="two">
            <template v-for="(standard, index) in standards || []" :key="standard.id">
              <v-list-item>
                <template v-slot:prepend>
                  <v-radio :value="standard.id" hide-details color="primary" />
                </template>
                <v-list-item-title>{{ standard.content }}</v-list-item-title>
              </v-list-item>
              <v-divider v-if="index < standards.length - 1" inset></v-divider>
            </template>
            <v-list-item v-if="standards.length === 0">
              <v-list-item-title>등록된 기준표가 없습니다.</v-list-item-title>
            </v-list-item>
          </v-list>
        </v-radio-group>
      </v-card-text>
      <v-card-actions class="justify-end">
        <v-btn color="primary" @click="selectStandard" :disabled="!selectedStandardId">선택</v-btn>
        <v-btn text @click="close">취소</v-btn>
      </v-card-actions>
    </v-card>
  </v-dialog>
</template>

<script setup>
import { ref, watch, computed, onMounted } from 'vue'
import { useIntroduceStandardStore } from '@/stores/introduceStandardStore'
import { useIntroduceStandardItemStore } from '@/stores/introduceStandardItemStore'

const props = defineProps({
  modelValue: Boolean
})
const emit = defineEmits(['update:modelValue', 'select'])

const visible = computed({
  get: () => props.modelValue,
  set: v => emit('update:modelValue', v)
})

const store = useIntroduceStandardStore()
const itemStore = useIntroduceStandardItemStore()
const selectedStandardId = ref(null)

onMounted(() => {
  store.fetchStandards()
})

const standards = computed(() => store.standards)

function close() {
  visible.value = false
}

function selectStandard() {
  const selected = standards.value.find(s => s.id === selectedStandardId.value)
  if (selected) {
    let items = []
    if (selected.items && selected.items.length > 0) {
      items = selected.items
    } else if (selected.itemIds && Array.isArray(selected.itemIds)) {
      items = itemStore.items.filter(item => selected.itemIds.includes(item.id))
    }
    emit('select', { ...selected, items })
    close()
  }
}
</script>

<style scoped>
.v-card-title {
  font-size: 1.2rem;
  font-weight: bold;
}
</style> 