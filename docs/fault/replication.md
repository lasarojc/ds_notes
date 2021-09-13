# Replicação
Embora a ideia de replicação seja simples, isto é, criar cópias de um componente para garantir que algum está sempre disponível para continuar a entregar as funcionalidades do sistema.
Mas para que tenhamos realmente cópias, é preciso que toda ação tomada por um componente seja também tomada pelas réplicas, e há várias possibilidades de como fazê-lo, cada uma com seus prós e contras.


## Multi-Escritores
### Encaminhamento de mensagens
A abordagem mais básica para a replicação consiste em permitir que todas as réplicas aceitem operações dos clientes e fazer com que as réplicas **repassem** as operações que receberam para as demais.
Esta abordagem pode ser implementada com diferentes mecanismo de comunicação, com diferentes resultados.
Por exemplo, se implementada o uso de UDP, todas as réplicas receberão as mesmas operações, exceto por alguma eventualmente perdida.
Se em vez disso usarem várias conexões TCP conectado todas as réplicas, desde que as réplicas estejam sempre ativas, todas receberão, em algum momento, todas as operações enviadas pelos clientes.

![](../drawings/replication.drawio#0)

Mesmo que todas as mensagens sejam entregues, a falta de ordenação no processamento de operações pode levar a estados inconsistentes entre as réplicas.
Por exemplo, se dois comandos são enviados, um para esvaziar o conteúdo de um arquivo e o outro pra apagá-lo, o processamento desordenado poderá levar uma réplica a ter um arquivo vazio e a outra a não ter o arquivo.

![](../drawings/replication.drawio#1)


### Anti-entropia
Para corrigir inconsistências, réplicas podem comunicar-se frequentemente identificando e corrigindo divergências em seus dados.
Este tipo de protocolo, denominado Anti-Entropia, pode ser implementado com um mecanismo de [*gossiping*](../comm/epidemics.md) e usando [relógios vetoriais](../time/logical.md) para concordar nas versões de dados conflitantes a se manter.
Usando anti-entropia réplicas podem garantir que, se novas operações cessarem, em algum momento estarão consistentes.
O termo em inglês para esta garantia é ***eventual consistency***[^eventual] e é como funcionam diversos bancos de dados não relacionais como Cassandra, Redis e Dynamo.

[^eventual]: *Eventual*, no inglês, quer dizer que algo vai acontecer, embora não se saiba quando.

![](../drawings/replication.drawio#2)

Apesar de levar à consistência entre réplicas, o estado alcançado não necessariamente faz sentido do ponto de vista da aplicação pois pode corresponder, por exemplo, a uma ordenação errada dos comandos emitidos por um dado cliente.
Por isso, esta técnica não pode ser aplicada em todas as situações.

![](../drawings/replication.drawio#3)


## Único Escritor
### Primário/Secundário
Nos foquemos na raiz do problema que aparentemente é a ordenação das operações.
Se cada processo pode receber as mensagens em qualquer ordem, ou seja, cada uma tem uma fila de mensagens independente, então as réplicas podem chegar a estados distintos.

![Primário/Backup](../drawings/replication.drawio#4)

Mas e se tivéssemos uma fila única?
No caso da replicação **primário/cópia**,[^mestreescravo] o primário é responsável por lidar com clientes e por informar cópias das modificações de estado, efetivamente mantendo a fila única e impondo uma ordenação de operações que faz sentido, correspondendo a ordem de entrega das mensagens.

[^mestreescravo]: Esta técnica também é conhecida como **mestre/escravo**, mas esta nomenclatura tem caído em desuso por razões óbvias.

![Primário/Backup](../drawings/replication.drawio#5)

Algumas observações são importantes sobre a forma como as operações são repassadas para as réplicas aqui.

###### Operações x Estado
Outro fator importante é que o primário não necessariamente precisa repassar a operação para réplica, podendo passar algo equivalente.
Por exemplo, se a operação é um comando SQL que demanda muito tempo para executar mas que altera poucos dados no banco, então o primário poderia enviar as atualizações para réplicas em vez de lhes exigir que reexecutem o SQL.
Por outro lado, um pequeno comando SQL poderia gerar uma grande modificação nos dados e, neste caso, repassar o SQL seria mais econômico em termos de comunicação.
A melhor abordagem depende de cada aplicação.

###### Cópias desatualizadas
Operações que levam a atualizações nos dados devem ser feitas sempre no processo primário, ou este não processaria as operações, podendo levar o primário a virar um gargalo do sistema.
Para amenizar esta situação, operações que apenas leiam dados poderiam ser enviadas para as réplicas, em vez de para o primário.
Contudo, como o primário primeiro executa a operação antes de repassá-la para as réplicas, há um atraso na execução pelas réplicas, o que quer dizer que as leituras poderiam retornar dados antigos.


### Replicação em Cadeia
A **replicação em cadeia** é uma generalização de primário/cópia em que os processos se organizam em um sequência para executar operações.

![Chain replication](../drawings/replication.drawio#6)


Como na abordagem original, **atualizações** no sistema são sempre **direcionadas ao primário**, a cabeça da sequência. 
**Leituras**, se absolutamente necessitarem dos dados escritos mais recentemente, também devem ser direcionadas à **cabeça**. 
Caso contrário, podem ser direcionadas aos processos na **cauda**, diminuindo a carga de trabalho na cabeça; quanto mais relaxado for a exigência de "frescor" dos dados, mais para o fim da cauda a requisição pode ser enviada.



### Lidando com falhas
Em abordagens baseadas em um primário, um passo essencial é identificar as falhas do processo primário para ativar um backup para que tome seu lugar.

![ha](../images/ha-diagram-animated.gif)

Nesta situação, o primeiro desafio está em identificar a falha.
Como já mencionamos e ainda iremos discutir, identificar corretamente a falha de um processo em um ambiente onde a comunicação acontece por troca de mensagens e assíncrono é impossível, pois não se pode diferenciar um processo falho de um lento.

Mesmo que a identificação fosse perfeita, ainda temos o problema das operações que já foram entregues para o primário mas que ainda não foram propagadas para as réplicas.
Quanto o backup assume, ele terá então um estado que está no passado do estado o primário, o que pode levar comportamento inaceitável.
Se este for o caso, replicação ativa pode ser usada.

## Replicação ativa

No caso da replicação ativa, as **várias cópias executam todos os comandos** enviados para o sistema, estando assim todas aptas a continuar a executar o serviço a qualquer instante, pelo menos se as operações de leitura forem enfileiradas também, ou poderiam chegar a uma réplica antes de uma escrita disparada anteriormente.

![Replicação ativa](../drawings/replication.drawio#7)


A técnica de replicação de [máquinas de estados](../time/logical/#comunicacao-em-grupo), brevemente discutida anteriormente é uma materialização da replicação ativa.
Como vimos anteriormente, replicação de máquinas de estados utiliza primitivas de comunicação em grupo, mas as primitivas vistas anteriormente não são funcionais principalmente por não serem tolerantes a falhas. 
Vejamos a porquê é difícil desenvolver primitivas tolerantes a falhas.


