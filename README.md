# Arcane Legacy

Mod para Minecraft 1.21.1 (NeoForge) que traz de volta a criação de feitiços no estilo do Ars Magica 2, com nomes, texturas e conteúdo próprios.

## O que já tem

- **Mana** por jogador: fica salva no mundo, regenera sozinha e é mantida ao morrer. A barra azul aparece acima da fome quando a mana não está cheia.
- **Mesa de Inscrição**: coloque até 6 runas na ordem forma → efeitos → modificadores. A tela diz se o feitiço é válido e quanto ele custa. A mesa tem 3 estados visuais: ociosa, em criação (partículas) e pronta (feixe de luz).
- **9 runas** (3 formas, 4 efeitos, 2 modificadores), na aba criativa do mod.

## Comandos de teste (operador)

- `/arcanelegacy mana get [jogador]`
- `/arcanelegacy mana set <valor> [jogadores]`
- `/arcanelegacy mana fill`

## Desenvolvimento

- `./gradlew runClient` abre o jogo com o mod.
- `./gradlew build` compila e roda os testes.
