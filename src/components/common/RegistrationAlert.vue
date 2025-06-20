<template>
    <v-alert v-if="visible" :type="type" class="registration-alert" border="start" variant="tonal">
        {{ message }}
    </v-alert>
</template>

<script setup>
// Props
defineProps({
    visible: {
        type: Boolean,
        default: false
    },
    type: {
        type: String,
        default: 'warning',
        validator: (value) => ['success', 'warning', 'error', 'info'].includes(value)
    },
    message: {
        type: String,
        default: ''
    }
})
</script>

<style scoped>
.registration-alert {
    position: fixed !important;
    top: 24px !important;
    right: 32px !important;
    left: auto !important;
    transform: none !important;
    z-index: 2000 !important;
    min-width: 320px !important;
    max-width: 90vw !important;
    backdrop-filter: blur(10px) !important;
    border-radius: 16px !important;
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1) !important;
    animation: slideInRight 0.3s ease-out;
}

@keyframes slideInRight {
    from {
        opacity: 0;
        transform: translateX(100%);
    }

    to {
        opacity: 1;
        transform: translateX(0);
    }
}

/* 반응형 디자인 */
@media (max-width: 768px) {
    .registration-alert {
        right: 16px !important;
        left: 16px !important;
        min-width: auto !important;
        max-width: none !important;
    }
}
</style>