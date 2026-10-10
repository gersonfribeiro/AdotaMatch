<script setup lang="ts">
// Ecossistema Vue
import { ref, watch, onMounted } from 'vue';

// PrimeVue e UI
import Dialog from 'primevue/dialog';
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Textarea from 'primevue/textarea';
import Message from 'primevue/message';

// Stores
import { useAuthStore } from '@/features/auth/stores/useAuthStore';

// Services
import { CAdoptionService } from '@/features/adopter/services/CAdoptionService';

// Types e Interfaces
import type { IAdoptionIntentResponse } from '@/features/adopter/models/IAdoptionIntent';

/**
 * @property {boolean} visible - Controla a exibição do diálogo modal.
 * @property {number} petId - Identificador do animal selecionado.
 * @property {string} petName - Nome do animal selecionado.
 * @property {number} compatibilityScore - Pontuação obtida na recomendação.
 * @property {string} adopterNarrative - Relato do estilo de vida informado.
 * @property {string} contactWhatsapp - WhatsApp da instituição ou pet shop.
 */
type TProps = {
  petId: number;
  petName: string;
  compatibilityScore: number;
  adopterNarrative: string;
  contactWhatsapp: string;
};

/**
 * @property {[]} success - Emitido quando a manifestação de interesse é concluída com sucesso.
 */
type TEmits = {
  success: [];
};

// Props e Emits
const props = defineProps<TProps>();
const emit = defineEmits<TEmits>();

// Stores
const authStore = useAuthStore();

// Reativas - Model
const isVisible = defineModel<boolean>('visible', { default: false });

// Reativas - Ref
const isSubmitting = ref(false);
const errorMessage = ref<string | null>(null);
const successResponse = ref<IAdoptionIntentResponse | null>(null);

const adopterName = ref('');
const adopterEmail = ref('');
const adopterPhone = ref('');
const adopterCity = ref('');
const messageToShelter = ref(
  `Olá! Gostei muito do perfil do ${props.petName} e gostaria de agendar uma conversa ou visita para conhecê-lo melhor.`
);

function fillFromAuthUser(): void {
  if (authStore.user) {
    if (!adopterName.value) adopterName.value = authStore.user.name;
    if (!adopterEmail.value) adopterEmail.value = authStore.user.email;
    if (!adopterPhone.value && authStore.user.phone) adopterPhone.value = authStore.user.phone;
    if (!adopterCity.value && authStore.user.city) adopterCity.value = authStore.user.city;
  }
}

onMounted(() => {
  fillFromAuthUser();
});

watch(isVisible, (pVal) => {
  if (pVal) {
    fillFromAuthUser();
  }
});

// Funções
async function handleSubmit(): Promise<void> {
  if (isSubmitting.value) return;

  if (!adopterName.value.trim() || !adopterEmail.value.trim() || !adopterPhone.value.trim() || !adopterCity.value.trim()) {
    errorMessage.value = 'Por favor, preencha todos os campos obrigatórios de contato.';
    return;
  }

  isSubmitting.value = true;
  errorMessage.value = null;

  try {
    const response = await CAdoptionService.submitIntent({
      petId: props.petId,
      petName: props.petName,
      adopterName: adopterName.value.trim(),
      adopterEmail: adopterEmail.value.trim(),
      adopterPhone: adopterPhone.value.trim(),
      adopterCity: adopterCity.value.trim(),
      compatibilityScore: props.compatibilityScore,
      adopterNarrative: props.adopterNarrative,
      messageToShelter: messageToShelter.value.trim(),
    });

    successResponse.value = response;
    emit('success');
  } catch (pErr: any) {
    errorMessage.value = pErr?.response?.data?.message || 'Falha ao registrar interesse. Tente novamente.';
  } finally {
    isSubmitting.value = false;
  }
}

function handleOpenWhatsapp(): void {
  const cleanPhone = props.contactWhatsapp.replace(/\D/g, '');
  const text = encodeURIComponent(
    `Olá! Submeti uma manifestação de interesse responsável pelo AdotaMatch para o(a) ${props.petName} (Protocolo #${successResponse.value?.id || ''}, Compatibilidade: ${Math.round(props.compatibilityScore)}%). Gostaria de saber os próximos passos!`
  );
  window.open(`https://wa.me/55${cleanPhone}?text=${text}`, '_blank');
}

function handleClose(): void {
  isVisible.value = false;
  successResponse.value = null;
  errorMessage.value = null;
}
</script>

<template>
  <Dialog
    v-model:visible="isVisible"
    modal
    :header="successResponse ? 'Manifestação Enviada com Sucesso!' : `Manifestar Interesse em Adotar: ${props.petName}`"
    :style="{ width: '92vw', maxWidth: '580px' }"
    :closable="!isSubmitting"
  >
    <!-- Estado de Sucesso com Protocolo -->
    <div v-if="successResponse" class="py-4 space-y-5 text-center">
      <div class="w-16 h-16 rounded-full bg-emerald-100 text-emerald-600 flex items-center justify-center mx-auto text-2xl">
        <i class="pi pi-check-circle"></i>
      </div>
      <div>
        <h3 class="text-xl font-bold text-slate-900">Solicitação Registrada!</h3>
        <p class="text-sm text-slate-600 mt-1">
          A instituição responsável pelo <strong>{{ props.petName }}</strong> recebeu seus dados e dossiê de compatibilidade de <strong>{{ Math.round(props.compatibilityScore) }}%</strong>.
        </p>
        <div class="mt-3 inline-block px-3 py-1 bg-slate-100 text-slate-800 rounded-lg text-xs font-mono">
          Protocolo: #{{ successResponse.id }}
        </div>
      </div>

      <div class="pt-2 flex flex-col sm:flex-row gap-3 justify-center">
        <Button
          v-if="props.contactWhatsapp"
          label="Conversar no WhatsApp Agora"
          icon="pi pi-whatsapp"
          class="bg-emerald-600 hover:bg-emerald-700 text-white font-medium px-4 py-2.5 rounded-xl transition"
          @click="handleOpenWhatsapp"
        />
        <Button
          label="Concluir"
          severity="secondary"
          class="px-4 py-2.5 rounded-xl font-medium"
          @click="handleClose"
        />
      </div>
    </div>

    <!-- Formulário de Envio -->
    <form v-else class="space-y-4 pt-2" @submit.prevent="handleSubmit">
      <p class="text-xs text-slate-500">
        Seu dossiê de perfil socioambiental e o score de compatibilidade calculado serão enviados para a equipe responsável pela triagem ética do animal.
      </p>

      <Message v-if="errorMessage" severity="error" :closable="false">
        {{ errorMessage }}
      </Message>

      <div class="space-y-1.5">
        <label class="block text-xs font-semibold text-slate-700">Seu Nome Completo *</label>
        <InputText v-model="adopterName" class="w-full" placeholder="Ex.: Maria de Oliveira" required />
      </div>

      <div class="grid grid-cols-1 sm:grid-cols-2 gap-3">
        <div class="space-y-1.5">
          <label class="block text-xs font-semibold text-slate-700">E-mail de Contato *</label>
          <InputText v-model="adopterEmail" type="email" class="w-full" placeholder="exemplo@email.com" required />
        </div>
        <div class="space-y-1.5">
          <label class="block text-xs font-semibold text-slate-700">WhatsApp / Telefone *</label>
          <InputText v-model="adopterPhone" class="w-full" placeholder="(32) 99999-9999" required />
        </div>
      </div>

      <div class="space-y-1.5">
        <label class="block text-xs font-semibold text-slate-700">Cidade e UF de Residência *</label>
        <InputText v-model="adopterCity" class="w-full" placeholder="Ex.: Ubá - MG" required />
      </div>

      <div class="space-y-1.5">
        <label class="block text-xs font-semibold text-slate-700">Mensagem para a Instituição / Pet Shop *</label>
        <Textarea v-model="messageToShelter" rows="3" class="w-full" required />
      </div>

      <div class="pt-4 flex justify-end gap-2 border-t border-slate-100">
        <Button
          label="Cancelar"
          severity="secondary"
          text
          :disabled="isSubmitting"
          @click="handleClose"
        />
        <Button
          type="submit"
          label="Enviar Manifestação de Interesse"
          icon="pi pi-send"
          :loading="isSubmitting"
          class="bg-emerald-600 hover:bg-emerald-700 text-white font-medium px-5 py-2.5 rounded-xl transition shadow-sm"
        />
      </div>
    </form>
  </Dialog>
</template>
