---
layout: default
title: DynamoDB
parent: DHT
nav_order: 2
---

DynamoDB é marco fundamental dos bancos de dados NoSQL. Neste [vídeo](https://www.youtube.com/watch?v=HaEPXoXVf2k), um dos integrantes do time que o desenvolve e também um de seus evangelizadores, descreve rapidamente o banco, os cenários em que deveria ser usado e diversos padrões de projeto para modelagem de dados.

Enquanto o assiste, alguns pontos devem ser ressaltados

* NoSQL surgiu da necessidade de escalabilidae dos bancos de dados, mas a escalabilidade implica maior exposição a particionamento da rede em que o sistema roda, que associado à necessidade de manutenção de alta disponibilidade, implica em perda de garantias de consistência (Ver o [Teorema CAP](https://en.wikipedia.org/wiki/CAP_theorem));
* *Partition keys* são as chaves usadas para roteamento dos dados, ou seja, as chaves discutidas anteriormente neste capítulo sobre sistema P2P;
* *Sort keys* são chaves usadas dentro de cada nó para ordenar os dados na hora de gerar as SSTables (*String Sorted Tables*), e se usadas em agregados de valores, são equivalentes ao *GROUP BY* do SQL;
* *Lambda functions*  são funções para processamento de dados executadas em entradas definidas por um pipeline de processamento sem a definição explícita de sockets e portas, em um modelo conhecido como [Serverless](https://en.wikipedia.org/wiki/Serverless_computing).



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
