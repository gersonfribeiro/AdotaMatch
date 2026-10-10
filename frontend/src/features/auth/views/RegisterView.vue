<script setup lang="ts">
// Ecossistema Vue
import { ref } from 'vue';
import { useRouter } from 'vue-router';

// PrimeVue e UI
import Button from 'primevue/button';
import InputText from 'primevue/inputtext';
import Password from 'primevue/password';
import Message from 'primevue/message';
import { useToast } from 'primevue/usetoast';

// Stores
import { useAuthStore } from '@/features/auth/stores/useAuthStore';

// Types
import type { TUserRole } from '@/features/auth/models/IUser';

// Router & Toast
const router = useRouter();
const toast = useToast();
const authStore = useAuthStore();

// Reativas - Ref
const selectedRole = ref<TUserRole>('ADOPTER');
const name = ref('');
const email = ref('');
const password = ref('');
const phone = ref('');
const city = ref('Ubá - MG');
const document = ref('');
const organizationName = ref('');
const isSubmitting = ref(false);

// Funções
async function handleRegister(): Promise<void> {
  if (!name.value.trim() || !email.value.trim() || !password.value.trim() || !phone.value.trim() || !city.value.trim()) {
    toast.add({
      severity: 'warn',
      summary: 'Campos Obrigatórios',
      detail: 'Preencha todos os campos fundamentais para criar sua conta.',
      life: 3000,
    });
    return;
  }

  isSubmitting.value = true;
  try {
    await authStore.register({
      name: name.value.trim(),
      email: email.value.trim(),
      password: password.value,
      role: selectedRole.value,
      phone: phone.value.trim(),
      city: city.value.trim(),
      document: document.value.trim() || undefined,
      organizationName: selectedRole.value === 'SHELTER' ? organizationName.value.trim() || name.value.trim() : undefined,
    });

    toast.add({
      severity: 'success',
      summary: 'Conta Criada com Sucesso!',
      detail: `Cadastro realizado como ${authStore.roleLabel}.`,
      life: 4000,
    });

    if (authStore.isShelter || authStore.isGuardian) {
      router.push('/shelter');
    } else {
      router.push('/match');
    }
  } catch {
    // Erro na store
  } finally {
    isSubmitting.value = false;
  }
}
</script>

<template>
  <div class="max-w-2xl mx-auto px-4 py-10 space-y-6">
    <div class="text-center space-y-2">
      <h1 class="text-3xl font-extrabold text-slate-900 tracking-tight">
        Criar Conta no AdotaMatch
      </h1>
      <p class="text-sm text-slate-600 max-w-lg mx-auto">
        Escolha o seu perfil para personalizar sua experiência de adoção responsável ou gestão de animais.
      </p>
    </div>

    <!-- Seleção Visual de Perfil -->
    <div class="grid grid-cols-1 md:grid-cols-3 gap-4">
      <!-- Card Adotante -->
      <div
        class="border-2 rounded-2xl p-4 cursor-pointer transition text-left space-y-2"
        :class="selectedRole === 'ADOPTER' ? 'border-indigo-600 bg-indigo-50/50 shadow-sm' : 'border-slate-200 bg-white hover:border-slate-300'"
        @click="selectedRole = 'ADOPTER'"
      >
        <div class="w-10 h-10 rounded-xl bg-indigo-100 text-indigo-700 flex items-center justify-center text-xl font-bold">
          🐾
        </div>
        <h3 class="font-bold text-slate-900 text-sm">Quero Adotar</h3>
        <p class="text-xs text-slate-500 leading-relaxed">
          Procuro um pet e quero recomendações assertivas com IA para combinar com minha rotina.
        </p>
      </div>

      <!-- Card Protetor Independente -->
      <div
        class="border-2 rounded-2xl p-4 cursor-pointer transition text-left space-y-2"
        :class="selectedRole === 'GUARDIAN' ? 'border-emerald-600 bg-emerald-50/50 shadow-sm' : 'border-slate-200 bg-white hover:border-slate-300'"
        @click="selectedRole = 'GUARDIAN'"
      >
        <div class="w-10 h-10 rounded-xl bg-emerald-100 text-emerald-700 flex items-center justify-center text-xl font-bold">
          🏡
        </div>
        <h3 class="font-bold text-slate-900 text-sm">Protetor / Tutor</h3>
        <p class="text-xs text-slate-500 leading-relaxed">
          Pessoa física que resgatou ou possui animais e deseja cadastrá-los para doação responsável.
        </p>
      </div>

      <!-- Card Pet Shop / Abrigo -->
      <div
        class="border-2 rounded-2xl p-4 cursor-pointer transition text-left space-y-2"
        :class="selectedRole === 'SHELTER' ? 'border-amber-600 bg-amber-50/50 shadow-sm' : 'border-slate-200 bg-white hover:border-slate-300'"
        @click="selectedRole = 'SHELTER'"
      >
        <div class="w-10 h-10 rounded-xl bg-amber-100 text-amber-700 flex items-center justify-center text-xl font-bold">
          🏢
        </div>
        <h3 class="font-bold text-slate-900 text-sm">Pet Shop / Abrigo</h3>
        <p class="text-xs text-slate-500 leading-relaxed">
          ONG, abrigo institucional ou empresa parceira com múltiplos animais sob custódia.
        </p>
      </div>
    </div>

    <!-- Formulário Cadastral -->
    <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-6 sm:p-8 space-y-6">
      <Message v-if="authStore.errorMessage" severity="error" :closable="true" @close="authStore.errorMessage = null">
        {{ authStore.errorMessage }}
      </Message>

      <form class="space-y-5" @submit.prevent="handleRegister">
        <div class="grid grid-cols-1 sm:grid-cols-2 gap-4">
          <!-- Nome ou Razão Social -->
          <div class="space-y-1.5 sm:col-span-2">
            <label class="block text-sm font-medium text-slate-700">
              {{ selectedRole === 'SHELTER' ? 'Nome do Pet Shop ou ONG *' : 'Nome Completo *' }}
            </label>
            <InputText
              v-model="name"
              class="w-full"
              :placeholder="selectedRole === 'SHELTER' ? 'Ex.: Pet Shop Mundo Animal' : 'Ex.: Gerson Fernandes'"
              required
            />
          </div>

          <!-- Nome da Razão Social Adicional (apenas empresas) -->
          <div v-if="selectedRole === 'SHELTER'" class="space-y-1.5 sm:col-span-2">
            <label class="block text-sm font-medium text-slate-700">Razão Social / Nome Fantasia Formal</label>
            <InputText
              v-model="organizationName"
              class="w-full"
              placeholder="Ex.: Mundo Animal Comércio e Serviços Ltda"
            />
          </div>

          <!-- E-mail -->
          <div class="space-y-1.5">
            <label class="block text-sm font-medium text-slate-700">E-mail *</label>
            <InputText
              v-model="email"
              type="email"
              class="w-full"
              placeholder="contato@exemplo.com"
              required
            />
          </div>

          <!-- Senha -->
          <div class="space-y-1.5">
            <label class="block text-sm font-medium text-slate-700">Senha (mínimo 6 caracteres) *</label>
            <Password
              v-model="password"
              :feedback="false"
              toggle-mask
              class="w-full"
              input-class="w-full"
              placeholder="••••••••"
              required
            />
          </div>

          <!-- WhatsApp de Contato -->
          <div class="space-y-1.5">
            <label class="block text-sm font-medium text-slate-700">WhatsApp / Telefone *</label>
            <InputText
              v-model="phone"
              class="w-full"
              placeholder="(32) 99999-8888"
              required
            />
          </div>

          <!-- Cidade / UF -->
          <div class="space-y-1.5">
            <label class="block text-sm font-medium text-slate-700">Cidade e Estado *</label>
            <InputText
              v-model="city"
              class="w-full"
              placeholder="Ubá - MG"
              required
            />
          </div>

          <!-- CPF ou CNPJ -->
          <div class="space-y-1.5 sm:col-span-2">
            <label class="block text-sm font-medium text-slate-700">
              {{ selectedRole === 'SHELTER' ? 'CNPJ (Opcional)' : 'CPF (Opcional)' }}
            </label>
            <InputText
              v-model="document"
              class="w-full"
              :placeholder="selectedRole === 'SHELTER' ? '00.000.000/0001-00' : '000.000.000-00'"
            />
          </div>
        </div>

        <Button
          type="submit"
          :label="selectedRole === 'ADOPTER' ? 'Cadastrar como Adotante' : selectedRole === 'GUARDIAN' ? 'Cadastrar como Protetor' : 'Cadastrar Pet Shop / Abrigo'"
          icon="pi pi-check"
          :loading="isSubmitting"
          class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-3 rounded-xl transition shadow-sm mt-2"
        />
      </form>

      <div class="text-center text-sm text-slate-500 pt-2 border-t border-slate-100">
        Já possui uma conta?
        <RouterLink to="/login" class="text-indigo-600 hover:text-indigo-800 font-semibold ml-1">
          Faça login
        </RouterLink>
      </div>
    </div>
  </div>
</template>
