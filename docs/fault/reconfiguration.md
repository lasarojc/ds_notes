# Pertinência de grupo

E se mais de um, de um mesmo conjunto de réplicas, falhar? 
Embora seja pequena a probabilidade de dois nós de um mesmo grupo falharem em instantes próximos, dado tempo suficiente, qualquer evento com probabilidade diferente de 0 acontecerá.
Precisamos de uma forma de trocar nós da aplicação que falharam por novos nós, ou seja, de formas de resolver o problema de **Pertinência de Grupo** (*Group Membership*) em um sistema distribuído.


## Visões
Seja $G$ o conjunto dos componentes do sistema; $G$ é a **visão** deste sistema.
No exemplo da imagem a seguir, $G$ consiste inicialmente de apenas o processo $p$, a *semente* do sistema.
Na sequência, outros processo vão se unindo ao grupo através de mudanças de visão, e quando processos falham, são excluídos pelo mesmo processo, mesmo que depois voltem a funcionar e novamente a compor o grupo.

![](../drawings/view_change.drawio#0)

###### Impossibilidade de Detecção de Falhas
Como já vimos, em um sistema distribuído assíncrono, é impossível distinguir com toda certeza um processo falho de um que está lento.
Assim, como é que no exemplo anterior os processos decidem corretamente excluir $p$ e $q$?
A verdade é que não há como ter certeza e o sistema ou aceita a possibilidade de injustiça e muda quando suspeitar de uma falha, ou corre o risco de ficar esperando *ad eternum* e não mudar, mesmo quando uma falha aconteceu.
Se a falha de fato não ocorreu, uma vez que o processo excluído estiver apto, ele encadeará uma nova visão que novamente o inclua.

![](../drawings/view_change.drawio#1)


## Sincronismos Virtual
**Sincronismo virtual** é uma especificação de como combinar o problema de gerenciamento de grupos com protocolos de comunicação em grupo, mais especificamente das garantias de ordenação e confiabilidade na entrega de mensagens quando tanto remetentes quanto destinatários podem sair ou ser excluídos do grupo a qualquer momento.
As propriedades a serem garantidas por um protocolo de sincronismo virtual são as seguintes:

* Se uma mensagem é enviada em uma visão, ela só pode ser entregue naquela visão.
* Se uma mensagem é entregue a um processo correto em uma visão, então é entregue a todos os processos corretos naquela visão.
* Se um processo não recebe a mensagem, ele não estará na próxima visão.
* Ao entrar em uma visão, o processo recebe o estado dos outros processos e seu estado se torna equivalente ao de um processo que recebeu todas as mensagens já entregues.

Por exemplo, se um processo $p$ envia uma mensagem $m$ e na mudança de visão seguinte $p$ é excluído, é preciso garantir que ou todos os outros processos recebem $m$ antes de $p$ ser excluído, e portanto ser um remetente válido, ou $p$ é excluído e nenhum dos processo recebe $m$.
Este comportamento faz com que as mudanças de visão sejam consistentes com a entrega de mensagens ou, em outras palavras, sincronizadas com a troca de mensagens, o que justifica o nome da técnica.

Ainda quanto ao relacionamento entre mudança de visões e comunicação em grupo, a própria troca pode acontecer usando comunicação em grupo.
Por exemplo, suponha que quando um processo $p$ queira entrar para a visão ele primeiro contate algum membro $q$ do grupo corrente e solicite sua inclusão.
Neste momento $q$ envia uma mensagem $m$ que comanda **incluir $p$ na visão** para todos os outros processos usando difusão totalmente ordenada.
As outras mensagens são totalmente ordenadas com $m$, satisfazendo parte do critério colocado acima.

A exclusão de um processo acontece de forma semelhante; se $q$ acha que $p$ deve ser excluído, então envia uma mensagem $m$ que comanda **excluir $p$ na visão**.
Se, além da ordem de exclusão, a mensagem $m$ também informar a visão em que $m$ foi enviada (ou informação equivalente), os processos saberão julgar que qualquer mensagem cujo remetente é $p$ e que seja entregue depois de $m$ deve ser ignorada.

![](../drawings/view_change.drawio#2)



###### ISIS Toolkit
O [ISIS Toolkit](http://www.cs.cornell.edu/Info/Projects/Isis/) é um sistema de sincronismo virtual tolerante a falhas desenvolvido por Ken Birman e outros[^isis] e que foi usado em diversos projetos na indústria e academia (NY Stock Exchange, Swiss Exchange, US Navy) pelo menos até 2009.
Este sistema influenciou e inspirou diversos outros, como Zookeeper, Totem, Horus, Transis, Spread, Ensamble, JGroups, Appia, QuickSilver e vSynch (inicialmente denominado ISIS 2 mas que foi renomeado por razões óbvias).

[^isis]: ISIS: An Environment for Constructing Fault-Tolerant Distributed Systems. Kenneth Birman, D. Skeen, A. El Abbadi, W. C. Dietrich and T. Raeuchle. May 1983.


###### Transferência de estado
A última propriedade especificada acima para o sincronismo virtual diz respeito à transferência de estado entre processos que entram e permanecem na visão.
Novos estados são gerados por eventos relevantes, como a recepção de uma mensagem com um comando a ser executado pelo processo.

![](../drawings/view_change.drawio#3)

Com parte do procedimento de mudança de visão, toda a informação necessária para que o novo processo comece a processar novos eventos e progrida em sua computação deve ser transferido para os processos que entram na visão.
Processos que estão saindo da visão, ao retornarem, devem ter seu estado também atualizado.


###### Partições primárias
Outro ponto importante, também ligado à questão da detecção de falhas, é o fato da rede poder ser particionada, levando processos em uma partição a supeitar dos processos que estão na outra.
No exemplo, após a partição, cada conjunto de processos gera uma nova visão, incompleta e inconsistente do sistema.

![](../drawings/view_change.drawio#4)

No exemplo, quando a partição da rede desaparece, aos processos sobra o trabalho de unificar os estados divergentes.
Para evitar esta situação, pode-se adotar um modelo de partição primária, em que somente pode ser formada uma nova partição se esta incluir pelo menos uma maioria dos processos pertencentes à visão anterior (ou que pelo menos uma maioria dos processos seja envolvida no processo de decisão da nova partição).

![](../drawings/view_change.drawio#5)

## Referências

* http://courses.cs.vt.edu/cs5204/fall05-gback/lectures/Lecture8.pdf
* [Building Secure and Reliable Network Applications](http://www.gsd.inesc-id.pt/~ler/docencia/tfd0405/bib/BSRNA.pdf)
