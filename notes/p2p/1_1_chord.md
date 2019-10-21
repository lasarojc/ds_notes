---
layout: default
title: Chord
parent: DHT
grand_parent: P2P
nav_order: 1
---

# Chord

Chord é uma sistema P2P de múltiplas aplicações desenvolvido pelos membros do CSAIL, do MIT, e publicado em 2001. Desde então, inspirou diversos outros sistemas, tornando-se sinônimo com P2P.

No Chord, cada nó tem um identificador único de **$m$ bits**, gerado aleatoriamente. Como $m$ normalmente é grande, com mais de uma centena de bits, a probabilidade de dois nós terem o mesmo identificar é desprezível.

O Chord mantém uma rede estruturada na forma de um **anel lógico**, em que os nós aparecem ordenadamente de acordo com seus identificadores.
A figura a seguir mostra as posições disponíveis no anel de um Chord com 4 bits (sem utilidade prática).

![](images/02-07.png)

Dados são também identificados por uma chave de **$m$ bits**. Esta chave é gerada por meio de uma função hash criptográfica a partir de alguma chave que faça sentido para a aplicação, por exemplo um nome, telefone, ou CPF.
Como a função hash é criptográfica, uma pequena variação na entrada implica em grande variação na saída, e para que observa apenas a saída da função, uma sequência de chaves é indistinguível de uma sequência aleatória.

Cada chave é associada a um nó, responsável por atender requisições de criação, consulta, modificação e remoção dos dados relacionados àquela chave.
O dado com chave $k$ é responsabilidade do nó com menor identificador $i \geq k$, aka, **sucessor de $k$** ($i = suc(k)$).

Na figura anterior, considere que apenas as posições em cinza estão preenchidas, isto é, que há apenas cinco nós no sistema, com identificadores 1, 4, 7, 12 e 15.
Neste cenário, o nó 7 é responsável por dados cujas chaves são 5, 6 e 7.

## Roteamento

Suponha que um cliente solicite ao Chord do exemplo anterior que armazene o valor $v$ associado à chave $k$.
A solicitação é feita pelo contato a um dos nós no sistema, que pode ou não ser o responsável por $k$.
Caso seja o responsável, a solicitação é executada localmente e uma resposta devolvida ao cliente.
Caso contrário, a requisição é repassada ou **roteada** para o nó correto.

Na rede estruturada definida até agora, uma opção óbvia é repassar a requisição para "a direita" sucessivamente até que alcance o nó correto. Esta solução, correta, tem custo da ordem do número de nós no sistema, $O(n)$.





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
