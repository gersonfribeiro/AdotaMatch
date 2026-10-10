// Ecossistema
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

// Services
import { CRecommendationService } from '@/features/recommendation/services/CRecommendationService';

// Types e Interfaces
import type { IRecommendationResult } from '@/features/recommendation/models/IRecommendationResult';
import type { TRecommendationRequest } from '@/features/recommendation/models/TRecommendationRequest';

/**
 * @description Store Pinia responsável pelo estado da recomendação de animais e controle de requisições.
 */
export const useRecommendationStore = defineStore('recommendation', () => {
  // Reativas
  const matches = ref<IRecommendationResult[]>([]);
  const isMatching = ref(false);
  const errorMessage = ref<string | null>(null);

  // Computadas
  const hasMatches = computed(() => matches.value.length > 0);
  const topMatch = computed(() => matches.value[0] || null);

  // Funções
  /**
   * @description Dispara o cálculo do algoritmo no backend com lock reativo.
   * @param pRequest Dados do perfil do adotante e narrativa textual.
   */
  async function fetchMatches(pRequest: TRecommendationRequest): Promise<void> {
    if (isMatching.value) return; // Lock de requisição
    isMatching.value = true;
    errorMessage.value = null;

    try {
      const results = await CRecommendationService.calculateMatches(pRequest);
      matches.value = results;
    } catch (pErr) {
      errorMessage.value = 'Não foi possível calcular a compatibilidade no momento. Tente novamente mais tarde.';
      matches.value = [];
      throw pErr;
    } finally {
      isMatching.value = false;
    }
  }

  /**
   * @description Reseta os resultados da consulta atual.
   */
  function clearMatches(): void {
    matches.value = [];
    errorMessage.value = null;
  }

  return {
    matches,
    isMatching,
    errorMessage,
    hasMatches,
    topMatch,
    fetchMatches,
    clearMatches,
  };
});
