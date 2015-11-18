YEK
====

YEK é um banco de dados chave-valor distribuído.

### Requisitos

+ Java 8 (JRE e JDK)
+ Make

### Como compilar

O YEK está dividido em dois aplicativos: o servidor e um cliente.
Ambos precisam ser compilados antes de ser executados. Basta rodar os comandos:

```
make server_jar
make client_jar
```

Para rodar o servidor initial execute `java -jar yek.jar`.
Para rodar os demais nós da rede, execute `java -jar yek.jar IP_DE_ALGUM_NO_DA_REDE`

Por fim, para rodar o cliente, utilize o comando `java -jar yek_cli.jar IP_DE_ALGUM_NO_DA_REDE PORTA`.
O mais fácil é se conectar ao nó inicial, já que ele possui porta fixa `32000`.

Para mais detalhes, veja a [documentação completa](https://github.com/Trindad/yek/blob/master/projeto.pdf).
