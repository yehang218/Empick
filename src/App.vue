<template>
  <<<<<<< HEAD <v-app>
    <Sidebar />
    <v-main>
      <router-view />
      <div>
        init frontend project
      </div>

      <div id="app">
        <EvaluationInput />
      </div>
    </v-main>
    </v-app>
</template>

<script setup>
import Sidebar from '@/components/common/sidebar/MainSidebar.vue'
import EvaluationInput from './components/EvaluationInput.vue'
</script>

<style scoped></style>
