---
nav_exclude: true
---
# TODO

Propor projeto de sistema NoSQL.
* Etapa 1 - Cliente/Servidor
    * Objetivos
        * Hash Table acessível remotamente por interface CRUD sobre HTML.
        * Armazenamento em disco com recuperação de dados no caso de falhas
    * Desafios
        * Especificação do protocolo para dados genéricos
        * Armazenamento atômico no disco
        * Multithreading para garantir escalabilidade
        * Controle de concorrência
        * Estágios
* Etapa 2 - P2P
    * Objetivos
        * DHT com roteamento estilo Chord
        * Armazenamento em Log de operações e em arquivo de snapshots
        * Comunicação usando RPC
    * Desafios
        * Uso adequado da interface funcional do RPC
        * Uso do log + snapshots para recuperação
        * Roteamento no anel
        * Bootstrap dos processos
        * Log Structured Merge Tree
* Etapa 3 - Tolerância a Falhas
    * Objetivos
        * Cada servidor é uma máquina de estados replicada
    * Desafios
        * Usar adequadamente Difusão Atômica
        * Entender Commit Distribuído