<template>
  <v-app>
    <Sidebar />
    <v-main>
      <router-view />
      <div>
        init frontend project
      </div>
    </v-main>
  </v-app>
</template>

<script setup>
import Sidebar from '@/components/common/sidebar/MainSidebar.vue'
</script>

<style scoped></style>
