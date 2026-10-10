/**
 * @description Contrato do resultado retornado para cada animal ranqueado no cálculo de recomendação híbrida.
 * @property {number} petId - Identificador único do animal.
 * @property {string} petName - Nome do animal.
 * @property {'DOG' | 'CAT'} species - Espécie do animal.
 * @property {'MALE' | 'FEMALE'} gender - Sexo biológico do animal.
 * @property {'MINI' | 'SMALL' | 'MEDIUM' | 'LARGE'} size - Porte físico.
 * @property {number} ageMonths - Idade estimada em meses.
 * @property {string} photoUrl - URL da fotografia principal de divulgação.
 * @property {string | null} videoUrl - URL de vídeo demonstrativo de comportamento.
 * @property {string} biography - História do resgate e perfil afetivo.
 * @property {number} compatibilityScore - Pontuação final global ponderada (0 a 100).
 * @property {number} semanticScore - Escore de similaridade contextual gerado via LLM (0.0 a 1.0).
 * @property {string[]} alignmentFactors - Lista de fatores explicativos de afinidade com o adotante.
 * @property {string[]} preventiveWarnings - Alertas de convivência e manejo para prevenir devoluções.
 * @property {string} shelterName - Nome do abrigo, ONG ou pet shop mantenedor.
 * @property {string} shelterCity - Cidade e estado onde o animal se encontra.
 * @property {string} contactWhatsapp - Número de WhatsApp da instituição para contato direto.
 */
export interface IRecommendationResult {
  petId: number;
  petName: string;
  species: 'DOG' | 'CAT';
  gender: 'MALE' | 'FEMALE';
  size: 'MINI' | 'SMALL' | 'MEDIUM' | 'LARGE';
  ageMonths: number;
  photoUrl: string;
  videoUrl: string | null;
  biography: string;
  compatibilityScore: number;
  semanticScore: number;
  alignmentFactors: string[];
  preventiveWarnings: string[];
  shelterName: string;
  shelterCity: string;
  contactWhatsapp: string;
}
