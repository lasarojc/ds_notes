# Modelos de Consistência
Quando implementamos um sistema distribuído, precisamos estar cientes de que **há custos inerentes** desta distribuição, introduzidos pela coordenação das partes que formam o todo.
E também precisamos estar cientes de é possível pensar em **diferentes níveis de coordenação**, com diferentes custos.

???sideslide "CDN"
    * Conteúdo é colocado próximo aos clientes. 
    * Conteúdo estático ou majoritariamente determinístico. 
    * Um pequeno atraso na replicação é tolerado.
    * Atualização acontece infrequentemente.

Considere, por exemplo, uma ***Content Delivery Network*** (CDN), um sistema que replica os dados de um "provedor de conteúdo" para colocar tal conteúdo próximo aos clientes.
A replicação acontece de forma adaptativa, dependendo de onde os clientes estiverem e dos custos envolvidos no processo.

[![](../images/cdn.jpeg)](https://www.creative-artworks.eu/why-use-a-content-delivery-network-cdn/)


Se o conteúdo é majoritariamente estático, entregar esta funcionalidade é "simples", implicando apenas em um pequeno atraso entre a publicação de novo conteúdo e sua disponibilização para os usuários.
Neste caso, um protocolo de difusão totalmente ordenada, como o RAFT, pode ser usado para garantir que todos os servidores vejam as mesmas mudanças, na mesma ordem, e que alcancem o mesmo estado final em algum momento.

Entretanto, os protocolos de difusão totalmente ordenada tem um alto custo para atualizar as réplicas e este custo pode ser demasiado para a aplicação.
Neste caso, podemos tentar relaxar os requisitos da aplicação, permitindo que atualizações sejam vistas em ordens diferentes por diferentes "réplicas", agora com aspas.
Por exemplo, se em vez de difusão atômica usássemos IP-Multicast, teríamos uma atualização mais barata dos dados, mas alguns valores escritos poderiam nunca ser vistos.
Dependendo de como estes relaxamentos sejam feitos, serão implementados diferentes **modelos de consistência**.

Embora não seja necessário, para falarmos sobre modelos de consistência falaremos sobre bancos de dados pois é algo próximo da realidade da maioria dos estudantes e torna mais fácil o entendimento do tópico.
Um banco de dados pode ser pensado, em sua forma mais simplista, como um conjunto de variáveis nas quais valores são armazenados.
Assim, clientes do banco de dados essencialmente executam comandos como **$X$ recebe 'João'** e **$Y$ recebe 'joao arroba hmail.com'**.
Obviamente que $X$ e $Y$ não precisam ser declarados antes da primeira escrita, assim como chaves primárias não são declaradas até que sejam usadas, e que o valor associado a uma variável pode ter várias partes, como **"{'Endereço':'Av 1, número 2', 'Profissão':'Computeiro'}** e cada parte um tipo. Essa á uma simplificação dos bancos de dados, mas uma simplificação poderosa.

![Single DB](../drawings/disdb.drawio#0)

Quando um processo se comunica com um banco de dados, ele o faz com certas expectativas quanto ao funcionamento deste banco.
Por exemplo, ao escrever um dados no banco, independentemente de como o banco é implementado, o cliente geralmente espera que as escritas aconteçam na ordem em que as disparou e que, ao ler uma variável, lhe seja retornado o "último" valor escrito na mesma.

![Single DB](../drawings/disdb.drawio#2)

Esta expectativa é independente do banco de dados ser implementado de forma distribuída ou não. Isto é, mesmo que os dados armazenados no banco sejam particionados ou replicados entre vários nós, o cliente espera que o banco tenha comportamento consistente com o de um banco não distribuído e retorne ou aquilo que escreveu ou algo mas recente.

![Single DB](../drawings/disdb.drawio#1)

???sidesline "Níveis de Consistência"
      * Consistência forte: leituras sempre retornam a versão mais recente do dado sendo lido.
           * Propagação instantânea ou *locks* dos dados sendo manipulados enquanto a propagação acontece.
      * Consistência fraca: leituras retornam algum dado escrito anteriormente.
           * Qualquer coisa vale
      * Consistência eventual: se não houver novas escritas, a partir de algum momento as leituras retornam a versão mais recente do dado sendo lido.
           * Propagação acontece no segundo plano

A expectativa, ou melhor, a forma como o banco de dados age dada uma interação com o cliente, ou clientes, é o que denominamos um **modelo de consistência**.
Em particular, a expectativa descrita acima é denominada **linearabilidade**, também conhecida como **consistência forte**.
Com o advento do NOSQL, mais e mais desenvolvedores buscam modelos alternativos, por exemplo, consistência ***eventual***, em que há a garantia de que atualizações estarão disponíveis a partir de algum momento para a leitura, mas não há uma definição clara de quando isso ocorrerá.[^eventual]

[^eventual]: Enquanto no Português *Eventual* quer dizer **possivelmente**, no inglês quer dizer **em algum momento** não determinado, mas vindouro.

![eventual meme](../images/eventual-meme0.png)

Enquanto consistência eventual traz melhoras de desempenho, trabalhar com este modelo implica em muito mais complexidade no desenvolvimento dos sistemas que usam o banco.

![eventual meme](../images/eventual-meme.png)


Um terceiro modelo geral de consistência seria a consistência **fraca**, em que a única garantia é de que o valor retornado nas leituras foi escrito em algum momento.
Na verdade, podemos pensar nos modelos de consistência como um espectro com **forte** e **fraca** nos extremos e diversos modelos, incluindo ***eventual***, no meio.
Diferentes bancos de dados oferecem diferentes modelos, com nomes parecidos ou até iguais e é preciso conhecer o que cada sistema está entregando para poder utilizá-lo da forma correta.

[![](../images/consistency-models.png)](https://arxiv.org/abs/1512.00168)



Além disso, os modelos podem ser divididos em **Centrados nos Dados**  e **Centrados nos Clientes**, sendo que no primeiro o modelo é definido em termos das garantias de consistência dos dados e, no segundo, em termos das garantias sobre o que os clientes vêem.[^util]

[^util]: Caso esteja se perguntando se este modelo e estudo tem alguma serventia para você, afinal nos bancos de dados com que trabalhou ou trabalha as operações são agrupadas em **transações** e não executadas individualmente e as transações garantem ACID, lhe asseguro que sim e que falaremos em transações mais adiante.