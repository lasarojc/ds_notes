# Sistemas Distribuídos

Infelizmente, programar sistemas distribuídos é muito mais complexo que sistemas centralizados.
De fato, quando começou, você aprendeu que programar era basicamente colar blocos Lego, que se encaixavam perfeitamente.
Bastava importar a biblioteca correta e invocar suas funções.

---
##### O Sonho

![Lego Rainbow](./images/lego0.jpg)

---

Sistemas maiores, contudo, aumentam consideravelmente a complexidade.
Mesmo que haja diferentes bibliotecas a serem usadas, com desenvolvedores e estilos diferentes, bem ou mal testadas, seus encaixes ainda fazem sentido.

---
##### A realidade
![Lego Hell](images/lego3.jpg)

---

Quando partimos para os sistemas distribuídos, a dificuldade aumenta ainda mais.
Agora temos peças que nunca foram pensadas para serem encaixadas e um pouco de *crazy glue* é necessária para encaixá-las.
Além disso, às vezes times diferentes montam partes diferentes do sistema, tendo visões diferentes do que deve fazer e de como as partes se comunicam.

---
##### O que vocês encontrarão aqui.

![Lego SD](images/lego4.jpg)
![Lego SD](images/lego5.jpg)

----

Programar distribuído é como programar com blocos de fabricante diferente, juntando com arame e cola, e com seu irmãozinho passando para lá e para cá, chutando as peças, mordendo algumas, enquanto seus pais brigam com você pq não arrumou o quarto.

Mas esta é a realidade da programação distribuída, por quê fazê-lo?

---

## Por quê?

---

O fato é que computadores individuais tem capacidade limitada de processamento e armazenamento, mas nossa necessidade de poder computacional cresce exponencialmente.

---
##### Crescimento da informação

![Data Growth](images/datagrowth.jpg)

---

Assim, precisamos crescer nosso poder computacional, mas aumentar a capacidade de um dispositivo mesmo de forma linear tem custo exponencial.

---
##### Custo de se "incrementar" seu computador.

![Custo de melhoria](images/exponential.jpg)

---

O que nos resta então é agregar o poder computacional de diversos computadores "baratos" para satisfazer nossas necessidades.
O remédio, contudo, é bem amargo: com muitos computadores conectados, vem a necessidade de coordená-los, de forma a agir de forma coerente, mesmo quando alguns deles falhe, e quanto mais computadores, maior é a probabilidade de que pelo menos um deles tenha uma CPU, disco, fonte, ou que quer que seja, falhando.

---

Computadores [falham](https://www.statista.com/statistics/430769/annual-failure-rates-of-servers/) o tempo todo!

---

Nós precisamos então entender este ambiente,
* qual a probabilidade de um nó falhar
* como os computadores, ou melhor, como os processos se comunicam? mensagens podem ser perdidas, atrasadas, corrompidas?
* os relógios dos computadores são sincronizados?
* quais os padrões de acesso ao serviços? Aumenta à noite? Diminui no verão?
* há agentes maliciosos que possam querer perturbar o sistema?

---
##### Modelos

* Comunicação
* Sincronismo
* Falhas

---

Definido o modelo computacional, podemos distribuir nosso sistema, isto é, dividir a computação/armazenamento em diversas máquinas, e coordenar suas ações para que sejam consistentes com a especificação, de forma a minimizar o tempo que o serviço fica fora do ar, entregando o serviço de acordo com expectativas especificadas. Para isto, precisamos entender 
* como falhas (bugs, por exemplo) afetam a execução de forma a 
* evitar que a falha de algum componente possa levar o sistema a parar como um todo,
* e a garantir que clientes em qualquer lugar do mundo tenham a mesma facilidade em acessar o serviço.

---
##### Expectativa

* Tolerância a falhas
* Desempenho
* Consistência

---

Vejamos algumas exemplos de tarefas executadas por sistemas distribuídos, que você usa hoje.

---
##### Exemplos

* Entregue este email para fulano@knowhere.uni.
* Envie o item X para este endereço, após cobrança de Y dinheiros da conta Z.
* Em um ambiente de simulação de batalhas em 3D, simule o disparo de um projétil nesta direção e sentido, com velocidade v, enquanto movimenta o avatar A para a esquerda.
* Autorize a transferência de X dinheiros da conta C para a conta C'.
* Movimente o braço mecânico que está segurando um bisturi, 3cm à direita, então abaixe-o 3mm, e movimente-o 4cm para a esquerda
* Inclua o comentário ``LOL!!!'' na lista de comentários do item XYZ, com marca de tempo T
* Leia o valor do sensor de temperatura S e, caso seu valor supere V, emita alarme luminoso vermelho intermitente e alarme sonoro

---

Um sistema distribuído implica em algum tipo de colaboração entre componentes, para permitir que recursos de um sejam usados por outro. Por exemplo, capacidade de armazenamento, de processamento, ou conexão física com uma impressora.

---
##### Compartilhar

* memória
* CPU
* impressora
* canal de comunicação
* localização

---

## Definição

Assim, uma possível definição de Sistema Distribuído, que me agrada, é a seguinte.

---
##### Sistema Distribuído

Coleção de sistemas computacionais (software ou hardware), independentes mas com alguma forma de comunicação, que colaboram na execução de alguma tarefa.

---

Uma mais realista talvez seja a de Leslie Lamport, que certa vez disse

> A distributed system is one in which the failure of a computer you didn't even know existed can render your own computer unusable.


## Como distribuir?

Apenas para reforçar, então, distribuir é
* Dividir a computação/armazenamento em diversas máquinas
* e coordenar suas ações para que resolvam a tarefa em questão de forma eficiente
* e usando de redundância para garantir que o serviço continua sendo provido a despeito de falhas.

Para colaborar, as as diversas partes do sistema distribuído devem se comunicar. 
Isto pode ser feito de diversas formas:
* Mensagens
* Streams
* Invocação remota de procedimentos
* Mais e mais abstrações

Implementar estas abstrações, para então desenvolver sistemas distribuídos, é complicado por quê os sistemas precisam ser coordenados em mínimos detalhes, a despeito das seguintes características:
* Falhas independentes
* Relógios dessincronizados
* Linguagens de desenvolvimento diferentes
* Sistemas operacionais diferentes
* Arquiteturas diferentes
* Desenvolvedores diferentes


Para facilitar o trabalho, entram e ação os *middleware*. 

---
##### Middleware

![](images/01-01.png)

> The software layer that lies between the operating system and applications on each side of a distributed computing system in a network.

[Sacha Krakowiak](https://web.archive.org/web/20050507151935/http://middleware.objectweb.org/)

---

Assim, os middleware facilitam a conexão entre nós e permitem o uso de protocolos mais abstratos que "mandar um monte de bytes" para lá e para cá, escondendo a complexidade da coordenação de sistemas independentes.
Desenvolver sistemas distribuídos sem usar um middleware é como desenvolver um aplicativo qualquer, sem usar bibliotecas: possível, mas complicado, e estará certamente reinventando a roda. Isto é, você praticamente tem que refazer o middleware antes de desenvolver o sistema em si.

Idealmente, com o middleware se obteria transparência total do fato da aplicação estar distribuída, levando o sistema, uma coleção de sistemas computacionais (software ou hardware) independentes, a se apresentar para o usuário como **um sistema único**. 
Pense no browser e na WWW: quanto você sabe sobre as páginas estarem particionadas em milhões de servidores?

---
##### Transparência

* Acesso: Representação de dados e mecanismos de invocação (arquitetura, formatos, linguagens...)
* Localização: Onde está o objeto?
* Relocação: Cliente não percebe se objeto for movimentado.
* Migração: Objeto não percebe se for movimentado. 
* Replicação: Objeto é replicado (tem várias cópias).
* Concorrência: Objeto é acessado por múltiplos clientes (sem interferência).
* Falha: Falha e recuperação não são percebidas (usando-se cópias)

---

Vejamos cada uma destas separadamente.

---
##### Transparência de acesso

Usar padrões abertos e interfaces bem definidas.
* Sistemas bem comportados e previsíveis (RPC/ASN.1)
* Que interajam bem com outros via interfaces bem definidas (REST)
* Suportem aplicações diferentes do mesmo jeito (API)

---

---
##### Transparência Localização e Relocação

* rede de baixa latência
* Serviços de nome
* Múltiplas cópias
* Cópias temporárias

---

---
##### Transparência Migração

* Virtualização

---

---
##### Transparência de Replicação

* Middleware de comunicação em grupo.

---

---
##### Transparência Concorrência

* controle de concorrência adequado.
* mecanismos para alcançar \emph{escalabilidade}(particionamento/sharding)

---

---
##### Transparência a Falhas

* mecanismos de tolerância a falhas (replicação)

---

Contudo, a realidade é dura e impede que transparência total seja obtida. Pois:

---
* Do ponto de vista do usuário
  * Usuários podem estar espalhados pelo mundo e perceberão latências diferentes.
* Do ponto de vista do desenvolvedor
  * Impossível distinguir um computador lento de um falho.
* De forma geral
  * Aumentar transparência custa desempenho. 

---


Observe que estas dificuldades vem da rede, que também é pedra fundamental para a existência de um Sistema Distribuído. 
Isto ocorre porquê assumimos, frequentemente, diversas inverdades quanto à rede.

---
##### Armadilhas da rede

* A latência é zero.
* A largura de banda é infinita.
* A rede é confiável.
* A rede é segura.
* A rede é homogênea.
* A rede é estática.
* A rede é de graça.
* A rede é administrada por você ou alguém acessível.

---


# TODO

Que possam ser redimensionados para atender a demandas de usuários e organizações.

O quê quer dizer um sistema ser escalável? Há vários tipos de escalabilidade.

---
##### Escalabilidade

* Escalabilidade
  * Tamanho: Número de usuários que suporta.
  * Geográfica: Região que cobre.
  * Administrativa: Número de domínios administrativos.
* Há várias possibilidades: seja específico e exija especificidade.

---


---
##### Como

* Esconda latência
  * Comunicação assíncrona: tarefas paralelas.
	E.g., em vez de validar formulário após preenchimento de cada campo, valide em paralelo enquanto usuário preenche campo seguinte.
  * Use callbacks.
	E.g., gatilhe tratamento de erro no formulário em função separada.

* Nem todo problema pode ser resolvido assim.
  Autenticação não pode ser assíncrono.

* Distribuição de tarefas.
  * Delegue computação aos clientes\\
		E.g., JavaScript e Applets Java.
  * Particione dados entre servidores\\
		E.g., Domain Name Service e World Wide Web.
* Aproxime dados dos clientes
  * Mantenha cópias de dados em múltiplos lugares.
  * Atualize dados de acordo com necessidade.\\
	E.g., cache do navegador.



E que se mantenham disponíveis a despeito de falhas.

Tolerar falhas
* Replicação e cache.
* Mantenha cópias de dados em múltiplos lugares.
* Atualize dados de acordo com necessidade.

Replicação x Inconsistências
* Múltiplas cópias $\rightarrow$ em sincronização $\rightarrow$ custos
  * Dado precisa ser consistente entre réplicas (mesmo valor em todo lugar) 
  * Protocolos de invalidação de cache.
  * Muita largura de banda.
  * Baixa latência.
Algumas aplicações toleram inconsistências, como carrinhos de compra.

Mas não todas, como sistemas de bancos.
