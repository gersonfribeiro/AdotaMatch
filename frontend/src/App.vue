<script setup lang="ts">
// PrimeVue e UI
import Toast from 'primevue/toast';

// Componentes
import BaseNavbar from '@/shared/components/BaseNavbar.vue';
</script>

<template>
  <div class="min-h-screen flex flex-col bg-slate-50">
    <Toast position="top-right" />
    <BaseNavbar />
    <main class="flex-1">
      <RouterView />
    </main>
    <footer class="bg-white border-t border-slate-200 py-6 text-center text-xs text-slate-500">
      <div class="max-w-6xl mx-auto px-4">
        <p>AdotaMatch &copy; 2026 — Sistema de Recomendação Inteligente para Adoção Responsável de Animais.</p>
        <p class="mt-1 text-slate-400">Desenvolvido com Vue 3, Vite, PrimeVue, Tailwind CSS, Kotlin e Spring Boot.</p>
      </div>
    </footer>
  </div>
</template>
