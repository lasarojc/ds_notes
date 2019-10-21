---
layout: default
title: Cassandra
parent: DHT
grandparent: P2P
nav_order: 3
---

O Cassandra, com seu modelo híbrido chave-valor/relacional, facilita o desenvolvimento de certas aplicações.

\begin{frame}{Apache Cassandra}
\begin{itemize}
\item Híbrido entre chave-valor e relacional
\item Valores, ou linhas, são agrupados em \emph{column families} (equivalente a tabelas)
\item Column Families, juntas formam um \emph{keyspace} (equivalente a um banco de dados)
\includegraphics[width=.7\textwidth]{images/cass_keyspace}
\end{itemize}
\end{frame}


\begin{frame}{Apache Cassandra}
\begin{itemize}
\item Chave mapeia para um conjunto de colunas
\includegraphics[width=.7\textwidth]{images/cass_column_family}

\item Cada coluna tem valor e timestamp
\includegraphics[width=.7\textwidth]{images/cass_column}


\item Linhas podem ser ordenadas por valores de algumas colunas (chave composta)

\item Colunas podem ser agrupadas em {\emph super-columns}\\
(Não recomendado)

%\item \emph{Range Queries} -- todas a linhas com chave em determinada faixa de valores
\end{itemize}
\end{frame}




\begin{frame}[fragile,allowframebreaks]{Apache Cassandra}
\begin{itemize}
\item Super-column families

%\begin{verbatim}
%UserList={    <- Table (Tables pertencem a um keyspace)
%     Cath:{   <- Chave
%         //Super column
%         username:{firstname:”Cath”, lastname:”Yoon”}
%         address:{ city:”Seoul”,postcode:”1234”}}
%           
%     Terry:{   //Super columns formam super column families
%         //Columns formam column families.
%         username:{firstname:”Terry”,lastname:”Cho”}
%         account:{bank:”hana”,account:”1234”}}
%}
%\end{verbatim}

\item Cassandra Query Language
\end{itemize}

\url{http://wiki.apache.org/cassandra/FrontPage}
\end{frame}


\begin{frame}{Demo}
\url{https://blog.rackspace.com/cassandra-by-example}

\url{http://wiki.apache.org/cassandra/GettingStarted}	
\end{frame}

É possível rodar o Cassandra facilmente em certas nuvens computacionais.


E há vários exemplos de aplicações desenvolvidas usando-se o cassandra.
\begin{frame}{Twissandra}
\url{https://github.com/twissandra/twissandra}
\end{frame}



\begin{frame}{Aplicação}
\url{https://github.com/datastax}

\url{https://github.com/datastax/java-driver}

\url{http://downloads.datastax.com/java-driver/cassandra-java-driver-3.0.0.tar.gz}

\url{http://www.devjavasource.com/cassandra/cassandra-crud-operation-using-java/}
\end{frame}


\begin{frame}{Para aprender mais}
\url{http://www.tutorialspoint.com/cassandra/}	
\end{frame}


\begin{frame}{Caso de uso: resolução de nomes}
\begin{itemize}
	\item \url{http://ufu.br}: IP
	\item \url{http://pop3.mail.ufu.br}: IP
	\item \url{/home/lasaro/public_html/sites/disciplinas}: INode
	\item \url{+55 34 99924 2358}: IMEI 
\end{itemize}

Como você implementaria usando uma DHT?
\pause
Chave normalizada
\end{frame}

