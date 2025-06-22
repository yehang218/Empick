<template>
    <v-dialog :model-value="modelValue" max-width="600" @update:modelValue="emit('update:modelValue', $event)">
        <v-card>
            <v-card-title>자기소개서 템플릿 선택</v-card-title>
            <v-card-text>
                <v-expansion-panels v-model="panel">
                    <v-expansion-panel v-for="template in templates" :key="template.id" :value="template.id">
                        <v-expansion-panel-title>
                             <v-radio-group v-model="selectedId" @click.stop>
                                <v-radio :value="template.id" />
                            </v-radio-group>
                            {{ template.title }}
                        </v-expansion-panel-title>
                        <v-expansion-panel-text>
                            <div v-if="loadingDetail && panel === template.id">
                                <v-progress-circular indeterminate size="20"></v-progress-circular>
                                <span>질문 목록을 불러오는 중...</span>
                            </div>
                            <div v-else-if="selectedTemplate && selectedTemplate.id === template.id">
                                <v-list-item v-for="item in selectedTemplate.items" :key="item.id">
                                    <v-list-item-title>
                                        - {{ item.title }}
                                    </v-list-item-title>
                                </v-list-item>
                            </div>
                        </v-expansion-panel-text>
                    </v-expansion-panel>
                </v-expansion-panels>
            </v-card-text>
            <v-card-actions>
                <v-spacer />
                <v-btn text @click="close">닫기</v-btn>
                <v-btn color="primary" text @click="confirmSelection">확인</v-btn>
            </v-card-actions>
        </v-card>
    </v-dialog>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useIntroduceTemplateStore } from '@/stores/introduceTemplateStore'
import { storeToRefs } from 'pinia';

const props = defineProps({
    modelValue: Boolean
})
const emit = defineEmits(['update:modelValue', 'select'])

const store = useIntroduceTemplateStore()
const { templates, selectedTemplate, loadingDetail } = storeToRefs(store)

const panel = ref(null);
const selectedId = ref(null);

onMounted(async () => {
    if (!templates.value || templates.value.length === 0) {
        await store.loadTemplates()
    }
})

watch(panel, (newPanelId) => {
    if (newPanelId) {
        if (selectedTemplate.value?.id !== newPanelId) {
             store.loadTemplateDetail(newPanelId);
        }
    }
});

const confirmSelection = () => {
    if (selectedId.value) {
        const template = templates.value.find(t => t.id === selectedId.value);
        emit('select', { id: selectedId.value, title: template?.title });
        close();
    } else {
        alert('템플릿을 선택해주세요.');
    }
}

const close = () => {
    panel.value = null;
    emit('update:modelValue', false);
}
</script>

<style scoped>
.v-expansion-panel-title {
    display: flex;
    align-items: center;
}
.v-radio-group {
    flex: 0 0 auto;
    margin-right: 8px;
}
</style>
