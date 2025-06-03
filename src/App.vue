<template>
  <v-app>
    <Sidebar />
    <v-main>
      <router-view />
    </v-main>
  </v-app>
</template>

<script setup>
import Sidebar from '@/components/common/MainSidebar.vue'
import { RouterView } from 'vue-router';

</script>