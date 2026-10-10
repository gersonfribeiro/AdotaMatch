<script setup lang="ts">
// Ecossistema Vue
import { ref } from 'vue';

// PrimeVue e UI
import Button from 'primevue/button';
import Tag from 'primevue/tag';

// Componentes
import ExplanationBanner from '@/features/recommendation/components/ExplanationBanner.vue';
import AdoptionIntentModal from '@/features/adopter/components/AdoptionIntentModal.vue';
import VideoModal from '@/shared/components/VideoModal.vue';

// Types e Interfaces
import type { IRecommendationResult } from '@/features/recommendation/models/IRecommendationResult';

/**
 * @property {IRecommendationResult} match - Dados da recomendação com escores e explicações.
 * @property {string} adopterNarrative - Relato de estilo de vida do adotante para compor o dossiê.
 */
type TProps = {
  match: IRecommendationResult;
  adopterNarrative?: string;
};

/**
 * @property {[id: number]} contactShelter - Emitido quando o adotante conclui a manifestação de interesse.
 */
type TEmits = {
  contactShelter: [id: number];
};

// Props e Emits
const props = withDefaults(defineProps<TProps>(), {
  adopterNarrative: '',
});
const emit = defineEmits<TEmits>();

// Reativas
const isIntentModalVisible = ref(false);
const isVideoModalVisible = ref(false);

// Funções
function handleOpenIntentModal(): void {
  isIntentModalVisible.value = true;
}

function handleOpenVideo(): void {
  isVideoModalVisible.value = true;
}

function handleIntentSuccess(): void {
  emit('contactShelter', props.match.petId);
}

function formatAge(pAgeMonths: number): string {
  if (pAgeMonths < 12) return `${pAgeMonths} meses`;
  const years = Math.floor(pAgeMonths / 12);
  const remainingMonths = pAgeMonths % 12;
  if (remainingMonths === 0) return `${years} ${years === 1 ? 'ano' : 'anos'}`;
  return `${years}a ${remainingMonths}m`;
}

function formatSize(pSize: string): string {
  const map: Record<string, string> = {
    MINI: 'Mini',
    SMALL: 'Pequeno',
    MEDIUM: 'Médio',
    LARGE: 'Grande',
  };
  return map[pSize] || pSize;
}
</script>

<template>
  <div class="bg-white rounded-2xl shadow-sm border border-slate-200 overflow-hidden transition duration-200 hover:shadow-md">
    <!-- Cabeçalho do Card com Foto e Informações Principais -->
    <div class="p-6 border-b border-slate-100 flex flex-col md:flex-row md:items-center justify-between gap-6">
      <div class="flex items-start sm:items-center gap-4">
        <!-- Foto do Animal com Fallback -->
        <div class="relative w-20 h-20 sm:w-24 sm:h-24 rounded-2xl overflow-hidden bg-slate-100 shrink-0 border border-slate-200 shadow-xs">
          <img
            v-if="props.match.photoUrl"
            :src="props.match.photoUrl"
            :alt="props.match.petName"
            class="w-full h-full object-cover"
          />
          <div v-else class="w-full h-full flex items-center justify-center font-bold text-2xl text-emerald-600 bg-emerald-50">
            {{ props.match.petName.charAt(0) }}
          </div>
          <span
            class="absolute bottom-1 right-1 px-1.5 py-0.5 rounded text-[10px] font-semibold uppercase tracking-wider text-white"
            :class="props.match.species === 'DOG' ? 'bg-amber-600' : 'bg-indigo-600'"
          >
            {{ props.match.species === 'DOG' ? 'Cão' : 'Gato' }}
          </span>
        </div>

        <!-- Dados Cadastrais -->
        <div class="space-y-1">
          <div class="flex flex-wrap items-center gap-2">
            <h3 class="text-xl sm:text-2xl font-bold text-slate-900 tracking-tight">
              {{ props.match.petName }}
            </h3>
            <Tag
              :value="props.match.gender === 'MALE' ? 'Macho' : 'Fêmea'"
              :severity="props.match.gender === 'MALE' ? 'info' : 'warn'"
              rounded
              class="text-xs"
            />
            <Tag :value="formatSize(props.match.size)" severity="secondary" rounded class="text-xs" />
            <span class="text-xs px-2 py-0.5 rounded-full bg-slate-100 text-slate-600 font-medium">
              {{ formatAge(props.match.ageMonths) }}
            </span>
          </div>

          <p class="text-xs text-slate-500 flex items-center gap-1">
            <i class="pi pi-building text-slate-400"></i>
            <span>{{ props.match.shelterName }}</span>
            <span class="text-slate-300">•</span>
            <i class="pi pi-map-marker text-slate-400"></i>
            <span>{{ props.match.shelterCity }}</span>
          </p>

          <p v-if="props.match.biography" class="text-xs text-slate-600 line-clamp-2 pt-1 max-w-xl italic">
            "{{ props.match.biography }}"
          </p>
        </div>
      </div>

      <!-- Selos de Pontuação (Métrica Ponderada + LLM) -->
      <div class="flex items-center gap-4 bg-slate-50 border border-slate-100 px-4 py-3 rounded-2xl self-start md:self-auto shrink-0">
        <div class="text-center">
          <span class="block text-3xl font-extrabold text-emerald-600 leading-none">
            {{ Math.round(props.match.compatibilityScore) }}%
          </span>
          <span class="text-[11px] text-slate-500 font-medium uppercase tracking-wider mt-1 block">
            Compatibilidade
          </span>
        </div>
        <div class="border-l border-slate-200 pl-4 text-center">
          <span class="block text-base font-bold text-slate-700">
            {{ Math.round(props.match.semanticScore * 100) }}%
          </span>
          <span class="text-[10px] text-slate-400 uppercase tracking-wider block">
            Alinhamento LLM
          </span>
        </div>
      </div>
    </div>

    <!-- Corpo com Explicabilidade e Alertas Preventivos -->
    <div class="p-6 space-y-6">
      <ExplanationBanner
        :alignment-factors="props.match.alignmentFactors"
        :preventive-warnings="props.match.preventiveWarnings"
      />

      <!-- Barra de Ações Responsáveis -->
      <div class="flex flex-wrap items-center justify-between gap-3 pt-2 border-t border-slate-100">
        <div class="flex items-center gap-2">
          <!-- Botão de Vídeo Comportamental -->
          <Button
            v-if="props.match.videoUrl"
            label="Vídeo do Pet"
            icon="pi pi-video"
            severity="secondary"
            text
            size="small"
            class="text-xs font-semibold text-slate-700"
            @click="handleOpenVideo"
          />
        </div>

        <div class="flex items-center gap-3">
          <Button
            label="Manifestar Interesse em Adotar"
            icon="pi pi-heart"
            class="bg-emerald-600 hover:bg-emerald-700 text-white font-medium px-5 py-2.5 rounded-xl transition shadow-xs"
            @click="handleOpenIntentModal"
          />
        </div>
      </div>
    </div>

    <!-- Modais de Ação -->
    <AdoptionIntentModal
      v-model:visible="isIntentModalVisible"
      :pet-id="props.match.petId"
      :pet-name="props.match.petName"
      :compatibilityScore="props.match.compatibilityScore"
      :adopterNarrative="props.adopterNarrative"
      :contactWhatsapp="props.match.contactWhatsapp"
      @success="handleIntentSuccess"
    />

    <VideoModal
      v-model:visible="isVideoModalVisible"
      :video-url="props.match.videoUrl"
      :title="`Comportamento de ${props.match.petName}`"
    />
  </div>
</template>
