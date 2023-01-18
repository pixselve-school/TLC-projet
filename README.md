# projet-tlc
Projet : Le compilateur While 

# Requirements
Compile antlr3 :
```shell
sh gradlew generateGrammarSource
gradlew.bat generateGrammarSource
```

# Launch the compiler
```shell
sh gradlew run
gradlew.bat run
```

# Parameters
Located on the Main class :
```java
String pathRead = "src/main/resources/and.txt";
String pathWrite3addr = "out/and.3addr";
String pathWriteJS = "out/and.js";
```
Or launch the main function on your IDEA with arguments :
```txt
input_file output_3addr output_js
```

# Launch output js file
```txt
node /out/and.js <parameters of main function>
```

# Documents
- [Description Technique](./docs/description-technique.md)
- [Methodologies Gestion de projet](./docs/methodologie-gestion-de-projet.md)
- [Analyse Sémantique](./docs/validation-compilateur.md)