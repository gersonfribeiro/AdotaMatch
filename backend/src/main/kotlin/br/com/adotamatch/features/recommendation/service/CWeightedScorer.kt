package br.com.adotamatch.features.recommendation.service

import br.com.adotamatch.features.pet.domain.CPet
import br.com.adotamatch.features.pet.domain.EPetSize
import br.com.adotamatch.features.recommendation.domain.CCriteriaBreakdown
import br.com.adotamatch.features.recommendation.dto.CRecommendationRequest
import br.com.adotamatch.features.recommendation.dto.EAdopterHousing
import org.springframework.stereotype.Component
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.round

/**
 * @description Camada de cálculo da similaridade ponderada multicritério.
 * Implementa formalmente as funções de similaridade s_c(u, a) descritas no artigo científico do TCC.
 */
@Component
class CWeightedScorer {

    companion object {
        const val WEIGHT_ENERGY_TIME = 0.25
        const val WEIGHT_SOLITARY_ROUTINE = 0.25
        const val WEIGHT_HOUSING_SIZE = 0.15
        const val WEIGHT_KIDS_PETS = 0.10
        const val WEIGHT_EXPERIENCE_CARE = 0.10
        const val WEIGHT_LLM_SEMANTIC = 0.15
    }

    /**
     * @description Calcula a pontuação final de compatibilidade combinando os 5 critérios convexos e o escore semântico.
     * @param pRequest Dados do perfil do adotante com tempo diário, moradia e relato.
     * @param pPet Dados comportamentais e biológicos do animal.
     * @param pSemanticScore Escore semântico contextual apurado via LLM/PLN (0.0 a 1.0).
     * @returns Pontuação final de compatibilidade normalizada entre 0.0 e 100.0.
     */
    fun calculateScore(
        pRequest: CRecommendationRequest,
        pPet: CPet,
        pSemanticScore: Double
    ): Double {
        val s1 = calculateEnergyTimeSimilarity(pPet.energyLevel, pRequest.dailyHoursAvailable)
        val s2 = calculateKidsPetsSimilarity(pRequest.hasChildren, pRequest.hasOtherPets, pPet.kidsFriendly, pPet.petsFriendly)
        val s3 = calculateHousingSizeSimilarity(pRequest.housingType, pPet.size)
        val s4 = calculateSolitarySimilarity(pPet.solitaryToleranceHours, pRequest.dailyHoursAvailable, pRequest.lifestyleNarrative)
        val s5 = calculateExperienceCareSimilarity(pRequest.experienceLevel, pPet.requiresSpecialCare)

        val weightedSum = (WEIGHT_ENERGY_TIME * s1) +
                (WEIGHT_SOLITARY_ROUTINE * s4) +
                (WEIGHT_HOUSING_SIZE * s3) +
                (WEIGHT_KIDS_PETS * s2) +
                (WEIGHT_EXPERIENCE_CARE * s5) +
                (WEIGHT_LLM_SEMANTIC * pSemanticScore.coerceIn(0.0, 1.0))

        return (weightedSum * 100.0).coerceIn(0.0, 100.0)
    }

    /**
     * @description Calcula o detalhamento discriminado dos 5 eixos matemáticos de similaridade e do alinhamento LLM.
     * @param pRequest Perfil socioambiental do adotante.
     * @param pPet Dados comportamentais do animal.
     * @param pSemanticScore Escore apurado pela LLM (0.0 a 1.0).
     * @returns Instância de CCriteriaBreakdown com valores normalizados entre 0.0 e 100.0.
     */
    fun calculateBreakdown(
        pRequest: CRecommendationRequest,
        pPet: CPet,
        pSemanticScore: Double
    ): CCriteriaBreakdown {
        val s1 = calculateEnergyTimeSimilarity(pPet.energyLevel, pRequest.dailyHoursAvailable)
        val s2 = calculateKidsPetsSimilarity(pRequest.hasChildren, pRequest.hasOtherPets, pPet.kidsFriendly, pPet.petsFriendly)
        val s3 = calculateHousingSizeSimilarity(pRequest.housingType, pPet.size)
        val s4 = calculateSolitarySimilarity(pPet.solitaryToleranceHours, pRequest.dailyHoursAvailable, pRequest.lifestyleNarrative)
        val s5 = calculateExperienceCareSimilarity(pRequest.experienceLevel, pPet.requiresSpecialCare)

        return CCriteriaBreakdown(
            energyTimeScore = round(s1 * 100.0 * 10.0) / 10.0,
            familyScore = round(s2 * 100.0 * 10.0) / 10.0,
            housingSizeScore = round(s3 * 100.0 * 10.0) / 10.0,
            solitudeScore = round(s4 * 100.0 * 10.0) / 10.0,
            experienceCareScore = round(s5 * 100.0 * 10.0) / 10.0,
            llmSemanticScore = round(pSemanticScore.coerceIn(0.0, 1.0) * 100.0 * 10.0) / 10.0
        )
    }

    /**
     * @description Similaridade s1: Demanda de energia do animal vs tempo diário disponível do adotante.
     * Avalia matematicamente se as horas livres cobrem a demanda de exercícios e estímulos do nível de energia.
     * @param pEnergyLevel Grau de energia do animal (1 a 5).
     * @param pDailyHours Horas livres declaradas pelo adotante (1 a 16).
     * @returns Coeficiente de similaridade entre 0.05 e 1.0.
     */
    fun calculateEnergyTimeSimilarity(pEnergyLevel: Int, pDailyHours: Int): Double {
        val requiredHours = when (pEnergyLevel) {
            1 -> 1.0
            2 -> 2.0
            3 -> 3.5
            4 -> 5.0
            else -> 6.5
        }

        return if (pDailyHours >= requiredHours) {
            1.0
        } else {
            val ratio = pDailyHours.toDouble() / requiredHours
            (ratio * ratio).coerceIn(0.05, 0.95)
        }
    }

    /**
     * @description Similaridade s2: Convivência familiar (crianças e outros animais).
     * @param pHasKids Presença de crianças no domicílio.
     * @param pHasPets Presença de outros pets no lar.
     * @param pKidsFriendly Sociabilidade do animal com crianças.
     * @param pPetsFriendly Sociabilidade do animal com outros pets.
     * @returns Coeficiente de similaridade entre 0.0 e 1.0.
     */
    fun calculateKidsPetsSimilarity(
        pHasKids: Boolean,
        pHasPets: Boolean,
        pKidsFriendly: Boolean,
        pPetsFriendly: Boolean
    ): Double {
        var score = 1.0
        if (pHasKids && !pKidsFriendly) score -= 0.5
        if (pHasPets && !pPetsFriendly) score -= 0.5
        return score.coerceIn(0.0, 1.0)
    }

    /**
     * @description Similaridade s3: Espaço físico de moradia vs porte do animal.
     * @param pHousing Tipo de moradia (apartamento, casa sem quintal, casa com quintal, rural).
     * @param pSize Porte do animal (mini, pequeno, médio, grande).
     * @returns Coeficiente de similaridade entre 0.0 e 1.0.
     */
    fun calculateHousingSizeSimilarity(pHousing: EAdopterHousing, pSize: EPetSize): Double {
        val housingScale = when (pHousing) {
            EAdopterHousing.APARTMENT -> 1
            EAdopterHousing.HOUSE_NO_YARD -> 2
            EAdopterHousing.HOUSE_YARD -> 3
            EAdopterHousing.RURAL -> 4
        }
        val sizeScale = when (pSize) {
            EPetSize.MINI -> 1
            EPetSize.SMALL -> 2
            EPetSize.MEDIUM -> 3
            EPetSize.LARGE -> 4
        }
        val diff = abs(housingScale - sizeScale)
        return (1.0 - (diff / 3.0)).coerceIn(0.0, 1.0)
    }

    /**
     * @description Similaridade s4: Tolerância à solidão diária vs ausência estimada.
     * Leva em conta atenuantes contextuais identificados na narrativa (home office ou rede de apoio familiar).
     * @param pSolitaryToleranceHours Horas máximas de isolamento toleradas pelo animal.
     * @param pDailyHours Horas livres disponíveis informadas pelo tutor.
     * @param pNarrative Relato em texto livre do estilo de vida.
     * @returns Coeficiente de similaridade entre 0.05 e 1.0.
     */
    fun calculateSolitarySimilarity(
        pSolitaryToleranceHours: Int,
        pDailyHours: Int,
        pNarrative: String = ""
    ): Double {
        val narrativeLower = pNarrative.lowercase()
        val isRemote = (narrativeLower.contains("remot") || narrativeLower.contains("home office") || narrativeLower.contains("trabalho de casa")) &&
                !narrativeLower.contains("presencial")
        val hasCoCarers = narrativeLower.contains("irmã") || narrativeLower.contains("irmão") ||
                narrativeLower.contains("mãe") || narrativeLower.contains("pai") ||
                narrativeLower.contains("família") || narrativeLower.contains("esposa") ||
                narrativeLower.contains("marido")

        var estimatedAloneHours = max(0, 16 - pDailyHours)

        if (isRemote) {
            estimatedAloneHours = (estimatedAloneHours * 0.4).toInt()
        } else if (hasCoCarers) {
            estimatedAloneHours = (estimatedAloneHours * 0.6).toInt()
        }

        return if (estimatedAloneHours <= pSolitaryToleranceHours) {
            1.0
        } else {
            val excess = estimatedAloneHours - pSolitaryToleranceHours
            max(0.05, 1.0 - (excess.toDouble() / 4.0))
        }
    }

    /**
     * @description Similaridade s5: Experiência prévia vs cuidados especiais requeridos.
     * @param pExpLevel Nível de experiência declarado pelo adotante (1 a 3).
     * @param pRequiresCare Indica se o animal requer manejo ou medicação continuada.
     * @returns Coeficiente de similaridade entre 0.0 e 1.0.
     */
    fun calculateExperienceCareSimilarity(pExpLevel: Int, pRequiresCare: Boolean): Double {
        val careDemand = if (pRequiresCare) 3 else 1
        return if (pExpLevel >= careDemand) {
            1.0
        } else {
            max(0.0, 1.0 - ((careDemand - pExpLevel) * 0.4))
        }
    }
}

