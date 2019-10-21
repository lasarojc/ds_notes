---
layout: default
title: Chord
parent: DHT
grand_parent: P2P
nav_order: 1
---


\begin{frame}{Chord}
\begin{beamerboxesrounded}{Chord e derivados}
	\begin{itemize}
		\item CSAIL (MIT) -- 2001
		\item Anel lógico
		\item Identificadores de nós: m-bits
		\item Identificadores de dados: m-bits
		\item Dado associado a um nó/identificador
		\item Dado com chave $k$ é responsabilidade do nó com menor identificador $i \geq k$, aka sucessor de $k$ ($i = suc(k)$)
	\end{itemize}
\end{beamerboxesrounded}

\pause
\includegraphics[width=.45\textwidth]{images/02-07}	
\end{frame}

\begin{frame}{Chord }
\begin{beamerboxesrounded}{Chord e derivados}
Roteamento?
\end{beamerboxesrounded}

\includegraphics[width=.45\textwidth]{images/02-07}	
\end{frame}


\begin{frame}{Chord}
\begin{beamerboxesrounded}{Roteamento eficiente}
	\begin{itemize}
		\item Sucessores
		\item Finger-table
	\end{itemize}
\end{beamerboxesrounded}

\includegraphics[width=.45\textwidth]{images/05-04}	
\end{frame}

\begin{frame}{Chord}
\begin{itemize}
	\item $FT_p[i] = suc(p+2^{i-1})$\\
	$FT_p[i]$ aponta para primeiro nó que sucede $p$ por pelo menos $ 2^{i-1}$
	\item Para achar nó responsável por $k$, $p$ encaminha requisição para nó na entrada $j$ tal que $FT_p[j] \leq k < FT_p[j + 1]$
\end{itemize}

\includegraphics[width=.45\textwidth]{images/05-04}	
\end{frame}

\begin{frame}{Chord}
\begin{itemize}
	\item Menor/Maior?
	\item 21 procurando 31?
\end{itemize}

\includegraphics[width=.45\textwidth]{images/05-04}	
\end{frame}

\begin{frame}{Churn}
\begin{itemize}
	\item Entrada e saída de nós
	\begin{itemize}
		\item Se sou o nó X e quero entrar na rede
		\item Quem é o sucessor S de X?
		\item Quem é o antecessor A do sucessor de X?
		\item Reconfigure S e A
	\end{itemize}
\end{itemize}
\end{frame}

Reorganização dos nós exige movimentação de dados. Como minimizar o impacto da reorganização? Ou pelo menos balanceá-la?

\begin{frame}{Churn}
\begin{itemize}
	\item Movimentação de dados
	\begin{itemize}
		\item Se sou novo na rede, parte dos ``meus'' dados estão com meu sucessor.
		\item Copie o dados
		\item Como satisfazer requisições durante o processo?
	\end{itemize}
	\pause
	\item Sucessor fica sobrecarregado provendo dados
	\item Nós virtuais
	\begin{itemize}
		\item Cada nó físico assume vários identificadores
		\item Movimentação é distribuída entre nós
	\end{itemize}
\end{itemize}
\end{frame}

\begin{frame}
Consistent hashing
\end{frame}



Pela replicação dos dados, consegue-se minimizar o impacto de falhas no sistema. 
\begin{frame}{Falhas}
\begin{itemize}
	\item Além do sucessor, conhecer sucessor do sucessor
	\item Em caso de suspeita de falha, reorganize ponteiros
	\item Perda de dados
	\pause
	\item Replicação para vizinhos
	\item Degradação graciosa
\end{itemize}
\end{frame}

\begin{frame}{Fator de replicação: $r$}
\begin{itemize}
	\item Para cada dado, há $r$ cópias
	\item Conflito: aquele que tem o maior \emph{vector clock} vence
	\item Versões concorrentes são resolvidas com abordagem genérica: pergunte ao operador.
\end{itemize}
\end{frame}





Nestes ``bancos de dados'' distribuídos, pode-se optar por uma alta latência ou por um modelo de consistência eventual, no caso bem comportado; no caso de falhas, espera-se que a consistência seja eventualmente alcançada, mas não se pode garanti-la.
\begin{frame}{Aplicações}
\begin{itemize}
\item Replicação + assincronismo = inconsistências
\item consistência eventual 
\end{itemize}
\end{frame}

\subsection{DynamoDB}

Este modelo é adequado a algumas aplicações, como o carrinho de compras da Amazon.com
\begin{frame}{Dynamo DB -- Consistência Eventual}
\begin{itemize}
\item \emph{Shopping Cart} da Amazon.com
\item Chave: identificador do usuário
\item Valor: conteúdo do carrinho de compras
\item Modificações do carrinho criam novas versões\\  \alert{Consistência eventual}
\item O que acontece no caso de falhas? Atrasos? \\   \alert{Múltiplas versões!}
\item E no caso de o carrinho ficar errado?
\end{itemize}

\url{http://aws.amazon.com/dynamodb/}
\end{frame}

O modelo de blob do Dynamo impõe algumas restrições de uso para o desenvolvedor.

\begin{frame}{Esquema de Dados}
\begin{itemize}
\item Valor no DynamoDB é um \alert{blob}
\item Serialização e desserialização é um problema menor
\item Entradas tem um Version Vector como versão
\item Quais os dados com chave entre X e y?
\end{itemize}
\end{frame}
