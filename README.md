# Code of Kutulu

## Statement mistakes
- Shelters start with 10 energy . [..] Shelters get refilled to 10 every 50 turns.
  => Shelters starts with 0 energy. [...] Shelters get regilles to 10 every multiple of 50 turns, except turn 0.

## Statement imprecision
- If a shelters is supposed to heal more than its energy for a turn (1 energy but two explorers for instance) everyone is healed and the shelter's energy is set to 0.
- PLAN and LIGHT are created by the explorer but they stay after the explorater is dead until they fade out.
- Minions pick there new target AFTER explorer moves.
- The prefered direction URDL is offseted by 1 more each turn. Meaning turn a minion will preferer Up instead of Right at turn 0 but he preferers Right instead of Up at turn 1 (RDLU), and so on.
