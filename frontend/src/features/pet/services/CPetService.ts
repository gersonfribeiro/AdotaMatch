// Services
import { CHttpClient } from '@/shared/services/CHttpClient';

// Types e Interfaces
import type { IPet, TPetCreateRequest, TPetUpdateRequest } from '@/features/pet/models/IPet';

/**
 * @description Parâmetros de filtro para consulta de animais.
 * @property {string} species - Filtro opcional por espécie (DOG ou CAT).
 * @property {string} status - Filtro opcional por status (AVAILABLE, ADOPTED).
 * @property {string} search - Filtro textual por nome, abrigo ou cidade.
 */
export type TPetFilterParams = {
  species?: 'DOG' | 'CAT';
  status?: string;
  search?: string;
};

/**
 * @description Serviço responsável pelas chamadas HTTP de gestão e catálogo de animais.
 */
export class CPetService {
  /**
   * @description Consulta a lista de animais cadastrados com filtros opcionais.
   * @param pParams Filtros de espécie, status ou busca textual.
   * @returns Lista de animais cadastrados.
   */
  public static async findAll(pParams?: TPetFilterParams): Promise<IPet[]> {
    try {
      const httpClient = CHttpClient.getInstance();
      const response = await httpClient.get<IPet[]>('/pets', {
        params: pParams,
      });
      return response.data;
    } catch (pError) {
      throw pError;
    }
  }

  /**
   * @description Busca os dados completos de um animal pelo identificador único.
   * @param pId Identificador único do animal.
   * @returns Dados detalhados do animal.
   */
  public static async findById(pId: number): Promise<IPet> {
    try {
      const httpClient = CHttpClient.getInstance();
      const response = await httpClient.get<IPet>(`/pets/${pId}`);
      return response.data;
    } catch (pError) {
      throw pError;
    }
  }

  /**
   * @description Cadastra (dá entrada em) um novo animal sob custódia de uma ONG ou Pet Shop.
   * @param pPayload Dados completos, fotos, vídeo e diário comportamental do animal.
   * @returns Animal recém-cadastrado com id atribuído.
   */
  public static async create(pPayload: TPetCreateRequest): Promise<IPet> {
    try {
      const httpClient = CHttpClient.getInstance();
      const response = await httpClient.post<IPet>('/pets', pPayload);
      return response.data;
    } catch (pError) {
      throw pError;
    }
  }

  /**
   * @description Atualiza os dados ou status de um animal existente.
   * @param pId Identificador único do animal.
   * @param pPayload Dados atualizados.
   * @returns Dados do animal persistidos.
   */
  public static async update(pId: number, pPayload: TPetUpdateRequest): Promise<IPet> {
    try {
      const httpClient = CHttpClient.getInstance();
      const response = await httpClient.put<IPet>(`/pets/${pId}`, pPayload);
      return response.data;
    } catch (pError) {
      throw pError;
    }
  }

  /**
   * @description Remove ou desativa um animal do sistema.
   * @param pId Identificador do animal.
   */
  public static async delete(pId: number): Promise<void> {
    try {
      const httpClient = CHttpClient.getInstance();
      await httpClient.delete(`/pets/${pId}`);
    } catch (pError) {
      throw pError;
    }
  }
}
