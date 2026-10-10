<script setup lang="ts">
// Ecossistema Vue
import { ref, onMounted, computed } from 'vue';
import { useRouter } from 'vue-router';

// PrimeVue e UI
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Select from 'primevue/select';
import Tag from 'primevue/tag';
import Dialog from 'primevue/dialog';

// Services
import { CPetService } from '@/features/pet/services/CPetService';

// Componentes
import VideoModal from '@/shared/components/VideoModal.vue';
import AdoptionIntentModal from '@/features/adopter/components/AdoptionIntentModal.vue';

// Types e Interfaces
import type { IPet } from '@/features/pet/models/IPet';

// Router
const router = useRouter();

// Constantes
const SPECIES_FILTER_OPTIONS = [
  { label: 'Todos os Animais', value: 'ALL' },
  { label: 'Apenas Cães', value: 'DOG' },
  { label: 'Apenas Gatos', value: 'CAT' },
];

// Reativas - Ref
const pets = ref<IPet[]>([]);
const isLoading = ref(false);
const searchQuery = ref('');
const selectedSpecies = ref<'ALL' | 'DOG' | 'CAT'>('ALL');

// Modais
const isVideoModalVisible = ref(false);
const selectedVideoUrl = ref<string | null>(null);
const selectedVideoTitle = ref('');

const isDetailsModalVisible = ref(false);
const selectedPet = ref<IPet | null>(null);

const isIntentModalVisible = ref(false);

// Computadas
const filteredPets = computed(() => {
  return pets.value.filter((pet) => {
    const matchesSpecies = selectedSpecies.value === 'ALL' || pet.species === selectedSpecies.value;
    const matchesSearch =
      !searchQuery.value.trim() ||
      pet.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      pet.shelterName.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      pet.shelterCity.toLowerCase().includes(searchQuery.value.toLowerCase());
    return matchesSpecies && matchesSearch;
  });
});

// Funções
async function loadPets(): Promise<void> {
  isLoading.value = true;
  try {
    pets.value = await CPetService.findAll({ status: 'AVAILABLE' });
  } catch (pErr) {
    pets.value = [];
  } finally {
    isLoading.value = false;
  }
}

function handleGoToMatch(): void {
  router.push('/match');
}

function handleOpenVideo(pPet: IPet): void {
  selectedVideoUrl.value = pPet.videoUrl;
  selectedVideoTitle.value = `Comportamento de ${pPet.name}`;
  isVideoModalVisible.value = true;
}

function handleOpenDetails(pPet: IPet): void {
  selectedPet.value = pPet;
  isDetailsModalVisible.value = true;
}

function handleOpenIntent(pPet: IPet): void {
  selectedPet.value = pPet;
  isIntentModalVisible.value = true;
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

onMounted(() => {
  loadPets();
});
</script>

<template>
  <div class="max-w-6xl mx-auto px-4 py-8 space-y-8">
    <!-- Banner de Orientação Ética e Recomendação -->
    <div class="bg-gradient-to-r from-emerald-800 to-teal-900 rounded-3xl p-6 sm:p-10 text-white shadow-md relative overflow-hidden">
      <div class="relative z-10 max-w-2xl space-y-4">
        <span class="inline-block px-3 py-1 bg-white/20 backdrop-blur-sm rounded-full text-xs font-semibold uppercase tracking-wider text-emerald-100">
          Adoção Consciente & Ética
        </span>
        <h1 class="text-2xl sm:text-4xl font-extrabold tracking-tight leading-tight">
          Conheça os Animais Acolhidos por ONGs e Pet Shops Parceiros
        </h1>
        <p class="text-emerald-100/90 text-sm sm:text-base leading-relaxed">
          Embora você possa navegar por todos os pets abaixo, recomendamos enfaticamente usar o 
          <strong>Diagnóstico Inteligente</strong> para descobrir a compatibilidade com sua rotina e receber alertas preventivos de convivência.
        </p>
        <div class="pt-2">
          <Button
            label="Fazer Diagnóstico de Compatibilidade com IA"
            icon="pi pi-sparkles"
            class="bg-white text-emerald-900 hover:bg-emerald-50 font-bold px-5 py-3 rounded-xl transition shadow-sm border-0"
            @click="handleGoToMatch"
          />
        </div>
      </div>
    </div>

    <!-- Barra de Filtros e Busca -->
    <div class="bg-white p-4 sm:p-5 rounded-2xl border border-slate-200 shadow-xs flex flex-col sm:flex-row items-center justify-between gap-4">
      <div class="w-full sm:w-72">
        <InputText
          v-model="searchQuery"
          placeholder="Buscar por nome, ONG ou cidade..."
          class="w-full text-sm"
        />
      </div>

      <div class="flex items-center gap-3 w-full sm:w-auto justify-end">
        <Select
          v-model="selectedSpecies"
          :options="SPECIES_FILTER_OPTIONS"
          option-label="label"
          option-value="value"
          class="w-full sm:w-48 text-sm"
        />
        <Button
          icon="pi pi-refresh"
          severity="secondary"
          text
          :loading="isLoading"
          @click="loadPets"
        />
      </div>
    </div>

    <!-- Grid de Animais -->
    <div v-if="isLoading" class="p-16 text-center text-slate-500">
      <i class="pi pi-spin pi-spinner text-3xl text-emerald-600 mb-2"></i>
      <p>Carregando catálogo de animais...</p>
    </div>

    <div v-else-if="filteredPets.length === 0" class="p-16 text-center text-slate-500 space-y-3">
      <div class="w-16 h-16 rounded-full bg-slate-100 text-slate-400 flex items-center justify-center mx-auto text-2xl">
        <i class="pi pi-search"></i>
      </div>
      <p class="font-bold text-slate-700">Nenhum animal encontrado com os filtros selecionados.</p>
      <p class="text-xs text-slate-500">Tente ajustar o termo de pesquisa ou a espécie.</p>
    </div>

    <div v-else class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
      <div
        v-for="pet in filteredPets"
        :key="pet.id"
        class="bg-white rounded-2xl border border-slate-200 shadow-xs overflow-hidden hover:shadow-md transition duration-200 flex flex-col justify-between"
      >
        <div>
          <!-- Foto e Tags -->
          <div class="relative h-56 bg-slate-100 overflow-hidden">
            <img
              v-if="pet.photoUrl"
              :src="pet.photoUrl"
              :alt="pet.name"
              class="w-full h-full object-cover transition duration-300 hover:scale-105"
            />
            <div v-else class="w-full h-full flex items-center justify-center text-3xl font-bold text-emerald-600">
              {{ pet.name.charAt(0) }}
            </div>

            <!-- Badges Flutuantes -->
            <div class="absolute top-3 left-3 flex items-center gap-1.5">
              <span
                class="px-2.5 py-1 rounded-md text-[11px] font-bold text-white shadow-xs"
                :class="pet.species === 'DOG' ? 'bg-amber-600' : 'bg-indigo-600'"
              >
                {{ pet.species === 'DOG' ? 'Cão' : 'Gato' }}
              </span>
              <span class="px-2 py-0.5 rounded-md text-[11px] font-semibold bg-white/90 text-slate-700 shadow-xs backdrop-blur-xs">
                {{ formatSize(pet.size) }}
              </span>
            </div>

            <!-- Botão de Vídeo Flutuante -->
            <button
              v-if="pet.videoUrl"
              class="absolute bottom-3 right-3 px-2.5 py-1 rounded-lg bg-black/70 hover:bg-black/90 text-white text-xs font-semibold flex items-center gap-1.5 backdrop-blur-xs transition"
              @click="handleOpenVideo(pet)"
            >
              <i class="pi pi-video text-xs"></i>
              <span>Vídeo</span>
            </button>
          </div>

          <!-- Conteúdo do Card -->
          <div class="p-5 space-y-3">
            <div class="flex items-center justify-between">
              <h3 class="text-xl font-bold text-slate-900">{{ pet.name }}</h3>
              <Tag
                :value="pet.gender === 'MALE' ? 'Macho' : 'Fêmea'"
                :severity="pet.gender === 'MALE' ? 'info' : 'warn'"
                rounded
                class="text-xs"
              />
            </div>

            <p class="text-xs text-slate-500 flex items-center gap-1">
              <i class="pi pi-building text-slate-400"></i>
              <span>{{ pet.shelterName }}</span>
              <span>•</span>
              <span>{{ pet.shelterCity }}</span>
            </p>

            <p class="text-xs text-slate-600 line-clamp-3 italic">
              "{{ pet.biography }}"
            </p>

            <!-- Tags de Convivência -->
            <div class="flex flex-wrap gap-1.5 pt-1">
              <span v-if="pet.kidsFriendly" class="text-[11px] px-2 py-0.5 rounded bg-emerald-50 text-emerald-700 border border-emerald-100">
                ✓ Aceita Crianças
              </span>
              <span v-if="pet.petsFriendly" class="text-[11px] px-2 py-0.5 rounded bg-emerald-50 text-emerald-700 border border-emerald-100">
                ✓ Aceita Outros Pets
              </span>
              <span class="text-[11px] px-2 py-0.5 rounded bg-slate-100 text-slate-600">
                Energia: {{ pet.energyLevel }}/5
              </span>
            </div>
          </div>
        </div>

        <!-- Ações do Card -->
        <div class="p-5 pt-0 flex items-center justify-between gap-2 border-t border-slate-100 mt-2">
          <Button
            label="Ver Detalhes"
            text
            size="small"
            class="text-xs font-semibold text-slate-700"
            @click="handleOpenDetails(pet)"
          />
          <Button
            label="Quero Adotar"
            icon="pi pi-heart"
            size="small"
            class="bg-emerald-600 hover:bg-emerald-700 text-white font-medium text-xs px-3.5 py-2 rounded-xl transition shadow-xs"
            @click="handleOpenIntent(pet)"
          />
        </div>
      </div>
    </div>

    <!-- Modal de Detalhes Completos do Animal -->
    <Dialog
      v-model:visible="isDetailsModalVisible"
      modal
      :header="selectedPet ? `Dossiê de ${selectedPet.name}` : 'Detalhes'"
      :style="{ width: '92vw', maxWidth: '680px' }"
    >
      <div v-if="selectedPet" class="space-y-5 py-2">
        <div class="h-64 rounded-2xl overflow-hidden bg-slate-100 relative">
          <img :src="selectedPet.photoUrl" :alt="selectedPet.name" class="w-full h-full object-cover" />
          <button
            v-if="selectedPet.videoUrl"
            class="absolute bottom-3 right-3 px-3 py-1.5 rounded-xl bg-black/80 text-white text-xs font-semibold flex items-center gap-2 hover:bg-black transition"
            @click="handleOpenVideo(selectedPet)"
          >
            <i class="pi pi-video"></i>
            <span>Assistir Vídeo</span>
          </button>
        </div>

        <div class="space-y-2">
          <h3 class="text-xl font-bold text-slate-900">História e Biografia</h3>
          <p class="text-sm text-slate-600 leading-relaxed">{{ selectedPet.biography }}</p>
        </div>

        <div class="space-y-2">
          <h3 class="text-sm font-bold text-slate-900 uppercase tracking-wider text-xs">Diário Comportamental da Equipe</h3>
          <div class="p-3.5 rounded-xl bg-slate-50 border border-slate-200 text-xs text-slate-700 leading-relaxed">
            {{ selectedPet.behavioralNotes }}
          </div>
        </div>

        <div class="grid grid-cols-2 sm:grid-cols-4 gap-3 text-center">
          <div class="p-3 rounded-xl bg-slate-50 border border-slate-100">
            <span class="block text-[11px] text-slate-400">Idade</span>
            <span class="font-bold text-slate-800 text-xs">{{ formatAge(selectedPet.ageMonths) }}</span>
          </div>
          <div class="p-3 rounded-xl bg-slate-50 border border-slate-100">
            <span class="block text-[11px] text-slate-400">Porte</span>
            <span class="font-bold text-slate-800 text-xs">{{ formatSize(selectedPet.size) }}</span>
          </div>
          <div class="p-3 rounded-xl bg-slate-50 border border-slate-100">
            <span class="block text-[11px] text-slate-400">Energia</span>
            <span class="font-bold text-slate-800 text-xs">{{ selectedPet.energyLevel }} / 5</span>
          </div>
          <div class="p-3 rounded-xl bg-slate-50 border border-slate-100">
            <span class="block text-[11px] text-slate-400">Tolerância Solidão</span>
            <span class="font-bold text-slate-800 text-xs">{{ selectedPet.solitaryToleranceHours }}h / dia</span>
          </div>
        </div>

        <div class="pt-4 flex justify-end gap-3 border-t border-slate-100">
          <Button label="Fechar" severity="secondary" text @click="isDetailsModalVisible = false" />
          <Button
            label="Manifestar Interesse em Adotar"
            icon="pi pi-heart"
            class="bg-emerald-600 hover:bg-emerald-700 text-white font-medium px-4 py-2.5 rounded-xl"
            @click="isDetailsModalVisible = false; handleOpenIntent(selectedPet);"
          />
        </div>
      </div>
    </Dialog>

    <!-- Modais Auxiliares -->
    <VideoModal
      v-model:visible="isVideoModalVisible"
      :video-url="selectedVideoUrl"
      :title="selectedVideoTitle"
    />

    <AdoptionIntentModal
      v-if="selectedPet"
      v-model:visible="isIntentModalVisible"
      :pet-id="selectedPet.id"
      :pet-name="selectedPet.name"
      :compatibilityScore="100"
      adopterNarrative="Manifestação direta a partir do catálogo de acolhidos."
      :contactWhatsapp="selectedPet.contactWhatsapp"
    />
  </div>
</template>
