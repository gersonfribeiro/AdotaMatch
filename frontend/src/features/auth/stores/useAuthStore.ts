// Ecossistema
import { ref, computed } from 'vue';
import { defineStore } from 'pinia';

// Services
import { CAuthService } from '@/features/auth/services/CAuthService';

// Types e Interfaces
import type { IUser, ILoginRequest, IRegisterRequest } from '@/features/auth/models/IUser';

// Constantes
const STORAGE_KEY = 'adotamatch_user';

export const useAuthStore = defineStore('auth', () => {
  // Reativas - Ref
  const user = ref<IUser | null>(loadInitialUser());
  const isLoading = ref(false);
  const errorMessage = ref<string | null>(null);

  // Computadas
  const isAuthenticated = computed(() => user.value !== null);
  const isAdopter = computed(() => user.value?.role === 'ADOPTER');
  const isGuardian = computed(() => user.value?.role === 'GUARDIAN');
  const isShelter = computed(() => user.value?.role === 'SHELTER');

  const roleLabel = computed(() => {
    switch (user.value?.role) {
      case 'ADOPTER':
        return 'Adotante';
      case 'GUARDIAN':
        return 'Protetor / Doador';
      case 'SHELTER':
        return 'Abrigo / Pet Shop';
      default:
        return 'Visitante';
    }
  });

  // Funções
  function loadInitialUser(): IUser | null {
    try {
      const stored = localStorage.getItem(STORAGE_KEY);
      return stored ? JSON.parse(stored) : null;
    } catch {
      return null;
    }
  }

  function saveUser(pUser: IUser | null): void {
    user.value = pUser;
    if (pUser) {
      localStorage.setItem(STORAGE_KEY, JSON.stringify(pUser));
    } else {
      localStorage.removeItem(STORAGE_KEY);
    }
  }

  async function login(pCredentials: ILoginRequest): Promise<void> {
    isLoading.value = true;
    errorMessage.value = null;
    try {
      const loggedUser = await CAuthService.login(pCredentials);
      saveUser(loggedUser);
    } catch (pErr: any) {
      errorMessage.value = pErr?.response?.data?.message || 'E-mail ou senha inválidos.';
      throw pErr;
    } finally {
      isLoading.value = false;
    }
  }

  async function register(pData: IRegisterRequest): Promise<void> {
    isLoading.value = true;
    errorMessage.value = null;
    try {
      const newUser = await CAuthService.register(pData);
      saveUser(newUser);
    } catch (pErr: any) {
      errorMessage.value = pErr?.response?.data?.message || 'Falha ao realizar cadastro.';
      throw pErr;
    } finally {
      isLoading.value = false;
    }
  }

  function logout(): void {
    saveUser(null);
  }

  return {
    user,
    isLoading,
    errorMessage,
    isAuthenticated,
    isAdopter,
    isGuardian,
    isShelter,
    roleLabel,
    login,
    register,
    logout,
  };
});
