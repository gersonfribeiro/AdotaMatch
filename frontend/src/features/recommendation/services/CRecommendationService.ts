// Services
import { CHttpClient } from '@/shared/services/CHttpClient';

// Types e Interfaces
import type { IRecommendationResult } from '@/features/recommendation/models/IRecommendationResult';
import type { TRecommendationRequest } from '@/features/recommendation/models/TRecommendationRequest';

/**
 * @description Serviço responsável por invocar a API de recomendação e compatibilização inteligente.
 */
export class CRecommendationService {
  /**
   * @description Envia o perfil do adotante ao backend e obtém o ranqueamento híbrido com explicabilidade.
   * @param pPayload Dados estruturados e narrativa de estilo de vida do adotante.
   * @returns Lista ranqueada de animais compatíveis com justificativas explicáveis.
   */
  public static async calculateMatches(
    pPayload: TRecommendationRequest
  ): Promise<IRecommendationResult[]> {
    try {
      const httpClient = CHttpClient.getInstance();
      const response = await httpClient.post<IRecommendationResult[]>(
        '/recommendations/match',
        pPayload
      );
      return response.data;
    } catch (pError) {
      // Repassa o erro para tratamento na camada de UX/Store
      throw pError;
    }
  }
}
