<template>
    <div class="page-header-modern">
        <div class="header-icon-wrapper">
            <v-icon :size="iconSize" class="header-icon">{{ icon }}</v-icon>
        </div>
        <div class="header-content">
            <h1 class="page-title-modern">{{ title }}</h1>
            <p class="page-subtitle-modern">{{ subtitle }}</p>
        </div>
    </div>
</template>

<script setup>
// Props
defineProps({
    icon: {
        type: String,
        default: 'mdi-home'
    },
    iconSize: {
        type: [String, Number],
        default: 32
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
.page-header-modern {
    display: flex;
    align-items: center;
    gap: 1.5rem;
    padding: 2rem;
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
    border-radius: 20px;
    border: 1px solid rgba(255, 255, 255, 0.2);
    backdrop-filter: blur(10px);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.08);
    margin-bottom: 2rem;
    position: relative;
    overflow: hidden;
}

.page-header-modern::before {
    content: '';
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    height: 4px;
    background: linear-gradient(90deg, #64748b 0%, #94a3b8 50%, #64748b 100%);
}

.header-icon-wrapper {
    display: flex;
    align-items: center;
    justify-content: center;
    width: 64px;
    height: 64px;
    background: linear-gradient(135deg, #ffffff 0%, #f8fafc 100%);
    border-radius: 16px;
    box-shadow: 0 8px 24px rgba(0, 0, 0, 0.1);
    border: 3px solid rgba(255, 255, 255, 0.8);
    flex-shrink: 0;
}

.header-icon {
    color: #64748b;
}

.header-content {
    flex: 1;
    min-width: 0;
}

.page-title-modern {
    font-size: 2rem;
    font-weight: 700;
    color: #1e293b;
    margin: 0 0 0.5rem 0;
    line-height: 1.2;
}

.page-subtitle-modern {
    font-size: 1.125rem;
    color: #64748b;
    margin: 0;
    font-weight: 400;
    line-height: 1.5;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
    .page-header-modern {
        flex-direction: column;
        text-align: center;
        padding: 1.5rem;
    }

    .page-title-modern {
        font-size: 1.5rem;
    }

    .page-subtitle-modern {
        font-size: 1rem;
    }
}
</style>