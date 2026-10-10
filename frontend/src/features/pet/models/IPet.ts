/**
 * @description Contrato completo de dados de um animal cadastrado sob custódia de ONG, abrigo ou pet shop.
 * @property {number} id - Identificador único no banco de dados.
 * @property {string} name - Nome do animal.
 * @property {'DOG' | 'CAT'} species - Espécie do animal.
 * @property {'MALE' | 'FEMALE'} gender - Sexo biológico.
 * @property {'MINI' | 'SMALL' | 'MEDIUM' | 'LARGE'} size - Porte físico.
 * @property {number} ageMonths - Idade estimada em meses.
 * @property {'AVAILABLE' | 'ADOPTED' | 'UNDER_TREATMENT'} status - Status de disponibilidade.
 * @property {number} energyLevel - Nível de energia de 1 (calmo) a 5 (hiperativo).
 * @property {number} solitaryToleranceHours - Tolerância à solidão diária em horas.
 * @property {boolean} kidsFriendly - Sociabilidade com crianças.
 * @property {boolean} petsFriendly - Sociabilidade com outros animais.
 * @property {boolean} requiresSpecialCare - Indicador de cuidados veterinários contínuos.
 * @property {boolean} isVaccinated - Vacinação em dia.
 * @property {boolean} isNeutered - Animal castrado.
 * @property {string} photoUrl - URL da fotografia de capa.
 * @property {string | null} additionalPhotos - URLs de fotos adicionais para galeria.
 * @property {string | null} videoUrl - URL de vídeo demonstrativo de comportamento.
 * @property {string} biography - História de acolhimento e sensibilização.
 * @property {string} behavioralNotes - Diário de observação comportamental mantido pelos cuidadores.
 * @property {string} shelterName - Nome do abrigo, ONG ou pet shop.
 * @property {string} shelterCity - Cidade e estado.
 * @property {string} contactWhatsapp - Número de WhatsApp para contato da adoção.
 */
export interface IPet {
  id: number;
  name: string;
  species: 'DOG' | 'CAT';
  gender: 'MALE' | 'FEMALE';
  size: 'MINI' | 'SMALL' | 'MEDIUM' | 'LARGE';
  ageMonths: number;
  status: 'AVAILABLE' | 'ADOPTED' | 'UNDER_TREATMENT';
  energyLevel: number;
  solitaryToleranceHours: number;
  kidsFriendly: boolean;
  petsFriendly: boolean;
  requiresSpecialCare: boolean;
  isVaccinated: boolean;
  isNeutered: boolean;
  photoUrl: string;
  additionalPhotos: string | null;
  videoUrl: string | null;
  biography: string;
  behavioralNotes: string;
  shelterName: string;
  shelterCity: string;
  contactWhatsapp: string;
}

/**
 * @description Payload enviado pelo mantenedor para dar entrada (cadastrar) em um novo animal.
 */
export type TPetCreateRequest = Omit<IPet, 'id' | 'status'>;

/**
 * @description Payload enviado para atualizar os dados cadastrais ou status de um animal.
 */
export type TPetUpdateRequest = Omit<IPet, 'id'>;
