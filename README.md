Sistemas Distribuídos na Prática
======

Este é o protótipo de um livro de Sistemas Distribuídos para cursos introdutórios ao tema, usando uma abordagem prática orientada a um projeto de sistemas de banco de dados NOSQL.

# Índice
[Há capítulos anteriores a este na documentação em LaTeX]

## Parte 1 - Fundamentals

* [Cliente Servidor](./basics/socket.md)
* [Multiprocessing e Multithreading](./basics/multiprogramming.md)
* [Invocação Remota de Procedimentos]()
* [Estudo de Caso: gRPC]()
* [Projeto](./projeto/client_server)

## Parte 2 - Coordenação

* [Concorrência](./concorrencia/concorrencia.md)
* [Coordenação](./coordenacao/coordenacao.md)
* [Tempo Físico](./tempo/fisico.md)
* [Tempo Lógico](./tempo/logico.md)

## Parte 3 - Peer-2-Peer

* [Tabelas de Espalhamento Distribuídas](./p2p/dht.md)
* [Estruturas de Dados para P2P e outros SD](./p2p/ed_sd.md)
* [DynamoDB Deep Dive](./p2p/dynamo.md)
* [Projeto](./projeto/p2p.md)

## Parte 4 - Tolerância a Falhas
* [Dependabilidade](./ft/dependabilidade.md)
* [Modelos](./ft/modelos.md)
* [Comunicação em Grupo](./ft/comunicao_grupo.md)
* [Replicação de Máquinas de Estados](./ft/smr.md)
* [Estudo de Caso: Raft](./fr/raft.md)
* [Estudo de Caso: Zookeeper](./ft/zookeeper.md)
* [Estudo de Caso: Atomix](./ft/atomix.md)
* [Projeto](./projeto/replicated.md)

## Parte 5 - Avançado?

* [Arquitetura Reativa](./reactive.md)
* [Map & Reduce](./mapreduce.md)