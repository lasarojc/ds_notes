## Como distribuir?

Apenas para reforçar, então, distribuir é
* dividir a computação/armazenamento em diversos componentes
* possivelmente geograficamente distribuídos
* coordenar suas ações para que resolvam a tarefa em questão de forma correta.

Com a distribuição objetiva-se
* usar recursos disponíveis nos hosts onde os componentes são executados
* e usar de redundância para garantir que o serviço sofra degradação graciosa em caso de falhas.

Para colaborar, as as diversas partes do sistema distribuído devem se comunicar.
Isto pode ser feito de diversas formas e em diversos níveis de abstração. Por exemplo:
* troca de mensagens,
* streams de dados,
* invocação remota de procedimentos
* ...

Implementar estas abstrações, para então desenvolver os sistemas distribuídos é complicado por quê os sistemas precisam ser coordenados em mínimos detalhes, a despeito das seguintes características:
* Falhas independentes
* Relógios dessincronizados
* Linguagens de desenvolvimento diferentes
* Sistemas operacionais diferentes
* Arquiteturas diferentes
* Desenvolvedores diferentes

Quero dizer, a complexidade de se implementar estas abstrações já é grande por si só. Se formos reinventar a roda a cada novo sistema, não faremos muitos avanços.
Mas, como vocês sabem, sistemas computacionais são como ogros (muito feios, às vezes), que por sua vez, são como cebolas (cheias de camadas), que nos fazem chorar quando precisamos descascá-las.

![](https://media.giphy.com/media/4RsEUfHym7tuw/200.gif)

Felizmente, para cada problema que tenha que resolver, há uma boa probabilidade de que alguém já tenha atacado o problema e disponibilizado uma solução, de forma comercial ou não.
Com sistemas distribuídos, não é diferente, e no caso da comunicação entre componentes distribuídos, a solução normalmente é usar um *middleware*.

---
##### Middleware

O *middleware*  é a camada *ware* que fica no *middle*, entre, o *software* e o *hardware*. 
Software, no caso, é a aplicação distribuída, e hardware é a **abstração** do *host*  em que executam os componentes, provida pelo sistema operacional.
Uso aqui o termo **abstração** porquê o sistema operacional pode encapsular o hardware, mas também pode encapsular outra abstração hardware, por exemplo, um máquina virtual.

---
##### Middleware

![Middleware](images/01-01.png)



> The software layer that lies between the operating system and applications on each side of a distributed computing system in a network.

---

Segundo, [Sacha Krakowiak](https://web.archive.org/web/20050507151935/http://middleware.objectweb.org/), as principais funções do middleware são:
* esconder a distribuição e o fato de que um aplicação é geralmente composta por múltiplas partes, executando em localizações geograficamente distintas,
* esconder a heterogeneidade dos vários componentes de hardware, sistemas operacionais e protocolos de comunicação
* prover interfaces uniformes, de alto nível e padronizadas para os desenvolvedores de aplicação e integradores, de forma que aplicações possam ser facilmente compostas, reusadas, portadas e feitas interoperáveis.


Assim, os middleware facilitam a conexão entre nós e permitem o uso de protocolos mais abstratos que "mandar um monte de bytes" para lá e para cá, escondendo a complexidade da coordenação de sistemas independentes.
Desenvolver sistemas distribuídos sem usar um middleware é como desenvolver um aplicativo qualquer, sem usar bibliotecas: possível, mas complicado, e estará certamente reinventando a roda. Isto é, você praticamente tem que refazer o middleware antes de desenvolver o sistema em si.

Idealmente, com o middleware se obteria transparência total do fato da aplicação estar distribuída, levando o sistema, uma coleção de sistemas computacionais (software ou hardware) independentes, a se apresentar para o usuário como **um sistema único**. 
Pense no browser e na WWW: quanto você sabe sobre as páginas estarem particionadas em milhões de servidores?

Podemos quebrar esta "transparência total" em várias transparências mais simples.
Vejamos cada uma destas separadamente.

---
##### Transparências

* Acesso
* Localização
* Relocação
* Migração: Objeto não percebe se for movimentado. 
* Replicação: Objeto é replicado (tem várias cópias).
* Concorrência: Objeto é acessado por múltiplos clientes (sem interferência).
* Falha: Falha e recuperação não são percebidas (usando-se cópias)

---


A transparência de acesso diz respeito à representação de dados e mecanismos de invocação (arquitetura, formatos, linguagens...).
Cada computador tem um arquitetura e uma forma de representar, por exemplo, números, e se dois componentes de um SD executam em máquinas com arquiteturas diferentes, como trocam números em ponto flutuante?


---
##### IEEE FP

![IEEE Floating Point](images/float_point.jpg)

* Half Precision (16 bit): 1 sign bit, 5 bit exponent, and 10 bit mantissa
* Single Precision (32 bit): 1 sign bit, 8 bit exponent, and 23 bit mantissa
* Double Precision (64 bit): 1 sign bit, 11 bit exponent, and 52 bit mantissa
* Quadruple Precision (128 bit): 1 sign bit, 15 bit exponent, and 112 bit mantissa

[Fonte](https://www.tutorialspoint.com/fixed-point-and-floating-point-number-representations)

---

A mesma questão é válida para representações de strings e classes, e diferenças de sistemas operacionais e linguagens.
Assim, para se tentar obter transaparência de acesso, é importante que se use padrões implementados em múltiplas arquiteturas.

---
##### Transparência de acesso

Usar padrões abertos e interfaces bem definidas.
* Sistemas bem comportados e previsíveis (RPC/ASN.1)
* Que interajam bem com outros via interfaces bem definidas (REST)
* Suportem aplicações diferentes do mesmo jeito (API)

---

A transparência de localização diz respeito a onde está o objeto: pouco importa ao usuário, se o serviço está dentro da mesma máquina em que acessa o serviço, se na sala do lado, ou na nuvem, do outro lado do globo, desde que o serviço seja provido de forma rápida e confiável.
A esta transparência é essencial uma boa distribuição do serviço, sobre uma rede com baixa latência.
Técnicas como *caching* de dados também são importantes.

As vezes os objetos precisam ser movimentados de uma localização à outra.
Se implementadas corretamente, as técnicas que entregam transparência de localização não deixam que o cliente perceba a movimentação, no que chamamos transparência de Relocação.

---
##### Transparência Localização e Relocação

* Rede de baixa latência
* Distribuição inteligente
  * E.g: Serviços de nome
* Múltiplas cópias
  * Cópias temporárias

---

Do ponto de vista do próprio serviço, não perceber que se está sendo movimentado é chamado transparência de Migração.
Um serviço com esta propriedade, não precisa ser parado e reconfigurado quando a mudança acontece.
Uma das formas de se implementar esta propriedade é através da migração provida por máquinas virtuais.

---
##### Transparência Migração

* Virtualização

---

Para se manter o serviço executando a despeito de falhas, é necessário replicá-lo.

---
##### Transparência de Replicação

* Middleware de comunicação em grupo.

---

---
##### Transparência Concorrência

* controle de concorrência adequado.
* mecanismos para alcançar *escalabilidade* (particionamento/*sharding*)

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
  * Comunicação assíncrona: tarefas paralelas.<br>
	E.g., em vez de validar formulário após preenchimento de cada campo, valide em paralelo enquanto usuário preenche campo seguinte.
  * Use callbacks.<br>
	E.g., gatilhe tratamento de erro no formulário em função separada.

* Nem todo problema pode ser resolvido assim.
  Autenticação não pode ser assíncrono.

* Distribuição de tarefas.
  * Delegue computação aos clientes<br>
		E.g., JavaScript e Applets Java.
  * Particione dados entre servidores<br>
		E.g., Domain Name Service e World Wide Web.
* Aproxime dados dos clientes
  * Mantenha cópias de dados em múltiplos lugares.
  * Atualize dados de acordo com necessidade.<br>
	E.g., cache do navegador.



E que se mantenham disponíveis a despeito de falhas.

Tolerar falhas
* Replicação e cache.
* Mantenha cópias de dados em múltiplos lugares.
* Atualize dados de acordo com necessidade.

Replicação x Inconsistências
* Múltiplas cópias ![](images/rightarrow.png) em sincronização ![](images/rightarrow.png) custos
* Dado precisa ser consistente entre réplicas (mesmo valor em todo lugar) 
* Protocolos de invalidação de cache.
* Muita largura de banda.
* Baixa latência.

Algumas aplicações toleram inconsistências, como carrinhos de compra.

Mas não todas, como sistemas de bancos.
