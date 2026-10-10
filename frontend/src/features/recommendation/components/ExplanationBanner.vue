<script setup lang="ts">
// Types e Interfaces
/**
 * @property {string[]} alignmentFactors - Lista de fatores explicativos de afinidade gerados via LLM.
 * @property {string[]} preventiveWarnings - Lista de alertas preventivos de manejo e adaptação.
 */
type TProps = {
  alignmentFactors: string[];
  preventiveWarnings: string[];
};

// Props
const props = defineProps<TProps>();
</script>

<template>
  <div class="mt-4 space-y-3">
    <!-- Fatores Positivos de Afinidade -->
    <div class="p-4 rounded-xl bg-emerald-50/80 border border-emerald-200">
      <div class="flex items-center gap-2 mb-2 text-emerald-800 font-semibold text-sm">
        <i class="pi pi-check-circle text-emerald-600"></i>
        <span>Fatores de Afinidade (Por que este animal combina com você):</span>
      </div>
      <ul class="space-y-1.5 pl-6 list-disc text-sm text-emerald-950">
        <li v-for="(factor, idx) in props.alignmentFactors" :key="idx">
          {{ factor }}
        </li>
      </ul>
    </div>

    <!-- Alertas Preventivos de Adaptação -->
    <div class="p-4 rounded-xl bg-amber-50/80 border border-amber-200">
      <div class="flex items-center gap-2 mb-2 text-amber-800 font-semibold text-sm">
        <i class="pi pi-exclamation-triangle text-amber-600"></i>
        <span>Alertas de Manejo e Desafios (Atenção para Adoção Responsável):</span>
      </div>
      <ul class="space-y-1.5 pl-6 list-disc text-sm text-amber-950">
        <li v-for="(warning, idx) in props.preventiveWarnings" :key="idx">
          {{ warning }}
        </li>
      </ul>
    </div>
  </div>
</template>
