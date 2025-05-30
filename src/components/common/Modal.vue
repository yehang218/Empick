<template>
  <div class="modal-overlay">
    <div class="modal-box">
      <p class="modal-message">
        <strong><u>{{ message }}</u></strong>
      </p>
      <div class="modal-buttons">
        <button class="yes" @click="$emit('confirm')">확인</button>
        <button class="no" @click="$emit('cancel')">취소</button>
      </div>
    </div>
  </div>
</template>

<script setup>
defineProps({
  message: {
    type: String,
    required: true
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
  background: rgba(0,0,0,0.3);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 999;
}

.modal-box {
  background: white;
  border-radius: 10px;
  padding: 2rem;
  text-align: center;
  box-shadow: 0 4px 10px rgba(0,0,0,0.1);
  min-width: 300px;
}

.modal-message {
  color: #4d774e;
  font-size: 1.1rem;
  margin-bottom: 1.5rem;
}

.modal-buttons {
  display: flex;
  justify-content: center;
  gap: 1rem;
}

button.yes {
  background-color: #85c88a;
  border: none;
  padding: 0.5rem 1.5rem;
  color: white;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
}

button.no {
  background-color: white;
  border: 1px solid #85c88a;
  padding: 0.5rem 1.5rem;
  color: #4d774e;
  border-radius: 5px;
  font-weight: bold;
  cursor: pointer;
}
</style>
