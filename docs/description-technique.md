# Description technique

## Description de l’architecture du compilateur et de la chaine de compilation depuis le code source en WHILE à la récupération d’un programme exécutable

```mermaid
flowchart
A[Lecture du fichier par le programme Java] --> B[Parsing du fichier par ANTLR] --> C[Analyse lexiale et syntaxique par ANTLR] --> D[Analyse sémantique] --> E[Génération du code intermédiaire] -- L'optimisation ici n'a pas été implémentée  --> F[Génération du code JavaScript]
```

Nous avons fait en sorte que cela soit facile à utiliser pour l'utilisateur. Le script prend en compte deux arguments. Le premier correspond au fichier d'entrée, le second au fichier de sortie.

## Description de l’AST

### Fonctions

```mermaid
graph TD
func1[FUNCTION] --> true
func1 --> input[INPUT]
func1 --> command[COMMANDS]
func1 --> output[OUTPUTS]
```

Le noeud fonction comporte 4 noeuds :

- Le premier correspond au nom de la fonction.
- Le deuxième `INPUT` correspond aux paramètre d’entrée de la fonction
- Le troisième `COMMANDS` correspond au corps de la fonction
- Le dernier `OUTPUTS` correspond aux variables de sortie

### If

```mermaid
graph TD
IF --> Opt1
IF --> C1[COMMANDS]
IF --> C2[COMMANDS]
```

Le noeud `IF` comprend 3 noeuds :

- Le premier `Opt1` correspond à la condition d'exécution
- Les deux autres `COMMANDS` correspondent aux instructions à exécuter si la condition est vraie ou fausse.

### Let

```mermaid
graph TD
LET --> VARS --> Result
LET --> EXPRS --> SYMB --> false
```

Le noeud `LET` comprend 2 noeuds :

- Le premier `VARS` correspond aux variables qui seront affectées
- Le deuxième `EXPRS` correspond à l'expression dont la valeur sera affectée aux variables définies dans le noeud `VARS`.
