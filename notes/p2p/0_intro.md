---
layout: default
title: P2P
nav_order: 5
has_children: true
---

Neste capítulo discutiremos mostrando como as DHT evoluiram do Chord,  essencialmente de uso acadêmico, para aplicações industriais na forma de bancos de dados como o Cassandra e o DynamoDB.

## Sistemas P2P

TODO: Expand 
======= 

* Características
  * Arquitetura decentralizada
  * Não há distinção de papéis entre nós, ou papéis tem igual relevância.
  * Não há servidores e clientes, mas iguais (pares) na computação.
  * Nós entram e saem frequentemente.
  * Redes sobrepostas (\emph{overlay}).
* Objetivos
  * Alto desempenho
  * Escalabilidade geográfica global
  * Auto-administração
  * Tolerância a falhas



TODO: \subsection{CAN}



\subsection[Estrutura]{Redes Estruturadas e Não estruturadas}

\begin{frame}{Redes Sobrepostas}
\begin{block}{Estruturadas}
	\begin{itemize}
		\item Estrutura bem definida
		\item Adição de dados é lenta
		\item Adição de nós é lenta
		\item Busca por dados é rápida
	\end{itemize}
\end{block}
\begin{block}{Não Estruturadas}
	\begin{itemize}
		\item Estrutura aleatória
		\item Adição de dados é rápida
		\item Adição de nós é rápida
		\item Busca por dados lenta
	\end{itemize}
\end{block}
\end{frame}


\begin{frame}{Estruturadas}
\includegraphics[width=.45\textwidth]{images/05-04}	

\begin{itemize}
	\item Inserção de nós requer atualização de informações
	\item Busca usa tabela de roteamento
\end{itemize}
\end{frame}

\begin{frame}{Estruturadas}
\includegraphics[width=.7\textwidth]{images/02-08}	

\begin{itemize}
	\item Cada nó é responsável por uma área
	\item A inserção/remoção de um nó causa um split/merge
\end{itemize}
\end{frame}

\begin{frame}{Não estruturada}
\includegraphics[width=.6\textwidth]{images/unstructured}	

\begin{itemize}
	\item Inserção afeta apenas nós contactados
	\item Busca requer varredura (p.e., inundação, random walk), ou índice
\end{itemize}


\href{http://gossple2.irisa.fr/~akermarr/LSDS-EPFL-unstructured.pdf}{Fonte}
\end{frame}


\begin{frame}{De não estruturada a estruturada}
%\includegraphics[width=.6\textwidth]{images/3d_thorus}	
%\href{https://clusterdesign.org/torus/}{Fujitsu and RIKEN, 2009}

\includegraphics[width=.6\textwidth]{images/02-11}	

\pause

Seleção de links segundo critério

\includegraphics[width=.6\textwidth]{images/02-10}	

\pause
Seja uma grade $N \times N$. Mantenha nós mais próximos:
\begin{itemize}
	\item $a = (x,y)$, $b = (x', y')$
	\item $dx_{a,b} = min(|x - x'|, N - |x - x'|)$
	\item $dy_{a,b} = min(|y - y'|, N - |y - y'|)$
	
\end{itemize}
\end{frame}

