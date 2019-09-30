---
layout: default
title: DynamoDB
parent: P2P
nav_order: 2
---

DynamoDB é marco fundamental dos bancos de dados NoSQL. Neste [vídeo](https://www.youtube.com/watch?v=HaEPXoXVf2k), um dos integrantes do time que o desenvolve e também um de seus evangelizadores, descreve rapidamente o banco, os cenários em que deveria ser usado e diversos padrões de projeto para modelagem de dados.

Enquanto o assiste, alguns pontos devem ser ressaltados

* NoSQL surgiu da necessidade de escalabilidae dos bancos de dados, mas a escalabilidade implica maior exposição a particionamento da rede em que o sistema roda, que associado à necessidade de manutenção de alta disponibilidade, implica em perda de garantias de consistência (Ver o [Teorema CAP](https://en.wikipedia.org/wiki/CAP_theorem));
* *Partition keys* são as chaves usadas para roteamento dos dados, ou seja, as chaves discutidas anteriormente neste capítulo sobre sistema P2P;
* *Sort keys* são chaves usadas dentro de cada nó para ordenar os dados na hora de gerar as SSTables (*String Sorted Tables*), e se usadas em agregados de valores, são equivalentes ao *GROUP BY* do SQL;
* *Lambda functions*  são funções para processamento de dados executadas em entradas definidas por um pipeline de processamento sem a definição explícita de sockets e portas, em um modelo conhecido como [Serverless](https://en.wikipedia.org/wiki/Serverless_computing).





TODO: To be Expanded
====================
