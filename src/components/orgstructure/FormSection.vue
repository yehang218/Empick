<template>
    <div class="form-section">
        <div class="section-header">
            <div class="section-icon-wrapper">
                <v-icon class="section-icon">{{ icon }}</v-icon>
            </div>
            <div class="section-title">
                <h3 class="section-main">{{ title }}</h3>
                <p class="section-sub">{{ subtitle }}</p>
            </div>
        </div>
        <div class="form-fields">
            <slot />
        </div>
    </div>
</template>

<script setup>
// Props
defineProps({
    icon: {
        type: String,
        required: true
    },
    title: {
        type: String,
        required: true
    },
    subtitle: {
        type: String,
        default: ''
    }
})
</script>

<style scoped>
/* 폼 섹션 */
.form-section {
    margin-bottom: 1.5rem;
    background: rgba(255, 255, 255, 0.7);
    border-radius: 16px;
    padding: 1.5rem;
    border: 1px solid rgba(226, 232, 240, 0.3);
    backdrop-filter: blur(10px);
    box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
    height: fit-content;
}

.section-header {
    display: flex;
    align-items: center;
    gap: 1rem;
    margin-bottom: 1.5rem;
    padding-bottom: 1rem;
    border-bottom: 1px solid rgba(226, 232, 240, 0.3);
}

.section-icon-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 40px;
    height: 40px;
    background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
    border-radius: 10px;
    box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
    border: 1px solid rgba(226, 232, 240, 0.3);
}

.section-icon {
    color: #64748b;
    font-size: 20px;
}

.section-title {
    flex: 1;
}

.section-main {
    font-size: 1.125rem;
    font-weight: 600;
    color: #334155;
    margin: 0 0 0.25rem 0;
    line-height: 1.3;
}

.section-sub {
    font-size: 0.875rem;
    color: #64748b;
    margin: 0;
    font-weight: 400;
}

.form-fields {
    margin: 0;
}

/* 반응형 디자인 */
@media (max-width: 960px) {
    .form-section {
        padding: 1rem;
    }

    .section-header {
        flex-direction: column;
        align-items: flex-start;
        text-align: left;
    }
}
</style>