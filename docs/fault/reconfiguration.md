# Reconfiguração

E se mais de um, de um mesmo conjunto de réplicas, falhar? 

Embora seja pequena a probabilidade de dois nós de um mesmo grupo falharem em instantes próximos, dado tempo suficiente, qualquer evento com probabilidade diferente de 0 acontecerá.

Precisamos de uma forma de trocar nós da aplicação que falharam por novos nós.
Este é problema denominado Pertinência de Grupo (*Group Membership*)

???todo "TODO"

<!--

Para não correr o risco, retire o processo falhos do grupo e coloque outro no lugar!

I.e., mude a visão que o sistema de quem é o grupo.


![](../images/vc.png)

Fonte: ![Paul Krzyzanowski](https://www.cs.rutgers.edu/~pxk/417/notes/virtual_synchrony.html)

$G$ é o grupo de processos participando do sistema, é a Visão do Sistema.


Inicialmente, $G$ consiste de apenas o processo $p$, como o processo que cria o cluster no Atomix. Na sequência, outros processo vão se unindo ao grupo através de View Changes. Uma vez que $p$ e $q$ estão no grupo, inicia-se a comunicação entre eles. Quando $r, s$ e $t$ aparecem, também entram no grupo por meio de uma nova visão.

Finalmente, quando ambos $p$ e $q$ falham, os outros processo os excluem da visão, e continuam funcionando normalmente.


###### Impossibilidade de Detecção de Falhas
Em um sistema distribuído assíncrono, é impossível distinguir com toda certeza um processo falho (parou de funcionar) de um que está lento.

Como decidir se mudar ou não de visão?


Ou aceita a imprecisão e muda quando suspeitar de uma falha, ou corre o risco de ficar esperando \emph{ad eternum} e não mudar, mesmo quando uma falha aconteceu.

Quando suspeitar de falha, reporte suspeita a outros processos, que também passarão a suspeitar.

Tome decisão baseado na suspeita, isto é, troque de visão quando houver suspeita.

Pague o preço de uma suspeita errada, isto é, quando um processo for removido da visão indevidamente, adicione-o novamente.



###### Sincronismos Virtual
Gerenciamento de Grupo/Group Membership e Comunicação em Grupo

* Processos se unem ao grupo
* Processos saem do grupo
* Processos enviam mensagens para o grupo
* Diferentes ordenações		
		* Atomic Multicast
		

Visão de Grupo

* Visão: conjunto de processos no sistema.
* Multicast feito para processos na visão.
* Visão é consistente entre os processos.
* Entrada e saída de processos muda a visão.

Eventos

* Mensagem
* Mudança de Visão
* Checkpoint



Sincronismo Virtual

Deve satisfazer

* Se uma mensagem é enviada em uma visão, ela só pode ser entregue naquela visão.
* Se uma mensagem é entregue a um processo correto em uma visão, então é entregue a todos os processos corretos naquela visão.
* Se um processo não recebe a mensagem, ele não estará na próxima visão.
* Ao entrar em uma visão, o processo recebe o estado dos outros processos e seu estado se torna equivalente ao de um processo que recebeu todas as mensagens já entregues.


A troca de Visão é uma barreira.


###### ISIS Toolkit
	
Sistema de Sincronismo Virtual tolerante a falhas desenvolvido por Ken Birman, Cornell University (\url{http://www.cs.cornell.edu/Info/Projects/Isis/})\\

	ISIS: An Environment for Constructing Fault-Tolerant Distributed Systems. Kenneth Birman, D. Skeen, A. El Abbadi, W. C. Dietrich and T. Raeuchle. May 1983.
	

	* 100.000's/s
	* Em uso até 2009
	* NY Stock Exchange
	* Swiss Exchange
	* US Navy
	* Precursos de sistemas como Zookeeker
	* Totem, ISIS, Horus, Transis (Partições), \alert{Spread}, \alert{Ensamble}, \alert{JGroups}, Appia, QuickSilver, vSynch (née ISIS 2)



Difusão Totalmente Ordenada

	* Corretude: Se um processo $p$ envia uma mensagem $m$ para processos no grupo $G$, então se $p$ não falha, todos os processos corretos em $G$ recebem a mensagem.
	
	* Acordo: Se um processo correto em $G$ recebe uma mensagem $m$, então todo processo correto em $G$ recebe $m$
	
	* Ordenação: Se um processo recebe mensagem $m$ e depois $n$, então qualquer processo que receba a mensagem $n$ deve primeiro receber $m$
	
	* Validade: Somente mensagens difundidas são entregues.


E se mandarmos mensagens do tipo ``A partir da entrega desta mensagem, o grupo de processos é $G$.''


Sincronismo Virtual
Deve satisfazer

	* Se uma mensagem é enviada em uma visão, ela só pode ser entregue naquela visão.\\
	Mensagens de troca de visão podem incrementar um contador\\
	Mensagens normais carregam o valor atual do contador\\
	Mensagem descartada se valor na mensagem é maior contador no destinatário
	
	* Se uma mensagem é entregue a um processo correto em uma visão, então é entregue a todos os processos corretos naquela visão.\\
	Pela difusão, se a mensagem de troca for entregue para um processo, será entregue para todos os corretos, na mesma ordem
	Se mensagem comum for entregue antes para algum, será entregue ante para todos.
	
	* Se um processo não recebe a mensagem, ele não estará na próxima visão.\\
	Se um processo não recebe uma mensagem comum que foi entregue pelos outros, então ele não troca de visão.

	* Ao entrar em uma visão, o processo recebe o estado dos outros processos e seu estado se torna equivalente ao de um processo que recebeu todas as mensagens já entregues.\\
	Caso contrário, não haveria porquê trocar os processos



State Transfer
\includegraphics[width=.7\textwidth]{images/state_transfer}


\href{http://www.gsd.inesc-id.pt/~ler/docencia/tfd0405/bib/BSRNA.pdf}{Building Secure and Reliable Network Applications}



Difusão Atômica $\equiv$ Sincronismo Virtual?}
Seria uma boa aproximação, mas que poderia ser relaxada. 

Em certas aplicações, FIFO ou Causal seriam suficientes dentro da visão, desde que a mensagem de mudança da visão seja totalmente ordenada com as comuns.



Particionamento}
E se dois subconjuntos mutuamente exclusivos se formarem e criarem visões independentes?

 \emph{Primary Partition Model} -- Somente a partição primária pode mudar de visão.

 Lembram-se que no Raft somente uma partição com uma maioria de processo pode decidir? É exatamente a mesma situação, pois os processos estão chegando a um Consenso sobre quem é a nova visão.



Extended Virtual Synchrony}
\emph{Primary Partition Model} -- Não é adequado a uma rede geograficamente distribuída (Internet scale).

 Lembram-se que no Raft somente uma partição com uma maioria de processo pode decidir? É exatamente a mesma situação, pois os processos estão chegando a um Consenso sobre quem é a nova visão.


É possível que no trabalho dois, alguns de vocês tenham tentado gerar locks do sistema para manipular objetos distribuídos no sistema. Esse locks são perigosos por quê processos pode travar/quebrar/falhar e nunca liberarem os locks. O uso de um algoritmo VS poderia ser usado para resolver o problema.\right 




http://courses.cs.vt.edu/cs5204/fall05-gback/lectures/Lecture8.pdf



-->
