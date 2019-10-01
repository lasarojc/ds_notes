---
layout: home
title: Prólogo
nav_order: 0
---

Sistemas Distribuídos na Prática
======

Escrever "bons" sistemas distribuídos é uma tarefa que esbarra em diversos obstáculos, cujo não menos importante é a definição de "bom", mas que deixaremos para discutir em outro momento.
No campo prático, as barreiras vão desde o uso de premissas inválidas (como a confiabilidade e custo zero da transmissão de dados na Internet) ao não entendimento de conceitos fundamentais de programação concorrente, de falta de *frameworks* para resolver determinados problemas ao excesso de opções para outros.
Do ponto de vista teórico, a complexidade vai da dificuldade de abstração de problemas à ao nível de abstração de problemas bem definidos, da impossibilidade de se resolver problemas aparentemente simples à realização de que com o uso da abstração correta se consegue resolver facilmente problemas complexos.

Para ajudá-lo a navegar neste mar de dificuldades, este conjunto de notas de aulas está sendo organizado de forma iniciar o estudante no desenvolvimento de sistemas distribuídos de forma gradual. 
Iniciamos com um trabalho de convencimento de que tais aplicações são importantes por já serem parte inexpurgável da infraestrutura computacional que usamos.
Revisamos então conceitos de redes de computadores e sistemas operacionais enquanto falamos sobre a arquitetura mais fundamental de computação distribuída, a Cliente/Servidor e como é usada para implementar um proto banco de dados distribuído, uma Tabela de Espalhamento Distribuída em memória.
À medida em que apresentamos problemas com nossa implementação ou modelo assumido inicialmente e buscamos por soluções, introduzimos novas abstrações, mais poderosas e mais complexas.
Ao final desta jornada, teremos fundamentado a construção de uma DHT com particionamento de dados entre nós usando protocolos para a par e replicação de máquinas de estados.
Em paralelo, teremos estudado diversos *frameworks* de computação distribuída atuais, como modelo ou bloco de construção para a resolução de nossos problemas.

Estas notas, em sua forma atual, são fortemente baseadas em uma literatura já antiquada, como a segunda edição do livro [Distributed Systems: Principles and Paradigms](https://www.amazon.com.br/Distributed-Systems-Principles-Andrew-Tanenbaum/dp/153028175X), de Andrew Tanenbaum, mas também em alguns materiais mais recentes disponíveis.
Em futuras iterações, as notas serão atualizadas para conter cada vez mais material autoral e referências mais atuais; fique ligado.

A seguir você encontra um esqueleto de como gostaria (hoje) que o material fosse organizado.
Este esqueleto está longe da realidade, então para realmente navegar no material disponível, use o menu à esquerda.



# Introdução
* [Motivação](./intro/motivacao.md)
* [O quê é e por quê distribuir?](./intro/definicao.md)
* [Como distribuir? O *middleware*](./intro/middleware.md)
* [Tipos e Arquiteturas](./intro/tipos.md)

# Comunicação

## Socket
* [No principio, era o Socket](./basics/socket.md)
* [*Multiprocessing* e *Multithreading* em Sistemas Distribuídos](./basics/multiprogramming.md)
* [Uma HT cliente/servidor](./projeto/client_server.md)

## Invocação Remota de Procedimentos
* [Abaixo os Sockets](./basics/rpc.md)
  * [Estudo de Caso: gRPC](./basics/grpc.md)
  * [Estudo de Caso: Thrift](./TODO.md)
  * [Estudo de Caso: RMI](./TODO.md)

## MOM
* [MOM](./TODO.md)

## Publish/Subscribe
* [*Publish/Subscribe*](./TODO.md)

# Arquiteturas

## Peer-2-Peer

* [Tabelas de Espalhamento Distribuídas](./p2p/dht.md)
* [Estruturas de Dados para P2P e outros SD](./p2p/ed_sd.md)
* [DynamoDB Deep Dive](./p2p/dynamo.md)
* [Uma DHT propriamente dita](./projeto/p2p.md)

## Micro-serviços

## Event-sourcing



# Coordenação

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

# Tecnologias "Quentes"
* [Arquitetura Reativa](./reactive.md)
* [Map & Reduce](./mapreduce.md)
