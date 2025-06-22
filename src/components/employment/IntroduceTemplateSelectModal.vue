<template>
    <v-dialog :model-value="modelValue" max-width="600" @update:modelValue="emit('update:modelValue', $event)">
        <v-card>
            <v-card-title>자기소개서 템플릿 선택</v-card-title>
            <v-card-text>
                <v-list>
                    <v-list-item v-for="template in templates" :key="template.id" @click="selectTemplate(template.id)"
                        class="template-item">
                        <v-list-item-title>{{ template.title }}</v-list-item-title>
                    </v-list-item>
                </v-list>
            </v-card-text>
            <v-card-actions>
                <v-spacer />
                <v-btn text @click="close">닫기</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useIntroduceTemplateStore } from '@/stores/introduceTemplateStore'

const props = defineProps({
    modelValue: Boolean
})
const emit = defineEmits(['update:modelValue', 'select'])

const store = useIntroduceTemplateStore()
const templates = ref([])

onMounted(async () => {
    await store.loadTemplates()
    templates.value = store.templates
})

const selectTemplate = (id) => {
    emit('select', id)
    emit('update:modelValue', false)
}
const close = () => emit('update:modelValue', false)
</script>

<style scoped>
.template-item {
    cursor: pointer;
}
</style>
