/**
 * @description Parâmetros e dados de entrada para geração do ranqueamento de compatibilidade.
 * @property {string} speciesPreference - Preferência de espécie (DOG, CAT, ANY).
 * @property {string} housingType - Tipo de moradia (APARTMENT, HOUSE_NO_YARD, HOUSE_YARD, RURAL).
 * @property {number} dailyHoursAvailable - Horas diárias disponíveis para dedicação direta.
 * @property {boolean} hasChildren - Indica a presença de crianças na residência.
 * @property {boolean} hasOtherPets - Indica a presença de outros animais na residência.
 * @property {number} experienceLevel - Nível de experiência com animais (1 - Iniciante, 2 - Intermediário, 3 - Experiente).
 * @property {string} lifestyleNarrative - Relato aberto em texto livre sobre rotina, lazer e expectativas para processamento via LLM.
 */
export type TRecommendationRequest = {
  speciesPreference: 'DOG' | 'CAT' | 'ANY';
  housingType: 'APARTMENT' | 'HOUSE_NO_YARD' | 'HOUSE_YARD' | 'RURAL';
  dailyHoursAvailable: number;
  hasChildren: boolean;
  hasOtherPets: boolean;
  experienceLevel: number;
  lifestyleNarrative: string;
};
