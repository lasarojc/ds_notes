no sql fcresceram rapidamente em uso e implementacoes.a facilidade e familiaridade do sql tem atrativos fortes. cockroach and yugabyte.
entender como funcionam é importate para qquer um interessado em SD.

In this project we will develop a rudimentary no SQL database and use that many difficulties to implement such a project to introduce concepts and frameworks related to the development of distributing systems. We will start by exploring the Waze stocked each other in a Distributed system. Then we will Dan will be explored different Architectures used to combine the efforts of components in the distributed system. Next we explore the guarantees that databases can provide to their users and how these guarantees are insured. 

To do move the session to an introductory part with either the preface or introduction itself.

O objetivo deste projeto é praticar o projeto de sistemas distribuídos, usando várias arquiteturas e tecnologias.
A ideia é implementar um banco de dados NoSQL (Not only SQL) rudimentar.
Mesmo uma versão simples de um banco de dados distribuído é um sistema complexo e por isso você deverá trabalhar em fases. Infelizmnte enquanto esta abordagem facilita a jornada, ela poderá levar a um pouco de retrabalho no final.

Para garantir que todo o seu esforço será concentrado no lugar certo e que sua avaliação seja justa, atente-se aos detalhes e aos passos na especificação abaixo.

* Etapa 1 - Cliente/Servidor usando RPC
    * Objetivos
        * Hash Table acessível remotamente por interface CRUD usando gRPC.
        * Armazenamento em disco com recuperação de dados no caso de falhas
    * Desafios
        * Especificação do protocolo para dados genéricos
        * Armazenamento atômico no disco
        * Multithreading para garantir escalabilidade
        * Controle de concorrência para garantir corretude nos dados armazenados.
    * Servidor
        * Todos os dados devem ser armazenados em um mapa Chave-Valor (Dicionário)
        * Chave é um número de precisão arbitrária do tipo BigInteger
        * Valor é uma tripla (Versão, Timestamp, Dados)
             * Versão é um inteiro com 64 bits (long)
             * Timestamp é um inteiro com 64 bits (long)
             * Dados é um vetor de bytes (byte[]) de tamanho arbitrário
        * O servidor implementa a seguinte API:
             * set(k,ts,d):(e,v') 
                 * adiciona ao mapa a entrada k-v, caso não exista uma entrada com a chave k, onde v=(1,ts,d)
                 * retorna a tupla (e,v') onde e=SUCCESS e v'=NULL se k-v foi inserido
                 * retorna a tupla (e,v') onde e=ERROR e v'=(ver,ts,data) se já existia uma entrada no banco de dados com a chave k e vers, ts e data correspondem, respectivamente, à versão, timestamp e dados de tal entrada
             * get(k):(e,v') 
                 * retorna a tupla (e,v') onde e=ERROR e v'=NULL se não há entrada no banco de dados com chave k
                 * retorna a tupla (e,v') onde e=SUCCESS e v'=(ver,ts,data) se já existia uma entrada no banco de dados com a chave k e vers, ts e data correspondem, respectivamente, à versão, timestamp e dados de tal entrada 
             * del(k):(e,v')
                 * remove a entrada k-v' do banco de dados se existir
                 * retorna a tupla (e,v') onde e=SUCCESS e v'=(ver,ts,data) se já existia uma entrada no banco de dados com a chave k e vers, ts e data correspondem, respectivamente, à versão, timestamp e dados de tal entrada 
                 * retorna a tupla (e,v') onde e=ERROR e v'=NULL se não existia entrada com chave k no banco de dados.
             * del(k,vers):(e,v')
                 * remove a entrada k-v' do banco de dados se existir e tiver versão v
                 * retorna a tupla (e,v') onde e=SUCCESS e v'=(vers,ts,data) se já existia uma entrada no banco de dados com a chave k e vers e ts e data correspondem, respectivamente, timestamp e dados de tal entrada 
                 * retorna a tupla (e,v') onde e=ERROR_NE e v'=NULL se não existia entrada com chave k no banco de dados.
                 * retorna a tupla (e,v') onde e=ERROR_WV e v'=(ver',ts,data) se já existia uma entrada no banco de dados com a chave k  version vers' not equal to vers, e  ts e data correspondem, respectivamente, timestamp e dados de tal entrada
             * testAndSet(k,v,vers):(e,v')
                 * atualiza o mapa se a versão atual no sistema corresponde à versão especificada.
                 * retorna a tupla (e,v') onde e=SUCCESS e v'=(ver,ts,data) se já existia uma entrada no banco de dados com a chave k e version vers, e  ts e data correspondem, respectivamente, timestamp e dados de tal entrada
                 * retorna a tupla (e,v') onde e=ERROR_NE e v'=NULL se não existia uma entrada no banco com chave k;
                 * retorna a tupla (e,v') onde e=ERROR_WV e v'=(ver',ts,data) se já existia uma entrada no banco de dados com a chave k  version vers' not equal to vers, e  ts e data correspondem, respectivamente, timestamp e dados de tal entrada
        * O mapa deve ser salvo em disco
            * Com periodicidade configurável, os dados do mapa devem ser salvos em disco.
            * Os dados em disco devem corresponder a uma versão dos dados em memória. Para entender, veja a seguinte sequência de eventos, que leva a uma versão em disco que nunca ocorreu em memória.
                * Dados em memória (1/lala, 2/lele, 3/lili)
                * Cópia para disco iniciada
                * Dados em disco (1/lala)
                * Dados em memória (1/lolo, 2/lele, 3/lili)
                * Dados em disco (1/lala, 2/lele)
                * Dados em memória (1/lolo, 2/lele, 3/lulu)
                * Dados em disco (1/lala, 2/lele, 3/lulu)

    * Cliente
        * O cliente deve implementar uma UI que permita a interação com o banco de dados usando todas as API
    * Testes
        * Um segundo cliente implementará as seguintes baterias de testes no sistema
            * Teste de todas as API levando a todos os tipos de resultados (sucesso e erro)
            * Teste de estresse em que a API seja exercitada pelo menos 1000 vezes e o resultado final deve ser demonstrado como esperado (por exemplo, inserir 1000 entradas com chaves distintas, atualizar a todas as entradas, ler todas a entradas e verificar que o valor, isto é, versão e dados, correspondem aos esperados.
        * Todos os testes apresentam os resultados esperados mesmo quando múltiplos clientes de teste são executados em paralelo.
    * Comunicação
        * Toda a comunicação entre cliente e servidor deve ser feita usando gRPC
    * Apresentação
        * Demonstrar que todos os itens da especificação foram seguidos
        * Demonstrar a corretude do sistema frente aos testes
        * Enumerar outros testes e casos cobertos implemententados
        * Demonstrar comportamento quando comunicação é interrompida no meio do teste

* Etapa 2 - Tolerância a Falhas
    * Objetivos 
         * Replicar o servidor para obter tolerância a falhas.
    * Desafios
         * Certificar-se de que o servidor é uma máquina de estados determinística
         * Compreender o uso de Difusão Atômica em nível teórico
         * Compreender o uso de Difusão Atômica em nível prático (Via [Ratis](https://lasarojc.github.io/ds_notes/fault/#estudo-de-caso-ratis))
         * Aplicar difusão atômica na replicação do servidor
    * Servidor
         * A API permanece a mesma e implementada via gRPC.
         * Requisições para o servidor (linha contínua) são encaminhadas via Ratis (linha tracejada) para ordená-las e entregar a todas as réplicas (linha pontilhada) para só então serem executadas e respondidas (pontilhado fino).  
         ![Arquitetura Etapa 2](drawings/abcast.drawio)
         * Dados não são mais armazenados em disco pela sua aplicação mas somente via Ratis.
    * Cliente
         * Sem alteração.
    * Testes
         * O mesmo *framework* de testes deve continuar funcional
    * Comunicação
         * Entre cliente e servidor, usar gRPC
         * Entre servidores, usar Ratis
    * Apresentação
         * Sem alteração, isto é, gravar um vídeo demonstrando que os requisitos foram atendidos.

<!--
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
        * Log Structured Merge Tree -->
