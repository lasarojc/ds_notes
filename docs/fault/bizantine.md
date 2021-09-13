# Falhas Bizantinas

<!--
Uma história de três exércitos -- Versão 2}
Exércitos estão às portas de Bizâncio, aka Constantinopla, aka Istambul.

Todos os exércitos tem que atacar em conjunto ou se retirar em conjunto.

Cada exército é comandado por um General. Alguns destes preferem atacar, enquanto outros preferem se retirar.

Alguns generais podem ter sido comprados, e mandar mensagens discrepantes para os outros, ou simplesmente não mandar mensagens.

Fonte: \href{http://research.microsoft.com/en-us/um/people/lamport/pubs/byz.pdf}{Lamport, L.; Shostak, R.; Pease, M. (1982). "The Byzantine Generals Problem" (PDF). ACM Transactions on Programming Languages and Systems. 4 (3): 382–401. doi:10.1145/357172.357176.}


Generais e Tenentes
Problema pode ser mudado para:

	* Comandante envia ordem.
	* Todos os tenentes leais executam ordem recebida.
	* Comandante pode ser traidor.




Generais e Tenentes
Suponha 3 exércitos. \\
Comandante (traidor) diz "Ataque!" Tenente A e "Retirada!" tenente B.\\
Ou \\
Comandante diz "Ataque!" a ambos. Tenente A segue a ordem mas B se retira.

 E se os tenentes trocarem informações?

 Como diferenciar casos em que Comandante ou Tenente é traidor?





Generais e Tenentes
Só há solução se mais de $\frac{2}{3}$ dos Generais/Tenentes são leais.


%http://www.drdobbs.com/cpp/the-byzantine-generals-problem/206904396?pgno=5

Comunicação}

	* Toda mensagem enviada é entregue corretamente.
	* A ausência de mensagem pode ser detectada (mensagem Null é entregue no lugar) (Sistema síncrono)




4/0}
General manda ordens.

Ausência de ordem = Retirada

Tenente repassa ordens

Maioria de comandos é comando a ser seguido



4/0}
General manda ordens.

Ausência de ordem = Retirada

Tenente repassa ordens

Maioria de comandos é comando a ser seguido



Comunicação}

	* Toda mensagem enviada é entregue corretamente.
	* Toda mensagem é assinada.
	* A ausência de mensagem pode ser detectada (mensagem Null é entregue no lugar) (Sistema síncrono)


 É possível detectar inconsistências e processos bizantinos.



%http://cs.brown.edu/courses/cs138/s16/lectures/19consen-notes.pdf


-->
