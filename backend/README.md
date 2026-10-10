# AdotaMatch — Backend REST API
> **Sistema Inteligente de Recomendação Híbrida e Explicável para Adoção Responsável de Animais**

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.20-7F52FF.svg?logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.4-6DB33F.svg?logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![Maven](https://img.shields.io/badge/Maven-3.9+-C71A36.svg?logo=apachemaven&logoColor=white)](https://maven.apache.org/)
[![OpenAPI](https://img.shields.io/badge/OpenAPI-Swagger_UI-85EA2D.svg?logo=swagger&logoColor=black)](http://localhost:8080/swagger-ui.html)
[![TLC Spec-Driven](https://img.shields.io/badge/Methodology-TLC_Spec--Driven-blue.svg)](#metodologia-tlc-spec-driven)

---

## 1. Visão Geral do Projeto

O **AdotaMatch Backend** provê os serviços computacionais do ecossistema AdotaMatch, cujo objetivo central é a **mitigação do reabandono, dos maus-tratos e da devolução traumática de cães e gatos** sob custódia de abrigos e organizações não governamentais (ONGs).

Diferente de sistemas tradicionais que apenas filtram animais por atributos rígidos em consultas de banco de dados, o AdotaMatch implementa um **Motor de Recomendação Híbrido em Múltiplos Estágios** que combina:
1. **Camada de Restrições Rígidas ($R(u, a) \in \{0, 1\}$):** Filtro eliminatório booleano para incompatibilidades intransponíveis (porte grande em apartamento pequeno sem quintal, intolerância a outros pets caso o adotante já tenha animais no lar, etc.).
2. **Camada de Pontuação Ponderada Multicritério ($s_c(u, a)$):** Modelagem matemática para compatibilizar nível de energia vs. tempo livre diário, tolerância à solidão vs. horas de ausência real estimada, espaço físico e experiência prévia.
3. **Inferência Semântica e Contextual via LLM / Heurística Contextual ($S_{\text{LLM}}(u, a)$):** Extração de nuances de relatos em texto livre do adotante (regime de trabalho, faculdade noturna, rede de apoio familiar) cruzadas com o prontuário comportamental do abrigo.
4. **Módulo de Explicabilidade Preventiva:** Geração em linguagem natural de *Fatores de Afinidade* e *Alertas de Manejo e Desafios* antes da efetivação da tutela.

---

## 2. Metodologia TLC Spec-Driven Development

Este repositório é governado pelo paradigma **Spec-Driven**:
* 📜 **[constitution.md](../constitution.md):** Princípios inegociáveis, convenções de código (prefixo `C` para classes, `I` para interfaces, `T` para types, `p` para parâmetros) e governança arquitetural.
* 🤖 **[agents.md](../agents.md):** Catálogo de papéis e diretrizes para atuação de Agentes de IA.
* 📋 **Especificações Técnicas e de Negócio (`../specs/`):**
  * `01-business-domain-spec.md`: Requisitos de negócio, atores e modelo conceitual.
  * `02-system-architecture-spec.md`: Decisões de Arquitetura (ADRs) e fluxos de dados.
  * `03-backend-technical-spec.md`: Contratos de API RESTful, payloads e persistência.
  * `05-recommendation-engine-spec.md`: Formalização matemática das funções de similaridade e engenharia de prompts.

---

## 3. Padrão Arquitetural

* **Vertical Slice by Feature + DDD + MVC Convencional:**
  * O código é estruturado por capacidades de negócio (`features/recommendation`, `features/pet`, `features/adopter`, `features/auth`, `shared`), garantindo alta coesão e baixo acoplamento.
  * Cada fatia implementa o padrão MVC direto (`controller`, `service`, `domain`, `dto`, `repository`), sem sobrecarga de Clean Architecture com dezenas de interfaces vazias.
  * **O Core Domain é o Sistema de Recomendação:** As operações de CRUD são secundárias e enxutas.

```txt
src/main/kotlin/br/com/adotamatch/
├── features/
│   ├── recommendation/          # [CORE DOMAIN DA APLICAÇÃO]
│   │   ├── controller/          # CRecommendationController.kt
│   │   ├── service/             # CRecommendationService.kt, CHardConstraintsFilter.kt, CWeightedScorer.kt
│   │   ├── client/              # ILlmClient.kt, CExternalLlmClient.kt, CHeuristicFallbackClient.kt
│   │   └── dto/                 # CRecommendationRequest.kt, CRecommendationResponse.kt
│   ├── auth/                    # [AUTENTICAÇÃO MULTI-PERFIL]
│   │   ├── controller/          # CAuthController.kt
│   │   ├── service/             # CAuthService.kt
│   │   ├── domain/              # CUser.kt, EUserRole.kt (ADOPTER, GUARDIAN, SHELTER)
│   │   └── dto/                 # CUserRegisterRequest.kt, CUserLoginRequest.kt, CUserAuthResponse.kt
│   ├── pet/                     # [FEATURE DE ANIMAIS E CUSTÓDIA]
│   │   ├── controller/          # CPetController.kt
│   │   ├── domain/              # CPet.kt (JPA Entity, Enums)
│   │   ├── dto/                 # CPetCreateRequest.kt, CPetResponse.kt, CPetUpdateRequest.kt
│   │   ├── repository/          # IPetRepository.kt
│   │   └── service/             # CPetService.kt
│   └── adopter/                 # [FEATURE DE INTENÇÕES DE ADOÇÃO]
│       ├── controller/          # CAdoptionIntentController.kt
│       ├── domain/              # CAdoptionIntent.kt
│       ├── repository/          # IAdoptionIntentRepository.kt
│       └── service/             # CAdoptionIntentService.kt
├── shared/                      # [COMPARTILHADOS]
│   ├── config/                  # CCorsConfig.kt, CDataSeeder.kt
│   └── exception/               # CGlobalExceptionHandler.kt (Problem Details RFC 7807)
└── AdotaMatchApplication.kt     # Ponto de entrada Spring Boot
```

---

## 4. Endpoints da API RESTful

### 4.1 Autenticação Multi-Perfil
* `POST /api/v1/auth/register` — Cadastro com segregação de papéis (`ADOPTER`, `GUARDIAN`, `SHELTER` com suporte a CNPJ e Razão Social).
* `POST /api/v1/auth/login` — Autenticação de usuários com emissão de token JWT.
* `GET /api/v1/auth/me?pId={id}` — Consulta de dados do usuário autenticado.

### 4.2 Motor de Recomendação Inteligente (Core)
* `POST /api/v1/recommendations/match` — Cálculo da recomendação ética ponderada com explicabilidade.
  * O campo `lifestyleNarrative` é **opcional**: o motor opera com 100% de precisão multicritério apenas com as variáveis quantitativas e enriquece os resultados semanticamente quando houver texto livre.

### 4.3 Gestão de Animais
* `GET /api/v1/pets` — Listagem com filtros por espécie (`pSpecies=DOG`, `pSpecies=CAT`).
* `GET /api/v1/pets/{id}` — Detalhamento completo do animal, métricas e prontuário.
* `POST /api/v1/pets` — Cadastro de novo animal (abrigos e protetores).
* `PUT /api/v1/pets/{id}` — Atualização cadastral e comportamental.

### 4.4 Intenções de Adoção
* `POST /api/v1/adoptions/intent` — Submissão de proposta de adoção responsável.
* `GET /api/v1/adoptions/intent/my?pAdopterEmail={email}` — Consulta do histórico de intenções do adotante.
* `GET /api/v1/adoptions/intent/pet/{petId}` — Consulta de pretendentes recebidos pelo abrigo.
* `PATCH /api/v1/adoptions/intent/{id}/status` — Atualização do status da triagem (`PENDING`, `APPROVED`, `REJECTED`, `COMPLETED`).

---

## 5. Como Executar

### 5.1 Requisitos
* **Java JDK:** Versão 21 ou 22 LTS.
* **Apache Maven:** Versão 3.9+.

### 5.2 Executar a Bateria Completa de Testes
```bash
mvn clean test
```

### 5.3 Executar a Suíte E2E Automatizada (População de 50 Animais)
No diretório `src/test/kotlin/br/com/adotamatch/e2e/`, implementamos uma suíte completa de testes de ponta a ponta:
* **`CAutomatedApiE2eTest.kt`:** Valida autenticação multi-perfil, cadastra em lote **50 animais (25 cães e 25 gatos com métricas variáveis)** via API, simula 5 personas de adotantes e valida o fluxo de intenções de adoção.
* **`CPopulateLiveRunnerTest.kt`:** Utilitário para popular a instância local ativa em `http://localhost:8080`.

Para executar especificamente a suíte E2E:
```bash
mvn test -Dtest=CAutomatedApiE2eTest
```

### 5.4 Inicializar a Aplicação
```bash
mvn spring-boot:run
```
Ou executando diretamente o JAR gerado:
```bash
java -jar target/adotamatch-backend-1.0.0-SNAPSHOT.jar
```

A API estará disponível em `http://localhost:8080`.
* **Swagger UI Interativo:** `http://localhost:8080/swagger-ui.html`
* **Especificação OpenAPI JSON:** `http://localhost:8080/api-docs`
* **Console H2 Database:** `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:adotamatchdb`, User: `sa`, Senha em branco)

---

## 6. Licença

Este projeto é desenvolvido para fins acadêmicos e sociais no âmbito do Bacharelado em Ciência da Computação do **UNIFAGOC**.
