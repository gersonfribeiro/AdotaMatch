<script setup lang="ts">
// Ecossistema Vue
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';

// PrimeVue e UI
import Button from 'primevue/button';
import Tag from 'primevue/tag';
import Message from 'primevue/message';

// Stores
import { useAuthStore } from '@/features/auth/stores/useAuthStore';

// Services
import { CAdoptionService } from '@/features/adopter/services/CAdoptionService';

// Types e Interfaces
import type { IAdoptionIntentResponse } from '@/features/adopter/models/IAdoptionIntent';

// Router & Stores
const router = useRouter();
const authStore = useAuthStore();

// Reativas - Ref
const intents = ref<IAdoptionIntentResponse[]>([]);
const isLoading = ref(false);
const errorMessage = ref<string | null>(null);

// Funções
async function loadIntents(): Promise<void> {
  if (!authStore.user?.email) return;

  isLoading.value = true;
  errorMessage.value = null;
  try {
    intents.value = await CAdoptionService.findMyIntents(authStore.user.email);
  } catch (pErr: any) {
    errorMessage.value = 'Não foi possível carregar o histórico de manifestações.';
  } finally {
    isLoading.value = false;
  }
}

function formatDate(pIso: string): string {
  try {
    const d = new Date(pIso);
    return d.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });
  } catch {
    return pIso;
  }
}

function getStatusSeverity(pStatus: string): 'info' | 'success' | 'warn' | 'danger' {
  switch (pStatus) {
    case 'APPROVED':
      return 'success';
    case 'IN_REVIEW':
      return 'warn';
    case 'REJECTED':
      return 'danger';
    default:
      return 'info';
  }
}

function getStatusLabel(pStatus: string): string {
  switch (pStatus) {
    case 'APPROVED':
      return 'Aprovada';
    case 'IN_REVIEW':
      return 'Em Análise';
    case 'REJECTED':
      return 'Não Aprovada';
    default:
      return 'Protocolada';
  }
}

onMounted(() => {
  if (!authStore.isAuthenticated) {
    router.push('/login');
  } else {
    loadIntents();
  }
});
</script>

<template>
  <div class="max-w-5xl mx-auto px-4 py-8 space-y-6">
    <div class="flex flex-col sm:flex-row sm:items-center justify-between gap-4 border-b border-slate-200 pb-6">
      <div>
        <h1 class="text-2xl sm:text-3xl font-extrabold text-slate-900 tracking-tight">
          Minhas Manifestações de Interesse
        </h1>
        <p class="text-sm text-slate-600 mt-1">
          Acompanhe os pedidos de adoção responsável enviados para abrigos, ONGs e tutores.
        </p>
      </div>

      <Button
        label="Buscar Novos Matches"
        icon="pi pi-sparkles"
        class="bg-indigo-600 hover:bg-indigo-700 text-white font-medium px-4 py-2.5 rounded-xl transition shadow-sm self-start sm:self-auto"
        @click="router.push('/match')"
      />
    </div>

    <Message v-if="errorMessage" severity="error" :closable="false">
      {{ errorMessage }}
    </Message>

    <!-- Estado Vazio -->
    <div v-if="!isLoading && intents.length === 0" class="text-center py-16 bg-white rounded-2xl border border-slate-200 p-8 space-y-4">
      <div class="w-16 h-16 mx-auto rounded-2xl bg-indigo-50 text-indigo-600 flex items-center justify-center text-3xl font-bold">
        🐾
      </div>
      <h3 class="text-lg font-bold text-slate-900">
        Nenhuma manifestação enviada até o momento
      </h3>
      <p class="text-sm text-slate-500 max-w-md mx-auto">
        Utilize nosso algoritmo de recomendação explicável para encontrar um cão ou gato compatível com sua rotina!
      </p>
      <Button
        label="Calcular Recomendações Responsáveis"
        icon="pi pi-arrow-right"
        class="bg-indigo-600 hover:bg-indigo-700 text-white font-medium px-5 py-2.5 rounded-xl transition shadow-sm"
        @click="router.push('/match')"
      />
    </div>

    <!-- Lista de Solicitações -->
    <div v-else class="space-y-4">
      <div
        v-for="intent in intents"
        :key="intent.id"
        class="bg-white rounded-2xl border border-slate-200 p-6 shadow-xs flex flex-col md:flex-row md:items-center justify-between gap-6 hover:shadow-sm transition"
      >
        <div class="space-y-2">
          <div class="flex items-center gap-3">
            <span class="text-lg font-extrabold text-slate-900">
              {{ intent.petName }}
            </span>
            <Tag :value="getStatusLabel(intent.status)" :severity="getStatusSeverity(intent.status)" />
            <span class="text-xs text-slate-400">
              Protocolo #{{ intent.id }}
            </span>
          </div>

          <p class="text-xs text-slate-500">
            Enviado em {{ formatDate(intent.createdAt) }}
          </p>

          <p class="text-sm text-slate-700 bg-slate-50 p-3 rounded-xl border border-slate-100 italic">
            "{{ intent.messageToShelter }}"
          </p>
        </div>

        <div class="flex sm:flex-col items-center sm:items-end justify-between border-t sm:border-t-0 pt-4 sm:pt-0 border-slate-100 shrink-0">
          <div class="text-right">
            <span class="text-xs text-slate-400 block">Afinidade com IA</span>
            <span class="text-2xl font-black text-emerald-600">
              {{ Math.round(intent.compatibilityScore) }}%
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>
