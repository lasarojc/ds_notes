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
Em uma instância com milhares de nós, **$O(n)$** é um custo muito alto, ainda mais se considerarmos que cada salto na rede sobreposta potencialmente cruza toda a Internet, uma vez que, reforçando, a proximidade na rede sobreposta não implica em proximidade na rede física abaixo.
Observe que o custo em termos de espaço para se implementar esta solução é **$O(1)$** para cada nó do sistema.

Outra alternativa é fazer com que cada nó do sistema conheça todos os outros. Assim, cada requisição pode ser diretamente encaminhada ao nó responsável por tratá-la. 
O custo do roteamento, neste caso, é **$O(1)$**, muito mais rápido que na abordagem anterior. O custo de armazenamento da *tabela de rotas* é, contudo, **$O(n)$**, o que pode ser proibitivo em uma rede com milhares de nós, apesar de ser uma solução viável em redes menores. Este é o caso do CassandraDB, uma banco de dados distribuído baseado no Chord, que estudaremos melhor mais adiante, considerado uma DHT de salto único (*single-hop* DHT).

Como frequentemente acontece, um solução melhor pode ser nem uma nem outra opção, mas algo intermediário.
O Chord propõe a criação de uma tabela de rotas também conhecida como *finger-table*, construída da seguinte forma, onde $m$ é a quantidade de bits usados para identificar nós no sistema:
* seja $F_p$ a *finger-table* do processo $p$;
* seja $F_p[i]$ a $i$-ésima da tabela; e,
* $F_p[i] = suc(p+2^{i-1})$.

Observe que nesta tabela, a $i$-ésima entrada aponta para o processo que no que sucede $p$ pelo menos $2^{i-1}$, e que esta distância de sucessão aumenta exponencialmente. Observe também que a maior distância é proporcional a metade do tamanho do anel.
Isto quer dizer que o último *finger* da tabela proporciona um salto de $1/2$ anel, o penúltimo $1/4$ do anel, o ante-penúltimo $1/8$, e assim sucessivamente.
Outra forma de se ver esta tabela é como proporcionando um salto de pelo menos metade da distância restante para o nó responsável pela chave, resultando em um roteamento com custo **$O(log n)$**.

![](images/fingertable.jpeg)

Mas como este potencial é explorado? Usando-se o seguinte algoritmo de busca pela entrada correta na tabela de roteamento, do ponto de vista do processo $p$:
* seja $k$ a chave para qual estamos procurando o sucessor;
* itere pela tabela até achar a primeira entrada cujo valor, i.e., o identificador de um nó, é maior que $k$;
* se a entrada é a primeira da tabela, então encaminhe a requisição para o nó apontado, pois ele é o sucessor de $k$, até onde $p$ consegue determinar;
* senão, encaminhe a requisição para a entrada anterior, pois o nó referenciado está mais próximo do sucessor para determiná-lo com segurança.

Considere no exemplo a seguir a busca pelo sucessor de 26, iniciada pelo nó 1.

![](images/05-04.png)

Duas observações são importantes aqui. A primeira, é que as comparações para se encontrar a entrada correta, deve respeitar o anel, por exemplo, em um anel com 32 posições, por exemplo, $31 < 0$. No seguinte exemplo, considere por exemplo a busca que o nó 21 faz pelo sucessor de 31; qual deve ser a entrada selecionada?

![](images/05-04.png)

A segunda observação é que não se pode encaminhar a requisição diretamente para o nó apontado na entrada encontrada, pois a visão de $p$ pode ser incompleta para partes distantes do anel.
Tente identificar exemplos no anel a seguir onde este comportamento seria errado.

A organização dos nós em um anel virtual e a distribuição da responsabilidade dos dados pelo particionamento do espaço das chaves de forma correspondente às faixas no anel lógico é a técnica conhecida como **espalhamento consistente**, do inglês, *consistent hashing*.


## Churn

Apesar do espalhamento consistente ser uma técnica muito útil, ela não resolve todos os problemas. Aliás, vários outros problemas precisam ser resolvidos, sendo o primeiro deles lidar com a entrada e saída de nós, principalmente por falhas de nós e comunicação.




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



[Simulador Chord](http://www.dennislambing.com/p2p-chord-simulation/)
[Boa referência](https://www.cs.cmu.edu/~dga/15-744/S07/lectures/16-dht.pdf)
