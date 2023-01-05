# projet-tlc
Projet : Le compilateur While 

# Requirements
Compile antlr3 :
```shell
gradle generateGrammarSource
```

# Parameters
Located on the Main class :
```java
String pathRead = "src/main/resources/and.txt";
String pathWrite = "out/and.js";
```
Or launch the program with arguments :
```txt
./program input_file output_file
```

# Launch output
```txt
node /out/and.js <parameters of main function>
```