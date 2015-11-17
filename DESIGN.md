Armazenamento e distribuição dos dados
======================================

O YEK é, de forma simplificada, um DHT (Distributed Hash Table). Há diversas formas de se implementar uma DHT. No YEK, será implementado o protocolo Chord como base para o funcionamento do banco de dados, sendo este usado também por outros sistemas de chave-valor.

A forma de particionamento das chaves e, consequentemente, a distribuição dos dados serão feitas com hashing consitente.

Hashing consistente é um tipo especial de hash, que tem por objetivo diminuir o remapeamento de chaves quando um nó é adicionado ou removido. Além disso, é utilizado para encontrar em qual nó uma chave está presente.

De modo geral o Chord cada nó escolhe um ID _n_-bit, geralmente através de uma função de hash sha-1; os IDs são organizados em um anel, cada nó é resposável por armazenar chaves próximas aos seus IDs.

Cada chave será também armazenada em outros nós, para que haja redundância dos dados, e no caso de falha, estes outros nós são capazes de atender à requisições.
Para garantir a alcançabilidade o YEK guarda em cada nó um lista com 4 sucessores e 4 predecessores. Assim, se o sucessor falhar a requisição poderá ser enviada para o próximo sucessor.

