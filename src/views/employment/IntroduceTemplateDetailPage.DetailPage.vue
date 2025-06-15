<template>
  <!-- No changes to template section -->
</template>

<script setup>
import { onMounted, ref } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const template = ref(null)

onMounted(async () => {
  template.value = await fetchTemplateDetail(route.params.id)
  console.log('Fetched template detail:', template.value)
})

const goList = () => router.push('/employment/recruitments/introduce-templates')
</script>

<style>
  /* No changes to style section */
</style> 