<template>
  <div class="modal-overlay">
    <div class="modal-box">
      <div class="modal-content">
        <div class="modal-icon">
          <i class="mdi mdi-alert-circle-outline"></i>
        </div>
        <p class="modal-message">
          {{ message }}
        </p>
        <div class="modal-buttons">
          <button v-if="showCancel" class="cancel" @click="$emit('cancel')">
            {{ cancelText }}
          </button>
          <button class="confirm" @click="$emit('confirm')">
            {{ confirmText }}
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  message: {
    type: String,
    required: true
  },
  showCancel: {
    type: Boolean,
    default: true
  },
  confirmText: {
    type: String,
    default: '확인'
  },
  cancelText: {
    type: String,
    default: '취소'
  }
})

defineEmits(['confirm', 'cancel'])
</script>

<style scoped>
.modal-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
  animation: fadeIn 0.2s ease-out;
}

.modal-box {
  background: white;
  border-radius: 16px;
  padding: 2rem;
  text-align: center;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.1);
  min-width: 320px;
  max-width: 90%;
  animation: slideIn 0.3s ease-out;
}

.modal-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 1.5rem;
}

.modal-icon {
  font-size: 3rem;
  color: #85c88a;
  margin-bottom: 0.5rem;
}

.modal-message {
  color: #2c3e50;
  font-size: 1.1rem;
  line-height: 1.5;
  margin: 0;
  font-weight: 500;
}

.modal-buttons {
  display: flex;
  justify-content: center;
  gap: 1rem;
  width: 100%;
  margin-top: 0.5rem;
}

button {
  padding: 0.75rem 2rem;
  border: none;
  border-radius: 8px;
  font-weight: 600;
  font-size: 1rem;
  cursor: pointer;
  transition: all 0.2s ease;
  min-width: 100px;
}

button:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

button:active {
  transform: translateY(0);
}

button.cancel {
  background-color: #f5f5f5;
  color: #666;
}

button.cancel:hover {
  background-color: #e0e0e0;
}

button.confirm {
  background-color: #85c88a;
  color: white;
}

button.confirm:hover {
  background-color: #6ab06f;
}

@keyframes fadeIn {
  from {
    opacity: 0;
  }

  to {
    opacity: 1;
  }
}

@keyframes slideIn {
  from {
    transform: translateY(-20px);
    opacity: 0;
  }

  to {
    transform: translateY(0);
    opacity: 1;
  }
}
</style>
