# Projet TLC - Compilateur While

Membres :
- GOARDOU Fabien
- KERICHARD Mael

## Compilation de la grammaire
```shell
sh gradlew generateGrammarSource
gradlew.bat generateGrammarSource
```

## Lancer le compilateur
### Parameters
Dans la classe Main :
```java
String pathRead = "src/main/resources/and.txt";
String pathWrite3addr = "out/and.3addr";
String pathWriteJS = "out/and.js";
```
Ou lancer la fonction main avec les arguments suivants :
```txt
input_file output_3addr output_js
```

### Lancement
Lancer gradle avec l'option `run`
```shell
sh gradlew run
gradlew.bat run
```

## Lancer le programme fournis
```txt
node /out/and.js <parameters of main function>
```

## Documents
- [Description Technique](./docs/description-technique.md)
- [Methodologies Gestion de projet](./docs/methodologie-gestion-de-projet.md)
