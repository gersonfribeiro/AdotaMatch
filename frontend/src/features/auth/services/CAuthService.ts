// Services
import { CHttpClient } from '@/shared/services/CHttpClient';

// Types e Interfaces
import type { IUser, ILoginRequest, IRegisterRequest } from '@/features/auth/models/IUser';

/**
 * @description Serviço responsável pelas chamadas HTTP de autenticação e perfis de usuário.
 */
export class CAuthService {
  /**
   * @description Realiza o login do usuário por e-mail e senha.
   * @param pRequest Credenciais de acesso.
   * @returns Dados do usuário autenticado e token de sessão.
   */
  public static async login(pRequest: ILoginRequest): Promise<IUser> {
    const response = await CHttpClient.getInstance().post<IUser>('/auth/login', pRequest);
    return response.data;
  }

  /**
   * @description Registra um novo usuário (Adotante, Protetor ou Abrigo/Pet Shop).
   * @param pRequest Dados de cadastro.
   * @returns Dados do usuário criado e token de sessão.
   */
  public static async register(pRequest: IRegisterRequest): Promise<IUser> {
    const response = await CHttpClient.getInstance().post<IUser>('/auth/register', pRequest);
    return response.data;
  }

  /**
   * @description Recupera os dados do usuário a partir do identificador.
   * @param pId Identificador único do usuário.
   * @returns Perfil do usuário autenticado.
   */
  public static async me(pId: number): Promise<IUser> {
    const response = await CHttpClient.getInstance().get<IUser>(`/auth/me?pId=${pId}`);
    return response.data;
  }
}
