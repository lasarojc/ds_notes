---
layout: home
title: Prólogo
nav_order: 0
---

Sistemas Distribuídos na Prática
======

Escrever "bons" sistemas distribuídos é uma tarefa que esbarra em diversos obstáculos, cujo não menos importante é a definição de "bom", mas que deixaremos para discutir em outro momento.
No campo prático, as barreiras vão desde o uso de premissas inválidas, como confiabilidade e custo zero da transmissão de dados via Internet, ao não entendimento de conceitos fundamentais de programação concorrente, da falta de *frameworks* para resolver certos problemas ao excesso de opções para outros.
Do ponto de vista teórico, a complexidade vai da dificuldade de abstração de problemas ao nível de abstração de problemas bem definidos, da impossibilidade de se resolver problemas aparentemente simples à realização de que com o uso da abstração correta se consegue resolver facilmente problemas complexos.

Para ajudá-lo a navegar neste mar de dificuldades, este conjunto de notas de aulas está sendo organizado de forma iniciar o estudante no desenvolvimento de sistemas distribuídos de forma gradual. 
Iniciamos com um trabalho de convencimento de que tais aplicações são importantes por já serem parte inexpurgável da infraestrutura computacional que usamos.
Revisamos então conceitos de redes de computadores e sistemas operacionais enquanto falamos sobre a arquitetura mais fundamental de computação distribuída, a Cliente/Servidor e de como é usada para implementar um proto banco de dados distribuído, uma Tabela de Espalhamento Distribuída em memória.
À medida em que apresentamos problemas com o modelo assumido inicialmente e com nossa implementação inicial, buscaremos por soluções enquanto introduzimos novas abstrações, mais poderosas e mais complexas.
Ao final desta jornada, teremos fundamentado a construção de uma DHT com particionamento de dados entre nós usando protocolos par-a-par e replicação de máquinas de estados.
Em paralelo, teremos estudado diversos *frameworks* de computação distribuída atuais, como modelo ou bloco de construção para a resolução de nossos problemas.

Estas notas, em sua forma atual, são fortemente baseadas em uma literatura já antiquada, como a segunda edição do livro [Distributed Systems: Principles and Paradigms](https://www.amazon.com.br/Distributed-Systems-Principles-Andrew-Tanenbaum/dp/153028175X), de Andrew Tanenbaum, mas também em alguns materiais mais recentes disponíveis livremente na Internet.
Em futuras iterações, as notas serão atualizadas para conter cada vez mais material autoral e referências mais atuais; fique ligado.

Para navegar no material, utilize o menu à esquerda. 
O índice abaixo é apenas um esqueleto com uma proposta em constante mutação do conteúdo total das notas.


----------

# Introdução
* [Motivação](./notes/intro/1_porque.md)
* [O quê é e por quê distribuir?](./notes/intro/2_oque.md)
* [Como distribuir? A mágica do *middleware*](./notes/intro/3_como.md)
* [Tipos e Arquiteturas](./notes/intro/4_tipos.md)


# Comunicação

## Socket
* [No princípio, era o Socket](./notes/basics/1_socket.md)
* [*Multiprocessing* e *Multithreading* em Sistemas Distribuídos](./notes/basics/2_multiprogramming.md)

## Invocação Remota de Procedimentos
* [Abaixo os Sockets](./notes/basics/3_rpc.md)
  * [Estudo de Caso: gRPC](./notes/basics/3_1_grpc.md)
  * [Estudo de Caso: Thrift](./notes/basics/3_2_thrift.md)
  * [Estudo de Caso: RMI](./TODO.md)

## Projeto
* [Uma HT cliente/servidor](./notes/projeto/projeto.md)


## MOM
* [MOM](./TODO.md)

## Publish/Subscribe
* [*Publish/Subscribe*](./TODO.md)


# Arquiteturas

## Revendo Cliente Servidor

TODO: Trazer parte da discussão sobre cliente servidor para cá e atualizar.

## Peer-2-Peer

* [Sistemas Par-a-Par](./notes/0_intro.md)
* [Tabelas de Espalhamento Distribuídas](./notes/p2p/1_dht.md)
  * [Estudo de Caso: Chord](./notes/p2p/1_1_chord.md)
  * [Estudo de Caso: Dynamo](./notes/p2p/1_2_dynamo.md)
  * [Estudo de Caso: Cassandra](./notes/p2p/1_1_cassandra.md)
* [Estruturas de Dados para P2P e outros SD](./p2p/2_ed_sd.md)

## Projeto
* [Uma DHT propriamente dita](./projeto/p2p.md)

## Microsserviços
* [Visão Geral](./notes/microservices/0_intro.md)

## Event-sourcing




# Coordenação
TODO: usar consistência entre réplicas da DHT para motivar a sincronização.

* [Coordenação](./coordenacao/coordenacao.md)
* [Tempo Físico](./tempo/fisico.md)
* [Tempo Lógico](./tempo/logico.md)



# Tolerância a Falhas

* [Dependabilidade](./ft/dependabilidade.md)
* [Modelos](./ft/modelos.md)
* [Comunicação em Grupo](./ft/comunicao_grupo.md)
  * [Replicação de Máquinas de Estados](./ft/smr.md)
  * [Estudo de Caso: Raft](./fr/raft.md)
  * [Estudo de Caso: Zookeeper](./ft/zookeeper.md)
  * [Estudo de Caso: Atomix](./ft/atomix.md)
* [Uma DHT tolerante a falhas ](./projeto/replicated.md)


# Tecnologias

* [Arquitetura Reativa](./reactive.md)
* [Map & Reduce](./mapreduce.md)
