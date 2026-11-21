"""Executa os cenários sintéticos do ensaio funcional do AdotaMatch."""
import json
import sys
from urllib.request import Request, urlopen

BASE_URL = sys.argv[1].rstrip("/")
CATALOG = {"Caramelo", "Luna", "Thor", "Mia", "Frajola", "Pipoca"}
DOGS = {"Caramelo", "Luna", "Thor", "Pipoca"}
FAMILY_DOGS = DOGS - {"Thor"}
CATS = CATALOG - DOGS

CASES = [
    ("P1 — sem restrições de convívio", "ANY", "HOUSE_YARD", 6, False, False, CATALOG),
    ("P2 — preferência por gatos", "CAT", "HOUSE_YARD", 6, False, False, CATS),
    ("P3 — cães em apartamento", "DOG", "APARTMENT", 6, False, False, FAMILY_DOGS),
    ("P4 — lar com crianças e pets", "DOG", "HOUSE_YARD", 6, True, True, FAMILY_DOGS),
    ("P5 — cães, tempo diário reduzido", "DOG", "HOUSE_YARD", 1, False, False, DOGS),
]
SPECIES = {"ANY": "Qualquer", "DOG": "Cão", "CAT": "Gato"}
HOUSING = {"HOUSE_YARD": "Casa com quintal", "APARTMENT": "Apartamento"}

def read_json(path, body=None):
    request = Request(
        BASE_URL + path,
        data=json.dumps(body).encode("utf-8") if body is not None else None,
        headers={"Content-Type": "application/json"},
    )
    with urlopen(request, timeout=90) as response:
        return json.load(response)

catalog = read_json("/api/v1/pets")
assert {pet["name"] for pet in catalog} == CATALOG
assert all(not pet["contactWhatsapp"] for pet in catalog)
results = []
for label, species, housing, hours, children, pets, expected in CASES:
    body = {
        "speciesPreference": species,
        "housingType": housing,
        "dailyHoursAvailable": hours,
        "hasChildren": children,
        "hasOtherPets": pets,
        "experienceLevel": 2,
        "lifestyleNarrative": "Trabalho presencial.",
    }
    matches = read_json("/api/v1/recommendations/match", body)
    names = [match["petName"] for match in matches]
    scores = [match["compatibilityScore"] for match in matches]
    assert len(names) == len(expected) and set(names) == expected, (label, names)
    assert all(0 <= score <= 100 for score in scores) and scores == sorted(scores, reverse=True), (label, scores)
    assert all(match["alignmentFactors"] and match["preventiveWarnings"] for match in matches), label
    results.append({
        "label": label, "species": SPECIES[species], "housing": HOUSING[housing],
        "count": len(matches), "first": names[0], "score": scores[0],
        "request": body,
        "ranking": [{"name": name, "score": score} for name, score in zip(names, scores, strict=True)],
    })
print(json.dumps({"catalogSize": len(catalog), "cases": results}, ensure_ascii=False, indent=2))
