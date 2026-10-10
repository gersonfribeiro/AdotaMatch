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

// Router & Toast
const router = useRouter();
const toast = useToast();
const authStore = useAuthStore();

// Reativas - Ref
const email = ref('');
const password = ref('');
const isSubmitting = ref(false);

// Funções
async function handleLogin(): Promise<void> {
  if (!email.value.trim() || !password.value.trim()) {
    toast.add({
      severity: 'warn',
      summary: 'Campos Obrigatórios',
      detail: 'Informe seu e-mail e senha para continuar.',
      life: 3000,
    });
    return;
  }

  isSubmitting.value = true;
  try {
    await authStore.login({
      email: email.value.trim(),
      password: password.value,
    });

    toast.add({
      severity: 'success',
      summary: 'Bem-vindo(a)!',
      detail: `Conectado como ${authStore.user?.name} (${authStore.roleLabel}).`,
      life: 4000,
    });

    // Redireciona conforme o perfil
    if (authStore.isShelter || authStore.isGuardian) {
      router.push('/shelter');
    } else {
      router.push('/match');
    }
  } catch {
    // Erro gerenciado na store
  } finally {
    isSubmitting.value = false;
  }
}

function handleQuickFill(pEmail: string): void {
  email.value = pEmail;
  password.value = 'senha123';
}
</script>

<template>
  <div class="max-w-md mx-auto px-4 py-12">
    <div class="bg-white rounded-2xl shadow-sm border border-slate-200 p-6 sm:p-8 space-y-6">
      <div class="text-center space-y-2">
        <div class="w-12 h-12 mx-auto rounded-xl bg-indigo-50 text-indigo-600 flex items-center justify-center text-2xl font-bold">
          <i class="pi pi-lock"></i>
        </div>
        <h1 class="text-2xl font-extrabold text-slate-900 tracking-tight">
          Entrar no AdotaMatch
        </h1>
        <p class="text-xs sm:text-sm text-slate-500">
          Acesse para gerenciar adoções, cadastrar animais ou buscar recomendações com IA.
        </p>
      </div>

      <!-- Mensagem de Erro da Store -->
      <Message v-if="authStore.errorMessage" severity="error" :closable="true" @close="authStore.errorMessage = null">
        {{ authStore.errorMessage }}
      </Message>

      <!-- Formulário de Login -->
      <form class="space-y-4" @submit.prevent="handleLogin">
        <div class="space-y-1.5">
          <label class="block text-sm font-medium text-slate-700">E-mail</label>
          <InputText
            v-model="email"
            type="email"
            class="w-full"
            placeholder="seu@email.com"
            autocomplete="email"
            required
          />
        </div>

        <div class="space-y-1.5">
          <label class="block text-sm font-medium text-slate-700">Senha</label>
          <Password
            v-model="password"
            :feedback="false"
            toggle-mask
            class="w-full"
            input-class="w-full"
            placeholder="••••••••"
            autocomplete="current-password"
            required
          />
        </div>

        <Button
          type="submit"
          label="Entrar na Plataforma"
          icon="pi pi-sign-in"
          :loading="isSubmitting"
          class="w-full bg-indigo-600 hover:bg-indigo-700 text-white font-medium py-3 rounded-xl transition shadow-sm"
        />
      </form>

      <!-- Botões de Acesso Rápido para Demonstração / Banca -->
      <div class="pt-4 border-t border-slate-100 space-y-2">
        <span class="block text-xs font-semibold text-slate-400 uppercase tracking-wider text-center">
          Acesso Rápido de Testes (Perfis)
        </span>
        <div class="grid grid-cols-3 gap-2 text-xs">
          <button
            type="button"
            class="p-2 rounded-lg border border-slate-200 hover:border-indigo-500 hover:bg-indigo-50 text-slate-700 transition text-center"
            @click="handleQuickFill('adotante@adotamatch.com.br')"
          >
            🐾 Adotante
          </button>
          <button
            type="button"
            class="p-2 rounded-lg border border-slate-200 hover:border-emerald-500 hover:bg-emerald-50 text-slate-700 transition text-center"
            @click="handleQuickFill('protetor@adotamatch.com.br')"
          >
            🏡 Protetor
          </button>
          <button
            type="button"
            class="p-2 rounded-lg border border-slate-200 hover:border-amber-500 hover:bg-amber-50 text-slate-700 transition text-center"
            @click="handleQuickFill('petshop@adotamatch.com.br')"
          >
            🏢 Pet Shop
          </button>
        </div>
      </div>

      <div class="text-center text-sm text-slate-500 pt-2">
        Não possui uma conta?
        <RouterLink to="/register" class="text-indigo-600 hover:text-indigo-800 font-semibold ml-1">
          Cadastre-se gratuitamente
        </RouterLink>
      </div>
    </div>
  </div>
</template>
