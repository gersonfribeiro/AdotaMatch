/**
 * @description Contrato para envio de manifestação formal de interesse em adoção.
 * @property {number} petId - Identificador do animal desejado.
 * @property {string} petName - Nome do animal.
 * @property {string} adopterName - Nome completo do adotante interessado.
 * @property {string} adopterEmail - E-mail do adotante.
 * @property {string} adopterPhone - WhatsApp ou telefone de contato.
 * @property {string} adopterCity - Cidade e estado do adotante.
 * @property {number} compatibilityScore - Pontuação obtida na recomendação por IA.
 * @property {string} adopterNarrative - Relato de rotina fornecido no formulário.
 * @property {string} messageToShelter - Mensagem direta para a ONG ou Pet Shop.
 */
export interface IAdoptionIntentRequest {
  petId: number;
  petName: string;
  adopterName: string;
  adopterEmail: string;
  adopterPhone: string;
  adopterCity: string;
  compatibilityScore: number;
  adopterNarrative: string;
  messageToShelter: string;
}

/**
 * @description Resposta de confirmação do registro de interesse na adoção.
 * @property {number} id - Número do protocolo gerado.
 * @property {number} petId - Identificador do animal.
 * @property {string} petName - Nome do animal.
 * @property {string} adopterName - Nome do adotante.
 * @property {number} compatibilityScore - Pontuação alcançada.
 * @property {string} [messageToShelter] - Mensagem direta enviada para a instituição.
 * @property {string} [adopterNarrative] - Relato de rotina fornecido na triagem.
 * @property {string} status - Status da análise.
 * @property {string} createdAt - Data e hora de criação.
 */
export interface IAdoptionIntentResponse {
  id: number;
  petId: number;
  petName: string;
  adopterName: string;
  compatibilityScore: number;
  messageToShelter?: string;
  adopterNarrative?: string;
  status: string;
  createdAt: string;
}
