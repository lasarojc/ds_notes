# Coordenação: Copycat 

[Copycat](http://atomix.io/copycat/) é um arcabouço de replicação de máquinas de estados implementada pela [Atomix](http://atomix.io/).
Na base do Copycat está uma implementação do Raft.
Sobre o Raft, uma API simples mas moderna permite a criação de máquinas de estados usando **lambdas**, **futures**, e o estilo **fluent** de encadeamento de invocações.

!!!note ""
    === "Lambda"
        * Classe com um único método.

        ```java
        class Tarefa implements Runnable {
          public void run(){
            while (true)
              System.out.println("Bem vindo a um loop infinito");
            }   
        }

        new Thread(new Tarefa()).start();
        ```

        * Classe anônima - uso único

        ```java
        new Thread( new Runnable() {
          public void run(){
            while (true)
              System.out.println("Bem vindo a um loop infinito");
          }   
        }).start();
        ```

        * Lambda

        ```java
        new Thread(() -> {
                        while (true)
                          System.out.println("Bem vindo a um loop infinito");
                      }).start();
        ```

    === "Fluent"
        * Encadeamento

        ```java
            Collection<Pessoa> c = ...;
            c.stream()
              .filter(p -> p.idade > 33)
              .map(Pessoa::sobrenomeNome)//.map(p -> p.sobrenomeNome())
              .forEach(s -> System.out.println(s));
        ```

    === "Future"
        * Promessa de computação e resultado.
      
        ```java
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Integer> futFib = executor.submit(() -> { return Fibonacci(217)};
        ```

        * Quando será executado?  Em algum momento.    
        * Como pegar o resultado? 

        ```java
        while (!futFib.isDone())
          System.out.println("tah calculando...");

        int fib217 = futFib.get();
        ```

        * Em qual thread?  Em algum thread. Depende do Executor Service usado.	

Há várias versões do Copycat disponíveis, com vantagens e desvantagens.

!!!note "Versões"
    === "Versão 1.1.4"
        * Baseado em http://atomix.io/copycat/docs/getting-started/ e https://www.baeldung.com/atomix
        * Código funcional em https://github.com/pluxos/atomix_labs
        * Documentação oficial removida

    === "Versão >= 2"
        * Melhor desempenho
        * Documentação ruim ou inexistente
        * https://github.com/atomix/atomix

    === "Versão 3"
        * em Go
        * evolução rápida
        * o código é a documentação


Aqui usaremos a versão 1.1.4, que apesar de antiga, é a melhor documentada atualmente, pelo tutorial referenciado acima.

* Clone e compile o projeto
	* Instale dependências:
      * git
      * maven 
      * JDK >= 1.8
	* `git clone https://github.com/pluxos/atomix_labs`
	* `cd atomix_labs`
	* `cd replication`
	* `mvn compile`
	* `mvn test`


Você deve ver uma saída semelhante à seguinte, o que quer dizer que seu código está compilando perfeitamente.

```bash
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO] ---------------------------------------
[INFO] BUILD SUCCESS
[INFO] ---------------------------------------
[INFO] Total time: 6.898 s
[INFO] Finished at: 2017-10-25T08:38:08-02:00
[INFO] Final Memory: 15M/159M
[INFO] ---------------------------------------
```

Antes de começar a escrever suas prórpia máquinas de estado, familiarize-se com a estrutura do projeto em https://github.com/pluxos/atomix_labs/tree/master/replication/src/main/java/atomix_lab/state_machine

Observe que há três pastas: 

* `type` - tipos dos dados mantidos pela replica (Edge e Vertex)  
   Os tipos são serializable para que o Java saiba como transformá-los em bytes.
* `command` - estruturas que contêm informações para modificar os tipos  
	Os comandos serão enviadas do cliente para o cluster e são naturalmente serializable.
* `client` - cria comandos e os envia para serem executados no cluster  
	Respostas podem ser esperadas síncrona ou assincronamente.
* `server` - recebe os comandos na ordem definida pelo Raft e os executa

O projeto foi construído seguindo as instruções no tutorial mencionado antes, saltando-se a parte dos snapshots, isto é:

* crie um projeto maven  
	eclipse tem template para isso
* adicione dependências no `pom.xml`  
	como so criei um projeto, coloquei as dependências tanto do cliente quando do servidor
* defina `Command` que modifiquem o estado das réplicas
* defina `Queries` que consultem o estado das réplicas
* implemente a réplica para lidar com os comandos
* implemente o cliente para emitir comandos


Para executar um servidor, você precisa passar diversos parâmetros

* identificador do processo (inteiro)
* IP do processo com identificador 0
* porta do processo com identificar 0
* IP do processo com identificador 1
* porta do processo com identificar 1
* ...

Sabendo seu identificador, o servidor sabe em qual porta escutar e em quais IP/porta se conectar para se comunicar com os outros servidores.

Para testar o projeto, execute três servidores, em três terminais distintos. 
Usando o maven, da linha de comando, basta executar os seguintes comandos[^\\]:

[^\\]: O `\\` no final da linha é só para mostrar que o comando continua na próxima e facilitar a visualização. 
Na hora de executar, use apenas uma linha, sem o `\\`.

```bash
mvn exec:java \\
  -Dexec.mainClass="atomix_lab.state_machine.server.GraphStateMachine" \\
  -Dexec.args="0 127.0.0.1 5000 127.0.0.1 5001 127.0.0.1 5002"
	
mvn exec:java \\
  -Dexec.mainClass="atomix_lab.state_machine.server.GraphStateMachine" \\
  -Dexec.args="1 127.0.0.1 5000 127.0.0.1 5001 127.0.0.1 5002"
	
mvn exec:java \\
  -Dexec.mainClass="atomix_lab.state_machine.server.GraphStateMachine" \\
  -Dexec.args="2 127.0.0.1 5000 127.0.0.1 5001 127.0.0.1 5002"
```


O cliente não precisa de um identificador, apenas dos pares IP/porta dos servidores.
Por exemplo, use o comando:

```bash
mvn exec:java \\
  -Dexec.mainClass="atomix_lab.state_machine.client.GraphClient" \\
  -Dexec.args="127.0.0.1 5000 127.0.0.1 5001 127.0.0.1 5002"
```


!!!exercise "Exercício"
    Uma vez executado o projeto, modifique-o para incluir uma nova operação (`Command`) e nova consulta (`Query`), de sua escolha.


#### Estudo de caso: Ratis

[Ratis](http://ratis.apache.org/) é um arcabouço de coordenação recentemente emancipado como um projeto no [Apache](https://apache.org).
Embora mal documentado, o projeto tem alguns exemplos que demonstram como usar abstrações já implementadas. 
A seguir veremos um passo-a-passo, baseado nestes exemplos, de como usar o Ratis para implementar uma máquina de estados replicada.

Crie um novo projeto Maven com o nome `ChaveValor` (eu estou usando IntelliJ, mas as instruções devem ser semelhantes para Eclipse).

![Novo Projeto Maven](../images/newmaven.png)

Abra o arquivo `pom.xml` do seu projeto e adicione o seguinte trecho, com as dependências do projeto, incluindo o próprio Ratis.

```xml
<dependencies>
    <!-- https://mvnrepository.com/artifact/org.apache.ratis/ratis-server -->
    <dependency>
        <groupId>org.apache.ratis</groupId>
        <artifactId>ratis-server</artifactId>
        <version>2.0.0</version>
    </dependency>

    <!-- https://mvnrepository.com/artifact/org.apache.ratis/ratis-netty -->
    <dependency>
        <groupId>org.apache.ratis</groupId>
        <artifactId>ratis-netty</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>org.apache.ratis</groupId>
        <artifactId>ratis-grpc</artifactId>
        <version>2.0.0</version>
    </dependency>
    <dependency>
        <groupId>com.beust</groupId>
        <artifactId>jcommander</artifactId>
        <version>1.78</version>
    </dependency>
    <dependency>
        <groupId>org.slf4j</groupId>
        <artifactId>slf4j-api</artifactId>
        <version>1.7.25</version>
    </dependency>
    <!-- https://mvnrepository.com/artifact/org.apache.logging.log4j/log4j-slf4j-impl -->
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-slf4j-impl</artifactId>
        <version>2.14.1</version>
        <scope>compile</scope>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-api</artifactId>
        <version>2.14.1</version>
    </dependency>
    <dependency>
        <groupId>org.apache.logging.log4j</groupId>
        <artifactId>log4j-core</artifactId>
        <version>2.14.1</version>
        <scope>provided</scope>
    </dependency>
</dependencies>
```

Adicione também o plugin Maven e o plugin para gerar um `.jar` com todas as dependências. Observe que estou usando Java 14, mas você pode mudar para a sua versão.

```xml
<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-compiler-plugin</artifactId>
            <version>${maven.compiler.version}</version>
            <configuration>
                <source>14</source>
                <target>14</target>
            </configuration>
        </plugin>
        <plugin>
            <artifactId>maven-assembly-plugin</artifactId>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>single</goal>
                    </goals>
                </execution>
            </executions>
            <configuration>
                <descriptorRefs>
                    <descriptorRef>jar-with-dependencies</descriptorRef>
                </descriptorRefs>
            </configuration>
        </plugin>
    </plugins>
</build>
```

Crie uma nova classe denominada `Cliente` no arquivo `Cliente.java`.
Nesta classe, iremos criar um objeto `RaftClient` que será usado para enviar operações para os servidores. 
Esta classe é importada juntamente com outras várias dependências, adicionadas no `pom.xml`, que devemos instanciar antes do `RaftClient`.

Neste exemplo eu coloco praticamente todos os parâmetros de configuração do Ratis *hardcoded* para simplificar o código.
Obviamente que voce deveria ser estes parâmetros como argumentos para o programa ou de um arquivo de configuração.

```java
import org.apache.ratis.client.RaftClient;
import org.apache.ratis.conf.Parameters;
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcFactory;
import org.apache.ratis.protocol.*;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class Cliente
{
```

O campo `raftGroupId` identifica um cluster Ratis; isso quer dizer que um mesmo processo pode participar de vários *clusters*, mas aqui nos focaremos em apenas um. O valor do campo deve ter exatamente caracteres, o que soma 32 bytes em java, e será interpretado como um [UUID](https://pt.wikipedia.org/wiki/Identificador_%C3%BAnico_universal).

`id2addr` é um mapa do identificador de cada processo no cluster para seu endereço IP + Porta.
Aqui usei várias portas distintas porquê todos os processos estão rodando na mesma máquina, mas se estivesse executando em máquinas distintas, com IP distintos, poderia usar a mesma porta em todos.

`addresses` é uma lista de `RaftPeer` construída a parti de `id2addr`.

O campo `raftGroup` é uma referência a todos os servidores, associados ao identificador do grupo, `raftGroupId`.


```java
    public static void main(String args[]) throws IOException
    {
        String raftGroupId = "raft_group____um"; // 16 caracteres.
        
        Map<String,InetSocketAddress> id2addr = new HashMap<>();
        id2addr.put("p1", new InetSocketAddress("127.0.0.1", 3000));
        id2addr.put("p2", new InetSocketAddress("127.0.0.1", 3500));
        id2addr.put("p3", new InetSocketAddress("127.0.0.1", 4000));

        List<RaftPeer> addresses = id2addr.entrySet()
                .stream()
                .map(e -> RaftPeer.newBuilder().setId(e.getKey()).setAddress(e.getValue()).build())
                .collect(Collectors.toList());

        final RaftGroup raftGroup = RaftGroup.valueOf(RaftGroupId.valueOf(ByteString.copyFromUtf8(raftGroupId)), addresses);
```

Uma vez criado o grupo, criamos o cliente usando a fábrica retornada por `RaftClient.newBuilder()`.
A fábrica deve ser configurada com os dados do grupo e o tipo de transporte, neste caso gRPC.
Também é necessário o identificador do processo que está se conectando ao grupo; neste caso, usamos um identificador aleatório qualquer, diferente do que faremos com os servidores.

```java
        RaftProperties raftProperties = new RaftProperties();

        RaftClient client = RaftClient.newBuilder()
                                      .setProperties(raftProperties)
                                      .setRaftGroup(raftGroup)
                                      .setClientRpc(new GrpcFactory(new Parameters())
                                      .newRaftClientRpc(ClientId.randomId(), raftProperties))
                                      .build();
```

Uma vez criado o cliente, podemos fazer invocações de operações nos servidores. Cada operação será invocada em todos os servidores, na mesma ordem.
Este protótipo suporta duas operações, `add` e `get`, incluindo algumas variações, que ignoraremos por enquanto.
A operação `add` é codificada como uma `String`, `add:k:v`, onde `k` e `v` são do tipo `String`. `add:k:v` adiciona uma entrada em um mapa implementado pelo nosso servidor com chave `k` e valor `v`.
Já a operação `get:k` recupera o valor `v` associado à chave `k`, se presente no mapa.

O método `RaftClient.io().send` é usado para enviar modificações para as réplicas e deve, necessariamente, passar pelo protocolo Raft.
Já o método `RaftClient.io().sendReadOnly` é usado para enviar consultas a qualquer das réplicas.
Ambos os métodos codificam o comando sendo enviado (`add:k:v` ou `get:k`) no formato interno do Ratis para as réplicas e retorna um objeto `RaftClientReply`, que pode ser usado para pegar a resposta da operação. 
O código é auto explicativo.

```java
        RaftClientReply getValue;
        CompletableFuture<RaftClientReply> compGetValue;
        String response;
        switch (args[0]){
            case "add":
                getValue = client.io().send(Message.valueOf("add:" + args[1] + ":" + args[2]));
                response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
                System.out.println("Resposta:" + response);
                break;
            case "get":
                getValue = client.io().sendReadOnly(Message.valueOf("get:" + args[1]));
                response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
                System.out.println("Resposta:" + response);
                break;
            case "add_async":
                compGetValue = client.async().send(Message.valueOf("add:" + args[1] + ":" + args[2]));
                getValue = compGetValue.get();
                response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
                System.out.println("Resposta: " + response);
                break;
            case "get_stale":
                getValue = client.io().sendStaleRead(Message.valueOf("get:" + args[1]), 0, RaftPeerId.valueOf(args[2]));
                response = getValue.getMessage().getContent().toString(Charset.defaultCharset());
                System.out.println("Resposta: " + response);
                break;
            default:
                System.out.println("comando inválido");
        }

        client.close();
    }
}
```

Um vez criado o cliente, crie a classe `Servidor`, no arquivo `Servidor.java`; a parte inicial do código é semelhante à do cliente.

```java
import org.apache.ratis.conf.RaftProperties;
import org.apache.ratis.grpc.GrpcConfigKeys;
import org.apache.ratis.protocol.RaftGroup;
import org.apache.ratis.protocol.RaftGroupId;
import org.apache.ratis.protocol.RaftPeer;
import org.apache.ratis.protocol.RaftPeerId;
import org.apache.ratis.server.RaftServer;
import org.apache.ratis.server.RaftServerConfigKeys;
import org.apache.ratis.thirdparty.com.google.protobuf.ByteString;
import org.apache.ratis.util.LifeCycle;

import java.io.File;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Servidor
{

    //Parametros: myId
    public static void main(String args[]) throws IOException, InterruptedException
    {
        String raftGroupId = "raft_group____um"; // 16 caracteres.

        //Setup for node all nodes.
        Map<String,InetSocketAddress> id2addr = new HashMap<>();
        id2addr.put("p1", new InetSocketAddress("127.0.0.1", 3000));
        id2addr.put("p2", new InetSocketAddress("127.0.0.1", 3500));
        id2addr.put("p3", new InetSocketAddress("127.0.0.1", 4000));

        List<RaftPeer> addresses = id2addr.entrySet()
                                          .stream()
                                          .map(e -> RaftPeer.newBuilder().setId(e.getKey()).setAddress(e.getValue()).build())
                                          .collect(Collectors.toList());
```

A primeira diferença vem na necessidade de identificar o servidor dentro do conjunto de servidores, o que é feito com um `RaftPeerId`.
Como cada servidor deve usar um identificador único, do conjunto pré-determinado em `id2addr`, o identificador é passado como argumento para o programa, obrigatoriamente.

```java
        //Setup for this node.
        RaftPeerId myId = RaftPeerId.valueOf(args[0]);

        if (addresses.stream().noneMatch(p -> p.getId().equals(myId)))
        {
            System.out.println("Identificador " + args[0] + " é inválido.");
            System.exit(1);
        }
```

Encare a seção seguinte como uma receita, mas observe que o método `RaftServerConfigKeys.setStorageDir` recebe o nome de uma pasta como argumento, que será usada para armazenar o estado da máquina de estados.
Se você executar o servidor múltiplas vezes, a cada nova execução o estado anterior do sistema será recuperado desta pasta.
Para **limpar** o estado, apague as pastas de cada servidor.

```java
        RaftProperties properties = new RaftProperties();
        properties.setInt(GrpcConfigKeys.OutputStream.RETRY_TIMES_KEY, Integer.MAX_VALUE);
        GrpcConfigKeys.Server.setPort(properties, 1000);
        RaftServerConfigKeys.setStorageDir(properties, Collections.singletonList(new File("/tmp/" + myId)));
```

A máquina de estados em si é especificada no próximo excerto, em `setStateMachine`, que veremos a seguir.

```java
        //Join the group of processes.
        final RaftGroup raftGroup = RaftGroup.valueOf(RaftGroupId.valueOf(ByteString.copyFromUtf8(raftGroupId)), id2addr);
        RaftServer raftServer = RaftServer.newBuilder()
                .setServerId(myId)
                .setStateMachine(new MaquinaDeEstados())
                .setProperties(properties)
                .setGroup(raftGroup)
                .build();
        raftServer.start();
```

Uma vez iniciado o servidor, basta esperar que ele termine antes de sair do programa.

```java
        while(raftServer.getLifeCycleState() != LifeCycle.State.CLOSED) {
            TimeUnit.SECONDS.sleep(1);
        }
    }
}
```

Vamos agora para a definição da classe `MaquinaDeEstados`, no arquivo `MaquinaDeEstados.java`.
Esta classe deve implementar a interface `org.apache.ratis.statemachine.StateMachine` e seus vários métodos ou, mais simples, estende `org.apache.ratis.statemachine.impl.BaseStateMachine`, a abordagem que usaremos aqui.

```java
public class MaquinaDeEstados extends BaseStateMachine
{
```

Por enquanto, ignoraremos o armazenamento do estado em disco, mantendo-o simplesmente em memória no campo `key2values`, e simplesmente implementaremos o processamento de comandos, começando pela implementação do método `query`.

Este método é reponsável por implementar operações que não alteram o estado da máquina de estados, enviadas com o método `RaftClient::sendReadOnly`. A única `query` no nosso sistema é o `get`.
No código, o conteúdo da requisição enviada pelo cliente deve ser recuperado em quebrado em operação (`get`) e chave , usando `:` como delimitador.
Recuperado o valor associado à chave, o mesmo é colocado em um `CompletableFuture` e retornado.

```java
    private final Map<String, String> key2values = new ConcurrentHashMap<>();

    @Override
    public CompletableFuture<Message> query(Message request) {
        final String[] opKey = request.getContent().toString(Charset.defaultCharset()).split(":");
        final String result = opKey[0]+ ":"+ key2values.get(opKey[1]);

        LOG.debug("{}: {} = {}", opKey[0], opKey[1], result);
        return CompletableFuture.completedFuture(Message.valueOf(result));
    }
```

O método `applyTransaction` implementa operações que alteram o estado, como `add`, enviadas com o método `RaftClient::send`. 
Da mesma forma que em `get`, a operação deve ser recuperada em quebrada em operação (`add`), chave e valor, usando `:` como delimitador.

```java

    @Override
    public CompletableFuture<Message> applyTransaction(TransactionContext trx) {
        final RaftProtos.LogEntryProto entry = trx.getLogEntry();
        final String[] opKeyValue = entry.getStateMachineLogEntry().getLogData().toString(Charset.defaultCharset()).split(":");

        final String result = opKeyValue[0]+ ":"+ key2values.put(opKeyValue[1], opKeyValue[2]);

        final CompletableFuture<Message> f = CompletableFuture.completedFuture(Message.valueOf(result));

        final RaftProtos.RaftPeerRole role = trx.getServerRole();
        LOG.info("{}:{} {} {}={}", role, getId(), opKeyValue[0], opKeyValue[1], opKeyValue[2]);

        return f;
    }
```

Pronto, você já tem uma máquina de estados replicada, bastando agora apenas compilá-la e executá-la.
Para compilar, de raiz do projeto execute o comando `mvn package`. 
A primeira vez que faz isso pode demorar um pouco pois várias dependências são baixadas da Internet.
Ao final da execução do comando você deveria ver algo semelhante ao seguinte

```bash
...
INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  4.793 s
[INFO] Finished at: 2020-12-06T23:06:32-03:00
[INFO] ------------------------------------------------------------------------
```

Então, em três terminais diferentes, execute os seguintes comandos:

```bash
java -cp target/ChaveValor-1.0-SNAPSHOT-jar-with-dependencies.jar Servidor p1
java -cp target/ChaveValor-1.0-SNAPSHOT-jar-with-dependencies.jar Servidor p2
java -cp target/ChaveValor-1.0-SNAPSHOT-jar-with-dependencies.jar Servidor p3
```

Para executar o cliente, em um outro terminal, faça, por exemplo,

```bash
java -cp target/ChaveValor-1.0-SNAPSHOT-jar-with-dependencies.jar Cliente add k1 testek1
java -cp target/ChaveValor-1.0-SNAPSHOT-jar-with-dependencies.jar Cliente get k1

java -cp target/ChaveValor-1.0-SNAPSHOT-jar-with-dependencies.jar Cliente add k2 testek2
```

Todo o código está disponível no [Github](https://github.com/lasarojc/ds_notes/tree/master/docs/fault/code/ChaveValor)

???todo "Exercício"
    * Adicionar operações 
        * `del`
        * `clear`

###### Operações assíncronas
???todo "TODO"
    * Operações assíncronas usando `async()` em vez de `io()`.
    * `CompletableFuture`


###### Leituras "velhas"
???todo "TODO"
    * *stale reads* usando `sendStaleRead` em vez de `sendRead`.
    * índice inicial
    * nó
    * `java -cp target/ChaveValor-1.0-SNAPSHOT-jar-with-dependencies.jar Cliente get_stale  k1 p1`