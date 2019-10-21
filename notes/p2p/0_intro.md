---
layout: default
title: P2P
nav_order: 5
has_children: true
---

Neste capítulo falaremos sobre os sistemas P2P, do inglês, *peer-to-peer*, e mostraremos como as DHT, o mais proeminente exemplo de P2P, evoluiram do Chord, essencialmente de uso acadêmico, aos bancos de dados NOSQL, com representantes como Cassandra e o DynamoDB, muito difundidos na indústria.

## Arquitetura P2P

Uma forma de ver a arquitetura P2P é como uma substituição dos papéis de clientes e servidores, onde há uma "hirarquia" entre os componentes, por uma onde todos os nós são pares na execução da tarefa em questão, isto é, executam as mesmas funções.
Um exemplo comum destas arquitetura são os sistemas de compartilhamento de arquivos, em que cada nó armazena e disponibiliza parte dos dados, bem como acessa os dados disponibilizados por outros nós.

Na prática, muitos sistemas mantém os papéis de clientes e servidores, mas distribuem as tarefas dos servidores entre pares para aquela função; são sistemas híbridos. É assim que funcionam, por exemplo, boa parte os bancos de dados NOSQL, como Cassandra e DynamoDB, que discutiremos mais adiante neste capítulo. 

Como principais características destes sistemas P2P, podemos destacar as seguintes:
* arquitetura decentralizada;
* não há distinção de papéis entre nós ou conjuntos de nós desempenham os mesmos papéis, em parceria;
* pode haver entrada e saída de nós do sistema com alta frequência; 
* nós se organizam em redes sobrepostas (em inglês, *overlay*), redes lógicas sobre as redes físicas.

Os principais objetivos do uso arquitetura P2P são comuns a todas as arquiteturas distribuídas, isto é:
* agregar poder computacional de múltiplos nós e
* tolerar falhas de componentes sem paralizar o serviço, isto é, alta-disponibilidade.

Devido à forma como são construídos, sistemas P2P também podem visar
* escalabilidade geográfica global, isto é, com nós espalhados por todo o globo e
* auto-administração, pois seria praticamente impossível centralizar a administração de tantos nós, com tantas configurações distintas e  em tantas localizações diferentes.


TODO: Exemplos de sistemas P2P

## Rede Sobreposta

Como já mencionado, em sistemas P2P, os nós ou componentes do sistema se organizam em uma rede *sobreposta* à rede física. Esta rede lógica é constituída pelos processos atuando como nós e pelos canais de comunicação estabelecidos entre os nós, tipicamente na forma de conexões TCP/IP.

Nestas redes sobrepostas são executados diversos algoritmos, como de descoberta de nós, roteamento de pacotes e de otimização de rotas pelo descarte e criação de conexões.
Uma vez que as conexões na rede sobreposta não correspondem a conexões físicas, como se pode ver na seguinte figura, vizinhos em um rede sobreposta não necessariamente correspondem a vizinhos na rede física e vice-versa.
Isto também implica que a otimização da rota lógica não necessariamente leva à otimização da rota física.

![[Por Gustavo Lacerda - UFRJ, Domínio público](https://pt.wikipedia.org/wiki/Peer-to-peer#/media/Ficheiro:Overlay_p2p.jpg)](images/overlay.jpg)


Dependendo em como esta rede é organizada (ou não), a mesma é classificada como *estruturada* ou *não-estruturada*.


### Rede Não-Estruturada

Se a rede é construída de forma aleatória, por exemplo deixando os nós se conectarem apenas aos vizinhos na rede no ponto em que se conectaram inicialmente, então esta é denomida uma rede *não-estruturada*. 
A figura a seguir é um exemplo que se percebe que nós tem graus diferentes de conectividade e que não estão particularmente organizados em nenhuma topologia.

![[Não-estruturada]({http://gossple2.irisa.fr/~akermarr/LSDS-EPFL-unstructured.pdf)](images/unstructured.png)

Suponha que esta rede 

### Rede Estruturada

Se as conexões são construídas e mantidas de forma a gerar uma topologia bem definida, chamamos esta rede de estruturada.
Nesta rede, a inserção de nós requer a propagação desta informação para outros nós e a atualização das conexões para manter a estrutura.
A estrutura geralmente serve ao propósito de associar os nós aos dados de uma forma planejada. 
Por exemplo, nós próximos na rede podem ser responsáveis por dados logicamente próximos.
Claramente, a inserção e acesso a dados nesta rede é mais custosa, pois independentemente de onde a requisição é feita, isto é, a partir de qual nó, ela deverá ser atendida por um nó específico. 


 Estruturada                      | Não-Estruturada 
----------------------------------|---------------------------
 Estrutura bem definida           | Estrutura aleatória
 Adição de dados é lenta          | Adição de dados é rápida
 Adição de nós é lenta            | Adição de nós é rápida
 Busca por dados é rápida         | Busca por dados lenta


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





\begin{itemize}
	\item Inserção afeta apenas nós contactados
	\item Busca requer varredura (p.e., inundação, random walk), ou índice
\end{itemize}



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

