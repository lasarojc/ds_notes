---
layout: default
title: DHT
parent: P2P
has_children: true
nav_order: 1
---

## Tabelas Hash
As tabelas hash tem uma interface muito simples de armazenamento de dados, sendo adequadas a vários cenários.
Em essência, são funções, no sentido matemático da palavra, que **mapeiam** uma chave para um valor.
* $f(K): V \cup$ \{null\}
* $K$: Universo de chaves
* $V$: Universo de valores
* isto é, $f(k) = v, k\in K, v \in V$ ou $v =$ null.

Na prática, são estrutas de dados adaptáveis, com um API muito simples.
* v' = put(k,v) //Retorna valor já existente
* v' = update(k,v) //Retorna valor já existente
* v' = get(k) //Retorna valor já existente
* v' = del(k) //Retorna valor já existente

Sobre os valores mapeados, dizemos que são ** *blobs* ** de dados, isto é, sem nenhuma forma distinta, e por isso podem ser usadas para resolver uma gama de resoluções. Além disso, é suas operações são eficientes em termos de tempo, uma vez que todas as operações tem tempo de execução (mais ou menos) constante.

## Distributed Hash Tables.


\begin{frame}[fragile,allowframebreaks]{Distributed Hash Tables}
Como distribuir um HT de forma que
\begin{itemize}
\item mantenha API e funcionalidade
\item agregue as capacidades de diversos hosts?
\end{itemize}

Desafios incluem
\begin{itemize}
\item O que usar como chave?
\item Como dividir carga (uniformemente) entre hosts?
\item Como rotear requisições para o host correto?
\end{itemize}
\end{frame}


\begin{frame}{Identificação}
\begin{itemize}
	\item Identificação única por objeto
	\item Identificador atribuído pela aplicação
	\item Exemplo, CPF da pessoa
\end{itemize}
\end{frame}

\begin{frame}{Divisão de carga}
\begin{itemize}
	\item Cada nó é responsável por uma faixa de valores
	\item 000.000.000-00 -- 111.111.111-00 -- Host1
	\item 111.111.111-01 -- 222.222.222-00 -- Host2
	\item 222.222.222-01 -- 333.333.333-00 -- Host3
	\item ...
\end{itemize}
\end{frame}

\begin{frame}{Divisão de carga}
\begin{itemize}
	\item Nomes
	\item A -- C -- Host1
	\item CA -- E -- Host2
	\item EA -- G -- Host3
	\item ...
\end{itemize}
\end{frame}

\begin{frame}{Divisão de carga}
A distribuição não é boa. 

Depende da distribuição dos dados.
\end{frame}



\begin{frame}{Identificação}
\begin{itemize}
	\item Seja $i$ o identificador do objeto, dado pela aplicação (e.g., CPF, nome)
	\item Seja $h$ uma função criptográfica
	\item $k = h(i)$ tem distribuição uniforme
	\item Por exemplo, MD5 tem $2^{160}$ possíveis valores
	\item Distribua os valores entre os hosts
\end{itemize}
\end{frame}

\begin{frame}{Roteamento}
\begin{itemize}
	\item Cada nó é responsável por um \emph{bucket}
	\item Chave $k$ vai para bucket é $k \% b$, onde $b$ é o número de buckets
	\item Como associar um bucket a um nó?
	\item Redes sobrepostas
\end{itemize}
\end{frame}

\begin{frame}{Redes Sobrepostas}
\begin{itemize}
	\item Uma rede lógica sobre uma rede física
	\item Conexões consideradas canais de comunicação
	\item Roteamento no nível da aplicação
	\item Estruturadas e não-estruturadas	
\end{itemize}

\pause
\includegraphics[width=.5\textwidth]{images/overlay}

\href{https://content.iospress.com/media/jhs/2017/23-1/jhs-23-1-jhs558/jhs-23-jhs558-g002.jpg?width=755}{Fonte}

%\includegraphics[width=.5\textwidth]{images/Network_Overlay}
%Fonte: \href{https://commons.wikimedia.org/w/index.php?curid=10086213}{Ludovic.ferre}
\end{frame}


