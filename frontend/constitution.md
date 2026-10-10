# Constituição do Repositório Frontend — AdotaMatch
## Stack: Vue 3 + Vite + TypeScript + PrimeVue + Tailwind CSS | TLC Spec-Driven Development

---

## 1. Visão Geral, Ontologia e Missão Social

O frontend do **AdotaMatch** é uma Single Page Application (SPA) moderna, reativa, responsiva e acessível, desenvolvida sob os preceitos do Trabalho de Conclusão de Curso em Ciência da Computação:
> **"ADOTAMATCH: SISTEMA DE RECOMENDAÇÃO BASEADO EM CONTEÚDO E MODELOS DE LINGUAGEM DE GRANDE PORTE (LLMs) PARA APOIO À ADOÇÃO RESPONSÁVEL DE ANIMAIS"** (UNIFAGOC, 2026).

A interface tem como missão central guiar o cidadão na **Jornada da Adoção Consciente**, proporcionando transparência e responsabilidade ética, prevenindo a devolução de animais e o reabandono.

---

## 2. Cláusulas Pétreas do TCC no Frontend

Fica terminantemente vedado a qualquer desenvolvedor ou agente de IA descumprir as seguintes diretrizes:

1. **Proibição de Catálogo/Vitrine Estilo E-Commerce Impulsivo:**
   A interface não deve funcionar como um mero catálogo de fotos ou vitrine de "produtos", no qual o usuário escolhe animais impulsivamente por critérios puramente estéticos ou raciais. O fluxo de recomendação deve sempre partir do **Questionário de Estilo de Vida e Capacidade de Manejo do Adotante**.
2. **Exibição Obrigatória da Explicabilidade e Alertas de Adaptação:**
   Na apresentação dos animais recomendados:
   * O **Score de Compatibilidade** ($\text{Score}(u, a)$) deve ser exibido com destaque claro (ex.: $92\%$ de Afinidade).
   * É obrigatório renderizar os **Fatores de Afinidade** (motivos pelos quais o perfil combina, em tons verdes/esmeralda).
   * É obrigatório renderizar os **Alertas Preventivos de Adaptação** (desafios de rotina, manejo e convivência, em tons âmbar/laranja), exigindo leitura ou consentimento informado do usuário antes do envio da manifestação de interesse à ONG.
3. **Fidelidade Estrita ao Design System do AdotaMatch:**
   * O layout deve respeitar rigorosamente a modelagem visual presente no arquivo [`adotamatch-design-system.pen`](file:///c:/Users/gerso/OneDrive/Documentos/TCC/adotamatch/design/adotamatch-design-system.pen) e no documento [`06-design-system-typography.md`](file:///c:/Users/gerso/OneDrive/Documentos/TCC/adotamatch/specs/06-design-system-typography.md).
   * Cores Oficiais: Verde Primário Brand (`#10B981` / `#059669`), Laranja Acolhimento (`#F97316`), Âmbar de Alerta (`#F59E0B` / `#D97706`), Neutros Slate (`#0F172A`, `#334155`, `#F8FAFC`).
   * Tipografia Oficial: `Inter` para textos de UI, leitura e formulários; `Poppins` para cabeçalhos, títulos de destaque e métricas de escore numérico.
4. **Lock de Requisições Obrigatório:**
   Toda submissão de dados ou disparo de recomendação deve ativar um lock de interface (estado de `loading` no botão ou spinner contextual), impedindo disparos múltiplos ou cliques concorrentes que sobrecarreguem o backend ou o serviço de LLM.
5. **Acessibilidade e Mobile-First (TAM e SUS):**
   A usabilidade deve atingir escore mínimo $\ge 75$ na escala SUS (*System Usability Scale*). Elementos interativos devem ter área mínima de toque de $44 \times 44\text{px}$, contraste mínimo WCAG AA ($4.5:1$) e total adaptação responsiva a smartphones e telas desktop.

---

## 3. Stack Tecnológica e Bibliotecas Homologadas

* **Framework Base:** Vue 3 (versão mais recente) com Composition API e sintaxe estrita `<script setup lang="ts">`.
* **Linguagem:** TypeScript em modo estrito (`strict: true`), proibição absoluta de tipos `any`.
* **Build e Bundler:** Vite com resolução de caminhos `@/` direcionados para `src/`.
* **Componentes de UI:** PrimeVue integrado com tema Aura e classes utilitárias do Tailwind CSS.
* **Gerenciamento de Estado:** Pinia (stores modulares desacopladas por responsabilidade).
* **Roteamento SPA:** Vue Router 4 com carregamento sob demanda (*lazy loading*).
* **Comunicação HTTP:** Axios encapsulado em classes de serviço estáticas (`C*Service`).
* **Ícones:** PrimeIcons e Lucide Icons.

---

## 4. Padrão Arquitetural e Fluxo de Dados

A aplicação obedece rigorosamente ao fluxo unilinear de responsabilidades:

```txt
Component (UI / PrimeVue) ──> Store (Pinia) ──> Composable (UX/Regras) ──> Service (HTTP/Axios) ──> Backend API
```

### 4.1 Estrutura de Diretórios (`src/`)
```txt
src/
├── assets/             # Estilos globais Tailwind, logos e tipografia
├── components/         # Componentes Vue reutilizáveis
│   ├── base/           # Componentes atômicos e agnósticos (BaseButton, BaseBadge)
│   └── domain/         # Componentes de domínio (MatchScoreBadge, PreventiveWarningCard, PetCard)
├── composables/        # Lógicas de estado reativo e controle de UX (useRecommendation, usePets)
├── constants/          # Constantes globais em UPPER_CASE
├── models/             # Types (T) e Interfaces (I) de contratos de dados
├── router/             # Definição e guardas das rotas do Vue Router
├── services/           # Classes estáticas (C) encapsulando chamadas Axios
├── stores/             # Stores Pinia para estado compartilhado
├── views/              # Páginas completas (Home, Questionnaire, Recommendations, PetDetails)
├── App.vue             # Componente raiz mínimo (apenas RouterView e Toasts globais)
└── main.ts             # Ponto de entrada, configuração do PrimeVue, Pinia e Router
```

---

## 5. Convenções de Código e Nomenclatura em TypeScript / Vue

### 5.1 Nomenclatura Global Estrita
* **Types:** Prefixados com `T` (ex.: `TRecommendationRequest`, `TPetStatus`, `TAdopterHabitation`).
* **Interfaces:** Prefixadas com `I` (ex.: `IPetResponse`, `IRecommendationResult`, `IUserCredentials`).
* **Classes:** Prefixadas com `C` (ex.: `CRecommendationService`, `CPetService`, `CFormatters`).
* **Constantes:** Em caixa alta com sublinhado (`UPPER_CASE`) (ex.: `API_BASE_URL`, `DEFAULT_PAGE_LIMIT`).
* **Atributos, Variáveis e Métodos:** Utilizam `camelCase` (ex.: `calculatedScore`, `loadRecommendations()`).
* **Parâmetros de Funções:** Devem iniciar obrigatoriamente com o prefixo `p` e possuir tipagem forte e explícita:
  ```ts
  function formatPercentage(pValue: number): string {
    return `${Math.round(pValue)}%`;
  }
  ```

### 5.2 Estrutura Padronizada do `<script setup>`
O bloco `<script setup lang="ts">` de todo componente deve seguir a ordem canônica com seções comentadas:

```vue
<script setup lang="ts">
// Types e Interfaces
/**
 * @property {boolean} loading - Define se o componente está em estado de processamento.
 * @property {number} score - Pontuação percentual calculada (0 a 100).
 */
type TProps = {
  loading: boolean;
  score: number;
};

/**
 * @property {[id: number]} select - Emitido quando o usuário confirma o interesse no animal.
 */
type TEmits = {
  select: [id: number];
};

// Props
const props = defineProps<TProps>();

// Emits
const emit = defineEmits<TEmits>();

// Constantes
const HIGH_COMPATIBILITY_THRESHOLD = 80;

// Reativas - Model
const isAcknowledged = defineModel<boolean>({ default: false });

// Reativas - Ref
const isSubmitting = ref(false);

// Computadas
const isHighAffinity = computed(() => props.score >= HIGH_COMPATIBILITY_THRESHOLD);

// Funções
function handleConfirm(pPetId: number): void {
  if (props.loading || isSubmitting.value) return;
  emit('select', pPetId);
}

// Observadores
watch(() => props.score, (pNewScore) => {
  // Tratamento reativo...
});

// Lifecycle Hooks
onMounted(() => {
  // Inicialização...
});

// Expose
defineExpose({
  handleConfirm,
});
</script>
```

### 5.3 Organização Padronizada de Imports
```ts
// Ecossistema Vue
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';

// PrimeVue e UI
import Button from 'primevue/button';
import Card from 'primevue/card';
import Badge from 'primevue/badge';

// Stores
import { useRecommendationStore } from '@/stores/useRecommendationStore';

// Constantes
import { HIGH_COMPATIBILITY_THRESHOLD } from '@/constants/HIGH_COMPATIBILITY_THRESHOLD';

// Types e Interfaces
import type { IRecommendationResult } from '@/models/IRecommendationResult';
import type { TRecommendationRequest } from '@/models/TRecommendationRequest';

// Composables
import { useRecommendation } from '@/composables/useRecommendation';

// Services
import { CRecommendationService } from '@/services/CRecommendationService';

// Componentes
import MatchScoreBadge from '@/components/domain/MatchScoreBadge.vue';
import PreventiveWarningCard from '@/components/domain/PreventiveWarningCard.vue';
```
