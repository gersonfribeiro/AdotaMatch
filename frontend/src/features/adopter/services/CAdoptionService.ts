// Services
import { CHttpClient } from '@/shared/services/CHttpClient';

// Types e Interfaces
import type { IAdoptionIntentRequest, IAdoptionIntentResponse } from '@/features/adopter/models/IAdoptionIntent';

/**
 * @description Serviço responsável pelas operações de manifestação formal de interesse em adoção.
 */
export class CAdoptionService {
  /**
   * @description Submete uma manifestação de interesse em adoção para a ONG ou pet shop.
   * @param pPayload Dados do adotante, relato e escore de compatibilidade alcançado.
   * @returns Confirmação com protocolo emitido pelo backend.
   */
  public static async submitIntent(
    pPayload: IAdoptionIntentRequest
  ): Promise<IAdoptionIntentResponse> {
    try {
      const httpClient = CHttpClient.getInstance();
      const response = await httpClient.post<IAdoptionIntentResponse>(
        '/adoptions/intent',
        pPayload
      );
      return response.data;
    } catch (pError) {
      throw pError;
    }
  }

  /**
   * @description Consulta os interessados em um determinado animal cadastrado.
   * @param pPetId Identificador do animal.
   * @returns Lista de intenções ordenadas por maior score de compatibilidade.
   */
  public static async findByPetId(pPetId: number): Promise<IAdoptionIntentResponse[]> {
    try {
      const httpClient = CHttpClient.getInstance();
      const response = await httpClient.get<IAdoptionIntentResponse[]>(
        `/adoptions/intent/pet/${pPetId}`
      );
      return response.data;
    } catch (pError) {
      throw pError;
    }
  }

  /**
   * @description Consulta as manifestações de interesse submetidas por um adotante.
   * @param pEmail E-mail do adotante.
   * @returns Lista de manifestações de interesse.
   */
  public static async findMyIntents(pEmail: string): Promise<IAdoptionIntentResponse[]> {
    try {
      const httpClient = CHttpClient.getInstance();
      const response = await httpClient.get<IAdoptionIntentResponse[]>(
        `/adoptions/intent/my?pEmail=${encodeURIComponent(pEmail)}`
      );
      return response.data;
    } catch (pError) {
      throw pError;
    }
  }
}
