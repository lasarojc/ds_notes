Agora que já estão convencidos de que não temos alternativas à distribuição, conhecem algumas das arquiteturas algumas das tecnologias usadas, vamos dar um passo para trás e para entendermos os fundamentos necessários à criação de sistemas escaláveis e tolerantes a falhas.
O primeiro desafio é entender o ambiente no qual estão inseridos, suas limitações e fragilidades e, para isso, precisamos entender como a computação é executada em cada uma das partes do sistema.

## Do processador à nuvem em 42 passos


=== "1"
     ![Animação](drawings/virtualization.drawio#1)

=== "2"
     ![Animação](drawings/virtualization.drawio#2)

=== "3"
     ![Animação](drawings/virtualization.drawio#3)

=== "4"
     ![Animação](drawings/virtualization.drawio#4)

=== "5"
     ![Animação](drawings/virtualization.drawio#5)

=== "6"
     ![Animação](drawings/virtualization.drawio#6)

=== "7"
     ![Animação](drawings/virtualization.drawio#7)

=== "8"
     ![Animação](drawings/virtualization.drawio#8)

=== "9"
     ![Animação](drawings/virtualization.drawio#9)

=== "10"
     ![Animação](drawings/virtualization.drawio#10)

=== "11"
     ![Animação](drawings/virtualization.drawio#11)

=== "42"
     ![Animação](drawings/virtualization.drawio#12)




Agora que relembramos como a computação é executada em nível de infraestrutura, consideremos um problema abstrato.

## Uma história de três exércitos
**Era uma vez** uma cidade estado no alto de uma montanha. 
A despeito de sofrer de falta de água, afinal, estava no alto de uma montanha, a cidade era invejada pelos vizinhos.
Como a cidade era muito bem fortificada, ela poderia se defender de qualquer **ataque em uma única frente**. 
Se atacada em **duas frentes**, contudo, cairia.
Sabendo disso, o rei de uma das cidades vizinhas resolveu tomar a cidade e repartiu suas forças em **dois exércitos** sob o comando de Alice (a sociedade era feminista) e Bastião (sim, Bastião, não Bob).[^2generalsparadox]

[^2generalsparadox]: Esta é uma variação do problema de coordenação de *gangsters* apresentado no em [Some constraints and trade-offs in the design of network communications](https://doi.org/10.1145%2F800213.806523)

![Paradoxo dos 2 Generais](drawings/2generals.drawio#0)

Um complicador no ataque é que a **comunicação entre os dois exércitos é feita por mensageiros** que devem contornar a montanha para alcançar o outro exército. 
O trajeto é complexo e cheio de armadilhas e por isso **mensageiros podem se perder** e demorar um longo tempo para chegar ou até mesmo **serem mortos** e nunca entregarem suas mensagens.

Alice, a comandante mais sênior, deve decidir quando atacar e informar a Bastião, por exemplo, simplesmente ordenando "**Atacar no dia 3, ao nascer do sol.**"
Bastião **obedecerá** a ordem de atacar contanto que esteja certo de que Alice também atacará, e é justamente daí que vem a dificuldade do problema.
Se mensagens podem ser perdidas, **Alice não tem garantias de que Bastião recebeu o comando** e por isso não pode simplesmente considerar como certo o ataque de Bastião.
Como o problema pode ser resolvido?

Uma resposta natural é usar **mensagens de confirmação**. Isto é, quando Bastião recebe uma ordem, envia um mensageiro de volta para Alice com uma confirmação da recepção.
Alice ao receber tal mensagem, sabe que Bastião executará a ordem, correto? Mas não é tão simples assim no caso da ordem de atacar.
Lembre-se que qualquer exército que ataque sozinho, perderá, seja Alice ou Bastião. 
Por isso, ao enviar uma mensagem de confirmação do ataque, Bastião precisa estar certo de que Alice a recebeu, ou atacará sozinho.
Novamente podemos apelar para uma mensagem de confirmação ou, neste caso, uma confirmação da confirmação.
E o problema se repete indefinidamente.


!!! info inline end "Paradoxo dos 2 Exércitos"
    * $A$ e $B$ devem concordar na hora do ataque.
	* $A$ ataca se estiver certo que $B$ atacará.
	* $B$ ataca se estiver certo que $A$ atacará.
	* A comunicação por troca de mensagens.
       * Mensagens podem ser arbitrariamente atrasadas.
	   * Mensagens podem ser perdidas.
	
	Como um exército tem certeza que o outro irá atacar?

Suponhamos que **há um algoritmo correto** que executa uma sequência finita de troca de mensagens em que ao final tanto Alice quanto Bastião estão seguros, e corretos em sua segurança, de que o outro também atacará. Seja $n$ o número máximo de mensagens trocadas. 
Em uma execução em que todas as $n$ mensagens possíveis são usadas, suponha sem perda de generalidade que Alice enviou a $n$-ésima mensagem.

![Paradoxo dos 2 Generais](drawings/2generals.drawio#1)

Observe que, do ponto de vista de Alice, uma execução do algoritmo em que a nenhuma mensagem é perdida, é indistinguível de uma execução em que a $n$-ésima mensagem é perdida.

![Paradoxo dos 2 Generais](drawings/2generals.drawio#2)

Dado que ao final da primeira execução completa **Alice ataca**, no final da execução onde a mensagem $n$ é perdida, Alice também deve atacar.
Mas se o algoritmo é correto, então também **Bastião ataca**, mesmo sem ter recebido a enésima mensagem. Logo, a enésima mensagem é desnecessária ao algoritmo, que deve funcionar com $n-1$ mensagens.

Repetindo-se o argumento mais $n-1$ vezes, temos que o algoritmo deve funcionar com zero mensagens, o que é um **absurdo**. Logo não existem algoritmos corretos para o problema como definido, isto é, em que mensagens podem ser perdidas; é **impossível** resolver o problema.

!!!info inline end "Impossibilidades"
    Impossibilidade de resolução x resolução na prática.

Apesar de ser impossível resolver este problema aparentemente simples, devemos fazê-lo frequentemente no mundo real. 
A resposta está no que consideramos como premissas válidas no ambiente em que tentamos solucionar o problema e quais exatamente são as propriedades de uma solução aceitável.


###### Impossibilidade
Quando dizemos que é impossível resolver um problema queremos dizer que é impossível produzir um algoritmo que **sempre levará a uma resposta correta**.
Isto quer dizer, ignorando-se algoritmos que sempre levarão a respostas incorretas, podemos produzir algoritmos que ou às vezes **levarão a respostas incorretas** ou que, mesmo que nunca levem a respostas incorretas, às vezes **não levarão a respostas** alguma; ambos podem ser úteis na prática.

Por exemplo, ainda no problema dos exércitos tentando tomar a cidade, suponha que em vez de mandar um único mensageiro com a ordem de ataque, Alice envie 100, ou 200, ou 1000.
A **confiança** de Alice de que Bastião também atacaria, seria muito maior e não precisaria receber uma confirmação de entrega de mensagens.
Esta abordagem faria com com que o ataque funcionasse com uma **alta probabilidade** $P$, mas com uma pequena probabilidade $P-1$ de levar a um ataque fracassado, onde $P$ pode ser feita **tão grande quanto se "queira"**.

Resultados de impossibilidade abundam na área de computação distribuída[^impossibilidades] e não podem nos desencorajar de continuar a buscar soluções práticas.

[^impossibilidades]: [Hundred Impossibility Proofs for Distributed Computing](https://groups.csail.mit.edu/tds/papers/Lynch/MIT-LCS-TM-394.pdf), [Impossibility Results for Distributed Computing](https://doi.org/10.2200/S00551ED1V01Y201311DCT012)



## Modelos computacionais
Dado a quantidade de ambientes reais em que as soluções dos nossos problemas abstratos precisam executar, com sua diversidade de sistemas operacionais, latências de rede, tamanhos de mensagens, etc, seria praticamente impossível provar alguma coisa geral e interessante sobre os algoritmos, como por exemplo se ele **funciona**.
Por isso, em vez de considerar cada ambiente específico, abstraímos os ambientes por meio de **modelos computacionais**, que capturam as premissas gerais dos ambientes, e só então escrevemos os algoritmos para tais modelos.

Isso que dizer que, na prática, antes de distribuir a computação/armazenamento em diversas máquinas e de forma a coordenar ações das diversas partes de forma a entregar o serviço de acordo com expectativas dos usuários, precisamos responder a algumas perguntas:

* Qual a probabilidade de um nó parar de funcionar?
* Como os nós se comunicam? Eles compartilham um espaço de endereçamento ou enviam mensagens uns para os outros?
* A quais atrasos a comunicação está sujeita? Pode haver atrasos infinitos?
* A comunicação pode ser corrompida?
* Os relógios dos hospedeiros marcam o mesmo valor no mesmo instante, ou melhor, são sincronizados?
* Há agentes que possam querer perturbar o sistema, por exemplo para ganhar acesso a mais recursos do que seria justo?

??? info inline end "Modelos"
    * Comunicação
    * Sincronismo
    * Falhas

Estas perguntas são normalmente divididas em três eixos, **Comunicação**, **Sincronismo** e **Falhas**, e a combinação das respostas define o modelo computacional adotado.

### Comunicação
De uma forma ou de outra, sistemas distribuídos tem à sua disposição múltiplos processadores e permitem o desenvolvimento de aplicações paralelas, isto é, onde múltiplas tarefas são executadas ao mesmo tempo ou **paralelamente**.
Contudo, por um lado, quando falamos em sistemas multiprocessados, normalmente estamos falando de sistemas em que os processadores estão **próximos** e compartilham um mesmo espaço de endereçamento, sejam computadores com múltiplos processadores ou sejam clusters de computadores conectados por um barramento de comunicação de altíssima largura de banda, como [Infiniband](https://en.wikipedia.org/wiki/InfiniBand) que abstraiam múltiplos segmentos de memória como um único espaço de endereçamento.
Seja como for, estes sistemas com **memória compartilhada** são normalmente usados para aplicações de computação intensiva e em cujo os componentes são mais **fortemente acoplados** e melhor estudados em um curso de computação paralela.

![Memória Compartilhada](drawings/shared_memory.drawio#0)


??? info inline end "Comunicação"
    * memória compartilhada
    * troca de mensagens

Por outro lado, estamos mais interessados aqui em sistemas de maior escala geográfica, o que se adequa melhor ao modelo de troca de mensagens, isto é, onde cada nó mantem controle total do seu espaço de endereçamento e só expõe seu estado via mensagens enviadas para os outros nós.
Este modelo é mais adequado ao desenvolvimento de aplicações com componentes **fracamente acoplados**, em que atrasos de comunicação e ocorrência de falhas independentes são intrínsecas.

![Passagem de Mensagens](drawings/shared_memory.drawio#2)

Memória Compartilhada Distribuída (DSM, do inglês, *Distributed Shared Memory*) é uma abordagem híbrida que tenta integrar a facilidade de se programar usando um único espaço de endereçamento mas com o nível de distribuição necessária a aplicações de larga escala, inclusive geográfica.

![Memória Compartilhada](drawings/memory.drawio)

Considere uma possível implementação em software da DSM, apresentada na próxima figura. 
Nesta abordagem, cada *host* contribui uma porção de sua memória para um *pool* global. Processos acessam o *pool* via **gerentes de memória**, que traduzem os endereços de um espaço de endereçamento virtual para um host e um endereço local a tal host, e usam *message passing*  para implementar o acesso. 
Esta abordagem resulta em uma arquitetura NUMA, isto é, *Non-Uniform Memory Access*, já que os acessos a endereços locais são mais rápidos que aos remotos.

![Memória Compartilhada Distribuída em Software](drawings/shared_memory.drawio#1)



### Sincronismo

??? info inline end "Sincronismo"
    * operações
    * comunicação
    * relógio
    * sincronização

Quanto ao sincronismo, se considera se os processos tem a **capacidade de medir a passagem de tempo**, isto é, se tem a acesso a relógios, o quão acurazes este são e o quão sincronizados são estes relógios uns com os outros.
Além disso, considera-se a existência de limites de tempo para execução de operações, por exemplo, o tempo um processador leva para executar uma operação de soma de dois inteiros, ou o tempo necessário para a entrega de uma mensagem ou acesso a uma região de memória.



### Falhas

Quanto às falhas, primeiro é preciso aceitar o fato de que componentes independentes podem falhar independentemente e que quanto mais *hosts*, maior é a probabilidade de que pelo menos um deles tenha uma CPU, disco, fonte, ou que quer que seja, apresentando problemas; e estejam certos, problemas acontecem o tempo todo.[^falham] 
Isto é importante pois se em sistemas monolíticos uma falha pode facilmente fazer com que o sistema todo pare e, portanto, não tente progredir na ausência de um componente, em um sistema distribuído queremos exatamente o contrário, isto é, que apesar da falha de um componente, os outros continuem prestando o serviço, mesmo de forma deteriorada, mas sem comprometer a corretude do sistema.

[^falham]: [Annual failure rates - servers](https://www.statista.com/statistics/430769/annual-failure-rates-of-servers/)

??? info inline end "Falhas"
    * detectável
    * temporização
    * quebras
    * maliciosas
    * perda e corrupção de mensagens

Para lidar com falhas, precisamos entender quais são suas possíveis formas, isto é, se o levam componentes falhos a parar de funcionar totalmente e de forma identificável por outros ou não, se há falhas "maliciosas", se os limites de tempo estabelecidos acima podem ser violados, se mensagens podem ser perdidas ou corrompidas.

### Modelo Assumido

??? info inline end "Outros"
    * carga de trabalho

Embora modelos clássicos sejam normalmente definidos em termos dos fatores acima, outras questões são também importantes, como o padrão da carga de trabalho do sistema (maior carga à noite? Na hora do almoço? *Black friday*?). Além de ignorarmos estes outros fatores, por enquanto assumiremos um modelo computacional amigável, com comunicação por troca de mensagens, relógios e limites de tempo para operações, mesmo que desconhecidos. Também assumiremos ausência de falhas, a não ser quando quisermos provocar a análise de situações mais interessantes. Este modelo será ajustado na medida em que avançarmos, para tornar nossas análises mais realistas.


### SD são como cebolas!

Uma vez definido o **modelo computacional** e identificado os **algoritmos adequados** aos problemas que queremos resolver, passamos à implementação.
Distribuir é **dividir** a computação/armazenamento em diversos componentes, **possivelmente geograficamente distantes**, e **coordenar** suas ações para que resolvam a tarefa em questão de forma correta.
Com a distribuição objetiva-se **usar recursos** disponíveis nos hosts onde os componentes são executados[^recursos] e usar de **redundância** para garantir que o serviço sofra **degradação graciosa** em caso de falhas, ou seja, fazer com que o serviço continue funcionando, mesmo que com **vazão reduzida**, **latência aumentada**, menor capacidade de tratamento de requisições concorrentes, ou com **funcionalidades desabilitadas**.

[^recursos]: Os recursos compartilhados vão desde alguns óbvios, como **capacidade de armazenamento** e de **processamento**, a própria **localização** de um nó, que pode ser geograficamente mais próxima e de menor latência até  um ponto de interesse, ou até mesmo a disponibilidade de uma conexão física com um recurso especial, como uma impressora.

??? info inline end "Abstrações"
    * Comunicação
        * Ordenação
        * Confiabilidade
        * Invocação de procedimentos remotos
    * Heterogeneidade
        * Linguagens
        * Arquiteturas
        * Sistemas Operacionais
        * Times

Para colaborar, as diversas partes do sistema distribuído devem se comunicar, o que pode pode ser feito de diversas formas e em diversos níveis de abstração. Por exemplo, no caso troca de mensagens, estas podem ser desde pacotes de bytes entregues pelo IP/UDP como por **troca de mensagens** ordenadas, **fluxos de dados**, ou **invocação remota de procedimentos**.
Implementar estas abstrações em si já é uma tarefa complicada, pois é preciso levar em consideração que os componentes de um sistema distribuído **falham independentemente**, executam em *hosts*  com **relógios dessincronizados**, são desenvolvidos usando-se **linguagens diversas**, **sistemas operacionais distintos**, com **arquiteturas diferentes** e por **times independentes**.

Apesar de tantas variáveis, as abstrações precisam permitir que as aplicações que as usem possam se coordenar nos mínimos detalhes. 
Dado que a complexidade de se implementar estas abstrações já é grande por si só, se formos reinventar a roda a cada novo sistema, não faremos muitos avanços.
Mas, como vocês bem sabem, camadas de abstração são a chave para se lidar com complexidade.
Assim, sistemas distribuídos são como cebolas, cheias de camadas e que nos fazem chorar quando precisamos descascá-las.[^ogros]
Felizmente, para cada problema que tenha que resolver, há uma boa probabilidade de que alguém já o tenha atacado e disponibilizado uma solução, de forma comercial ou não.

[^ogros]: Lembrem-se que também ![ogros são como cebolas](https://media.giphy.com/media/4RsEUfHym7tuw/200.gif) e você não quer que seu sistema seja como ogros, temperamentais e mal-cheirosos. Logo, planeje bem suas camadas de abstração.