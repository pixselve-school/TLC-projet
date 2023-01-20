# Analyse post moterm
Fonctionnalités implémentées :
- Analyse lexicale (antlr)
- Analyse sémantique
- While vers 3addr
- 3addr vers JS

Fonctionnalités non implémentées :
- Optimizer
- Foreach
- Certains tests
- Appel d'une fonction avec comme argument une autre fonction

L'utilisation de gradle nous a permis de gérer facilement nos dépendances et notre compilation, **dont la compilation du code source antlr**. En revanche nous n'avons pas réussi à le compiler en jar avec toutes les dépendances. Peut-être que maven aurait été plus simple dans ce cas.

La gestion de projet via github a été très utile pour la répartition des tâches et le report de bugs.

Un aspect du projet qui n'a pas été assez réfléchi en amont est le langage 3 adresses. Effectivement, chacun avait sa vision de l'implémentation de ce langage, ce qui a posé problème entre la génération et le parsing.

Si nous devions refaire le projet, il faudrait d'abord mettre par écrit chaque aspect de la compilation :
- l'AST attendu
- ce que l'on vérifie
- le langage 3addr pour chaque cas
- comment gérer les paramètres et les variables dans une fonction du langage cible