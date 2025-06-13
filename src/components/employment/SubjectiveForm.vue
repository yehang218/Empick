<template>
    <div>
        <v-textarea label="문제를 입력해주세요." v-model="form.content" auto-grow :error-messages="errors.content" />
        <v-text-field label="정답을 입력해주세요." v-model="form.answer" :error-messages="errors.answer" />
    </div>
</template>

<script setup>
defineProps({
    form: Object,
    errors: {
        type: Object,
        default: () => ({})
    }
});
</script>