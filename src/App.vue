<template>
<v-app>
    <Sidebar />
    <v-main>
      <router-view />
      <div>
        init frontend project
      </div>
    </v-main>
    <RouterView />
  </v-app>
</template>

<script setup>
import Sidebar from '@/components/common/sidebar/MainSidebar.vue'
import { RouterView } from 'vue-router';
</script>

<style scoped></style>
