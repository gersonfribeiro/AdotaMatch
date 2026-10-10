// Outros
import axios, { type AxiosInstance } from 'axios';

// Constantes
import { API_BASE_URL, DEFAULT_REQUEST_TIMEOUT } from '@/shared/constants/C_API_CONFIG';

/**
 * @description Cliente HTTP centralizado utilizando Axios configurado com timeouts e base URL.
 */
export class CHttpClient {
  private static instance: AxiosInstance;

  /**
   * @description Retorna a instância única do cliente HTTP Axios.
   * @returns Instância configurada do Axios.
   */
  public static getInstance(): AxiosInstance {
    if (!CHttpClient.instance) {
      CHttpClient.instance = axios.create({
        baseURL: API_BASE_URL,
        timeout: DEFAULT_REQUEST_TIMEOUT,
        headers: {
          'Content-Type': 'application/json',
          Accept: 'application/json',
        },
      });
    }
    return CHttpClient.instance;
  }
}
