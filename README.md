YEK
====

YEK é um banco de dados chave-valor distribuído.

### Como compilar

O sistema de build é feito usando a ferramenta [Buildr](https://buildr.apache.org), que é um gem ruby.

```
gem install buildr
```

Após intalá-la, execute o seguinte comando para (somente) compilar o código:

```
buildr compile
```

Para compilar e executar:

```
buildr run
```

E finalmente, para distribuir um `.jar`, execute:

```
buildr package
```
