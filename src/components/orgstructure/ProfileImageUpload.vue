<template>
    <div class="profile-container">
        <div class="profile-image-wrapper" @click="triggerFileInput">
            <template v-if="profileImageUrl">
                <v-img :src="profileImageUrl" class="profile-image" cover />
            </template>
            <template v-else>
                <div class="profile-placeholder">
                    <v-icon size="48" class="placeholder-icon">mdi-camera-plus</v-icon>
                    <p class="placeholder-text">사진 업로드</p>
                </div>
            </template>
            <div class="image-overlay">
                <v-icon color="white" size="24">mdi-camera-plus</v-icon>
            </div>
            <input ref="fileInputRef" type="file" accept="image/jpeg,image/png,image/webp" style="display: none;"
                @change="onFileChange" />
        </div>

        <v-btn class="upload-btn" :variant="profileImageFile ? 'tonal' : 'outlined'"
            :color="profileImageFile ? 'success' : 'primary'" @click="triggerFileInput" size="small">
            <v-icon size="16" class="mr-1">
                {{ profileImageFile ? 'mdi-check' : 'mdi-upload' }}
            </v-icon>
            {{ photoButtonText }}
        </v-btn>
    </div>
</template>

<script setup>
import { ref } from 'vue'

// Props
defineProps({
    profileImageUrl: {
        type: String,
        default: ''
    },
    profileImageFile: {
        type: File,
        default: null
    },
    photoButtonText: {
        type: String,
        default: '사진 업로드'
    }
})

// Emits
const emit = defineEmits(['fileChange'])

// Refs
const fileInputRef = ref(null)

// Methods
const triggerFileInput = () => {
    fileInputRef.value?.click()
}

const onFileChange = (event) => {
    emit('fileChange', event)
}
</script>

<style scoped>
.profile-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 1rem;
    width: 100%;
}

/* 프로필 이미지 스타일 */
.profile-image-wrapper {
    position: relative;
    width: 180px;
    height: 180px;
    border-radius: 20px;
    overflow: hidden;
    cursor: pointer;
    background: linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%);
    border: 3px solid rgba(255, 255, 255, 0.8);
    box-shadow: 0 8px 32px rgba(0, 0, 0, 0.12);
    transition: all 0.3s ease;
}

.profile-image-wrapper:hover {
    transform: translateY(-4px);
    box-shadow: 0 12px 40px rgba(0, 0, 0, 0.18);
}

.profile-image-wrapper:hover .image-overlay {
    opacity: 1;
}

.profile-image {
    width: 100%;
    height: 100%;
    object-fit: cover;
}

.profile-placeholder {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    width: 100%;
    height: 100%;
    background: linear-gradient(135deg, #f1f5f9 0%, #e2e8f0 100%);
}

.placeholder-icon {
    color: #94a3b8;
    margin-bottom: 0.5rem;
}

.placeholder-text {
    color: #64748b;
    font-size: 0.875rem;
    font-weight: 500;
    margin: 0;
}

.image-overlay {
    position: absolute;
    top: 0;
    left: 0;
    right: 0;
    bottom: 0;
    background: rgba(0, 0, 0, 0.5);
    display: flex;
    align-items: center;
    justify-content: center;
    opacity: 0;
    transition: opacity 0.3s ease;
    backdrop-filter: blur(2px);
}

.upload-btn {
    min-width: 140px;
    border-radius: 12px !important;
    font-weight: 600 !important;
    text-transform: none !important;
}

/* 반응형 디자인 */
@media (max-width: 768px) {
    .profile-image-wrapper {
        width: 140px;
        height: 140px;
    }
}

@media (max-width: 480px) {
    .profile-image-wrapper {
        width: 120px;
        height: 120px;
    }

    .upload-btn {
        min-width: 120px;
        font-size: 0.875rem;
    }
}
</style>