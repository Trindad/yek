YEK
====

YEK é um banco de dados chave-valor distribuído.

### Requisitos

+ Java 8 (JRE e JDK)
+ Make

### Como compilar

Anteriormente, o sistema de build do YEK usava um aplicativo chamado Buildr, porém o mesmo não se mostrou flexível o suficiente para atender as necessidades. O novo sistema de build do YEK usa o Make, que é muito mais simples de ter instalado e mais simples de usar. Abaixo seguem os comandos para rodar o YEK.

##### Servidor inicial

```
make run_initial_server
```

##### Demais servidores

```
make run_server ip="<ip_do_no_inicial>"
```

##### Cliente

```
make run_client ip="<ip_de_um_no_da_rede>" port="<porta_do_no>"
```
