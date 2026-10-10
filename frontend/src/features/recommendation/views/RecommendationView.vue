<script setup lang="ts">
// Ecossistema
import { ref } from 'vue';

// PrimeVue e UI
import Button from 'primevue/button';
import Textarea from 'primevue/textarea';
import Select from 'primevue/select';
import InputNumber from 'primevue/inputnumber';
import Checkbox from 'primevue/checkbox';
import Message from 'primevue/message';
import { useToast } from 'primevue/usetoast';

// Stores
import { useRecommendationStore } from '@/features/recommendation/stores/useRecommendationStore';

// Componentes
import MatchCard from '@/features/recommendation/components/MatchCard.vue';

// Types e Interfaces
import type { TRecommendationRequest } from '@/features/recommendation/models/TRecommendationRequest';

// Composables
const toast = useToast();

// Stores
const recommendationStore = useRecommendationStore();

// Constantes
const HOUSING_OPTIONS = [
  { label: 'Apartamento', value: 'APARTMENT' },
  { label: 'Casa sem quintal', value: 'HOUSE_NO_YARD' },
  { label: 'Casa com quintal murado', value: 'HOUSE_YARD' },
  { label: 'Chácara / Sítio / Área Rural', value: 'RURAL' },
];

const SPECIES_OPTIONS = [
  { label: 'Indiferente (Cães ou Gatos)', value: 'ANY' },
  { label: 'Exclusivamente Cães', value: 'DOG' },
  { label: 'Exclusivamente Gatos', value: 'CAT' },
];

// Reativas
const formData = ref<TRecommendationRequest>({
  speciesPreference: 'DOG',
  housingType: 'HOUSE_YARD',
  dailyHoursAvailable: 3,
  hasChildren: false,
  hasOtherPets: false,
  experienceLevel: 2,
  lifestyleNarrative: '',
});

// Funções
async function handleSubmit(): Promise<void> {
  if (recommendationStore.isMatching) return;
  try {
    await recommendationStore.fetchMatches(formData.value);
  } catch (pErr) {
    // Erro já tratado na store
  }
}

function handleContactShelter(pPetId: number): void {
  toast.add({
    severity: 'success',
    summary: 'Interesse Registrado!',
    detail: `Solicitação vinculada com sucesso para o animal #${pPetId}. A equipe do abrigo entrará em contato para a conversa responsável!`,
    life: 5000,
  });
}
</script>

<template>
  <div class="max-w-5xl mx-auto px-4 py-8 space-y-8">
    <!-- Cabeçalho -->
    <div class="text-center space-y-2">
      <h1 class="text-3xl font-extrabold text-slate-900 tracking-tight">
        AdotaMatch: Recomendação Inteligente com LLM
      </h1>
      <p class="text-slate-600 max-w-2xl mx-auto text-sm sm:text-base">
        Nosso algoritmo híbrido cruza suas variáveis de rotina e moradia com o relato em texto livre,
        identificando o animal ideal e apontando alertas de convivência para prevenir devoluções.
      </p>
    </div>

    <!-- Formulário de Perfil do Adotante -->
    <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-6 sm:p-8">
      <h2 class="text-xl font-bold text-slate-900 mb-6 flex items-center gap-2">
        <i class="pi pi-sliders-h text-indigo-600"></i>
        <span>Preencha seu Perfil e Estilo de Vida</span>
      </h2>

      <form class="space-y-6" @submit.prevent="handleSubmit">
        <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
          <!-- Preferência de Espécie -->
          <div class="space-y-1.5">
            <label class="block text-sm font-medium text-slate-700">Preferência de Espécie</label>
            <Select
              v-model="formData.speciesPreference"
              :options="SPECIES_OPTIONS"
              option-label="label"
              option-value="value"
              class="w-full"
            />
          </div>

          <!-- Tipo de Moradia -->
          <div class="space-y-1.5">
            <label class="block text-sm font-medium text-slate-700">Espaço de Moradia</label>
            <Select
              v-model="formData.housingType"
              :options="HOUSING_OPTIONS"
              option-label="label"
              option-value="value"
              class="w-full"
            />
          </div>

          <!-- Horas Diárias Disponíveis -->
          <div class="space-y-1.5">
            <label class="block text-sm font-medium text-slate-700">Tempo Diário Disponível (Horas)</label>
            <InputNumber
              v-model="formData.dailyHoursAvailable"
              :min="1"
              :max="12"
              show-buttons
              class="w-full"
            />
          </div>

          <!-- Composição Familiar -->
          <div class="space-y-3 pt-2">
            <span class="block text-sm font-medium text-slate-700">Composição do Lar</span>
            <div class="flex items-center gap-6">
              <div class="flex items-center gap-2">
                <Checkbox v-model="formData.hasChildren" binary input-id="chk_children" />
                <label for="chk_children" class="text-sm text-slate-700">Crianças residentes</label>
              </div>
              <div class="flex items-center gap-2">
                <Checkbox v-model="formData.hasOtherPets" binary input-id="chk_pets" />
                <label for="chk_pets" class="text-sm text-slate-700">Outros animais no lar</label>
              </div>
            </div>
          </div>
        </div>

        <!-- Relato em Texto Livre (Processamento via LLM) -->
        <div class="space-y-1.5 pt-2">
          <label class="block text-sm font-semibold text-indigo-950 flex items-center justify-between">
            <span>Conte sobre sua rotina, hábitos e expectativas:</span>
            <span class="text-xs text-indigo-600 font-normal">Análise Semântica Contextual (Opcional)</span>
          </label>
          <Textarea
            v-model="formData.lifestyleNarrative"
            rows="4"
            class="w-full border-indigo-200 focus:border-indigo-500 rounded-xl"
            placeholder="Ex.: Trabalho presencial com horário comercial, moro em casa com quintal. Gosto de animais calmos que fiquem bem durante o dia e curtam passeios ao entardecer..."
          />
          <p class="text-xs text-slate-400">
            Opcional: detalhe sua rotina para refinamento contextual por IA ou deixe em branco para recomendação baseada diretamente nas variáveis acima.
          </p>
        </div>

        <!-- Botão com Lock Reativo -->
        <div class="flex justify-end pt-4 border-t border-slate-100">
          <Button
            type="submit"
            label="Calcular Recomendações Responsáveis"
            icon="pi pi-sparkles"
            :loading="recommendationStore.isMatching"
            class="bg-indigo-600 hover:bg-indigo-700 text-white font-medium px-6 py-3 rounded-xl transition shadow-sm"
          />
        </div>
      </form>
    </div>

    <!-- Mensagem de Erro -->
    <Message v-if="recommendationStore.errorMessage" severity="error" :closable="false">
      {{ recommendationStore.errorMessage }}
    </Message>

    <!-- Lista de Resultados Ranqueados -->
    <div v-if="recommendationStore.hasMatches" class="space-y-6">
      <div class="flex items-center justify-between">
        <h2 class="text-2xl font-bold text-slate-900">
          Animais Compatíveis com seu Estilo de Vida
        </h2>
        <span class="text-sm text-slate-500 font-medium">
          {{ recommendationStore.matches.length }} candidatos encontrados
        </span>
      </div>

      <div class="space-y-6">
        <MatchCard
          v-for="match in recommendationStore.matches"
          :key="match.petId"
          :match="match"
          :adopter-narrative="formData.lifestyleNarrative"
          @contact-shelter="handleContactShelter"
        />
      </div>
    </div>
  </div>
</template>
