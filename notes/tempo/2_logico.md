---
layout: default
title: Tempo Lógico
parent: Tempo
nav_order: 2
---

# Relógio Lógicos

Nas soluções anteriores, um nó precisa esperar por muito tempo antes de usar um recurso. 
E se ele aprendesse antes que os outros nós não farão requisições? 
Que não haverão sobreposições de requisições? 
E se houvesse um relógio que avançasse não com o tempo, mas com eventos interessantes do sistema?
Esta é a ideia dos **relógios lógicos**.

---
###### Relógios Lógicos
* Envio e recepção (e possivelmente outros eventos) fazem o relógio ``ticar''.
* Processos podem usar este relógio para concordar na **ordem** dos eventos, mesmo que não no **instante** do evento.
* Não há **fonte da verdade** em termos de tempo.
* Cada processo mantém seu próprio relógio que pode ser relacionado com relógios de outros processos.

Para chegarmos aos relógios lógicos, precisamos primeiro entender a relação *Happened-Before*, proposta por Leslie Lamport em [Time, Clocks and the Ordering of Events in a Distributed System. July 5, 1978](http://amturing.acm.org/p558-lamport.pdf), que lhe rendeu um [Prêmio Turing em 2014](https://www.microsoft.com/en-us/research/blog/leslie-lamport-receives-turing-award/).
Neste artigo, se estabelece o vocabulário para falar sobre ordem de eventos em um sistema computacional, em especial um distribuído.

---
##### Happened-Before
* $a \rightarrow b$: evento $a$ aconteceu antes do evento $b$
* Considerando um único processo (thread):
  * Se $a$ foi executado antes de $b$ no processo, então $a \rightarrow b$.
* Considerando dois processos:
  * Se $a$ é o envio de uma mensagem e $b$ sua recepção, então $a \rightarrow b$.
* Transitividade faz sentido:
  * Se $a \rightarrow b$ e $b \rightarrow c$, então $a \rightarrow c$

---

Esta relação captura a **causalidade** entre eventos. Isto é, se $a \rightarrow b$ então $a$ potencialmente causou $b$ ($a$ precede $b$ em uma ordem causal).
Note que se $a \rightarrow b$ é falso e $b \rightarrow a$ é falso, então $a$ e $b$ são **concorrentes**.

Se capturarmos a causalidade de eventos, podemos usar esta informação para ordenar o se processamento, de forma a fazer sentido.
Considere o seguinte exemplo:

TODO: Exemplo e emails com pergunta e respostas.

Para que computadores possam usar a causalidade, precisamos capturar a relação de acontecer antes em um sistema.
Lamport propôs uma tal forma, que denominou relógio lógico, mas que hoje é conhecido universalmente como Relógios de Lamport.
Estes relógios permitem associar um *timestamp* a eventos de forma a se garantir a seguinte propriedade:
* seja $e$ um evento
* seja $C(e)$ o valor do relógio lógico quando associado a $e$
* se $a \rightarrow b$ então $C(a) < C(b)$

Mas como definir a função $C$?
Experimentemos a seguinte definição:
* Seja $c_p$ um contador em $p$ com valor inicialmente igual a 0.
* $C(e) = c++$ no momento em que $e$ ocorreu.
* Usamos como $ < $ a relação normal de inteiros.
Assim, cada processo conta os eventos executados localmente.
Veja um exemplo desta definição em ação.

!(LC - Primeira tentativa)(imagess/lc_cont.png)

É verdade neste cenário que se $a \rightarrow b$ então $C(a) < C(b)$?  
Observe com atenção os eventos $f$ e $k$. Para estes, a regra não é respeitada.
Para que seja, precisamos garantir que, na recepção de uma mensagem, os contadores sejam atualizados para que sejam maiores tanto que os relógios dos eventos locais quanto dos eventos que antecederam o envio da mensagem sendo recebida.
Com este ajuste, temos os Relógios de Lamport.

## Lamport Clock

* Seja $c_p$ um contador em $p$ com valor inicialmente igual a 0.
* Se o evento $e$ é uma operação local, $C(e) = ++c$ no momento em que $e$ ocorreu.
* Se o evento $e$ é o envio de uma mensagem, então $C(e)$ é enviado com a mensagem como seu timestamp.
* Se o evento $e$ é a recepção de uma mensagem com timestamp $ts$, então $C(e) = max(c,ts)+1$.

!(LC - Primeira tentativa)(imagess/lc_lamport.png)


Neste caso, temos que para quaisquer eventos $a,b$,  se $a \rightarrow b$ então $C(a) < C(b)$.


TODO: Exemplo em que não é bom o suficiente.


Se $a \rightarrow b$ então $C(a) < C(b)$. Contudo, a volta não é verdade, isto é, se $C(a) < C(b)$ então $a \rightarrow b$.
Esta propriedade é interessante na ordenação de eventos, pois evita que eventos concorrentes sejam ordenados.
Entram os relógios vetoriais.

## Relógio vetorial
Sejam $n$ processos. No processo $p$
* Seja $c_p[i], 1 \leq i \leq n$ um contador, inicialmente igual a 0.
* Se o evento $e$ é uma operação local, $c_p[p]++$ e $C(e) = c_p$ no momento em que $e$ ocorreu.
* Se o evento $e$ é o envio de uma mensagem, então $C(e)$ é enviado com a mensagem como seu timestamp.
* Se o evento $e$ é a recepção de uma mensagem com timestamp $ts$ de $q$, então
  * $c_p[i] = max(c_p[i], ts[i]), i \neq p$
  * $c_p[p]++$
  * $C(e) = c_p$

!(Relógio Vetorial)[images/lc_vc.png]

Como dito, este relógio lógico tem a seguinte propriedade: se $a \rightarrow b \RightLeftArrow C(a) < C(b)$.
Mas como é defido $ < $ para vetores?
* $V = V' \iff V[i] = V'[i], 1 \leq i \leq n$
* $V \leq V' \iff V[i] \leq V'[i], 1 \leq i \leq n$

Sejam dois eventos $e$ e $e'$
* Se $e \rightarrow e' \iff V(e) < V(e')$
* Se $V(e) \not\leq V(e')$ e $V(e') \not\leq V(e)$, são concorrentes.

Mas o que quer dizer $c_p[q] = k$?
Quer dizer que $p$ sabe que $q$ enviou $k$ mensagens.
E daí? O que pode ser feito com isso?  
Com estes mecanismos é possível implementar
* Multicast Totalmente Ordenado:
  * Multicast: mensagens são enviadas de 1 para n (comunicação em grupo)
  * Totalmente Ordenado: todos os processos entregam as mensagens na mesma ordem
* Multicast Causalmente Ordenado:
  * Causalmente Ordenado: uma mensagem só é entregue se todas as que causalmente a precedem já foram entregues.

\begin{frame}{E daí?}
\includegraphics[width=.6\textwidth]{images/06-11}

Se todos recebem na mesma ordem, o resultado final é o mesmo para todos.

\alert{State Machine Replication}
\end{frame}


\begin{frame}{Como implementar?}

\includegraphics[width=.6\textwidth]{images/06-10}

Transparente para a aplicação.

\end{frame}


\begin{frame}{Multicast Totalmente Ordenado}
\begin{itemize}
\item Fila com prioridade em cada processo.
\item Mensagens são enviadas a todos os processos e colocadas em uma fila local.
\item Mensagens recebidas são colocadas na fila local e ack é enviado de volta.
\item $p$ só entrega uma mensagem $m$ recebida de $q$, com timestamp $ts$ quando
\begin{itemize}
\item $m$ está na cabeça da fila de $p$
\item Para cada processo $q$, há uma mensagem $m'$ de $q$, $ts'$, na fila de $p$ tal $ts < ts'$
\end{itemize}
\item Canais confiáveis e FIFO.
\end{itemize}
\end{frame}

\begin{frame}{Multicast Causalmente Ordenado}
\begin{itemize}
\item Mensagens são enviadas a todos os processos.
\item $p$ incrementa $c_p[p]$ somente no envio de mensagens.
\item $p$ só entrega uma mensagem recebida de $q$, com timestamp $ts$ quando
\begin{itemize}
\item $ts[q] = c_p[q]+1$
\item $ts[k] \leq c_p[k], k \neq q$
\end{itemize}
\end{itemize}

\includegraphics[width=.5\textwidth]{images/06-13}
\end{frame}

\begin{frame}{Multicast Causalmente Ordenado}

\includegraphics[width=.5\textwidth]{images/06-13}

Considere $c_{P_2}[0,2,2]$ e $ts=[1,3,0]$, de $P_0$. O que $P_2$ está esperando? Como age ao receber mensagem com $ts$?
\end{frame}



\subsection{Relógios Híbridos}

\begin{frame}{Google TrueTime}
%\includegraphics[width=.7\textwidth]{images/lc_hybrid}

\href{https://cloud.google.com/spanner/docs/true-time-external-consistency}{Fonte}
\end{frame}

\begin{frame}{Hibrido}
\includegraphics[width=.7\textwidth]{images/lc_hybrid}

Onde se lê 3,13, leia-se 3,10,3.

\href{http://muratbuffalo.blogspot.com.br/2014/07/hybrid-logical-clocks.html}{Fonte}
\end{frame}


%\section{Exclusao Mútua Revisitada}

%\begin{frame}{Exclusão Mútua Revisitada}
%TODO: retorno da exclusão mútua: Lamport, Ricart e agrawalla, and Maekawa (https://www.coursera.org/learn/cloud-computing-2/lecture/GMHYN/2-4-maekawas-algorithm-and-wrap-up)

%TODO \href{http://www.cs.cmu.edu/~dga/15-440/F10/lectures/Distributed-Mutual-Exclusion-slides.pdf}
%\end{frame}
