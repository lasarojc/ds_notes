---
layout: default
title: Eleição de Líderes
parent: Coordenação
nav_order: 2
---

Assim, este agoritmo também pode não ser adequado para certas situações. Vamos tentar reacessar os problemas da primeira abordagem.
Por um lado, o uso de um líder para coordenar ações em um SD simplifica o projeto, mas, por outro, o coordenador pode se tornar um ponto único de falha, como no algoritmo de exclusão mútua centralizado.
Mas e se substituíssemos o coordenador no caso de falhas? Este é o problema conhecido como eleição de líderes.

## Eleição de Líderes

O problema da escolha de um processo centralizador pode ser posto informamente como:

---
##### Eleição de Líderes

* Procedimento pelo qual um processo é escolhido dentre os demais processos.
* Todos os processos identificam o mesmo processo como eleito.
* Uma nova eleição deve acontecer sempre que o líder corrente se tornar indisponível.

---




Experimentemos com protocolos triviais. Vamos eleger um líder na sala. Do que precisamos?

---
##### Eleição de representate de sala

* Votação?
  * Identidade
* Teste de força?
* Estabilidade?

---

PARA SER TERMINADO
==================


\begin{frame}{Identidade}
Antes de qualquer coisa, é preciso ser possível identificar um processo.

Como isso pode ser feito, na prática?

\pause

\begin{itemize}
	\item PID -- Process Identier
	\item IP -- Internet Protocol Address
	\item Socket -- IP + Port
\end{itemize}
\end{frame}


\begin{frame}{Algoritmo do Brigão/Bully}
\begin{itemize}
	\item Selecione o processo ``vivo'' com o maior identificador!
	\item Quando $p$ acha que o líder está morto:
	\begin{itemize}
		\item Envia mensagem ``eleição,$p$'' para todos os processo com identificador maior
		\item Se ninguém responde, $p$ assume como líder
		\item Se algum responde, aguarda notificação.
	\end{itemize}
	\item Quando $q$ recebe ``eleição,$p$''
	\begin{itemize}
		\item Envia ``ok'' para $p$
		\item Fica ciente de que o coordenador atual está morto
	\end{itemize}
\pause
	\item Ao assumir como líder, o processo notifica a todos os outros
	\item Se um processo falho se recupera, inicia uma eleição.
	
\end{itemize}
\end{frame}

\begin{frame}{Algoritmo do Brigão/Bully}
	\includegraphics[width=.75\textwidth]{images/bully}
	
	\href{https://my.oschina.net/juliashine/blog/88173}{Fonte}
\end{frame}

\begin{frame}{Algoritmo do Anel}
\begin{itemize}
	\item Organize os nós em um anel lógico
	\item Quando $p$ acha que o líder está morto:
	\begin{itemize}
		\item Envia mensagem \{$p$\} para ``a direita'' no anel.
		\item Se processo à direita está falho, salte-o, e assim por diante.
	\end{itemize}
	\pause
	\item Quando $q$ recebe \{$p$\}
	\begin{itemize}
		\item Envia  \{$p,q$\} para a direita.
	\end{itemize}
	\pause
	\item Quando $p$ recebe $S$ tal que $q \in S$
	\begin{itemize}
		\item Escolhe menor id em $S$, por exemplo, e anuncia como líder.
	\end{itemize}
\end{itemize}
\end{frame}

\begin{frame}{Chang \& Robert's}
\begin{itemize}
	\item Organize os nós em um anel lógico
	\item Quando $p$ acha que o líder está morto:
	\begin{itemize}
		\item Envia mensagem $p$ para ``a direita'' no anel, saltando falhos.
		\item Liga flag ``participante''
	\end{itemize}
	\pause
	\item Quando $q$ recebe $p$
	\begin{itemize}
		\item Se $p > q$, repassa $p$ para a direita.
		\item Senão, envia  $q$ para a direita.
		\item Liga flag ``participante''		
	\end{itemize}
	\pause
	\item Quando $p$ recebe $q$ da esquerda 
	\begin{itemize}
		\item Se ``participante'' está ligado, identifica $q$ como líder.
		\item Desliga ``participante''
		\item Se $p \neq q$, repassa $q$ à direita
	\end{itemize}
\end{itemize}
\end{frame}


\begin{frame}[allowframebreaks]{Yo-Yo}
\begin{itemize}
	\item Grafos incompletos
	\item Duas fases
\end{itemize}

	\framebreak

	\begin{block}{Fase 1}
	\begin{itemize}
		\item $p$ envia seu identificador para seus vizinhos.
		\item Quando $q$ recebe $p$
		\begin{itemize}
			\item Se $p>q$, adiciona aresta $q\rightarrow p$
			\item Senão, adiciona aresta $q\leftarrow p$
			\item Fonte (source)
			\item Vertedouro (sink)
			\item Interno
		\end{itemize}
	\end{itemize}
	\end{block}

\framebreak

	\begin{block}{Fase 2: Yo-\alert{Yo}}
	\begin{itemize}
	\item Fontes enviam seus identificadores para seus vizinhos.
	\item Interno espera msg de todas as arestas de entrada, escolhe o menor id, e repassa para arestas de saída.
	\item Vertedouro espera msg de todas as arestas de entrada e escolhe o menor id.
	\end{itemize}
	\end{block}

\framebreak

	\begin{block}{Fase 2: \alert{Yo}-Yo}
	\begin{itemize}
	\item Vertedouro envia S para vizinhos de onde viu menor valor e N para os demais.
	\item Interno repassa S para o vizinho correspondente ao menor id e N para os demais.
	\item Fonte espera por todos os votos. Se todos são S, continua; caso contrário, desiste.
	\item N inverte a direção das arestas em que trafega.
	\item Possível otimizar para eliminar nós e arestas irrelevantes.
	\end{itemize}
	\end{block}
\framebreak

	\includegraphics[width=.8\textwidth]{images/yoyo}*
	
\begin{small}
a) The network, b) Oriented network after setup phase, c) YO- phase in which source values are passed, d)-YO phase sending responses from sinks, e) updated structure after -YO phase. **

*\href{https://commons.wikimedia.org/w/index.php?curid=36757409}{Fonte: Hemis62 - Own work, CC BY-SA 4.0, }

**\href{https://en.wikipedia.org/wiki/Leader_election}{Fonte}
\end{small}
\end{frame}

\begin{frame}{Problemas?}
O que acontece se a rede é particionada?
\end{frame}

\begin{frame}{Split Brain}
\begin{itemize}
	\item Network Partitioning: rede dividida em duas partes incomunicáveis.
	\item Múltiplas eleições podem acontecer em paralelo.
	\item Múltiplos líderes em paralelo.
	\item Como lidar com este problema?	
	\pause
	\begin{itemize}
		\item Use primeiro algoritmo e só eleja líder após maioria de votos.
		\item Rede redundante, disco compartilhado \pause ,... centralização...\pause volta ao primeiro caso.
	\end{itemize}
\end{itemize}
\end{frame}

\begin{frame}{Detecção de Falhas}
Eleição de líderes perfeita é impossível em cenários realísticos.
\begin{itemize}
	\item Detecção de falhas perfeita é impossível...
	\item em sistemas distribuídos assíncronos (Internet)
	\item sujeitos à partições (Internet)
	\item com requisitos de disponibilidade total.
	\pause
	\item Falemos mais sobre este problema depois.
\end{itemize}
\end{frame}

#Fim da aula 12
