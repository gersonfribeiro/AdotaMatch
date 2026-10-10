<script setup lang="ts">
// Ecossistema Vue
import { useRouter, useRoute } from 'vue-router';

// PrimeVue e UI
import Button from 'primevue/button';
import Tag from 'primevue/tag';

// Stores
import { useAuthStore } from '@/features/auth/stores/useAuthStore';

// Router & Stores
const router = useRouter();
const route = useRoute();
const authStore = useAuthStore();

// Funções
function navigateTo(pPath: string): void {
  router.push(pPath);
}

function isActive(pPath: string): boolean {
  return route.path === pPath;
}

function handleLogout(): void {
  authStore.logout();
  router.push('/login');
}
</script>

<template>
  <header class="bg-white border-b border-slate-200 sticky top-0 z-50 shadow-xs">
    <div class="max-w-6xl mx-auto px-4 h-16 flex items-center justify-between">
      <!-- Marca e Instituição -->
      <div class="flex items-center gap-3 cursor-pointer" @click="navigateTo('/')">
        <div class="w-10 h-10 rounded-xl bg-emerald-600 text-white flex items-center justify-center font-bold text-lg shadow-sm">
          <i class="pi pi-heart-fill"></i>
        </div>
        <div>
          <span class="font-extrabold text-lg text-slate-900 tracking-tight">
            Adota<span class="text-emerald-600">Match</span>
          </span>
          <span class="hidden md:inline-block ml-2 text-xs px-2 py-0.5 rounded-md bg-emerald-50 text-emerald-700 font-semibold border border-emerald-100">
            Adoção Ética & IA • UNIFAGOC
          </span>
        </div>
      </div>

      <!-- Links de Navegação Principal -->
      <nav class="flex items-center gap-1 sm:gap-2">
        <Button
          label="Match com IA"
          icon="pi pi-sparkles"
          :text="!isActive('/match')"
          :class="isActive('/match') ? 'bg-indigo-50 text-indigo-700 font-bold' : 'text-slate-700 hover:text-indigo-600 font-medium'"
          class="text-xs sm:text-sm rounded-xl px-2.5 sm:px-3 py-2"
          @click="navigateTo('/match')"
        />

        <Button
          label="Catálogo"
          icon="pi pi-th-large"
          :text="!isActive('/pets')"
          :class="isActive('/pets') ? 'bg-emerald-50 text-emerald-700 font-bold' : 'text-slate-700 hover:text-emerald-600 font-medium'"
          class="text-xs sm:text-sm rounded-xl px-2.5 sm:px-3 py-2"
          @click="navigateTo('/pets')"
        />

        <!-- Links Específicos por Perfil Autenticado -->
        <Button
          v-if="authStore.isAdopter"
          label="Minhas Adoções"
          icon="pi pi-list-check"
          :text="!isActive('/my-adoptions')"
          :class="isActive('/my-adoptions') ? 'bg-indigo-50 text-indigo-700 font-bold' : 'text-slate-700 hover:text-indigo-600 font-medium'"
          class="text-xs sm:text-sm rounded-xl px-2.5 sm:px-3 py-2"
          @click="navigateTo('/my-adoptions')"
        />

        <Button
          v-if="authStore.isShelter || authStore.isGuardian"
          :label="authStore.isGuardian ? 'Meus Animais p/ Doação' : 'Painel do Pet Shop / Abrigo'"
          icon="pi pi-building"
          :text="!isActive('/shelter')"
          :class="isActive('/shelter') ? 'bg-emerald-50 text-emerald-700 font-bold' : 'text-slate-700 hover:text-emerald-600 font-medium'"
          class="text-xs sm:text-sm rounded-xl px-2.5 sm:px-3 py-2"
          @click="navigateTo('/shelter')"
        />

        <!-- Bloco de Autenticação / Perfil do Usuário -->
        <div class="ml-2 pl-2 border-l border-slate-200 flex items-center gap-2">
          <!-- Usuário Autenticado -->
          <template v-if="authStore.isAuthenticated">
            <div class="hidden lg:flex items-center gap-2 bg-slate-50 border border-slate-200 rounded-xl px-3 py-1.5">
              <span class="text-xs font-semibold text-slate-800 max-w-[140px] truncate">
                {{ authStore.user?.name }}
              </span>
              <Tag
                :value="authStore.roleLabel"
                :severity="authStore.isAdopter ? 'info' : authStore.isGuardian ? 'success' : 'warn'"
                class="text-[10px]"
              />
            </div>

            <Button
              icon="pi pi-sign-out"
              text
              severity="secondary"
              title="Sair da conta"
              class="rounded-xl w-9 h-9 p-0 text-slate-500 hover:text-rose-600"
              @click="handleLogout"
            />
          </template>

          <!-- Usuário Não Autenticado -->
          <template v-else>
            <Button
              label="Entrar"
              text
              class="text-xs sm:text-sm text-slate-700 hover:text-indigo-600 font-semibold px-2.5 py-1.5"
              @click="navigateTo('/login')"
            />
            <Button
              label="Cadastrar"
              class="text-xs sm:text-sm bg-indigo-600 hover:bg-indigo-700 text-white font-medium rounded-xl px-3 py-1.5 shadow-xs"
              @click="navigateTo('/register')"
            />
          </template>
        </div>
      </nav>
    </div>
  </header>
</template>
