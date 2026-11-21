/**
 * @description Configurações globais de comunicação com a API RESTful do backend.
 */
export const PUBLIC_DEMO = import.meta.env.VITE_PUBLIC_DEMO === 'true';

export const API_BASE_URL = PUBLIC_DEMO
  ? '/api/v1'
  : import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080/api/v1';

export const DEFAULT_REQUEST_TIMEOUT = 30000;
