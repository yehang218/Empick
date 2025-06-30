<template>
  <div class="search-bar">
    <input :value="modelValue" type="text" placeholder="검색" @input="$emit('update:modelValue', $event.target.value)"
      @keyup.enter="$emit('search', $event.target.value)" />
    <button @click="$emit('search', modelValue)">
      <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" fill="none" stroke="black" stroke-width="2"
        viewBox="0 0 24 24">
        <circle cx="11" cy="11" r="8" />
        <line x1="21" y1="21" x2="16.65" y2="16.65" />
      </svg>
    </button>
  </div>
</template>

<script setup>
defineProps({
  modelValue: {
    type: String,
    required: true
  }
})

defineEmits(['update:modelValue', 'search'])
</script>

<style scoped>
.search-bar {
  display: flex;
  align-items: center;
  border: 1px solid #7b8dbd;
  border-radius: 20px;
  padding: 0.4rem 1rem;
  width: 300px;
  font-size: 1rem;
  background-color: white;
}

.search-bar input {
  border: none;
  outline: none;
  flex: 1;
  font-size: 1rem;
  font-family: inherit;
  color: #333;
  background-color: transparent;
}

.search-bar input::placeholder {
  color: #aaa;
}

.search-bar button {
  background: none;
  border: none;
  cursor: pointer;
  padding: 0;
  margin-left: 0.5rem;
  display: flex;
  align-items: center;
}
</style>
