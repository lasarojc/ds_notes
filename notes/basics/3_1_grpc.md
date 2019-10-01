---
layout: default
title: Estudo de Caso - gRPC
parent: RPC
nav_order: 1
---

# Estudo de Caso RPC: gRPC

gRPC é um framework para invocação remota de procedimentos multi-linguagem e sistema operacional, usando internamente pelo Google há vários anos para implementar sua arquitetura de micro-serviços.
Inicialmente desenvolvido pelo Google, o gRPC é hoje de código livre encubado pela Cloud Native Computing Foundation.

O sítio [https://grpc.io](https://grpc.io) documenta muito bem o gRPC, inclusive os [princípios](https://grpc.io/blog/principles/) que nortearam seu projeto.

O seu uso segue, em linhas gerais, o modelo discutido nas seções anteriores, isto é, inicia-se pela definição de estruturas de dados e serviços, "compila-se" a definição para gerar stubs na linguagem desejada, e compila-se os stubs juntamente com os códigos cliente e servidor para gerar os binários correspondentes.
Vejamos a seguir um tutorial passo a passo, em Java, baseado no [quickstart guide](https://grpc.io/docs/quickstart/java.html).

## Instalação

Os procedimentos de instalação dependem da linguagem em que pretende usar o gRPC, tanto para cliente quanto para servidor.
No caso do **Java**, **não há instalação propriamente dita**.

## Exemplo Java

Observe que o repositório base apontado no tutorial serve de exemplo para diversas linguagens e diversos serviços, então sua estrutura é meio complicada. Nós nos focaremos aqui no exemplo mais simples, uma espécie de "hello word" do RPC.

### Pegando o código
Para usar os exemplos, você precisa clonar o repositório com o tutorial, usando o comando a seguir.


```bash
git clone -b v1.19.0 https://github.com/grpc/grpc-java
```

Uma vez clonado, entre na pasta de exemplo do Java e certifique-se que está na versão 1.19, usada neste tutorial.

```bash
cd grpc-java\examples
git checkout v1.19.0
```

### Compilando e executando
O projeto usa [gradle](https://gradle.org/) para gerenciar as dependências. Para, use o *wrapper* do gradle como se segue.

```bash
./gradlew installDist
```

Caso esteja na UFU, coloque também informação sobre o proxy no comando.

```bash
./gradlew -Dhttp.proxyHost=proxy.ufu.br -Dhttp.proxyPort=3128 -Dhttps.proxyHost=proxy.ufu.br -Dhttps.proxyPort=3128 installDist
```

Como quando usamos sockets diretamente, para usar o serviço definido neste exemplo, primeiros temos que executar o servidor.

```bash
./build/install/examples/bin/hello-world-server
```

Agora, em **um terminal distinto** e a partir da mesma localização, execute o cliente, quantas vezes quiser.

```bash
./build/install/examples/bin/hello-world-client
```

### O serviço

O exemplo não é muito excitante, pois tudo o que o serviço faz é enviar uma saudação aos clientes.
O serviço é definido no seguinte arquivo `.proto`, localizado em `./src/main/proto/helloworld.proto`.

```protobuf
message HelloRequest {
  string name = 1;
}

message HelloReply {
  string message = 1;
}


// The greeting service definition.
service Greeter {
  rpc SayHello (HelloRequest) returns (HelloReply) {}
}
```

No arquivo, inicialmente são definidas duas mensagens, usadas como requisição (cliente para servidor) e outra como resposta (servidor para cliente) do serviço definido em seguida.

A mensagem `HelloRequest` tem apenas um campo denominado `name`, do tipo `string`. Esta mensagem conterá o nome do cliente, usado na resposta gerada pelo servidor.

A mensagem `HelloReply` também tem um campo do tipo `string`, denominado `message`, que conterá a resposta do servidor.

O serviço disponível é definido pela palavra chave `service`e de nome `Greeter`; é importante entender que este nome será usado em todo o código gerado pelo compilador gRPC e que se for mudado, todas as referências ao código gerado devem ser atualizadas.

O serviço possui apenas uma operação, `SayHello`, que recebe como entrada uma mensagem `HelloRequest` e gera como resposta uma mensagem `HelloReply`.
Caso a operação precisasse de mais do que o conteúdo de `name` para executar, a mensagem `HelloRequest` deveria ser estendida, pois não há passar mais de uma mensagem para a operação.
Por outro lado, embora seja possível passar zero mensagens, esta não é uma prática recomendada.
Isto porquê caso o serviço precisasse ser modificado no futuro, embora seja possível estender uma mensagem, não é possível modificar a assinatura do serviço. 
Assim, caso não haja a necessidade de se passar qualquer informação para a operação, recomenda-se que seja usada uma mensagem de entrada vazia, que poderia ser estendida no futuro.
O mesmo se aplica ao resultado da operação.

Observe também que embora o serviço de exemplo tenha apenas uma operação, poderia ter múltiplas.
Por exemplo, para definir uma versão em português da operação `SayHello`, podemos fazer da seguinte forma.

```protobuf
message HelloRequest {
  string name = 1;
}

message HelloReply {
  string message = 1;
}

message OlaRequest {     // <<<<<====
  string name = 1;
}

message OlaReply {       // <<<<<====
  string message = 1;
}

service Greeter {
  rpc SayHello (HelloRequest) returns (HelloReply) {}
  rpc DigaOla (OlaRequest) returns (OlaReply) {}// <<<<<====
}
...
```

Observe que a nova operação recebe como entrada  mensagens `OlaRequest` e `OlaReply`, que tem definições exatamente iguais a `HellorRequest` e `HelloReply`.
Logo, em vez de definir novas mensagens, poderíamos ter usado as já definidas. Novamente, esta não é uma boa prática, pois caso fosse necessário evoluir uma das operações para atender a novos requisitos e estender suas mensagens, não será necessário tocar o restante do serviço.
Apenas reforçando, é boa prática definir *requests* e *responses* para cada método, a não ser que não haja dúvida de que serão para sempre iguais.


### Implementando um serviço

Agora modifique o arquivo `.proto` como acima, para incluir a operação `DigaOla`, recompile e reexecute o serviço.
Não dá certo, não é mesmo? Isto porquê você adicionou a definição de uma nova operação, mas não incluiu o código para implementá-la.
Façamos então a modificação do código, começando por `./src/main/java/io/grpc/examples/helloworld/HelloWorldServer.java`.
Este arquivo define a classe que **implementa** o serviço `Greeter`, `GreeterImpl`, com um método para cada uma das operações definidas. 
Para confirmar, procure por `sayHello`para encontrar a implementação de `SayHello`; observe que a diferença do `casing` vem das boas práticas de Java, de definir métodos e variáveis em *Camel casing*.

Para que sua versão estendida do serviço `Greeter` funcione, defina um método correspondendo à `DigaOla`, sem consultar o código exemplo abaixo, mas usando o código de `sayHello` como base; não se importe por enquanto com os métodos sendo invocados.
Note que os `...` indicam que parte do código, que não sofreu modificações, foi omitido.

```java
...
private class GreeterImpl extends GreeterGrpc.GreeterImplBase {
...

  @Override
  public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
      ...
  }

  @Override
  public void digaOla(OlaRequest req, StreamObserver<OlaReply> responseObserver) {
    OlaReply reply = 
      OlaReply.newBuilder().setMessage("Ola " + req.getName()).build();
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
  }
}
```

Se você recompilar e reexecutar o código, não perceberá qualquer mudança na saída do programa. Isto porquê embora tenha definido um novo serviço, você não o utilizou. Para tanto, agora modifique o cliente, em `src/main/java/io/grpc/examples/helloworld/HelloWorldClient.java`, novamente se baseando no código existente e não se preocupando com "detalhes".

```java
public void greet(String name) {
  logger.info("Will try to greet " + name + " ...");
...
  OlaRequest request2 = OlaRequest.newBuilder().setName(name).build();
  OlaReply response2;
  try {
    response2 = blockingStub.digaOla(request2);
  } catch (StatusRuntimeException e) {
    logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
   return;
  }
  logger.info("Greeting: " + response2.getMessage());
}
```

Agora sim, você pode reexecutar cliente e servidor.

```bash
./gradlew installDist
./build/install/examples/bin/hello-world-server &
./build/install/examples/bin/hello-world-client
```

Percebeu como foi fácil adicionar uma operação ao serviço? Agora nos foquemos nos detalhes.

#### Stub do servidor

* Como criar o servidor
* Como definir o serviço
* Como "startar" o servidor.

#### Stub do cliente

* Stub bloqueante
* Stub não bloqueante

#### IDL gRPC

Outras características da IDL do gRPC

* Tipos básicos
  * bool: boolean (true/false)
  * double: 64-bit; ponto-flutuante 
  * float: 32-bit; ponto-flutuante 
  * i32: 32-bit; inteiro sinalizado 
  * i64: 64-bit; inteiro sinalizado
  * siXX: signed
  * uiXX: unsigned
  * sfixedXX: codificação de tamanho fixo
  * bytes: 8-bit; inteiro sinalizado
  * string: string UTF-8 ou ASCII 7-bit
  * Any: tipo indefinido

* [Diferentes traduções](https://developers.google.com/protocol-buffers/docs/proto3)

* Coleções
Defina e implemente uma operação `DigaOlas` em que uma lista de nomes é enviada ao servidor e tal que o servidor responda com uma longa string cumprimentando todos os nomes, um ap;os o outro.

* *Streams*
  - Do lado do servidor

  ```java
   List<String> listOfHi = Arrays.asList("e aih", "ola", "ciao", "bao", "howdy", "s'up");

   @Override
   public void digaOlas(OlaRequest req, StreamObserver<OlaReply> responseObserver) {
   for (String hi: listOfHi)
   {
     OlaReply reply = OlaReply.newBuilder().setMessage(hi + ", " req.getName()).build();
     responseObserver.onNext(reply);
   }
   responseObserver.onCompleted();
   }
  ```
  - Do lado do cliente
  
  ```java
   OlaRequest request = OlaRequest.newBuilder().setName(name).build();
   try {
       Iterator<OlaReply> it = blockingStub.digaOlas(request);
       while (it.hasNext()){
         OlaReply response = it.next();
         logger.info("Greeting: " + response.getMessage());
       }
    } catch (StatusRuntimeException e) {
       logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
       return;
    }
  ```




## Exemplo Python

```bash
apt-get install python3
apt-get install python3-pip
python3 -m pip install --upgrade pip
python3 -m pip install grpcio
python3 -m pip install grpcio-tools

git clone -b v1.10.x https://github.com/grpc/grpc
cd grpc/examples/python/helloworld
python3 greeter\_server.py
python3 greeter\_client.py
```

Para recompilar os stubs, faça

```bash
python3 -m grpc_tools.protoc -I../../protos --python_out=. --grpc_python_out=. ../../protos/helloworld.proto
```

Modifique o servidor

```Python
def DigaOla(self, request, context):
	return helloworld_pb2.OlaReply(message='Ola, %s!' + request.name)
```

Modifique o cliente

```Python
response = stub.DigaOla(helloworld_pb2.OlaRequest(name='zelelele'))
print("Greeter client received: " + response.message)
```
