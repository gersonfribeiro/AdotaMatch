/**
 * @description Papéis de acesso disponíveis no sistema AdotaMatch.
 */
export type TUserRole = 'ADOPTER' | 'GUARDIAN' | 'SHELTER';

/**
 * @description Contrato do perfil de usuário retornado pelo backend.
 * @property {number} id - Identificador único do usuário.
 * @property {string} name - Nome completo do usuário ou da instituição.
 * @property {string} email - Endereço de e-mail de acesso.
 * @property {TUserRole} role - Papel do usuário (Adotante, Protetor ou Abrigo/Pet Shop).
 * @property {string} phone - Telefone celular ou WhatsApp de contato.
 * @property {string} city - Cidade e UF de atuação.
 * @property {string} document - CPF para pessoa física ou CNPJ para empresas.
 * @property {string} organizationName - Razão social ou nome fantasia para ONGs/Pet Shops.
 * @property {string} token - Token de sessão autenticada.
 */
export interface IUser {
  id: number;
  name: string;
  email: string;
  role: TUserRole;
  phone: string;
  city: string;
  document?: string | null;
  organizationName?: string | null;
  token: string;
}

/**
 * @description Parâmetros para requisição de login.
 * @property {string} email - E-mail do usuário.
 * @property {string} password - Senha de acesso.
 */
export interface ILoginRequest {
  email: string;
  password: string;
}

/**
 * @description Parâmetros para criação de conta de usuário.
 * @property {string} name - Nome ou razão social.
 * @property {string} email - E-mail de cadastro.
 * @property {string} password - Senha de acesso.
 * @property {TUserRole} role - Tipo de perfil desejado.
 * @property {string} phone - WhatsApp de contato.
 * @property {string} city - Cidade e Estado.
 * @property {string} document - CPF ou CNPJ opcional.
 * @property {string} organizationName - Nome da instituição opcional.
 */
export interface IRegisterRequest {
  name: string;
  email: string;
  password: string;
  role: TUserRole;
  phone: string;
  city: string;
  document?: string;
  organizationName?: string;
}
