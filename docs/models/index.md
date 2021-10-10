# Modelos
Agora que já estão convencidos de que não temos alternativas à distribuição, conhecem algumas das arquiteturas algumas das tecnologias usadas, vamos dar um passo para trás e para entendermos os fundamentos necessários à criação de sistemas escaláveis e tolerantes a falhas.
Comecemos por consider um problema abstrato, que pode ser mapeado para um problema de computação distribuída.

## Uma história de três exércitos
**Era uma vez** uma cidade estado no alto de uma montanha. 
A despeito de sofrer de falta de água, afinal, estava no alto de uma montanha, a cidade era invejada pelos vizinhos.
Como a cidade era muito bem fortificada, ela poderia se defender de qualquer **ataque em uma única frente**. 
Se atacada em **duas frentes**, contudo, cairia.
Sabendo disso, o rei de uma das cidades vizinhas resolveu tomar a cidade e repartiu suas forças em **dois exércitos** sob o comando de Alice (a sociedade era feminista) e Bastião (sim, Bastião, não Bob).[^2generalsparadox]

[^2generalsparadox]: Esta é uma variação do problema de coordenação de *gangsters* apresentado no em [Some constraints and trade-offs in the design of network communications](https://doi.org/10.1145%2F800213.806523)

![Paradoxo dos 2 Generais](../drawings/2generals.drawio#0)

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

![Paradoxo dos 2 Generais](../drawings/2generals.drawio#1)

Observe que, do ponto de vista de Alice, uma execução do algoritmo em que a nenhuma mensagem é perdida, é indistinguível de uma execução em que a $n$-ésima mensagem é perdida.

![Paradoxo dos 2 Generais](../drawings/2generals.drawio#2)

Dado que ao final da primeira execução completa **Alice ataca**, no final da execução onde a mensagem $n$ é perdida, Alice também deve atacar.
Mas se o algoritmo é correto, então também **Bastião ataca**, mesmo sem ter recebido a enésima mensagem. Logo, a enésima mensagem é desnecessária ao algoritmo, que deve funcionar com $n-1$ mensagens.

Repetindo-se o argumento mais $n-1$ vezes, temos que o algoritmo deve funcionar com zero mensagens, o que é um **absurdo**. Logo não existem algoritmos corretos para o problema como definido, isto é, em que mensagens podem ser perdidas; é **impossível** resolver o problema.

!!!info inline end "Impossibilidades"
    Impossibilidade de resolução x resolução na prática.

Apesar de ser impossível resolver este problema aparentemente simples, devemos fazê-lo frequentemente no mundo real. 
A resposta está no que consideramos como premissas válidas no ambiente em que tentamos solucionar o problema e quais exatamente são as propriedades de uma solução aceitável.


## Impossibilidades
Quando dizemos que é impossível resolver um problema não queremos dizer que é impossível resolver o problem em quaisquer circunstâncias, mas apenas nas circunstâncias nas quais a prova foi feita.

No caso do exemplo anterior, a impossibilidade implica que é impossível produzir um algoritmo que **sempre levará a uma resposta correta** com um número finito de mensagens, como havia sido assumido.
Isto quer dizer, excluindo-se algoritmos que sempre levarão a respostas incorretas, ainda podemos produzir algoritmos que ou às vezes **levarão a respostas incorretas** ou que, mesmo que nunca levem a respostas incorretas, às vezes **não levarão a respostas** alguma; ambos podem ser úteis na prática.

Por exemplo, ainda no problema dos três exércitos tentando tomar a cidade, suponha que em vez de mandar um único mensageiro com a ordem de ataque, Alice envie 100, ou 200, ou 1000.
A **confiança** de Alice de que Bastião também atacaria, seria muito maior e não precisaria receber uma confirmação de entrega de mensagens.
Esta abordagem faria com com que o ataque funcionasse com uma **alta probabilidade** $P$, mas com uma pequena probabilidade $P-1$ de levar a um ataque fracassado, onde $P$ pode ser feita **tão grande quanto se "queira"**.

Resultados de impossibilidade abundam na área de computação distribuída[^impossibilidades] e não podem nos desencorajar de continuar a buscar soluções práticas.
Frequentemente a solução está em identificar premissas mais "amigáveis" que possam ser assumidas e, com isso, enfraquecer o problema.

[^impossibilidades]: [Hundred Impossibility Proofs for Distributed Computing](https://groups.csail.mit.edu/tds/papers/Lynch/MIT-LCS-TM-394.pdf), [Impossibility Results for Distributed Computing](https://doi.org/10.2200/S00551ED1V01Y201311DCT012)