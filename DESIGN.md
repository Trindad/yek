Armazenamento e distribuição dos dados
======================================

O YEK é, de forma simplificada, um DHT (Distributed Hash Table).
A forma de particionamento das chaves e distribuição dos dados, é feita com hashing consitente. 

Hashing consistente é um tipo especial de hash, que tem por objetivo diminuir o remapeamento de chaves quando um nó é adicionado ou removido. Além disso, é utilizado para encontrar em qual nó uma chave está presente.
O protocolo utilizado para o DHT é o Chord.

De modo geral o Chord cada nó escolhe um ID n-bit, geralmente através de uma função de hash; os IDs são organizados em um anel, cada nó é resposável por armazenar chaves próximas aos seus IDs. 