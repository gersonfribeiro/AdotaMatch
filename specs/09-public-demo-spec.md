# Demonstração pública do AdotaMatch

A demonstração permite à banca executar o recomendador descrito no artigo com o catálogo de exemplo do projeto. O formulário, a pontuação ponderada, as restrições rígidas, os fatores de afinidade e os alertas de adaptação integram o fluxo público.

- O serviço Docker publica a interface e a API no mesmo endereço HTTPS. O Spring Boot serve os arquivos gerados pelo Vite.
- O perfil `demo` usa H2 em memória e repõe o catálogo a cada inicialização. Os contatos e as contas de exemplo ficam fora da carga nesse perfil.
- A interface pública oferece o catálogo e o questionário. A API pública aceita a leitura do catálogo e o cálculo de recomendações. A demonstração dispensa cadastro e operações de adoção.
- O termo semântico da pontuação usa a heurística local já implementada. Uma avaliação com inferência externa deve registrar o provedor e a configuração usados.
- A publicação no Render usa o plano Free e a checagem de saúde consulta o catálogo.

## Verificação

Abra `/match` diretamente, execute o questionário e confira a ordenação, os fatores de afinidade e os alertas. Abra `/pets` e verifique o catálogo de exemplo. Confira também que operações de cadastro e alteração de animais ficam indisponíveis.

O ensaio funcional do artigo usa os seis animais do catálogo e cinco perfis sintéticos. Para reproduzi-lo com Python 3.10 ou superior, execute `python specs/ensaio_adotamatch.py https://ENDERECO-DA-DEMO`. O script verifica os candidatos elegíveis, a ordem e os limites dos escores e a presença das explicações. A saída JSON registra as entradas e o ranqueamento observado.
