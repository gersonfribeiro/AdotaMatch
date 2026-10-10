# Constituição Global do Ecossistema AdotaMatch
## Metodologia TLC Spec-Driven Development (Total Lifecycle Context & Specification-Driven)

---

## 1. Preâmbulo, Ontologia e Missão Social

O **AdotaMatch** é uma solução de software concebida sob o rigor da Ciência da Computação aplicada, tendo como fundamento teórico e prático o Trabalho de Conclusão de Curso intitulado:
> **"ADOTAMATCH: SISTEMA DE RECOMENDAÇÃO BASEADO EM CONTEÚDO E MODELOS DE LINGUAGEM DE GRANDE PORTE (LLMs) PARA APOIO À ADOÇÃO RESPONSÁVEL DE ANIMAIS"** (UNIFAGOC, 2026).

### 1.1 Missão Inegociável
A razão de existir do sistema é a **mitigação do reabandono, dos maus-tratos e da devolução traumática de cães e gatos** sob custódia de organizações não governamentais (ONGs) e protetores independentes. 

Toda decisão arquitetural, todo componente de interface e todo algoritmo implementado nesta base de código devem responder diretamente à pergunta: **"Esta funcionalidade apoia uma adoção ética, consciente e duradoura ou apenas estimula uma aquisição impulsiva baseada em estética?"**

### 1.2 Cláusula Pétrea de Alinhamento com o TCC
Fica terminantemente vedado a qualquer desenvolvedor, revisor ou Agente de Inteligência Artificial alterar o núcleo computacional do sistema para desvios conceituais tais como:
1. Transformar o AdotaMatch em um catálogo estático ou e-commerce de animais com busca booleana simples (SQL `WHERE` estrito).
2. Eliminar o componente de **Modelos de Linguagem de Grande Porte (LLMs)** do pipeline de recomendação e explicabilidade.
3. Substituir a explicabilidade transparente por modelos "caixa-preta" inacessíveis ao entendimento do adotante.
4. Omitir ou suavizar os **Alertas Preventivos de Manejo e Desafios de Adaptação** por receio de reduzir o número bruto de solicitações de contato.

---

## 2. Os 5 Pilares do TLC Spec-Driven Development no AdotaMatch

1. **A Especificação Precede o Código (Spec-First):**
   Nenhuma linha de código em Kotlin ou TypeScript é aceita sem referência explícita a uma especificação em `specs/`. O código é a materialização da especificação, nunca o inverso.
2. **Vertical Slice by Feature com DDD Pragmático:**
   As aplicações não se organizam em camadas horizontais distantes. O código estrutura-se em fatias verticais autônomas (`recommendation`, `pet`, `adopter`), adotando um **MVC Convencional** direto dentro de cada fatia, sem sobrecargas desnecessárias de Clean Architecture.
3. **O Core Domain é o Sistema de Recomendação:**
   O valor do software reside na inteligência do cruzamento de perfis e na explicabilidade da IA. Operações de CRUD de cadastros são secundárias e devem permanecer enxutas.
4. **Resiliência e Determinismo de IA:**
   LLMs não operam soltas. Todo processamento de linguagem natural é ancorado em esquemas formais (JSON Schema), com temperatura restrita ($T \le 0.3$), sanitização contra *Prompt Injection* e **Fallback Heurístico Obrigatório** para garantir 100% de disponibilidade.
5. **Rastreabilidade Bidirecional com a Literatura Científica:**
   Todo critério, peso e métrica de validação do software possui lastro comprovado nas referências da monografia: *Wu et al. (2024)*, *Lin et al. (2024)*, *Lops et al. (2011)*, *Hawes et al. (2020)*, *Protopopova e Gunter (2017)*, *Powers (2011)*, *Davis (1989)* e *Brooke (1996)*.

---

## 3. Padrões Globais de Engenharia de Software

### 3.1 Nomenclatura Global Estrita
* **Classes:** Iniciam obrigatoriamente com o prefixo `C` (ex.: `CRecommendationService`, `CPet`, `MatchCard.vue`).
* **Interfaces:** Iniciam obrigatoriamente com o prefixo `I` (ex.: `IPetRepository`, `IRecommendationResult`).
* **Types / Aliases:** Iniciam obrigatoriamente com o prefixo `T` (ex.: `TRecommendationRequest`, `TPetStatus`).
* **Constantes:** Grafadas em caixa alta com sublinhado (`UPPER_CASE`) (ex.: `DEFAULT_PAGE_SIZE`, `WEIGHT_ENERGY_TIME`).
* **Atributos, Métodos e Variáveis:** Utilizam `camelCase` (ex.: `dailyHoursAvailable`, `calculateScore()`).
* **Parâmetros de Funções:** Devem iniciar obrigatoriamente com o prefixo `p` e declarar tipagem explícita e forte (ex.: `fun calculateScore(pAdopter: CAdopter, pPet: CPet): Double`, `function formatScore(pScore: number): string`).

### 3.2 Documentação Interna (JSDoc e KDoc)
* Bloco único de documentação acima de cada símbolo público.
* Proibido documentar propriedades individualmente dentro do corpo de tipos/interfaces; utilizar `@property {Tipo} nome - Descrição.` no bloco pai.
* Funções documentadas com `@description`, `@param` para cada `pParametro` e `@returns`.

---

## 4. Governança dos Repositórios

O projeto divide-se em dois repositórios oficiais sincronizados:
* 📦 **Backend:** [`gersonfribeiro/adotamatch-backend`](https://github.com/gersonfribeiro/adotamatch-backend) (Kotlin 2.0 + Spring Boot 3.3+ via Maven).
* 📦 **Frontend:** [`gersonfribeiro/adotamatch-frontend`](https://github.com/gersonfribeiro/adotamatch-frontend) (Vue 3 + Vite + PrimeVue + Tailwind via npm).

Ambos devem conter suas respectivas constituições locais e espelho atualizado das especificações em `specs/`.
