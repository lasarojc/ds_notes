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

Na prática, são estruturas de dados adaptáveis, com um API muito simples.
* v' = put(k,v) //Retorna valor já existente
* v' = update(k,v) //Retorna valor já existente
* v' = get(k) //Retorna valor já existente
* v' = del(k) //Retorna valor já existente

Sobre os valores mapeados, dizemos que são ** *blobs* ** de dados, isto é, sem nenhuma forma distinta, e por isso podem ser usadas para resolver uma gama de resoluções. Além disso, é suas operações são eficientes em termos de tempo, uma vez que todas as operações tem tempo de execução (mais ou menos) constante.


## Distributed Hash Tables.

Se as tabelas de espalhamento são estruturas de dados úteis, uma versão distribuída seria ainda mais útil, principalmente porquê ela poderia ser **tolerante a falhas** e ter **escalabilidade linear**.
Justamente desta idea que surgem as DHT, literalmente tabelas de espalhamento distribuídas (inglês *distributed hash tables*), estruturas de dados que mantém **a mesma API** e funcionalidades de tabelas de espalhamento, mas que **agrega capacidades de diversos hosts**.

Os desafios na implementação de DHT incluem
* "O que usar como chave?", uma vez que tal estrutura precisa ser genérica para que possa ser aplicada a diversos problemas;
* "Como dividir a carga entre hosts?", para garantir um bom balanceamento de carga; e, 
* "Como rotear requisições para o host correto?", uma vez que os dados devem ser particionados entre hosts para garantir escalabilidade.

### Identificação

A identificação de objetos precisa ser facilmente **determinável pela aplicação** para permitir a recuperação precisa dos dados. 
Por exemplo, pode-se dividir faixas de nomes entre os processos.
* A -- C -- Host1
* CA -- E -- Host2
* EA -- G -- Host3
* ...

Esta distribuição tem três problemas graves. O primeiro, é no fato de nomes não serem **unívocos**.
Neste caso, uma exemplo melhor seria o uso do CPF.
* 000.000.000-00 -- 111.111.111-00 -- Host1
* 111.111.111-01 -- 222.222.222-00 -- Host2
* 222.222.222-01 -- 333.333.333-00 -- Host3
* ...

O segundo problema, presente também no uso de CPF, tem a ver com a distribuição da carga de trabalho entre os hosts.
Nem nomes e nem CPF tem distribuição uniforme, então alguns nós ficariam mais carregados que outros.

O terceiro problema tem a ver com o uso de chaves não genéricas, dependentes da aplicação.
Para este problema, poderíamos usar um identificador auto-incrementável, por exemplo, mas em muitas situações esta abordagem implicaria em dificuldade para se recuperar os dados: "qual é mesmo o identificador numérico do livro [How Fascism Works](https://ler.amazon.com.br/kp/embed?asin=B0796DNSVZ&preview=newtab&linkCode=kpe&ref_=cm_sw_r_kb_dp_fAlUDbMBJM4RP)?"

Para resolver estes três problemas, recorremos a uma abordagem usada na literatura da área, dividindo a identificação em duas camadas:
* Seja $i$ o identificador do objeto, dado pela aplicação (e.g., CPF, nome, telefone)
* Seja $h$ uma função criptográfica
* Seja $k = h(i)$ o identificador do objeto $i$.

Se usarmos, por exemplo, MD5, é fato que $k$ tem distribuição uniforme no espaço de 0 a $2^{160}-1$ possíveis valores.
Para dividirmos os dados entre os hosts também uniformemente, distribua os valores entre os hosts em função de $k$.
Alguns exemplos de divisão são:
* definia *buckets* para cada host e atribua o dado com chave $k$ para bucket $k \% b$, onde $b$ é o número de buckets
* divida a faixa de valores em $b$ segmentos e atribua a cada host uma faixa
* dados $2^n$ hosts, atribua ao host $0 < x < 2^n-1$ os dados cujas chaves terminem com o valor $x$.

São várias as formas de se dividir os dados e estão intimamente ligadas à rede sobreposta que se pretende montar.
Vejamos um caso específico e famoso, o Chord, e de dois outros sistemas que se inspiraram nele.