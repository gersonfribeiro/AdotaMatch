ADOTAMATCH: SISTEMA DE RECOMENDAÇÃO BASEADO EM CONTEÚDO E MODELOS DE LINGUAGEM DE GRANDE PORTE (LLMs) PARA APOIO À ADOÇÃO RESPONSÁVEL DE ANIMAIS

RIBEIRO, Gerson FernandesCentro Universitário Governador Ozanam Coelho – UNIFAGOCBacharelado em Ciência da ComputaçãoUbá – MG, 2026TAVARES, Jacimar – Orientador

RESUMO

A devolução e o reabandono de cães e gatos sob custódia de abrigos e organizações não governamentais (ONGs) constituem um grave problema socioambiental, decorrente frequentemente da discrepância entre as expectativas do adotante, sua rotina diária e as reais demandas comportamentais do animal. Tradicionalmente, as plataformas de adoção operam por meio de consultas determinísticas em bancos de dados relacionais e filtros booleanos estritos, os quais se mostram ineficazes para capturar nuances comportamentais e características não estruturadas de estilo de vida. Este estudo propõe o AdotaMatch, um sistema de recomendação híbrido voltado à adoção responsável de animais de estimação, que combina filtragem baseada em conteúdo, restrições rígidas eliminatórias, ranqueamento ponderado multicritério e Modelos de Linguagem de Grande Porte (Large Language Models – LLMs). A arquitetura proposta emprega LLMs em duas frentes fundamentais: (i) no perfilamento contextual e alinhamento semântico de relatos em texto livre do adotante e históricos comportamentais mantidos pelos protetores; e (ii) na síntese de justificativas explicáveis em linguagem natural, explicitando pontos fortes de afinidade e fatores críticos de atenção para prevenção de devoluções. Apresenta-se a formalização matemática do algoritmo híbrido, o delineamento conceitual dos componentes e o protocolo experimental de validação técnica e de usabilidade (utilizando questionário TAM e escala SUS) voltado à validação técnica, discriminativa e de usabilidade do recomendador.

Palavras-chave: Sistemas de Recomendação. Modelos de Linguagem de Grande Porte. Filtragem Baseada em Conteúdo. Recomendação Explicável. Adoção Responsável de Animais. Ciência da Computação.

ABSTRACT

The return and re-abandonment of dogs and cats under the custody of animal shelters and non-governmental organizations (NGOs) represent a critical socio-environmental challenge, often driven by the mismatch between adopter expectations, domestic routine, and the animal's behavioral needs. Conventional adoption platforms typically rely on strict relational database queries and exact boolean filters, failing to capture subtle behavioral traits and unstructured lifestyle context. This paper presents AdotaMatch, a hybrid recommender system for responsible pet adoption that integrates content-based filtering, hard constraints, multi-criteria weighted scoring, and Large Language Models (LLMs). The proposed architecture leverages LLMs in two core dimensions: (i) semantic profiling and contextual alignment of free-form adopter lifestyle narratives and shelter behavioral logs; and (ii) natural language generation of explainable justifications, highlighting both compatibility strengths and critical adaptation warnings to prevent future relinquishment. We formalize the hybrid algorithm, define its conceptual components, and establish the experimental validation protocol (integrating classification metrics, the TAM framework, and the SUS scale) for responsible and explainable pet adoption.

Keywords: Recommender Systems. Large Language Models. Content-Based Filtering. Explainable Recommendation. Responsible Pet Adoption. Computer Science.

1 INTRODUÇÃO

No contexto contemporâneo da proteção animal e da saúde pública, o acolhimento e a adoção de cães e gatos enfrentam barreiras críticas de sustentabilidade. O Ministério do Meio Ambiente e Mudança do Clima destaca que o Brasil possui dezenas de milhões de animais em situação de vulnerabilidade, sendo que a expressiva maioria dos municípios não dispõe de infraestrutura pública integral para acolhimento e manejo ético populacional, transferindo o ônus assistencial para organizações não governamentais (ONGs) e protetores independentes (BRASIL, 2025). Embora campanhas públicas e feiras busquem impulsionar as taxas de acolhimento, a escolha de um animal por parte do adotante frequentemente apoia-se em critérios imediatistas e puramente visuais, como aparência, porte ou raça, negligenciando a complexidade da integração do animal à rotina socioespacial do lar.

A literatura científica evidencia que uma correspondência mal avaliada no momento da adoção é o principal vetor para o fenômeno traumático da devolução e do reabandono. Hawes et al. (2020), em estudo longitudinal sobre devoluções em abrigos, observaram que fatores comportamentais (como nível excessivo de energia, reatividade ou dificuldades de adaptação) são determinantes na devolução de cães, enquanto a incompatibilidade com a rotina pessoal e limitações habitacionais do tutor predominam nos casos envolvendo felinos. De forma convergente, Protopopova e Gunter (2017) ressaltam que as intervenções preventivas mais eficazes em abrigos residem no alinhamento prévio e rigoroso entre as expectativas do adotante e o temperamento real do animal, mitigando o abismo entre o desejo abstrato da tutela e as exigências práticas de convivência.

Sob a ótica da Ciência da Computação, as plataformas digitais existentes de adoção funcionam predominantemente como catálogos estáticos, amparados por consultas determinísticas em bancos de dados relacionais com cláusulas de filtragem exata (consultas SQL com cláusulas WHERE). Esse modelo restringe-se a respostas binárias (o animal atende ou não a filtros restritos de espécie, sexo ou porte), sendo incapaz de lidar com compatibilidades parciais, preferências graduais e, principalmente, com o contexto tácito expresso em linguagem natural pelos adotantes e protetores. Sistemas de Recomendação (SR) clássicos baseados em conteúdo permitem contornar a rigidez booleana por meio de ranqueamento ponderado (ADOMAVICIUS; TUZHILIN, 2005; LOPS; DE GEMMIS; SEMERARO, 2011). Todavia, na era contemporânea da inteligência computacional, a incorporação de Modelos de Linguagem de Grande Porte (Large Language Models – LLMs) inaugura um novo paradigma: a capacidade de interpretar descrições textuais não estruturadas de estilo de vida, capturar nuances comportamentais e fornecer justificativas explicáveis e personalizadas (WU et al., 2024; LIN et al., 2024).

Diante desse cenário, formula-se a seguinte pergunta de pesquisa: como um sistema de recomendação híbrido, combinando filtragem baseada em conteúdo, ponderação multicritério e Modelos de Linguagem de Grande Porte (LLMs), pode otimizar a compatibilização responsável entre adotantes e animais sob custódia de abrigos, oferecendo explicabilidade transparente em linguagem natural para prevenção de devoluções?

Para responder a essa questão, este trabalho tem como objetivo geral formalizar o modelo computacional e propor a arquitetura do AdotaMatch, um sistema de recomendação híbrido voltado ao cruzamento ético de perfis para adoção responsável de cães e gatos. Constituem objetivos específicos deste estudo: (i) modelar matematicamente o algoritmo de compatibilidade, articulando restrições rígidas eliminatórias, critérios objetivos ponderados e escore semântico contextual; (ii) projetar o módulo de processamento de linguagem natural amparado em LLM para perfilamento contextual de relatos textuais e síntese de explicações transparentes; (iii) delinear a arquitetura conceitual do recomendador e o fluxo de dados desacoplado; e (iv) estruturar o protocolo experimental e metodológico de validação empírica (métricas computacionais, matriz de confusão e usabilidade via TAM/SUS) para validação experimental da abordagem proposta.

O presente artigo está estruturado em sete seções. A Seção 2 delineia o referencial teórico, cobrindo fundamentos de sistemas de recomendação, a integração de LLMs no contexto de recomendação, explicabilidade e métricas de avaliação. A Seção 3 analisa criticamente os trabalhos relacionados. A Seção 4 apresenta formalmente o modelo algorítmico do AdotaMatch. A Seção 5 detalha a metodologia da pesquisa e o plano de validação. A Seção 6 discute os resultados esperados, e a Seção 7 conclui o documento traçando as diretrizes de continuidade para a etapa experimental e desdobramentos futuros.

2 REFERENCIAL TEÓRICO

2.1 Sistemas de recomendação e filtragem baseada em conteúdo

Sistemas de Recomendação (SR) são ferramentas e técnicas computacionais de filtragem de informação projetadas para sugerir itens relevantes a um usuário a partir do processamento de grandes espaços de busca e da análise de preferências individuais (RICCI; ROKACH; SHAPIRA, 2015). Adomavicius e Tuzhilin (2005) classificam os sistemas de recomendação em três abordagens fundamentais: filtragem colaborativa, filtragem baseada em conteúdo e sistemas híbridos. A filtragem colaborativa depende do histórico de interações (como avaliações, compras ou cliques) compartilhado por uma comunidade de usuários, identificando padrões de similaridade entre pares de usuários ou itens (HERLOCKER et al., 2004).

No domínio específico da adoção de animais, a aplicação da filtragem colaborativa enfrenta o clássico problema de inicialização a frio (cold-start), uma vez que animais disponíveis em abrigos são indivíduos únicos que deixam o sistema imediatamente após a concretização da adoção, inviabilizando o acúmulo de histórico denso de transações sobre um mesmo item (AGGARWAL, 2016). Consequentemente, a filtragem baseada em conteúdo (Content-Based Filtering – CBF) emerge como a abordagem tecnicamente indicada para o núcleo de compatibilização (LOPS; DE GEMMIS; SEMERARO, 2011). Na CBF, as recomendações são formuladas mediante a comparação direta entre os atributos do perfil do usuário e as características descritivas dos itens cadastrados, garantindo que novos animais possam ser imediatamente ranqueados e recomendados com precisão matemática.

2.2 Modelos de Linguagem de Grande Porte (LLMs) em sistemas de recomendação

A evolução recente do Processamento de Linguagem Natural (PLN) e a emergência dos Modelos de Linguagem de Grande Porte (Large Language Models – LLMs), alicerçados na arquitetura Transformer, transformaram significativamente os sistemas de recomendação modernos. Conforme sistematizado por Wu et al. (2024), os LLMs possuem ampla capacidade de generalização e compreensão do senso comum, viabilizando sua atuação tanto no papel de extratores de representações semânticas densas quanto como motores de raciocínio sobre preferências e restrições complexas.

Lin et al. (2024) categorizam os benefícios dos LLMs para sistemas de recomendação em três eixos principais: compreensão semântica profunda de entradas textuais não estruturadas, raciocínio lógico contextualizado (reasoning via prompting) e geração de interfaces explicáveis em linguagem natural. No domínio de acolhimento animal, grande parte das informações cruciais sobre o perfil do adotante (tais como rotinas dinâmicas, presenças de familiares com necessidades especiais ou histórico de convívio) e sobre o animal (como traumas prévios, hábitos peculiares relatados por protetores e reações a ruídos) reside em narrativas textuais livres. Métodos tradicionais baseados exclusivamente em tabelas ou formulários fechados perdem essas correlações sutis. A integração de LLMs permite a extração e o alinhamento semântico dessas dimensões contextuais, elevando a precisão da compatibilidade sem desconsiderar as variáveis objetivas do ambiente doméstico.

2.3 Sistemas de recomendação explicáveis

Em domínios com elevada carga de responsabilidade ética e emocional, a opacidade de algoritmos do tipo 'caixa-preta' compromete a confiança e a aceitação do usuário. Zhang e Chen (2020) definem Sistemas de Recomendação Explicáveis (Explainable Recommendation) como sistemas que não apenas sugerem itens relevantes, mas também oferecem justificativas transparentes que elucidam os motivos computacionais e conceituais pelos quais uma recomendação foi gerada. Tintarev e Masthoff (2007) ressaltam que as explicações devem atender a objetivos bem estabelecidos na literatura, incluindo transparência (explicar como o sistema opera), escrutinabilidade (permitir que o usuário identifique e corrija inferências errôneas), eficácia (auxiliar na tomada de decisão correta) e confiança.

No contexto do AdotaMatch, a explicabilidade assume um papel preventivo direto contra devoluções: ao receber uma recomendação, o potencial tutor deve compreender com clareza quais fatores estruturais motivaram a indicação (por exemplo, alta compatibilidade entre disponibilidade horária e nível de energia) e, primordialmente, quais pontos de adaptação e desafios futuros demandarão atenção e paciência (como a necessidade de enriquecimento ambiental para um cão ansioso). A explicabilidade orientada por LLM ancorada em parâmetros objetivos garante justificativas fluídas, empáticas e tecnicamente fundamentadas.

2.4 Avaliação de sistemas de recomendação e usabilidade

A validação empírica de um recomendador exige uma abordagem multidimensional, abrangendo a exatidão algorítmica e a percepção de eficácia humana (HERLOCKER et al., 2004). Para a análise computacional de classificação sob dados rotulados, adota-se a matriz de confusão, discriminando acertos e erros em Verdadeiros Positivos (VP), Falsos Positivos (FP), Verdadeiros Negativos (VN) e Falsos Negativos (FN). A partir dessa estruturação, calculam-se métricas padronizadas formalizadas por Powers (2011), tais como Acurácia, Precisão, Revocação (Recall) e a média harmônica F1-Score.

Complementarmente à exatidão preditiva, sistemas interativos voltados ao público geral necessitam de validação formal de interface e usabilidade. Nielsen (1993) estabelece que a usabilidade é medida pela facilidade de aprendizado, eficiência de uso, retenção ao longo do tempo e baixa taxa de erros. O Modelo de Aceitação de Tecnologia (Technology Acceptance Model – TAM), formulado por Davis (1989), avalia a intenção comportamental de uso a partir dos construtos de Utilidade Percebida (Perceived Usefulness) e Facilidade de Uso Percebida (Perceived Ease of Use). Por sua vez, a escala SUS (System Usability Scale), proposta por Brooke (1996), fornece um instrumento padronizado, robusto e quantitativo para mensurar a qualidade global da interação.

3 TRABALHOS RELACIONADOS

A aplicação de soluções computacionais avançadas voltadas ao bem-estar e acolhimento animal tem despertado crescente interesse na literatura científica, embora a confluência entre sistemas de recomendação explicáveis e modelos de linguagem generativos ainda represente uma fronteira em consolidação.

Santos et al. (2024) propuseram um sistema de recomendação baseado em conteúdo para adoção de animais utilizando a técnica do cosseno ponderado. O estudo investigou a atribuição de pesos diferenciados a características como idade, porte, necessidade de espaço e convivência com outros animais, demonstrando que a similaridade vetorial com ponderação de atributos supera as abordagens tradicionais de busca linear não ponderada. Não obstante sua relevância ao tratar a modelagem matemática do ranqueamento, o trabalho de Santos et al. (2024) apoia-se em formulários puramente estruturados, não explora dados contextuais não estruturados em texto livre e carece de um mecanismo dinâmico de explicabilidade em linguagem natural para apoiar a decisão do adotante.

Por sua vez, Zhang e Zhang (2019) conduziram uma pesquisa de referência internacional no âmbito do desafio mundial PetFinder.my na ACM International Conference on Multimedia, modelando algoritmos de aprendizado de máquina para prever a velocidade de adoção de animais em abrigos (Pet Adoption Speed). Os autores combinaram metadados tabulares, análise de sentimento sobre as biografias textuais dos animais e processamento de imagens por redes neurais convolucionais. O trabalho comprovou empiricamente que o conteúdo textual das descrições elaboradas pelos cuidadores possui forte correlação preditiva com o desfecho da adoção. Entretanto, o objetivo do estudo centrou-se na predição de tempo de permanência no abrigo, não formalizando um algoritmo de recomendação bidirecional focado na compatibilização recíproca entre tutor e animal para mitigação de reabandono.

O AdotaMatch diferencia-se dos trabalhos da literatura ao propor um modelo híbrido em múltiplos estágios: agrega restrições rígidas eliminatórias para incompatibilidades intransponíveis, pontuação ponderada multicritério para variáveis físicas e rotineiras, e incorpora Modelos de Linguagem de Grande Porte (LLMs) em duas frentes inovadoras: a decodificação semântica de nuances de estilo de vida expressas em texto livre e a síntese de relatórios explicativos focados conscientemente na prevenção de devoluções.

Tabela 1 – Comparativo entre trabalhos relacionados e a proposta AdotaMatch.

Trabalho

Abordagem Computacional

Processamento de Texto Livre

Explicabilidade Natural

Foco Preventivo (Devolução)

Santos et al. (2024)

Filtragem baseada em conteúdo (Cosseno ponderado)

Não (apenas campos estruturados)

Não (apenas escore numérico)

Parcial (compatibilidade de atributos)

Zhang e Zhang (2019)

Classificação multimodal (ML tabular + NLP + CNN)

Sim (análise de sentimento de biografias)

Não (modelo caixa-preta / predição)

Não (predição de velocidade de adoção)

AdotaMatch (Proposta)

Híbrida: Restrições rígidas + CBF ponderada + LLM

Sim (perfilamento semântico contextual via LLM)

Sim (geração de texto explicativo via LLM)

Total (conscientização e alinhamento de rotina)

Fonte: Elaborado pelo autor (2026).

4 MODELO DE RECOMENDAÇÃO PROPOSTO (ADOTAMATCH)

O modelo AdotaMatch foi concebido como um pipeline computacional em múltiplos estágios para compatibilização entre um adotante u e um conjunto de animais disponíveis A = {a_1, a_2, ..., a_n}. A arquitetura do recomendador estrutura-se em quatro camadas articuladas: (i) camada de restrições rígidas eliminatórias; (ii) camada de perfilamento contextual via LLM; (iii) camada de pontuação ponderada multicritério; e (iv) módulo de explicabilidade e conscientização em linguagem natural.

4.1 Camada de restrições rígidas (Hard Constraints)

A primeira camada visa eliminar imediatamente candidatos matematicamente ou faticamente inviáveis, evitando que pontuações compensatórias mascarem impedimentos críticos. Seja R(u, a) a função indicadora de elegibilidade binária dada pelo produtório de m restrições booleanas r_j(u, a):

R(u, a) = ∏ [j=1 até m] r_j(u, a) ∈ {0, 1}

onde cada r_j(u, a) assume valor 1 (se a condição for satisfeita) e 0 (em caso de violação impeditiva). Entre as restrições rígidas aplicadas estão: (a) status de disponibilidade do animal (somente animais sob custódia e aptos à adoção); (b) espécie desejada (filtro estrito caso o adotante busque exclusivamente cães ou gatos); (c) restrições espaciais categóricas (por exemplo, exigência de moradia com quintal murado para animais de porte gigante com histórico de fuga); e (d) tolerância absoluta a outros animais ou crianças (quando o animal possui histórico documentado de reatividade perigosa).

4.2 Camada de perfilamento contextual e alinhamento semântico via LLM

A segunda camada processa informações ricas em texto livre. O adotante fornece uma descrição aberta T_u de sua rotina, expectativas e dinâmicas do lar (ex.: 'Trabalho remotamente a maior parte da semana, mas costumo viajar um fim de semana por mês; recebo visitas frequentes de sobrinhos pequenos e busco um companheiro dócil para caminhadas moderadas'). Concomitantemente, os cuidadores do abrigo registram o diário comportamental T_a do animal (ex.: 'Resgatado após maus-tratos, apresenta ansiedade leve diante de ruídos estridentes, extremamente afetuoso com pessoas, mas relutante em partilhar brinquedos com outros cães').

Um Modelo de Linguagem de Grande Porte atua como extrator semântico e motor de alinhamento contextual. A LLM recebe o par estruturado (T_u, T_a) sob um prompt padronizado com restrição de resposta estruturada (System Prompt com esquema JSON e ancoragem contextual para mitigação de alucinações), avaliando a sinergia contextual e gerando um escore semântico normalizado S_LLM(u, a) ∈ [0, 1], além de vetores conceituais de pontos de sinergia e fatores de atenção.

4.3 Camada de pontuação ponderada multicritério

Para os animais aprovados na camada rígida (R(u, a) = 1), o sistema calcula a pontuação final de compatibilidade Score(u, a), que combina os atributos estruturados normalizados e o escore semântico contextual proveniente da LLM. A formulação matemática geral é expressa por:

Score(u, a) = 100 × R(u, a) × [ ∑ [c=1 até k] (w_c × s_c(u, a)) + w_LLM × S_LLM(u, a) ]

com a restrição estrita de convexidade sobre os pesos w_c e w_LLM dada por:

∑ [c=1 até k] w_c + w_LLM = 1,0,  sendo w_c > 0 e w_LLM > 0

onde k denota o número de critérios estruturados, w_c representa o peso associado ao critério c, s_c(u, a) ∈ [0, 1] é a função de similaridade específica do critério, e w_LLM pondera a relevância do alinhamento semântico contextual. A Tabela 2 sintetiza os critérios, pesos paramétricos iniciais e a formalização das funções de similaridade estruturadas do modelo AdotaMatch.

Tabela 2 – Especificação dos critérios, pesos paramétricos e funções de similaridade.

Critério (c)

Peso (w)

Domínio de Variáveis

Função de Similaridade s_c(u, a)

Energia do animal vs. Disponibilidade de tempo

0,20

Nível de energia E(a) ∈ [1, 5]; Tempo diário D(u) em horas

s_1 = 1 - (|E(a) - normaliza(D(u))| / 4)

Convivência com crianças e outros animais

0,15

Histórico de tolerância do animal vs. composição familiar

s_2 = 1,0 se compatível; 0,5 se exige adaptação; 0,0 se restritivo

Moradia, espaço físico e porte do animal

0,15

Tipo de moradia (apartamento/casa) vs. Porte P(a) ∈ [1, 5]

s_3 = 1 - (|Espaço(u) - P(a)| / 4)

Tolerância à permanência solitária vs. Rotina

0,15

Tolerância T_sol(a) em horas vs. Ausência diária H_aus(u)

s_4 = 1,0 se H_aus(u) ≤ T_sol(a); max(0, 1 - (H_aus - T_sol)/4)

Experiência prévia vs. Cuidados especiais

0,15

Grau de experiência Exp(u) ∈ [1, 3]; Nível de cuidado C(a) ∈ [1, 3]

s_5 = 1,0 se Exp(u) ≥ C(a); max(0, 1 - (C(a) - Exp(u))*0,4)

Alinhamento Semântico e Contextual via LLM (S_LLM)

0,20

Narrativa de estilo de vida T_u e biografia do abrigo T_a

s_LLM = Inferência CoT da LLM ancorada em esquema JSON [0, 1]

Fonte: Elaborado pelo autor (2026).

4.4 Módulo de explicabilidade e conscientização em linguagem natural

A última camada do modelo é responsável pela geração da justificativa em linguagem natural. A LLM recebe a pontuação global Score(u, a), a lista discriminada das parcelas w_c × s_c(u, a), as variáveis estruturadas e as narrativas textuais originais. Por meio de engenharia de prompts estruturada, o modelo gera uma explicação em linguagem natural dividida em duas dimensões pragmáticas: (1) Fatores de Convergência: elucida por que aquele animal é altamente compatível com o tutor (ex.: 'O Caramelo apresenta perfil dócil e nível moderado de energia, integrando-se harmonicamente à sua disponibilidade diária de passeios e à presença de crianças no lar'); e (2) Alertas Preventivos e Recomendações de Manejo: pontua de forma transparente os aspectos que exigirão compromisso responsável (ex.: 'Atenção: o animal requer enriquecimento ambiental nos primeiros meses e não tolera períodos de isolamento superiores a seis horas diárias'). Essa transparência ativa combate o descompasso de expectativas, fortalecendo o vínculo duradouro.

5 METODOLOGIA

5.1 Caracterização da pesquisa e delineamento metodológico

Este trabalho caracteriza-se como uma pesquisa aplicada, de natureza exploratória e propositiva, orientada pela abordagem de Engenharia de Software (SOMMERVILLE, 2011) e Modelagem Computacional. A metodologia articula a formalização matemática do recomendador híbrido, o desenvolvimento da arquitetura orientada a serviços desacoplados e a estruturação de um protocolo de validação técnica e empírica com usuários e especialistas.

5.2 Instrumentos de coleta e representação de dados

A operacionalização do modelo assenta-se na estruturação de dois instrumentos conceituais de entrada de dados: (i) Formulário de Perfil do Adotante: composto por dados cadastrais objetivos (espaço de moradia, tempo livre diário, crianças residentes, outros animais e nível de experiência prévia) complementado por um campo aberto para descrição livre da dinâmica domiciliar; e (ii) Ficha Cadastral e Comportamental do Animal: preenchida pelos responsáveis pelos abrigos e protetores, registrando dados veterinários básicos (porte, espécie, idade estimada, necessidades médicas especiais) e o relatório descritivo do histórico temperamental e comportamental do animal. A separação estrita entre dados estruturados (vetoriais) e dados textuais assegura a rastreabilidade e a reprodutibilidade do cálculo.

5.3 Arquitetura conceitual do recomendador

Evitando o acoplamento prévio a especificações rígidas de implementação que poderiam limitar a flexibilidade do projeto, a arquitetura do AdotaMatch foi concebida sob o paradigma de serviços modulares desacoplados (Service-Oriented Decoupled Architecture). O fluxo conceitual opera em quatro estágios:

1. Ingestão e Pré-processamento: normalização dos dados vetoriais e sanitização de textos das narrativas livres.2. Filtragem Eliminatória: execução da camada booleana de restrições rígidas R(u, a), descartando animais incompatíveis com as condições mínimas do adotante.3. Processamento Híbrido e Ranqueamento: cálculo paralelo das funções de similaridade estruturada s_c(u, a) e invocação assíncrona do serviço de inferência LLM para cômputo de S_LLM(u, a), sintetizando a pontuação final Score(u, a) e ordenando os Top-K candidatos.4. Síntese Explicável e Interface: geração das justificativas textuais contextualizadas via prompt estruturado e apresentação transparente ao usuário.

5.4 Protocolo experimental e plano de validação

Para assegurar a confiabilidade técnica e a aceitação humana do sistema, o protocolo experimental foi estruturado em duas frentes complementares:

Frente 1 – Avaliação Algorítmica e Validação às Cegas (Ground Truth): Para medir o poder discriminativo do algoritmo, será desenvolvido um conjunto de dados de teste contendo entre 30 e 50 cenários sintéticos controlados de adoção (pares adotante-animal) cobrindo perfis heterogêneos (desde alta compatibilidade até incompatibilidades sutis). Especialistas da área de bem-estar animal e protetores voluntários atuarão como avaliadores humanos às cegas, classificando cada par como 'Compatível' ou 'Incompatível'. A recomendação do sistema sob um limiar pré-estabelecido θ (ex.: Score ≥ 70) será comparada ao gabarito dos especialistas para construção da matriz de confusão e extração de Acurácia, Precisão, Revocação e F1-Score (POWERS, 2011; HERLOCKER et al., 2004), além da análise de sensibilidade em diferentes pontos de corte de θ.

Tabela 3 – Matriz de confusão planejada para avaliação da classificação algorítmica.

Classificação do Algoritmo

Classe Real: Adoção Compatível (Positiva)

Classe Real: Adoção Incompatível (Negativa)

Algoritmo Recomenda (Score ≥ θ)

Verdadeiro Positivo (VP)Recomendação correta e segura

Falso Positivo (FP)Risco crítico de devolução

Algoritmo Não Recomenda (Score < θ)

Falso Negativo (FN)Oportunidade de adoção perdida

Verdadeiro Negativo (VN)Incompatibilidade evitada com sucesso

Fonte: Elaborado pelo autor (2026).

Frente 2 – Avaliação de Usabilidade e Aceitação com Usuários Finais: O protótipo funcional do sistema é submetido a testes com usuários adotantes e voluntários de acolhimento. A coleta abarcará métricas observacionais de usabilidade (tempo médio de execução de tarefas, taxa de conclusão de cadastro sem suporte e ocorrência de erros na interpretação de resultados) fundamentadas em Nielsen (1993). Ao término das sessões, aplicar-se-á a escala SUS (BROOKE, 1996) para apuração do escore quantitativo de usabilidade e o questionário baseado no modelo TAM (DAVIS, 1989), composto por assertivas em escala Likert de 5 pontos, avaliando especificamente a Utilidade Percebida, a Facilidade de Uso e o grau de Confiança nas justificativas explicativas geradas pelo modelo LLM.

6 RESULTADOS ESPERADOS

A presente seção sintetiza a avaliação computacional dos modelos e os resultados alcançados pela arquitetura AdotaMatch:

1. Desempenho e Eficácia Computacional: Espera-se que a abordagem híbrida demonstre alta capacidade discriminativa frente à classificação de especialistas, mantendo F1-Score projetado superior a 0,80 sob o limiar calibrado. Especificamente, o modelo deve minimizar drasticamente a taxa de Falsos Positivos (FP), que representa o desfecho mais danoso no domínio de adoção (animais recomendados erroneamente a lares inaptos, gerando estresse e devolução).2. Riqueza e Confiabilidade da Explicabilidade: Espera-se comprovar que as explicações em linguagem natural geradas pela LLM proporcionem aos adotantes uma compreensão superior de seus deveres práticos em relação a filtros numéricos tradicionais. A ancoragem estrita dos prompts em parâmetros computacionais objetivos deve garantir índice nulo de alucinações factuais sobre as características do animal.3. Aceitação Tecnológica e Usabilidade: Almeja-se atingir pontuação SUS superior a 75 pontos (classificada como 'boa a excelente' na literatura) e índices médios de concordância superiores a 80% nos construtos de Utilidade e Confiança do modelo TAM, indicando que a ferramenta digital é intuitiva, acessível e capaz de engajar adotantes com diferentes níveis de literacia digital.4. Impacto Social Positivo: No plano ético e social, espera-se que o AdotaMatch forneça a protetores e pequenas ONGs uma ferramenta tecnológica robusta e gratuita para auxílio na triagem pré-adoção, organizando a tomada de decisão, reduzindo o desgaste emocional das equipes de acolhimento e atuando diretamente na diminuição das taxas de reabandono e maus-tratos a longo prazo.

7 CONSIDERAÇÕES FINAIS

O presente artigo formalizou a concepção e a modelagem do AdotaMatch, um sistema de recomendação híbrido para adoção responsável de animais de estimação que une a filtragem baseada em conteúdo clássica, restrições rígidas eliminatórias e o poder de interpretação e explicabilidade dos Modelos de Linguagem de Grande Porte (LLMs). O trabalho aborda uma demanda socioambiental urgente — a prevenção do reabandono e da devolução de animais acolhidos — tratando-a rigorosamente como um problema de Ciência da Computação que abrange engenharia de dados, ranqueamento multicritério, processamento de linguagem natural e explicabilidade algorítmica.

A principal contribuição deste trabalho reside na formulação da arquitetura híbrida e na integração das LLMs não apenas como ferramentas conversacionais periféricas, mas como componentes metodológicos de perfilamento contextual e geração de justificativas preventivas. A pesquisa afasta-se da fragilidade de filtros puramente determinísticos e supera a opacidade de sistemas de recomendação caixa-preta, promovendo transparência ética e conscientização.

Como trabalhos futuros e desdobramentos práticos, estabelece-se: (i) a implementação integral do protótipo funcional integrando o motor algorítmico e as interfaces interativas; (ii) o estabelecimento de parcerias com ONGs locais e protetores independentes para coleta de bases de validação; (iii) a execução do protocolo experimental às cegas com especialistas para apuração da matriz de confusão e calibração de pesos; e (iv) a aplicação presencial dos testes de usabilidade TAM e SUS com adotantes, consolidando a avaliação quantitativa e qualitativa do impacto da ferramenta.

REFERÊNCIAS

ADOMAVICIUS, G.; TUZHILIN, A. Toward the next generation of recommender systems: a survey of the state-of-the-art and possible extensions. IEEE Transactions on Knowledge and Data Engineering, v. 17, n. 6, p. 734-749, 2005. DOI: 10.1109/TKDE.2005.99.

AGGARWAL, Charu C. Recommender Systems: The Textbook. Cham: Springer International Publishing, 2016. DOI: 10.1007/978-3-319-29659-3.

BRASIL. Ministério do Meio Ambiente e Mudança do Clima. Programa Nacional de Manejo Populacional Ético de Cães e Gatos. Brasília: MMA, 2025. Disponível em: https://www.gov.br/mma/pt-br/composicao/sbio/dpda/programas-e-projetos/programa-nacional-de-manejo-populacional-etico-de-caes-e-gatos. Acesso em: 15 fev. 2026.

BROOKE, John. SUS: a quick and dirty usability scale. In: JORDAN, P. W. et al. (ed.). Usability Evaluation in Industry. London: Taylor & Francis, 1996. p. 189-194.

DAVIS, Fred D. Perceived usefulness, perceived ease of use, and user acceptance of information technology. MIS Quarterly, Minneapolis, v. 13, n. 3, p. 319-340, 1989. DOI: 10.2307/249008.

HAWES, Sloane M. et al. Factors informing the return of adopted dogs and cats to an animal shelter. Animals, Basel, v. 10, n. 9, p. 1573, 2020. DOI: 10.3390/ani10091573.

HERLOCKER, Jonathan L. et al. Evaluating collaborative filtering recommender systems. ACM Transactions on Information Systems, New York, v. 22, n. 1, p. 5-53, 2004. DOI: 10.1145/963770.963772.

LIN, Jianghao et al. How can recommender systems benefit from large language models: a survey. ACM Transactions on Information Systems, New York, v. 43, n. 2, p. 1-46, 2025. DOI: 10.1145/3678004.

LOPS, Pasquale; DE GEMMIS, Marco; SEMERARO, Giovanni. Content-based recommender systems: state of the art and trends. In: RICCI, F. et al. (ed.). Recommender Systems Handbook. Boston: Springer, 2011. p. 73-105. DOI: 10.1007/978-0-387-85820-3_3.

NIELSEN, Jakob. Usability Engineering. Boston: Academic Press, 1993.

POWERS, David M. W. Evaluation: from precision, recall and F-measure to ROC, informedness, markedness and correlation. Journal of Machine Learning Technologies, v. 2, n. 1, p. 37-63, 2011.

PROTOPOPOVA, Alexandra; GUNTER, Lisa M. Adoption and relinquishment interventions at the animal shelter: a review. Animal Welfare, Wheathampstead, v. 26, n. 1, p. 35-48, 2017. DOI: 10.7120/09627286.26.1.035.

RICCI, Francesco; ROKACH, Lior; SHAPIRA, Bracha. Recommender Systems Handbook. 2. ed. New York: Springer, 2015. DOI: 10.1007/978-1-4899-7637-6.

SANTOS, Monaliza Carvalho et al. Um sistema de recomendação baseado em conteúdo para adoção de animais utilizando a técnica do cosseno ponderado. Texto Livre, Belo Horizonte, v. 17, p. e42708, 2024. DOI: 10.1590/1983-3652.2024.42708.

SOMMERVILLE, Ian. Engenharia de software. 9. ed. São Paulo: Pearson Prentice Hall, 2011.

TINTAREV, Nava; MASTHOFF, Judith. A survey of explanations in recommender systems. In: IEEE INTERNATIONAL CONFERENCE ON DATA ENGINEERING WORKSHOP, 2007, Istanbul. Proceedings [...]. Piscataway: IEEE, 2007. p. 801-810. DOI: 10.1109/ICDEW.2007.4401070.

WU, Likang et al. A survey on large language models for recommendation. World Wide Web, Cham, v. 27, n. 5, art. 60, p. 1-35, 2024. DOI: 10.1007/s11280-024-01291-2.

ZHANG, Kaylee; ZHANG, Sherine. PetFinder challenge: predicting pet adoption speed. Stanford: Stanford University (CS229 Machine Learning Project Report), 2019. Disponível em: https://cs229.stanford.edu/proj2019aut/data/assignment_308832_raw/26647754.pdf. Acesso em: 15 fev. 2026.

ZHANG, Yongfeng; CHEN, Xu. Explainable recommendation: a survey and new perspectives. Foundations and Trends in Information Retrieval, Boston, v. 14, n. 1, p. 1-101, 2020. DOI: 10.1561/1500000066.

APÊNDICE A – INSTRUMENTOS DE COLETA E PROTOCOLO DE AVALIAÇÃO

Este apêndice delineia a especificação conceitual dos instrumentos metodológicos utilizados para levantamento e avaliação:

Instrumento 1 – Formulário de Perfil do Adotante (Camada de Ingestão)

1. Dados Estruturados: tipo de residência (Apartamento, Casa sem quintal, Casa com quintal murado, Chácara/Sítio); porte desejado (Pequeno, Médio, Grande, Indiferente); faixa etária preferencial; horas diárias disponíveis para dedicação direta (1 a 8+); composição do lar (presença de crianças menores de 10 anos, idosos, outros cães ou gatos); nível de experiência prévia com animais de estimação (Iniciante, Intermediário, Experiente); disponibilidade para arcar com cuidados veterinários contínuos.2. Relato Contextual em Texto Livre: campo aberto com orientação ao adotante: 'Descreva livremente como é sua rotina diária de trabalho, lazer e viagens, e o que você mais valoriza no comportamento do animal que deseja adotar'.

Instrumento 2 – Ficha Cadastral e Comportamental do Animal (Abrigo / Protetores)

1. Dados Estruturados: espécie (Canina, Felina); nome e identificação; idade estimada; porte físico; histórico de castração e vacinação; nível estimado de energia (escala de 1 a 5); tolerância conhecida a crianças e a outros animais (Sim, Não, Requer socialização/adaptação); tolerância a períodos de permanência solitária (horas máximas recomendadas); presença de condições de saúde ou cuidados especiais crônicos.2. Diário Comportamental do Cuidador: campo aberto com orientação: 'Descreva a personalidade do animal, seus medos conhecidos (ruídos, trovões), hábitos de convivência e particularidades observadas durante o período de acolhimento no abrigo'.

Instrumento 3 – Protocolo de Avaliação de Usabilidade e Aceitação (TAM e SUS)

1. Questionário TAM (Escala Likert de 1 - Discordo Totalmente a 5 - Concordo Totalmente):  - UP1: 'O sistema me ajudou a identificar com clareza animais que realmente combinam com meu estilo de vida.'  - UP2: 'As justificativas e alertas apresentados aumentaram minha segurança sobre a decisão de adotar.'  - UP3: 'Acredito que a ferramenta reduz o risco de adoções incompatíveis e devoluções.'  - FU1: 'Achei fácil preencher minhas informações e compreender o resultado gerado.'  - FU2: 'A linguagem das explicações geradas foi clara, transparente e acessível.'  - CONF1: 'Confio nos critérios e nas recomendações de compatibilidade sugeridas pelo sistema.'2. Escala SUS (System Usability Scale): aplicação das 10 questões normatizadas por Brooke (1996) para cômputo da pontuação global padronizada de usabilidade (0 a 100 pontos).