<script setup lang="ts">
// Ecossistema Vue
import { ref, onMounted, computed } from 'vue';

// PrimeVue e UI
import Button from 'primevue/button';
import Dialog from 'primevue/dialog';
import InputText from 'primevue/inputtext';
import InputNumber from 'primevue/inputnumber';
import Textarea from 'primevue/textarea';
import Select from 'primevue/select';
import Checkbox from 'primevue/checkbox';
import Tag from 'primevue/tag';
import Message from 'primevue/message';
import { useToast } from 'primevue/usetoast';

// Services
import { CPetService } from '@/features/pet/services/CPetService';
import { CAdoptionService } from '@/features/adopter/services/CAdoptionService';

// Types e Interfaces
import type { IPet, TPetCreateRequest } from '@/features/pet/models/IPet';
import type { IAdoptionIntentResponse } from '@/features/adopter/models/IAdoptionIntent';

// Stores
import { useAuthStore } from '@/features/auth/stores/useAuthStore';

// Composables
const toast = useToast();
const authStore = useAuthStore();

// Constantes
const SPECIES_OPTIONS = [
  { label: 'Cão', value: 'DOG' },
  { label: 'Gato', value: 'CAT' },
];

const GENDER_OPTIONS = [
  { label: 'Macho', value: 'MALE' },
  { label: 'Fêmea', value: 'FEMALE' },
];

const SIZE_OPTIONS = [
  { label: 'Mini', value: 'MINI' },
  { label: 'Pequeno', value: 'SMALL' },
  { label: 'Médio', value: 'MEDIUM' },
  { label: 'Grande / Gigante', value: 'LARGE' },
];

const ENERGY_OPTIONS = [
  { label: '1 - Muito Calmo / Sedentário', value: 1 },
  { label: '2 - Baixa Energia', value: 2 },
  { label: '3 - Moderado / Equilibrado', value: 3 },
  { label: '4 - Ativo / Brincalhão', value: 4 },
  { label: '5 - Hiperativo / Alta Demanda', value: 5 },
];

// Reativas - Ref
const pets = ref<IPet[]>([]);
const isLoading = ref(false);
const errorMessage = ref<string | null>(null);
const successMessage = ref<string | null>(null);

// Modais
const isCreateModalVisible = ref(false);
const isSubmitting = ref(false);

const isIntentsModalVisible = ref(false);
const selectedPetForIntents = ref<IPet | null>(null);
const petIntents = ref<IAdoptionIntentResponse[]>([]);
const isLoadingIntents = ref(false);

// Formulário de Cadastro de Novo Pet
const newPet = ref<TPetCreateRequest>({
  name: '',
  species: 'DOG',
  gender: 'MALE',
  size: 'MEDIUM',
  ageMonths: 12,
  energyLevel: 3,
  solitaryToleranceHours: 6,
  kidsFriendly: true,
  petsFriendly: true,
  requiresSpecialCare: false,
  isVaccinated: true,
  isNeutered: true,
  photoUrl: '',
  additionalPhotos: '',
  videoUrl: '',
  biography: '',
  behavioralNotes: '',
  shelterName: 'Pet Shop & Abrigo Patas Amigas',
  shelterCity: 'Ubá - MG',
  contactWhatsapp: '32999887766',
});

// Computadas
const availablePetsCount = computed(() => pets.value.filter((p) => p.status === 'AVAILABLE').length);
const dogsCount = computed(() => pets.value.filter((p) => p.species === 'DOG').length);
const catsCount = computed(() => pets.value.filter((p) => p.species === 'CAT').length);

// Funções
async function loadPets(): Promise<void> {
  isLoading.value = true;
  errorMessage.value = null;
  try {
    pets.value = await CPetService.findAll();
  } catch (pErr: any) {
    errorMessage.value = 'Não foi possível carregar a lista de animais acolhidos.';
  } finally {
    isLoading.value = false;
  }
}

function handleOpenCreateModal(): void {
  if (authStore.user) {
    newPet.value.shelterName = authStore.user.organizationName || authStore.user.name;
    if (authStore.user.city) {
      newPet.value.shelterCity = authStore.user.city;
    }
    if (authStore.user.phone) {
      newPet.value.contactWhatsapp = authStore.user.phone;
    }
  }
  // Pré-preenche sugestão de foto caso vazia para agilizar testes
  if (!newPet.value.photoUrl) {
    newPet.value.photoUrl = 'https://images.unsplash.com/photo-1543466835-00a7907e9de1?w=800&auto=format&fit=crop&q=80';
  }
  isCreateModalVisible.value = true;
}

async function handleCreatePet(): Promise<void> {
  if (isSubmitting.value) return;

  if (!newPet.value.name.trim() || !newPet.value.photoUrl.trim() || !newPet.value.biography.trim() || !newPet.value.behavioralNotes.trim()) {
    errorMessage.value = 'Preencha todos os campos obrigatórios marcados com asterisco.';
    return;
  }

  isSubmitting.value = true;
  errorMessage.value = null;

  try {
    await CPetService.create(newPet.value);
    successMessage.value = `Animal ${newPet.value.name} cadastrado com sucesso no sistema!`;
    isCreateModalVisible.value = false;
    // Reseta form
    newPet.value.name = '';
    newPet.value.biography = '';
    newPet.value.behavioralNotes = '';
    await loadPets();
  } catch (pErr: any) {
    errorMessage.value = 'Falha ao salvar animal. Verifique os dados e tente novamente.';
  } finally {
    isSubmitting.value = false;
  }
}

async function handleViewIntents(pPet: IPet): Promise<void> {
  selectedPetForIntents.value = pPet;
  isIntentsModalVisible.value = true;
  isLoadingIntents.value = true;
  try {
    petIntents.value = await CAdoptionService.findByPetId(pPet.id);
  } catch (pErr) {
    petIntents.value = [];
  } finally {
    isLoadingIntents.value = false;
  }
}

async function handleDeletePet(pId: number): Promise<void> {
  if (!confirm('Deseja realmente remover este registro de animal?')) return;
  try {
    await CPetService.delete(pId);
    toast.add({
      severity: 'success',
      summary: 'Registro Removido',
      detail: 'O animal foi removido com sucesso.',
      life: 4000,
    });
    await loadPets();
  } catch (pErr) {
    toast.add({
      severity: 'error',
      summary: 'Erro ao Excluir',
      detail: 'Não foi possível remover o animal no momento.',
      life: 5000,
    });
  }
}

function formatAge(pAgeMonths: number): string {
  if (pAgeMonths < 12) return `${pAgeMonths} meses`;
  const years = Math.floor(pAgeMonths / 12);
  const remainingMonths = pAgeMonths % 12;
  if (remainingMonths === 0) return `${years} ${years === 1 ? 'ano' : 'anos'}`;
  return `${years}a ${remainingMonths}m`;
}

onMounted(() => {
  loadPets();
});
</script>

<template>
  <div class="max-w-6xl mx-auto px-4 py-8 space-y-8">
    <!-- Cabeçalho da Área do Mantenedor -->
    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-slate-200 pb-6">
      <div>
        <div class="flex items-center gap-2">
          <span class="px-2.5 py-1 rounded-md bg-emerald-50 text-emerald-700 text-xs font-bold border border-emerald-200 uppercase tracking-wider">
            {{ authStore.isGuardian ? 'Painel do Protetor Independente' : 'Painel do Mantenedor' }}
          </span>
          <span class="text-xs text-slate-500">
            {{ authStore.isGuardian ? 'Tutor Responsável & Resgates' : 'ONGs, Abrigos & Pet Shops' }}
          </span>
        </div>
        <h1 class="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight mt-1">
          {{ authStore.isGuardian ? 'Meus Animais para Doação Responsável' : 'Gestão de Animais & Entrada de Acolhidos' }}
        </h1>
        <p class="text-sm text-slate-600 max-w-2xl mt-1">
          {{ authStore.isGuardian
            ? 'Cadastre os animais resgatados ou sob sua custódia, documente o temperamento para a triagem da IA e acompanhe os adotantes compatíveis.'
            : 'Cadastre novos animais com mídias e diário comportamental detalhado para alimentar a inferência da LLM e avalie os adotantes interessados por ordem de afinidade ética.'
          }}
        </p>
      </div>

      <!-- Botão Principal de Cadastro -->
      <Button
        label="Dar Entrada em Novo Pet"
        icon="pi pi-plus"
        class="bg-emerald-600 hover:bg-emerald-700 text-white font-medium px-5 py-3 rounded-xl transition shadow-sm shrink-0 self-start sm:self-auto"
        @click="handleOpenCreateModal"
      />
    </div>

    <!-- Mensagens de Feedback -->
    <Message v-if="successMessage" severity="success" :closable="true" @close="successMessage = null">
      {{ successMessage }}
    </Message>
    <Message v-if="errorMessage" severity="error" :closable="true" @close="errorMessage = null">
      {{ errorMessage }}
    </Message>

    <!-- Cards de Métricas do Mantenedor -->
    <div class="grid grid-cols-1 sm:grid-cols-3 gap-4">
      <div class="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs flex items-center justify-between">
        <div>
          <span class="text-xs font-semibold text-slate-500 uppercase tracking-wider">Total sob Custódia</span>
          <span class="block text-2xl font-bold text-slate-900 mt-1">{{ pets.length }} animais</span>
        </div>
        <div class="w-12 h-12 rounded-xl bg-slate-100 text-slate-700 flex items-center justify-center text-xl">
          <i class="pi pi-box"></i>
        </div>
      </div>

      <div class="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs flex items-center justify-between">
        <div>
          <span class="text-xs font-semibold text-emerald-600 uppercase tracking-wider">Disponíveis p/ Adoção</span>
          <span class="block text-2xl font-bold text-emerald-700 mt-1">{{ availablePetsCount }} ativos</span>
        </div>
        <div class="w-12 h-12 rounded-xl bg-emerald-50 text-emerald-600 flex items-center justify-center text-xl">
          <i class="pi pi-check-circle"></i>
        </div>
      </div>

      <div class="bg-white p-5 rounded-2xl border border-slate-200 shadow-xs flex items-center justify-between">
        <div>
          <span class="text-xs font-semibold text-indigo-600 uppercase tracking-wider">Espécies no Abrigo</span>
          <span class="block text-2xl font-bold text-indigo-900 mt-1">{{ dogsCount }} Cães / {{ catsCount }} Gatos</span>
        </div>
        <div class="w-12 h-12 rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center text-xl">
          <i class="pi pi-heart"></i>
        </div>
      </div>
    </div>

    <!-- Tabela / Lista de Animais Cadastrados -->
    <div class="bg-white rounded-2xl border border-slate-200 shadow-xs overflow-hidden">
      <div class="p-6 border-b border-slate-100 flex items-center justify-between">
        <h2 class="text-lg font-bold text-slate-900 flex items-center gap-2">
          <i class="pi pi-list text-emerald-600"></i>
          <span>Catálogo de Animais sob Gestão</span>
        </h2>
        <Button
          label="Atualizar"
          icon="pi pi-refresh"
          text
          size="small"
          :loading="isLoading"
          @click="loadPets"
        />
      </div>

      <div v-if="isLoading" class="p-12 text-center text-slate-500">
        <i class="pi pi-spin pi-spinner text-3xl text-emerald-600 mb-2"></i>
        <p>Carregando animais...</p>
      </div>

      <div v-else-if="pets.length === 0" class="p-12 text-center text-slate-500 space-y-3">
        <div class="w-16 h-16 rounded-full bg-slate-100 text-slate-400 flex items-center justify-center mx-auto text-2xl">
          <i class="pi pi-inbox"></i>
        </div>
        <p class="font-medium text-slate-700">Nenhum animal cadastrado ainda.</p>
        <p class="text-xs text-slate-500 max-w-sm mx-auto">
          Dê entrada no primeiro animal preenchendo as fotos, vídeo e notas comportamentais.
        </p>
      </div>

      <div v-else class="divide-y divide-slate-100">
        <div
          v-for="pet in pets"
          :key="pet.id"
          class="p-5 flex flex-col md:flex-row md:items-center justify-between gap-4 hover:bg-slate-50/60 transition"
        >
          <!-- Info e Mídia do Pet -->
          <div class="flex items-center gap-4">
            <div class="relative w-16 h-16 rounded-xl overflow-hidden bg-slate-100 shrink-0 border border-slate-200">
              <img
                v-if="pet.photoUrl"
                :src="pet.photoUrl"
                :alt="pet.name"
                class="w-full h-full object-cover"
              />
              <div v-else class="w-full h-full flex items-center justify-center font-bold text-slate-400">
                {{ pet.name.charAt(0) }}
              </div>
            </div>

            <div class="space-y-1">
              <div class="flex items-center gap-2">
                <h3 class="font-bold text-slate-900 text-base">{{ pet.name }}</h3>
                <Tag :value="pet.species === 'DOG' ? 'Cão' : 'Gato'" rounded class="text-xs" />
                <Tag :value="pet.gender === 'MALE' ? 'Macho' : 'Fêmea'" severity="secondary" rounded class="text-xs" />
                <span class="text-xs px-2 py-0.5 rounded bg-slate-100 text-slate-600 font-medium">
                  {{ formatAge(pet.ageMonths) }}
                </span>
              </div>
              <p class="text-xs text-slate-500 flex items-center gap-1.5">
                <span>{{ pet.shelterName }}</span>
                <span>•</span>
                <span>{{ pet.shelterCity }}</span>
                <span>•</span>
                <span class="text-emerald-700 font-medium">Energia: {{ pet.energyLevel }}/5</span>
              </p>
            </div>
          </div>

          <!-- Ações do Pet -->
          <div class="flex items-center gap-2 self-end md:self-auto shrink-0">
            <Button
              label="Ver Interessados"
              icon="pi pi-users"
              severity="secondary"
              size="small"
              class="rounded-xl text-xs font-medium"
              @click="handleViewIntents(pet)"
            />
            <Button
              icon="pi pi-trash"
              severity="danger"
              text
              size="small"
              class="rounded-xl"
              @click="handleDeletePet(pet.id)"
            />
          </div>
        </div>
      </div>
    </div>

    <!-- Modal para Dar Entrada em Novo Animal -->
    <Dialog
      v-model:visible="isCreateModalVisible"
      modal
      header="Dar Entrada em Novo Animal (Acolhimento ONG / Pet Shop)"
      :style="{ width: '92vw', maxWidth: '800px' }"
      :closable="!isSubmitting"
    >
      <form class="space-y-6 pt-3" @submit.prevent="handleCreatePet">
        <!-- Seção 1: Identificação Básica -->
        <div class="space-y-4">
          <h4 class="text-xs font-bold text-slate-500 uppercase tracking-wider border-b pb-1">
            1. Dados Cadastrais Gerais
          </h4>
          <div class="grid grid-cols-1 sm:grid-cols-3 gap-3">
            <div class="space-y-1">
              <label class="block text-xs font-semibold text-slate-700">Nome do Animal *</label>
              <InputText v-model="newPet.name" class="w-full" placeholder="Ex.: Mel, Tob" required />
            </div>
            <div class="space-y-1">
              <label class="block text-xs font-semibold text-slate-700">Espécie *</label>
              <Select v-model="newPet.species" :options="SPECIES_OPTIONS" option-label="label" option-value="value" class="w-full" />
            </div>
            <div class="space-y-1">
              <label class="block text-xs font-semibold text-slate-700">Sexo Biológico *</label>
              <Select v-model="newPet.gender" :options="GENDER_OPTIONS" option-label="label" option-value="value" class="w-full" />
            </div>
          </div>

          <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div class="space-y-1">
              <label class="block text-xs font-semibold text-slate-700">Porte Físico *</label>
              <Select v-model="newPet.size" :options="SIZE_OPTIONS" option-label="label" option-value="value" class="w-full" />
            </div>
            <div class="space-y-1">
              <label class="block text-xs font-semibold text-slate-700">Idade Estimada (Meses) *</label>
              <InputNumber v-model="newPet.ageMonths" :min="1" :max="240" class="w-full" required />
            </div>
          </div>
        </div>

        <!-- Seção 2: Mídias (Fotos e Vídeos) -->
        <div class="space-y-4">
          <h4 class="text-xs font-bold text-slate-500 uppercase tracking-wider border-b pb-1">
            2. Publicação de Mídias (Fotos e Vídeos)
          </h4>
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div class="space-y-1">
              <label class="block text-xs font-semibold text-slate-700">URL da Foto Principal (Capa) *</label>
              <InputText v-model="newPet.photoUrl" class="w-full" placeholder="https://exemplo.com/foto.jpg" required />
            </div>
            <div class="space-y-1">
              <label class="block text-xs font-semibold text-slate-700">URL de Vídeo Demonstrativo (MP4 / YouTube)</label>
              <InputText v-model="newPet.videoUrl" class="w-full" placeholder="https://exemplo.com/video.mp4" />
            </div>
          </div>

          <!-- Preview em tempo real da Foto -->
          <div v-if="newPet.photoUrl" class="flex items-center gap-3 p-3 bg-slate-50 rounded-xl border border-slate-200">
            <img :src="newPet.photoUrl" alt="Preview" class="w-16 h-16 rounded-lg object-cover" />
            <span class="text-xs text-slate-500">Preview da foto de capa</span>
          </div>
        </div>

        <!-- Seção 3: Biografia e Sensibilização -->
        <div class="space-y-1.5">
          <label class="block text-xs font-semibold text-slate-700">História de Resgate e Biografia Afetiva *</label>
          <Textarea
            v-model="newPet.biography"
            rows="3"
            class="w-full"
            placeholder="Conte a história do animal, como foi acolhido e traços de carinho..."
            required
          />
        </div>

        <!-- Seção 4: Perfil Comportamental e Diário para a LLM -->
        <div class="space-y-4">
          <h4 class="text-xs font-bold text-slate-500 uppercase tracking-wider border-b pb-1">
            3. Variáveis Comportamentais & Diário para LLM
          </h4>
          <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
            <div class="space-y-1">
              <label class="block text-xs font-semibold text-slate-700">Nível de Energia (1 a 5) *</label>
              <Select v-model="newPet.energyLevel" :options="ENERGY_OPTIONS" option-label="label" option-value="value" class="w-full" />
            </div>
            <div class="space-y-1">
              <label class="block text-xs font-semibold text-slate-700">Tolerância Máxima à Solidão (Horas/Dia) *</label>
              <InputNumber v-model="newPet.solitaryToleranceHours" :min="1" :max="16" class="w-full" required />
            </div>
          </div>

          <div class="flex flex-wrap items-center gap-6 pt-1">
            <div class="flex items-center gap-2">
              <Checkbox v-model="newPet.kidsFriendly" binary input-id="in_kids" />
              <label for="in_kids" class="text-xs text-slate-700 font-medium">Sociável com crianças</label>
            </div>
            <div class="flex items-center gap-2">
              <Checkbox v-model="newPet.petsFriendly" binary input-id="in_pets" />
              <label for="in_pets" class="text-xs text-slate-700 font-medium">Sociável com outros animais</label>
            </div>
            <div class="flex items-center gap-2">
              <Checkbox v-model="newPet.isVaccinated" binary input-id="in_vac" />
              <label for="in_vac" class="text-xs text-slate-700 font-medium">Vacinação em dia</label>
            </div>
            <div class="flex items-center gap-2">
              <Checkbox v-model="newPet.isNeutered" binary input-id="in_neut" />
              <label for="in_neut" class="text-xs text-slate-700 font-medium">Castrado</label>
            </div>
          </div>

          <div class="space-y-1.5">
            <label class="block text-xs font-semibold text-indigo-950 flex items-center justify-between">
              <span>Diário de Observações Comportamentais (Processado pela LLM): *</span>
              <span class="text-xs text-indigo-600 font-normal">Base do Alinhamento Semântico</span>
            </label>
            <Textarea
              v-model="newPet.behavioralNotes"
              rows="3"
              class="w-full border-indigo-200"
              placeholder="Descreva hábitos observados: 'Gosta de brincar com bolinha, não late para campainha, fica muito tranquilo durante a tarde...'"
              required
            />
          </div>
        </div>

        <!-- Seção 5: Dados do Mantenedor -->
        <div class="space-y-3">
          <h4 class="text-xs font-bold text-slate-500 uppercase tracking-wider border-b pb-1">
            4. Dados do Mantenedor Responsável
          </h4>
          <div class="grid grid-cols-1 sm:grid-cols-3 gap-3">
            <div class="space-y-1">
              <label class="block text-xs font-semibold text-slate-700">Nome da ONG / Pet Shop *</label>
              <InputText v-model="newPet.shelterName" class="w-full" required />
            </div>
            <div class="space-y-1">
              <label class="block text-xs font-semibold text-slate-700">Cidade e UF *</label>
              <InputText v-model="newPet.shelterCity" class="w-full" required />
            </div>
            <div class="space-y-1">
              <label class="block text-xs font-semibold text-slate-700">WhatsApp de Contato *</label>
              <InputText v-model="newPet.contactWhatsapp" class="w-full" placeholder="32999999999" required />
            </div>
          </div>
        </div>

        <div class="pt-4 flex justify-end gap-3 border-t border-slate-100">
          <Button label="Cancelar" severity="secondary" text :disabled="isSubmitting" @click="isCreateModalVisible = false" />
          <Button
            type="submit"
            label="Cadastrar Animal no Sistema"
            icon="pi pi-check"
            :loading="isSubmitting"
            class="bg-emerald-600 hover:bg-emerald-700 text-white font-medium px-5 py-2.5 rounded-xl transition shadow-sm"
          />
        </div>
      </form>
    </Dialog>

    <!-- Modal de Visualização da Fila de Interessados -->
    <Dialog
      v-model:visible="isIntentsModalVisible"
      modal
      :header="`Interessados no Animal: ${selectedPetForIntents?.name || ''}`"
      :style="{ width: '92vw', maxWidth: '720px' }"
    >
      <div class="py-2 space-y-4">
        <div v-if="isLoadingIntents" class="p-8 text-center text-slate-500">
          <i class="pi pi-spin pi-spinner text-2xl text-emerald-600 mb-2"></i>
          <p>Consultando manifestações de interesse...</p>
        </div>

        <div v-else-if="petIntents.length === 0" class="p-8 text-center text-slate-500 space-y-2">
          <p class="font-medium">Nenhum adotante manifestou interesse para este pet ainda.</p>
          <p class="text-xs">Quando um adotante preencher o diagnóstico de recomendação e solicitar contato, ele aparecerá aqui com o score de compatibilidade.</p>
        </div>

        <div v-else class="space-y-3">
          <div
            v-for="intent in petIntents"
            :key="intent.id"
            class="p-4 rounded-xl border border-slate-200 bg-slate-50/70 space-y-2"
          >
            <div class="flex items-center justify-between">
              <div>
                <span class="font-bold text-slate-900 text-base">{{ intent.adopterName }}</span>
                <span class="text-xs text-slate-500 ml-2">Protocolo #{{ intent.id }}</span>
              </div>
              <span class="px-2.5 py-1 rounded-full text-xs font-extrabold bg-emerald-100 text-emerald-800">
                {{ Math.round(intent.compatibilityScore) }}% de Afinidade
              </span>
            </div>
            <p class="text-xs text-slate-600">
              Status: <Tag :value="intent.status" severity="info" rounded class="text-[10px]" />
            </p>
          </div>
        </div>

        <div class="pt-3 flex justify-end">
          <Button label="Fechar" severity="secondary" @click="isIntentsModalVisible = false" />
        </div>
      </div>
    </Dialog>
  </div>
</template>
