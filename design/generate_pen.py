# -*- coding: utf-8 -*-
"""
Script de geração da modelagem visual em JSON do arquivo .pen do Design System AdotaMatch.
Organiza Tipografia, Paleta de Cores (PrimeVue Aura + Tailwind) e Componentes do Core de Recomendação.
"""

import json

def build_pen_document():
    doc = {
        "version": "1.0",
        "name": "AdotaMatch - Design System, Tipografia e Paleta de Cores (PrimeVue)",
        "viewport": {
            "x": 0,
            "y": 0,
            "width": 2400,
            "height": 1800,
            "backgroundColor": "#f8fafc"
        },
        "nodes": []
    }

    nodes = doc["nodes"]

    # ----------------------------------------------------
    # 1. HEADER DO DESIGN SYSTEM
    # ----------------------------------------------------
    nodes.append({
        "id": "header_container",
        "name": "Header do Design System",
        "type": "frame",
        "x": 60,
        "y": 40,
        "width": 2280,
        "height": 130,
        "backgroundColor": "#ffffff",
        "borderRadius": 16,
        "borderColor": "#e2e8f0",
        "borderWidth": 1,
        "children": [
            {
                "id": "header_brand_badge",
                "name": "Brand Badge",
                "type": "rectangle",
                "x": 90,
                "y": 65,
                "width": 140,
                "height": 32,
                "backgroundColor": "#eef2ff",
                "borderRadius": 8,
                "borderColor": "#c7d2fe",
                "borderWidth": 1
            },
            {
                "id": "header_badge_text",
                "name": "Badge Text",
                "type": "text",
                "x": 105,
                "y": 72,
                "text": "AdotaMatch UI",
                "fontFamily": "Inter, sans-serif",
                "fontSize": 12,
                "fontWeight": 700,
                "color": "#4f46e5"
            },
            {
                "id": "header_title",
                "name": "Título Principal",
                "type": "text",
                "x": 250,
                "y": 62,
                "text": "Design System, Tipografia & Paleta de Cores (PrimeVue Aura + Tailwind)",
                "fontFamily": "Outfit, sans-serif",
                "fontSize": 26,
                "fontWeight": 800,
                "color": "#0f172a"
            },
            {
                "id": "header_subtitle",
                "name": "Subtítulo e Contexto",
                "type": "text",
                "x": 250,
                "y": 105,
                "text": "Modelagem visual dos tokens de interface, escala tipográfica, componentes PrimeVue e sistema de explicabilidade contra devoluções de animais.",
                "fontFamily": "Inter, sans-serif",
                "fontSize": 14,
                "fontWeight": 400,
                "color": "#64748b"
            }
        ]
    })

    # ----------------------------------------------------
    # 2. COLUNA DA ESQUERDA: TIPOGRAFIA (x: 60, y: 190, w: 1100, h: 1530)
    # ----------------------------------------------------
    typo_children = [
        # Título da Seção
        {
            "id": "typo_title",
            "name": "Título da Seção de Tipografia",
            "type": "text",
            "x": 90,
            "y": 220,
            "text": "1. Escala Tipográfica & Famílias de Fontes",
            "fontFamily": "Outfit, sans-serif",
            "fontSize": 20,
            "fontWeight": 700,
            "color": "#0f172a"
        },
        {
            "id": "typo_desc",
            "name": "Descrição da Tipografia",
            "type": "text",
            "x": 90,
            "y": 250,
            "text": "Tipografia oficial: 'Inter' (UI, leitura, inputs PrimeVue), 'Outfit' (títulos e score) e 'JetBrains Mono' (código/tokens).",
            "fontFamily": "Inter, sans-serif",
            "fontSize": 13,
            "fontWeight": 400,
            "color": "#64748b"
        },
        # Cabeçalho da Tabela Tipográfica
        {
            "id": "typo_table_header",
            "name": "Header da Tabela Tipográfica",
            "type": "rectangle",
            "x": 90,
            "y": 285,
            "width": 1040,
            "height": 40,
            "backgroundColor": "#f1f5f9",
            "borderRadius": 8
        },
        {
            "id": "th_level", "type": "text", "x": 110, "y": 298, "text": "Nível / Função",
            "fontFamily": "Inter, sans-serif", "fontSize": 12, "fontWeight": 700, "color": "#334155"
        },
        {
            "id": "th_specs", "type": "text", "x": 260, "y": 298, "text": "Especificação Técnica (px / rem / line-height / peso)",
            "fontFamily": "Inter, sans-serif", "fontSize": 12, "fontWeight": 700, "color": "#334155"
        },
        {
            "id": "th_usage", "type": "text", "x": 680, "y": 298, "text": "Aplicação no AdotaMatch",
            "fontFamily": "Inter, sans-serif", "fontSize": 12, "fontWeight": 700, "color": "#334155"
        }
    ]

    # Lista de Itens Tipográficos
    type_scales = [
        {
            "name": "Display Hero",
            "specs": "48px / 3.00rem | Line: 1.15 | Weight: 800",
            "usage": "Títulos de impacto da Hero Section e % de compatibilidade",
            "sample": "Adoção Consciente e Inteligente",
            "family": "Outfit, sans-serif", "size": 40, "weight": 800, "color": "#1e1b4b"
        },
        {
            "name": "Heading 1 (H1)",
            "specs": "36px / 2.25rem | Line: 1.20 | Weight: 700",
            "usage": "Títulos de páginas principais (/match, /pets)",
            "sample": "Encontre o Animal Ideal para sua Rotina",
            "family": "Outfit, sans-serif", "size": 30, "weight": 700, "color": "#0f172a"
        },
        {
            "name": "Heading 2 (H2)",
            "specs": "28px / 1.75rem | Line: 1.25 | Weight: 700",
            "usage": "Seções de formulário e blocos de conteúdo",
            "sample": "Perfil do Adotante e Estilo de Vida",
            "family": "Outfit, sans-serif", "size": 24, "weight": 700, "color": "#0f172a"
        },
        {
            "name": "Heading 3 (H3)",
            "specs": "22px / 1.375rem | Line: 1.30 | Weight: 600",
            "usage": "Nome do animal no MatchCard e títulos de modais",
            "sample": "Caramelo — Macho, Porte Médio",
            "family": "Inter, sans-serif", "size": 20, "weight": 600, "color": "#1e293b"
        },
        {
            "name": "Heading 4 (H4)",
            "specs": "18px / 1.125rem | Line: 1.35 | Weight: 600",
            "usage": "Subtítulos internos e agrupadores de campos",
            "sample": "Fatores de Afinidade & Alertas de Manejo",
            "family": "Inter, sans-serif", "size": 17, "weight": 600, "color": "#334155"
        },
        {
            "name": "Body Large",
            "specs": "18px / 1.125rem | Line: 1.60 | Weight: 400",
            "usage": "Parágrafos introdutórios e chamadas explicativas",
            "sample": "Nosso algoritmo híbrido cruza suas variáveis com o relato em texto livre para mitigar devoluções.",
            "family": "Inter, sans-serif", "size": 16, "weight": 400, "color": "#475569"
        },
        {
            "name": "Body Regular (Base)",
            "specs": "16px / 1.00rem | Line: 1.50 | Weight: 400",
            "usage": "Texto padrão, inputs PrimeVue (Select, Textarea), botões",
            "sample": "Trabalho em regime de home office 3x por semana e procuro um cão dócil para caminhadas.",
            "family": "Inter, sans-serif", "size": 15, "weight": 400, "color": "#1e293b"
        },
        {
            "name": "Body Small",
            "specs": "14px / 0.875rem | Line: 1.45 | Weight: 400 / 500",
            "usage": "Descrições de cards, justificativas e mensagens de validação",
            "sample": "Atenção: este animal requer enriquecimento ambiental para adaptação à permanência solitária.",
            "family": "Inter, sans-serif", "size": 13, "weight": 400, "color": "#64748b"
        },
        {
            "name": "Caption / Tag",
            "specs": "12px / 0.75rem | Line: 1.30 | Weight: 600 / 700",
            "usage": "Tags de espécie, porte, selos de status e metadados",
            "sample": "COMPATIBILIDADE: 88% | SOB CUSTÓDIA: ONG AMOR DE PATAS",
            "family": "Inter, sans-serif", "size": 11, "weight": 700, "color": "#4f46e5"
        },
        {
            "name": "Code / Data Spec",
            "specs": "13px / 0.812rem | Line: 1.40 | Weight: 500",
            "usage": "JSON Schema da LLM, tokens e dados técnicos",
            "sample": "{ \"semanticScore\": 0.90, \"alignmentFactors\": [\"Home office favorável\"] }",
            "family": "JetBrains Mono, monospace", "size": 12, "weight": 500, "color": "#047857"
        }
    ]

    current_y = 345
    for idx, item in enumerate(type_scales):
        box_y = current_y
        typo_children.append({
            "id": f"type_box_{idx}",
            "name": f"Box {item['name']}",
            "type": "rectangle",
            "x": 90,
            "y": box_y,
            "width": 1040,
            "height": 95,
            "backgroundColor": "#ffffff" if idx % 2 == 0 else "#f8fafc",
            "borderRadius": 8,
            "borderColor": "#e2e8f0",
            "borderWidth": 1
        })
        # Rótulo Nível
        typo_children.append({
            "id": f"type_name_{idx}",
            "type": "text", "x": 110, "y": box_y + 14,
            "text": item["name"],
            "fontFamily": "Inter, sans-serif", "fontSize": 14, "fontWeight": 700, "color": "#0f172a"
        })
        # Specs Técnicas
        typo_children.append({
            "id": f"type_spec_{idx}",
            "type": "text", "x": 260, "y": box_y + 14,
            "text": item["specs"],
            "fontFamily": "JetBrains Mono, monospace", "fontSize": 11, "fontWeight": 500, "color": "#6366f1"
        })
        # Aplicação
        typo_children.append({
            "id": f"type_usage_{idx}",
            "type": "text", "x": 680, "y": box_y + 14,
            "text": item["usage"],
            "fontFamily": "Inter, sans-serif", "fontSize": 11, "fontWeight": 500, "color": "#64748b"
        })
        # Amostra Visual
        typo_children.append({
            "id": f"type_sample_{idx}",
            "type": "text", "x": 110, "y": box_y + 45,
            "text": item["sample"],
            "fontFamily": item["family"], "fontSize": item["size"], "fontWeight": item["weight"], "color": item["color"]
        })
        current_y += 110

    nodes.append({
        "id": "typography_frame",
        "name": "Frame de Tipografia",
        "type": "frame",
        "x": 60,
        "y": 190,
        "width": 1100,
        "height": 1530,
        "backgroundColor": "#ffffff",
        "borderRadius": 16,
        "borderColor": "#cbd5e1",
        "borderWidth": 1,
        "children": typo_children
    })

    # ----------------------------------------------------
    # 3. COLUNA DA DIREITA - SUPERIOR: PALETA DE CORES (x: 1200, y: 190, w: 1140, h: 780)
    # ----------------------------------------------------
    color_children = [
        {
            "id": "color_title",
            "name": "Título Cores",
            "type": "text", "x": 1230, "y": 220,
            "text": "2. Paleta de Cores & Tokens Semânticos (PrimeVue Aura + Tailwind)",
            "fontFamily": "Outfit, sans-serif", "fontSize": 20, "fontWeight": 700, "color": "#0f172a"
        },
        {
            "id": "color_desc",
            "name": "Descrição Cores",
            "type": "text", "x": 1230, "y": 250,
            "text": "Esquema cromático intencional: Índigo (Conexão e Confiança), Esmeralda (Afinidade), Âmbar (Alertas de Manejo) e Slate (Superfícies).",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 400, "color": "#64748b"
        }
    ]

    palettes = [
        {
            "name": "Primary Brand (Índigo — Identidade, Ações Principais e Inteligência)",
            "y": 290,
            "swatches": [
                {"name": "50", "hex": "#eef2ff", "text_color": "#312e81"},
                {"name": "100", "hex": "#e0e7ff", "text_color": "#312e81"},
                {"name": "200", "hex": "#c7d2fe", "text_color": "#1e1b4b"},
                {"name": "300", "hex": "#a5b4fc", "text_color": "#1e1b4b"},
                {"name": "400", "hex": "#818cf8", "text_color": "#ffffff"},
                {"name": "500 (Base)", "hex": "#6366f1", "text_color": "#ffffff"},
                {"name": "600 (Action)", "hex": "#4f46e5", "text_color": "#ffffff"},
                {"name": "700", "hex": "#4338ca", "text_color": "#ffffff"},
                {"name": "800", "hex": "#3730a3", "text_color": "#ffffff"},
                {"name": "900", "hex": "#312e81", "text_color": "#ffffff"}
            ]
        },
        {
            "name": "Success / Affinity (Esmeralda — Fatores Positivos de Compatibilidade)",
            "y": 415,
            "swatches": [
                {"name": "50 (Bg Card)", "hex": "#ecfdf5", "text_color": "#064e3b"},
                {"name": "100", "hex": "#d1fae5", "text_color": "#064e3b"},
                {"name": "200 (Border)", "hex": "#a7f3d0", "text_color": "#064e3b"},
                {"name": "500 (Icon)", "hex": "#10b981", "text_color": "#ffffff"},
                {"name": "600", "hex": "#059669", "text_color": "#ffffff"},
                {"name": "700 (Texto)", "hex": "#047857", "text_color": "#ffffff"},
                {"name": "800", "hex": "#065f46", "text_color": "#ffffff"},
                {"name": "900", "hex": "#064e3b", "text_color": "#ffffff"}
            ]
        },
        {
            "name": "Warning / Preventive Alerts (Âmbar — Conscientização contra Devolução)",
            "y": 540,
            "swatches": [
                {"name": "50 (Bg Alert)", "hex": "#fffbeb", "text_color": "#78350f"},
                {"name": "100", "hex": "#fef3c7", "text_color": "#78350f"},
                {"name": "200 (Border)", "hex": "#fde68a", "text_color": "#78350f"},
                {"name": "500 (Icon)", "hex": "#f59e0b", "text_color": "#ffffff"},
                {"name": "600", "hex": "#d97706", "text_color": "#ffffff"},
                {"name": "700 (Texto)", "hex": "#b45309", "text_color": "#ffffff"},
                {"name": "800", "hex": "#92400e", "text_color": "#ffffff"},
                {"name": "900", "hex": "#78350f", "text_color": "#ffffff"}
            ]
        },
        {
            "name": "Neutrals & Surfaces (Slate — Telas, Bordas e Contraste Acessível)",
            "y": 665,
            "swatches": [
                {"name": "50 (Canvas)", "hex": "#f8fafc", "text_color": "#0f172a"},
                {"name": "100 (Bg Sec)", "hex": "#f1f5f9", "text_color": "#0f172a"},
                {"name": "200 (Border)", "hex": "#e2e8f0", "text_color": "#0f172a"},
                {"name": "400 (Muted)", "hex": "#94a3b8", "text_color": "#0f172a"},
                {"name": "500", "hex": "#64748b", "text_color": "#ffffff"},
                {"name": "600 (Text Sec)", "hex": "#475569", "text_color": "#ffffff"},
                {"name": "700", "hex": "#334155", "text_color": "#ffffff"},
                {"name": "800", "hex": "#1e293b", "text_color": "#ffffff"},
                {"name": "900 (Text Pri)", "hex": "#0f172a", "text_color": "#ffffff"}
            ]
        }
    ]

    for p_idx, pal in enumerate(palettes):
        p_y = pal["y"]
        color_children.append({
            "id": f"pal_label_{p_idx}",
            "type": "text", "x": 1230, "y": p_y,
            "text": pal["name"],
            "fontFamily": "Inter, sans-serif", "fontSize": 14, "fontWeight": 700, "color": "#1e293b"
        })
        start_x = 1230
        for s_idx, swatch in enumerate(pal["swatches"]):
            sw_x = start_x + (s_idx * 105)
            color_children.append({
                "id": f"swatch_{p_idx}_{s_idx}",
                "type": "rectangle",
                "x": sw_x,
                "y": p_y + 25,
                "width": 95,
                "height": 55,
                "backgroundColor": swatch["hex"],
                "borderRadius": 8,
                "borderColor": "#cbd5e1",
                "borderWidth": 1
            })
            color_children.append({
                "id": f"sw_name_{p_idx}_{s_idx}",
                "type": "text", "x": sw_x + 8, "y": p_y + 32,
                "text": swatch["name"],
                "fontFamily": "Inter, sans-serif", "fontSize": 10, "fontWeight": 600, "color": swatch["text_color"]
            })
            color_children.append({
                "id": f"sw_hex_{p_idx}_{s_idx}",
                "type": "text", "x": sw_x + 8, "y": p_y + 62,
                "text": swatch["hex"],
                "fontFamily": "JetBrains Mono, monospace", "fontSize": 9, "fontWeight": 500, "color": swatch["text_color"]
            })

    nodes.append({
        "id": "colors_frame",
        "name": "Frame de Paleta de Cores",
        "type": "frame",
        "x": 1200,
        "y": 190,
        "width": 1140,
        "height": 600,
        "backgroundColor": "#ffffff",
        "borderRadius": 16,
        "borderColor": "#cbd5e1",
        "borderWidth": 1,
        "children": color_children
    })

    # ----------------------------------------------------
    # 4. COLUNA DA DIREITA - INFERIOR: COMPONENTES PRIMEVUE MODELADOS (x: 1200, y: 810, w: 1140, h: 910)
    # ----------------------------------------------------
    comp_children = [
        {
            "id": "comp_title",
            "name": "Título Componentes",
            "type": "text", "x": 1230, "y": 835,
            "text": "3. Componentes PrimeVue Modelados para o Core de Recomendação",
            "fontFamily": "Outfit, sans-serif", "fontSize": 20, "fontWeight": 700, "color": "#0f172a"
        },
        {
            "id": "comp_desc",
            "name": "Descrição Componentes",
            "type": "text", "x": 1230, "y": 865,
            "text": "Implementação dos componentes do ecossistema: Botões, Tags de Atributos e o MatchCard completo com Explicabilidade Preventiva.",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 400, "color": "#64748b"
        },
        # --- SUBSEÇÃO A: BOTÕES PRIMEVUE ---
        {
            "id": "lbl_buttons", "type": "text", "x": 1230, "y": 905,
            "text": "A. Botões (PrimeVue Button + Tailwind)",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 700, "color": "#334155"
        },
        # Botão Primário
        {
            "id": "btn_primary", "type": "rectangle", "x": 1230, "y": 930, "width": 260, "height": 44,
            "backgroundColor": "#4f46e5", "borderRadius": 10
        },
        {
            "id": "btn_primary_text", "type": "text", "x": 1260, "y": 943,
            "text": "Calcular Recomendações (Action)",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 600, "color": "#ffffff"
        },
        # Botão Sucesso / Contato
        {
            "id": "btn_success", "type": "rectangle", "x": 1510, "y": 930, "width": 240, "height": 44,
            "backgroundColor": "#059669", "borderRadius": 10
        },
        {
            "id": "btn_success_text", "type": "text", "x": 1535, "y": 943,
            "text": "Manifestar Interesse Responsável",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 600, "color": "#ffffff"
        },
        # Botão Outlined / Secundário
        {
            "id": "btn_outline", "type": "rectangle", "x": 1770, "y": 930, "width": 170, "height": 44,
            "backgroundColor": "#ffffff", "borderRadius": 10, "borderColor": "#cbd5e1", "borderWidth": 1
        },
        {
            "id": "btn_outline_text", "type": "text", "x": 1805, "y": 943,
            "text": "Refazer Consulta",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 600, "color": "#334155"
        },
        # --- SUBSEÇÃO B: TAGS PRIMEVUE ---
        {
            "id": "lbl_tags", "type": "text", "x": 1230, "y": 995,
            "text": "B. Tags & Badges de Metadados (PrimeVue Tag)",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 700, "color": "#334155"
        },
        # Tag Cão
        {
            "id": "tag_dog", "type": "rectangle", "x": 1230, "y": 1020, "width": 80, "height": 28,
            "backgroundColor": "#f1f5f9", "borderRadius": 14, "borderColor": "#cbd5e1", "borderWidth": 1
        },
        {
            "id": "tag_dog_text", "type": "text", "x": 1248, "y": 1026, "text": "Cão",
            "fontFamily": "Inter, sans-serif", "fontSize": 12, "fontWeight": 600, "color": "#334155"
        },
        # Tag Gato
        {
            "id": "tag_cat", "type": "rectangle", "x": 1320, "y": 1020, "width": 80, "height": 28,
            "backgroundColor": "#f1f5f9", "borderRadius": 14, "borderColor": "#cbd5e1", "borderWidth": 1
        },
        {
            "id": "tag_cat_text", "type": "text", "x": 1338, "y": 1026, "text": "Gato",
            "fontFamily": "Inter, sans-serif", "fontSize": 12, "fontWeight": 600, "color": "#334155"
        },
        # Tag Porte Médio
        {
            "id": "tag_medium", "type": "rectangle", "x": 1410, "y": 1020, "width": 110, "height": 28,
            "backgroundColor": "#e0f2fe", "borderRadius": 14, "borderColor": "#bae6fd", "borderWidth": 1
        },
        {
            "id": "tag_medium_text", "type": "text", "x": 1424, "y": 1026, "text": "Porte Médio",
            "fontFamily": "Inter, sans-serif", "fontSize": 12, "fontWeight": 600, "color": "#0369a1"
        },
        # Tag Alta Compatibilidade
        {
            "id": "tag_score", "type": "rectangle", "x": 1530, "y": 1020, "width": 170, "height": 28,
            "backgroundColor": "#ecfdf5", "borderRadius": 14, "borderColor": "#a7f3d0", "borderWidth": 1
        },
        {
            "id": "tag_score_text", "type": "text", "x": 1542, "y": 1026, "text": "Alta Afinidade (88%)",
            "fontFamily": "Inter, sans-serif", "fontSize": 12, "fontWeight": 700, "color": "#047857"
        },
        # --- SUBSEÇÃO C: CARD DE RECOMENDAÇÃO MODELADO (MATCHCARD) ---
        {
            "id": "lbl_card_model", "type": "text", "x": 1230, "y": 1070,
            "text": "C. Card de Recomendação com Explicabilidade Preventiva (MatchCard)",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 700, "color": "#334155"
        },
        # Container do MatchCard
        {
            "id": "matchcard_frame", "type": "rectangle", "x": 1230, "y": 1095, "width": 1080, "height": 580,
            "backgroundColor": "#ffffff", "borderRadius": 16, "borderColor": "#e2e8f0", "borderWidth": 1
        },
        # Topo do Card
        {
            "id": "mc_avatar", "type": "rectangle", "x": 1260, "y": 1120, "width": 64, "height": 64,
            "backgroundColor": "#e0e7ff", "borderRadius": 32
        },
        {
            "id": "mc_avatar_letter", "type": "text", "x": 1283, "y": 1135, "text": "C",
            "fontFamily": "Outfit, sans-serif", "fontSize": 26, "fontWeight": 800, "color": "#4338ca"
        },
        {
            "id": "mc_pet_name", "type": "text", "x": 1340, "y": 1125, "text": "Caramelo",
            "fontFamily": "Outfit, sans-serif", "fontSize": 24, "fontWeight": 800, "color": "#0f172a"
        },
        {
            "id": "mc_pet_meta", "type": "text", "x": 1340, "y": 1160,
            "text": "Cão | Porte Médio | 2 anos estimados | Sob custódia: ONG Amor de Patas (Ubá - MG)",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 500, "color": "#64748b"
        },
        # Selo do Score
        {
            "id": "mc_score_box", "type": "rectangle", "x": 2130, "y": 1120, "width": 150, "height": 64,
            "backgroundColor": "#f8fafc", "borderRadius": 12, "borderColor": "#e2e8f0", "borderWidth": 1
        },
        {
            "id": "mc_score_val", "type": "text", "x": 2155, "y": 1128, "text": "88%",
            "fontFamily": "Outfit, sans-serif", "fontSize": 26, "fontWeight": 900, "color": "#4f46e5"
        },
        {
            "id": "mc_score_lbl", "type": "text", "x": 2145, "y": 1162, "text": "Compatibilidade",
            "fontFamily": "Inter, sans-serif", "fontSize": 10, "fontWeight": 600, "color": "#94a3b8"
        },
        # Banner de Fatores de Afinidade (Verde)
        {
            "id": "mc_banner_success", "type": "rectangle", "x": 1260, "y": 1205, "width": 1020, "height": 140,
            "backgroundColor": "#ecfdf5", "borderRadius": 12, "borderColor": "#a7f3d0", "borderWidth": 1
        },
        {
            "id": "mc_bs_title", "type": "text", "x": 1280, "y": 1220,
            "text": "✓ Fatores de Afinidade (Por que este animal combina com sua rotina):",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 700, "color": "#065f46"
        },
        {
            "id": "mc_bs_item1", "type": "text", "x": 1300, "y": 1250,
            "text": "• Sua rotina flexível com home office oferece a presença constante ideal para o temperamento dócil de Caramelo.",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 400, "color": "#064e3b"
        },
        {
            "id": "mc_bs_item2", "type": "text", "x": 1300, "y": 1280,
            "text": "• Seu interesse declarado por caminhadas diárias harmoniza-se com a energia moderada (nível 3) do animal.",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 400, "color": "#064e3b"
        },
        {
            "id": "mc_bs_item3", "type": "text", "x": 1300, "y": 1310,
            "text": "• O animal possui histórico documentado de excelente paciência e afeto com crianças no abrigo.",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 400, "color": "#064e3b"
        },
        # Banner de Alertas Preventivos (Âmbar)
        {
            "id": "mc_banner_warning", "type": "rectangle", "x": 1260, "y": 1360, "width": 1020, "height": 140,
            "backgroundColor": "#fffbeb", "borderRadius": 12, "borderColor": "#fde68a", "borderWidth": 1
        },
        {
            "id": "mc_bw_title", "type": "text", "x": 1280, "y": 1375,
            "text": "⚠ Alertas de Manejo e Desafios de Adaptação (Prevenção Ativa de Devolução):",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 700, "color": "#92400e"
        },
        {
            "id": "mc_bw_item1", "type": "text", "x": 1300, "y": 1405,
            "text": "• Requer enriquecimento ambiental e mordedores nas primeiras semanas para adaptação a períodos sozinho sem latidos de ansiedade.",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 400, "color": "#78350f"
        },
        {
            "id": "mc_bw_item2", "type": "text", "x": 1300, "y": 1435,
            "text": "• Apresenta sensibilidade moderada a trovões e ruídos altos; reserve espaço protegido dentro de casa.",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 400, "color": "#78350f"
        },
        {
            "id": "mc_bw_item3", "type": "text", "x": 1300, "y": 1465,
            "text": "• Como em toda adoção, reserve os primeiros 30 dias para o período natural de adaptação e criação de vínculo afetivo.",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 400, "color": "#78350f"
        },
        # Rodapé do MatchCard com Botão
        {
            "id": "mc_btn_action", "type": "rectangle", "x": 1980, "y": 1520, "width": 300, "height": 44,
            "backgroundColor": "#4f46e5", "borderRadius": 10
        },
        {
            "id": "mc_btn_action_txt", "type": "text", "x": 2010, "y": 1533,
            "text": "Manifestar Interesse Responsável",
            "fontFamily": "Inter, sans-serif", "fontSize": 13, "fontWeight": 600, "color": "#ffffff"
        }
    ]

    nodes.append({
        "id": "components_frame",
        "name": "Frame de Componentes PrimeVue",
        "type": "frame",
        "x": 1200,
        "y": 810,
        "width": 1140,
        "height": 910,
        "backgroundColor": "#ffffff",
        "borderRadius": 16,
        "borderColor": "#cbd5e1",
        "borderWidth": 1,
        "children": comp_children
    })

    return doc

if __name__ == "__main__":
    doc = build_pen_document()
    output_path = r"adotamatch\design\adotamatch-design-system.pen"
    with open(output_path, "w", encoding="utf-8") as f:
        json.dump(doc, f, indent=2, ensure_ascii=False)
    print(f"Arquivo .pen gerado com sucesso em: {output_path}")
