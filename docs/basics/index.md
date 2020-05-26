# Fundamentos

A pedra fundamental da construção de sistemas distribuídos é a capacidade de comunicação entre seus componentes.
No mundo de hoje, isto quer dizer que os *hosts* dos componentes devem possuir interfaces de rede e que estas interfaces estejam ligadas a uma rede com capacidade de roteamento de dados, estabelecendo um **canal de comunicação** entre os componentes.
Além do canal, é também necessário que se estabeleça um **protocolo de comunicação**, que define as regras para que a comunicação aconteça, por exemplo, a gramática para formação de mensagens.

Também importantes, de um ponto de vista prático do desenvolvimento, são os conceitos de concorrência e paralelismo.
Afinal, um componente pode necessitar manter várias "conversas" em paralelo com múltiplos outros componentes.

Neste capítulo, revisaremos de forma rápida tanto conceitos de redes de computadores quanto de concorrência e paralelismo.


## Canais e Protocolos de Comunicação

Um canal de comunicação é o meio pelo qual os elementos da conversa entre os componentes do sistema distribuído são transmitidos e o protocolo são as regras codificam tal conversa.
Por exemplo, quando você fala com uma pessoa, cara-a-cara, o meio de comunicação é o ar e o protocolo utilizado é a linguagem conhecida pelas duas partes, o Português por exemplo.
Na prática, canais de comunicação podem ter diversas formas e características, por exemplo:

* Ponto-a-ponto
	* Eficiente
	* Caro para muitos nós
	* Roteamento trivial
* Compartilhado
  	* Colisões
  	* Menor custo
  	* Roteamento mais complicado

Nas redes atuais, pode se dizer que o meio mais utilizado é provido pela arquitetura **Ethernet**, que trata da comunicação nós usando um **barramento compartilhado**.
Sobre este meio, são usados protocolos para, por exemplo,

* Controle de acesso ao meio 
* Transmissão de mensagens
* Evitar e tratar colisões

As redes Ethernet, contudo, cobrem pequenas áreas e para se ter conversas "mais interessantes", é necessário que se conecte diversas destas redes.
A conversa então é feita por meio de intermediários, ** *gateways* ** que conectam duas ou mais redes, permitindo que mensagens de um interlocutor sejam **roteadas** para o outro, via tais intermediários.

Um exemplo interessante das questões ligadas à manutenção da conversa entre dois pontos é a decisão sobre o uso de **comutação de pacotes** (*packet switching*) ou de **circuitos** (*circuit switching*).

* Comutaçao de pacotes 
	* Dados divididos em pacotes
	* Cada pacote viaja independentemente
	* Pacotes são perdidos
	* Latência variável
* Circuit switching
	* Caminho dedicado
	* Recursos reservados
	* Pacotes de tamanho fixo
	* Latência constante

Outro fator importante é o MTU, o tamanho máximo de um pacote em determinada rede. É necessário entender que qualquer quantidade de dados maior que o MTU precisará ser dividida em múltiplos pacotes. Também é importante perceber que redes são heterogêneas, e que o vários segmentos no caminho entre origem e destino podem ter MTU diferentes, levando à fragmentação de pacotes em trânsito e, possivelmente, entrega desordenada dos mesmos.

Finalmente, há a questão importante é relativa à confiabilidade na transmissão dos elementos da conversa, isto é, se a rede deve garantir ou não que algo "dito" por um interlocutor deve garantidamente ser "ouvido" pelo outro, ou se a mensagem pode ser perdida no meio.

Felizmente boa parte da complexidade da resolução destas questões é abstraída do desenvolvedor dos sistemas distribuídos, isto é, **você**, lhe cabendo apenas a decisão de qual protocolo utilizar.
Nas redes atuais, a conversa em componentes será feita, em algum nível, por meio dos protocolos da arquitetura **Internet**.


## A Internet

A Internet tem este nome por usar o protocolo de interconexão de redes indepententes, o *internetworking protocol*, ou IP.
Para a aplicaçãu usando o IP, todas as redes se comportam com uma única e coerente rede, exceto por alguns detalhes.
Os elementos que conectam as diversas redes são denominados **roteadores** e fazem um **melhor esforço** para encaminhar os pacotes de dados do remetente ao destinatário.

![A Internet](images/network.png)

Se você se lembrar da pilha de protocolos de comunicação de referência OSI, lembrará que há sete camadas na mesma.
Cada camada é responsável pela comunicação em um nível e serve de fundação para a funcionalidade da camada de cima.
Cada camada tem um **cabeçalho** (*header*) e uma **carga** (*payload*) e o conjunto de cabeçalho + carga de uma camada é considerado carga da camada inferior.
Assim, embora tenha-se a impressão de que cada camada conversa com a equivalente do outro lado da comunicação, na prática, a comunicação desce e sobe a pilha. 

![image](images/04-01.png)


1. Bits
* Frames/quadros; controle de fluxo; acesso ao meio.
* Datagramas/pacotes; roteamento
* Controle de fluxo; fim a fim; confiabilidade; tcp e udp
* Streams/fluxos; conexões lógicas; restart; checkpoint; http, ssl
* Objetos; json, xml; criptografia
* Aplicações; http, pop, ftp

[![OSI x IP](images/osi-ip.jpg)](http://computing.dcu.ie/~humphrys/Notes/Networks/intro.2.html)

Embora o IP se refira estritamente ao protocolo da camada 3 da pilha, nos referimos à pilha que usa este protocolo como a pilha IP.
Comparada à pilha OSI, a IP é mais simples, como se vê na figura.
Como usuários da pilha IP, temos que entender como a camada 3 funciona, mas dificilmente interagiremos com algo além da camada 4, a camada de **transporte**.

Como se vê, as camadas 5 e 6 não estão presentes na pilha IP e as funcionalidades correspondentes são implementadas na camada 7, de aplicaçao.
Contudo, não tema! Estas funcionalidades podem se normalmente implementadas por meio de *frameworks* ou do *middleware* em uso.
Alguns exemplos de tais funcionalidades são

* (De)Serialização
* Nomeamento
* Criptografia
* Replicação
* Invocação remota de procedimentos

A grande vantagem desta abordagem é que se pode implementar exatamente e somente as funcionalidades desejadas.
Este característica é conhecida como o [argumento fim-a-fim no projeto de sistemas](http://web.mit.edu/Saltzer/www/publications/endtoend/endtoend.pdf); uma análise recente deste argumento foi feita [aqui](https://blog.acolyer.org/2014/11/14/end-to-end-arguments-in-system-design/).


## No princípio, era o Socket

Na prática, para implementarmos a comunicação entre processos, usamos **sockets**.
Para se definir um socket a partir de um **host** é necessário identificar o outro fim da comunicação, isto é, o outro *host*, ou melhor, uma de suas interfaces de rede.
Os sockets são então a abstração dos canais de comunicação, mas como dito antes, é necessário definir também os protocolos usados por estes sockets.
O primeiro protocolo é o de endereçamento, que define qual pilha de protocolos usar, na camada 3.
No caso da pilha IP, usa-se o protocolo AF\_INET ou PF\_INET.
Escolhido o protocolo, 

* cada interface tem um endereço MAC, na camada 2, que o identifica entre as interfaces na mesma rede local, e 
* cada interface tem um endereço IPv4/IPv6 de 32/128 bits, que o indentifica entre todos os hosts na Internet [^obs]. 

[^obs]: Endereços IP não públicos não server como identificadores únicos na Internet.

Mas dentro de um *host*, podem haver diversas aplicações sendo executadas. Como identificar exatamente com qual se quer conversar?
Isto é feito pela definição uma porta:

* Porta: 16 bits
     * [IANA](http://www.iana.org) (Internet Assigned Numbers Authority)
     * Bem conhecidas -- 0-1023
     * Proprietárias -- 49151
     * Dinâmicas -- 65535

Também é necessário definir também o protocolo de transporte dos dados, na camada 4.
Novamente, no caso da pilha IP, pode-se usar TCP (**SOCK\_STREAM**) ou UPD (**SOCK\_DGRAM**).

A API usada para estabelecer a conversa via socket tem várias chamadas, que devem ser executadas na ordem certa no processo iniciando a conversa e naquele que aceita participar da mesma. Comecemos estudando o TCP.

### TCP

O fluxograma da criação de um socket TCP é apresentado na seguinte figura:
```mermaid
stateDiagram-v2
   Servidor --> Entrada/Saída
   Cliente --> Entrada/Saída
   Entrada/Saída --> sc

   state Servidor {
     ss: Cria socket
     sb: Associa porta
     sl: Escuta conexões
     sa: Aceita conexões
     ss --> sb 
     sb --> sl
     sl --> sa
   }

   state Entrada/Saída {
     leitura --> escrita
     escrita --> leitura
   }

   state Cliente {   
     cs: Cria socket
     cc: Inicia conexão
     cs --> cc
   }

   state sc: Fecha conexão
```

<!--![image](images/04-15.png)-->

Estabelecido o socket, o mesmo pode ser usado como **arquivo**, isto é, lendo-se e escrevendo-se bytes.
O que exatamente deve ser escrito e como o que é lido deve ser interpretado é o protocolo da camada 7, **sua responsabilidade**.

Vejamos um exemplo do uso de sockets, em Python.
O seguinte arquivo pode ser nomeado, por exemplo, `server.py`, mas não pode, de forma alguma, ser nomeado `socket.py`.

```python
#server.py
#!/usr/bin/python                               # This is server.py file

import socket                                   # Import socket module

s = socket.socket()                             # Create a socket object
host = socket.gethostname()	               # Get local machine name
port = 12345                                    # Reserve a port for your service.
s.bind((host, port))                            # Bind to the port

s.listen(5)                                     # Now wait for client connections.
while True:
   c, addr = s.accept()                         # Establish connection with client.
   print('Got connection from', addr)
   c.send('Thank you for connecting'.encode())
   c.close()                                    # Close the connection
```

Para executá-lo, execute o seguinte comando em um terminal. 

```bash
python server.py
```

Em outro terminal, execute **um dos** dois comandos a seguir[^nc]: 
[^nc]: Se o segundo comando não funcionar, tente `nc` em vez de `netcat`.

```bash
telnet localhost 12345
```

```bash
netcat localhost 12345
```

O que está acontecendo aqui é um processo criou um socket e ficou aguardando uma conexão, usando o código em Python.
Tanto o telnet quando o netcat são programas genéricos para se conversar com outro processo usando TCP/IP.
Aqui, estes programas simplesmente se conectaram e imprimiram o que quer que o primeiro processo lhes tenha enviado, assumindo que correspondia a uma string, o que neste caso é correto.
Simples, não é mesmo?

Em geral, denominamos o processo que fica aguardando a conexão de **servidor** e o processo que se conecta de **cliente**. Isto por quê, em geral, o servidor executa alguma tarefa, serve, o cliente, embora isto não seja necessariamente verdade.


Por completude, vamos também escrever o código do cliente, agora que você já sabe que o servidor funciona.
Do lado cliente, estabelece-se uma conexão apontando-se para onde está o servidor.
```Python
#client.py

#!/usr/bin/python                               # This is client.py file

import socket                                   # Import socket module

s = socket.socket()                             # Create a socket object
host = socket.gethostname()                # Get local machine name
port = 12345                                    # Reserve a port for your service.

s.connect((host, port))
data = s.recv(1024)
print(data.decode())
s.close()                                         # Close the socket when done
```

E para se executar o cliente, faça:
```bash
python client.py
```

Observe que o `socket.close()` encerra a conexão do lado de quem invoca. Na contraparte, invocações a `socket.recv()` retornam com 0 bytes lidos.

A título de comparação, em Java, a criação do socket do lado do servidor seria muito mais simples, consistindo apenas em: 
```Java
Socket s = new ServerSocket(port);
```

O cliente em Java também é simplificado.
```Java
Socket s = new Socket(hostname,port);
```

#### Exercício: Múltiplos Pacotes

Façamos agora uma modificação no código do servidor para que envie não uma, mas duas mensagens para o cliente. Isto é, modifique seu servidor assim

```Python
...
   c.send('Thank you for connecting'.encode())
   c.send('Come back often'.encode())
...
```

Agora execute novamente o cliente e veja o que acontece.  Consegue explicar o fenômeno?

Modifiquemos o cliente agora, para que tenha dois `recv`, assim.
```Python
...
print("1")
data = s.recv(1024)
print(data.decode())
print("2")
data = s.recv(1024)
print(data.decode())
...
```

E agora, o que acontece? A saída é como esperava? Como explica este fenômeno e como poderia corrigí-lo?

####  Exercício: Ping-Pong

Modifique cliente e servidor tal que o cliente envie uma mensagem passada na linha de comando ao servidor e fique esperando uma resposta, e tal que o servidor fique esperando uma mensagem e então solicite ao operador que digite uma resposta e a envie para o cliente. O loop continua até que o usuário digite SAIR, e a conexão seja encerrada.

<table>
<tr>
<td> Terminal 1</td> <td> Terminal 2</td>
</tr>
<tr>
<td> 
```bash
python server.py
Esperando conexão.
Esperando mensagem.
Mensagem recebida: lalala
Digite resposta: lelele
Resposta enviada.
Conexão encerrada.
Esperando conexão.
```
</td>
<td>
```bash
python client.py
Digite mensagem: lalala
Mensagem enviada.
Esperando resposta.
Resposta recebida: lelele
Digite mensagem: SAIR
Desconectando.
```
</td>
</tr>
</table>

Observe que para ler do teclado em Python 2 você deve usar `x = raw_input()`, enquanto que em Python 3 seria `x = input()`. Além disso, em Python 2, você deve remover as invocações para `encode` e `decode`.



### UDP 

No exemplo anterior, usamos o protocolo TCP (o padrão da API). Caso quiséssemos usar UDP, precisaríamos nos atentar a alguns detalhes.

A criação do socket é feita explicitando-se o uso de **datagramas**: `s = socket.socket(socket.AF_INET,socket.SOCK_DGRAM)`

Um servidor UDP não executa `listen` ou `accept` e, em Python, simplesmente executa `data, addr = sock.recvfrom(4096)` para receber o datagrama, onde `data` é o conteúdo recebido e  `addr` o endereço de quem enviou o datagrama.

Neste caso, um mesmo socket é usado para manter comunicação com múltiplos interlocutores. Para enviar uma resposta a um interlocutor em específico, `addr` é usado: `#!py3 sent = sock.sendto(data, addr)`, onde `sent` é a quantidade de bytes enviados.

Além deste detalhe, é importante manter em mente outras características do UDP:

* falta de ordem
* falta de confiabilidade
* menos dados lidos que enviados.
* mais dados lidos que enviados (pode acontecer também no TCP)

Com tantas dificuldades para se usar o UDP, fica a questão: **para que serve UDP?**


#### Exercício: Ping-Pong UDP
Modifique o código do exercício Ping-Pong para usar UDP em vez de TCP na comunicação entre nós.
Execute múltiplos clientes ao mesmo tempo. Como o seu servidor lida com isso? Modifique-o para mandar um "eco" da mensagem recebida de volta ao remetente. 


### IP-Multicast

Imagine que você tenha que enviar um *stream* de vídeo para um destinatário, mostrando como você está jogando o mais novo jogo da velha no mercado.
Qual protocolo de transporte você usaria? TCP, provavelmente, já que garante a entrega ordenada dos pacotes do vídeo.
Como você já sabe, o TCP envia confirmações de pacotes recebidos e usa uma janela deslizante para determinar quais pacotes reenviar, o que pode causar interrupções na execução do vídeo.
Além do mais, as pessoas provavelmente preferirão perder alguns quadros que perder a sincronia com sua excitante partida.
Parece que uma opção melhor seria então usar UDP, correto?

Imagine agora que os mesmos dados devam ser enviados para múltiplos destinatários (você está ficando famoso!)
Com múltiplos destinatários, múltiplos controles precisariam ser mantidos no TCP, o que pode se tornar custoso; mais uma razão para usar UDP!

Para terminar, lhe darei uma razão final: IP-Multicast!
Multicast, em oposição ao Unicast, é a capacidade de enviar mensagens para um grupo de destinatários, em vez de apenas um. 

![Multicast](images/ipmulticast.jpg)

IP-Multicast é uma implementação desta ideia, usando umaa configuração específica do UDP, associada a recursos dos comutadores de rede, para otimizar o envio dos mesmos dados a múltiplos destinatários.
Grupos são identificados por endereços IP especiais, conhecidos como Classe D (224.0.0.0-239.255.255.255), e propagados pela rede.

[![](images/ipmulticast2.png)](http://www.dasblinkenlichten.com/understanding-ip-multicast/)

Quando um pacote é enviado para o endereço do grupo, **todos** os membros do grupo recebem tal mensagem.
Melhor dizendo, todos os membros podem receber a mensagem, mas como estamos falando de UDP, **é possível que alguns não recebam**.
Além disso, **não há garantia qualquer sobre a ordem de recepção das mensagens**.

Apenas reforçando, IP-Multicast só funciona com UDP, pois lidar com retransmissões em um grupo grande levaria a um estado imenso sendo mantido na origem dos dados.
Outro ponto importante é que pelo podencial desestabilizador do IP-Multicast, ele é normalemente limitado à pequenas seções das redes.

Mas experimentemos com esta tecnologia na prática.
Criemos um programa que **criar Socket UDP**, **associa-o a um grupo**, e **recebe pacotes** destinados ao grupo.

```Java
// MReceiver.java
import java.io.*;
import java.net.*;

public class MReceiver {
  public static void main(String[] args) {
    byte[] inBuf = new byte[256];
    try {
      MulticastSocket socket = new MulticastSocket(8888);
      InetAddress address = InetAddress.getByName("224.2.2.3");
      socket.joinGroup(address);
      while (true) {
        DatagramPacket inPacket = new DatagramPacket(inBuf, inBuf.length);
        socket.receive(inPacket);
        String msg = new String(inBuf, 0, inPacket.getLength());
        System.out.println("From " + inPacket.getAddress() + " Msg : " + msg);
      }
    }catch (IOException ioe) {
      System.out.println(ioe);
    }
  }
}
```

Instancie múltiplos processos deste, na mesma máquina e em máquinas distintas.
Agora criemos um programa que envia pacotes para o dito grupo.

```Java 
// MSender.java
import java.io.*;
import java.net.*;
public class MSender {
 public static void main(String[] args) {
  byte[] outBuf;
  final int PORT = 8888;
  try {
   DatagramSocket socket = new DatagramSocket();
   long counter = 0;
   InetAddress address = InetAddress.getByName("224.2.2.3");
   while (true) {
    counter++;
    outBuf = ("Multicast numero " + counter + " " + address).getBytes();
    DatagramPacket outPacket = new DatagramPacket(outBuf, outBuf.length, address, PORT);
    socket.send(outPacket);
    try { Thread.sleep(500); }catch (InterruptedException ie) {}
   }
  } catch (IOException ioe) { System.out.println(ioe); }
 }
}
```

Observe como a mesma mensagem é recebida pelos vários membros e que como diferentes fontes tem seus pacotes recebidos.

A título de curiosidade, IP-Multicast também está presente em IPv6, mas com algumas pequenas diferenças 

!!! note "IP-Multicast em IPv6[^ipv6multi]"
    In IPv6, the left-most bits of an address are used to determine its type. For a multicast address, the first 8 bits are all ones, i.e. FF00::/8. Further, bit 113-116 represent the scope of the address, which can be either one of the following 4: Global, Site-local, Link-local, Node-local.

    In addition to unicast and multicast, IPv6 also supports anycast, in which a packet can be sent to any member of the group, but need not be sent to all members.''

[^ipv6multi]: [IP-Multicast em IPv6](http://www.baeldung.com/java-broadcast-multicast)


#### Exercício: IP-Multicast

Implemente e teste o seguinte sevidor.

```Python
import socket
import struct

MCAST_GRP = '224.1.1.1'
MCAST_PORT = 5007

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
sock.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
sock.bind((MCAST_GRP, MCAST_PORT))
mreq = struct.pack("=4sl", socket.inet_aton(MCAST_GRP), socket.INADDR_ANY)
#4 bytes (4s) seguidos de um long (l), usando ordem nativa (=)

sock.setsockopt(socket.IPPROTO_IP, socket.IP_ADD_MEMBERSHIP, mreq)

while True:
    print(sock.recv(10240).decode())
```


Implemente e teste o seguinte cliente.

```Python
import socket

MCAST_GRP = '224.1.1.1'
MCAST_PORT = 5007

sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM, socket.IPPROTO_UDP)
sock.setsockopt(socket.IPPROTO_IP, socket.IP_MULTICAST_TTL, 2)
sock.sendto(input().encode(), (MCAST_GRP, MCAST_PORT))
```

---

### Referências

* [UDP em Python](http://pymotw.com/2/socket/udp.html)
* [UDP em Python](http://www.tutorialspoint.com/python/python_networking.htm)
* [Multicast em Java](lycog.com/programming/multicast-programming-java/)
* [Multicast em Python](https://stackoverflow.com/questions/603852/multicast-in-python)



## Multiprogramação e *Multithreading* em Sistemas Distribuídos

É impossível pensar em sistemas distribuídos sem pensar em concorrência na forma de múltiplos processos executando, normalmente, em hosts distintos.
De fato, os exemplos que apresentamos até agora consistem todos em um processo cliente requisitando ações de algum processo servidor.
Apesar disso, a interação entre tais processos aconteceu sempre de forma sincronizada, *lock-step*, em que o cliente requisitava o serviço e ficava bloqueado esperando a resposta do servidor, para então prosseguir em seu processamento, e o servidor fica bloqueado esperando requisições, que atende e então volta a dormir.
Este cenário, apresentado na figura a seguir, mostra que apesar do uso de processadores distintos e da concorrência na execução dos processos, temos um baixo grau de efetivo paralelismo.

```mermaid
sequenceDiagram
    activate Cliente
    note left of Cliente: Ativo (gerando requisição)
    note right of Servidor: Inativo (esperando requisição)
	Cliente->>+Servidor: Request
    deactivate Cliente
    note left of Cliente: Inativo (esperando resposta)
    note right of Servidor: Ativo (processando requisição)
	Servidor-->>-Cliente: Response
	activate Cliente
    note left of Cliente: Ativo (processando resposta
	note right of Servidor: Inativo (esperando requisição)
	deactivate Cliente
```


Para usarmos melhor os recursos disponíveis, tanto do lado dos clientes quanto servidores, uma das razões de ser da computação distribuída, temos então que pensar em termos eventos sendo disparados entre os componentes, que devem ser tratados assim que recebidos ou tão logo haja recursos para fazê-lo. 
Estes eventos correspondem tanto a requisições quanto a respostas (efetivamente tornando difícil a distinção).
Além disso, sempre que possível, um componente não deve ficar exclusivamente esperando por eventos, aproveitando a chance executar outras tarefas até que eventos sejam recebidos.
Dada que processos interagem com a rede usando sockets, cuja operação de leitura é bloqueante, para aumentar a concorrência em um processo, precisamos falar de multi-threading.

Há duas razões claras para estudarmos multi-threading. A primeira, de ordem prática, é a discutida acima: permitir o desenvolvimento de componentes que utilizem "melhormente" os recursos em um host.
A segunda, didática, é o fato que muitos dos problemas que aparecem em programação multi-thread, aparecem em programação multi-processo (como nos sistemas distribuídos), apenas em um grau de complexidade maior.
Para relembrar, há várias diferenças entre *threads* e processos, mas a abstração é essencialmente a mesma:

| Processo | Thread |
|----------|--------|
| Instância de um programa | "Processo leve"|
| Estado do processo | Estado do thread | 
| Função main | "qualquer" função|
| Memória privada ao processo| Compartilha estado do processo que os contém|
| Código, Stack, Heap, descritores (e.g, file descriptors), controle de acesso | Stack, variáveis locais |
| IPC - Inter process communication  | IPC -- Inter process communication|
| Sistema operacional | Diferentes implementações |
| | Posix, C++, Java, ...|

Vejamos como o uso de múltiplos threads podem melhorar o desenvolvimento de sistemas distribuídos na prática.
Considere os exemplos de clientes e servidores vistos [anteriormente](#tcp).
Imagine que em vez do serviço simples feito no exemplo, o servidor retorne uma página Web.
Detalhes do protocolo seguido por navegadores e servidores serão vistos mais tarde. Por agora, considere apenas que uma requição `GET arquivo.html` será enviada para o servidor que lerá o arquivo especificado do sistema de arquivos; como você sabe, ler um arquivo é uma operação lenta e que não requer CPU.


### Cliente multithreaded

Do ponto de vista do cliente, a vantagem do uso de múltiplos threads são claras: permite lidar com **várias tarefas concorrentemente**, por exemplo solicitar CSS, HTML e imagens concorrentemente, **escondendo latência** das várias operações, e permite **organizar código** em blocos/módulos.
Se você usar o console de desenvolvimento do navegador, verá que trinta e seis requisições são feitas para carregar a página [www.google.com](https://www.google.com); um número muito maior é feito na carga de [www.bing.com](https://www.bing.com).

!!! todo
    Estender

---

### Servidor multithreaded

#### Single-threaded
Há diversas possibilidades de uso de threads em servidores. A mais simples é usar apenas um, com temos feito até agora:

1. o cliente envia a requisição para o servidor
2. o servidor aceita a conexão em seu único thread
3. uma tarefa é gerada para ler o arquivo
4. o arquivo é lido, de forma bloqueante, e uma resposta para o cliente é preparada
5. a resposta é enviada para o cliente, de forma bloqueante
6. a requisição é descartada
7. o thread do servidor volta a esperar uma nova requisição

![Single Threaded](./images/singlethreadedserver.gif)

#### Thread per request

Outra opção é criar um novo thread para cada nova requisição, levando a múltiplos threads atendendo a múltiplas requisições concorrentemente.
Desta forma, quando um thread é bloqueado para leitura de arquivos do disco, outros clientes podem continuar sendo atendidos.

![Multi Threaded](./images/multithreadedserver.gif)

Contudo, o número de threads que se pode criar em um SO é limitado. 
Além disso, a criação e destruição de threads é cara e por isso devemos evitar este processo.

#### Thread pool

Assim, temos uma outra opção que também usa múltiplos threads, que usa *pools*  de threads para lidar com as requições.
No cerne desta abordagem, junto com um pool de threads, fica uma fila bloquenante **thread-safe**, isto é, que se mantem correta mesmo quando múltiplos threads operam nela tanto para inserir quanto remover tarefas, e que bloqueia os threads que tentam inserir quando a fila está cheia ou remover quando ela está vazia.

[![Pool Threaded](./images/poolthreadedserver.gif)](https://www3.nd.edu/~dthain/courses/cse30341/spring2009/project4/project4.html)

Um thread principal é encarregado de receber as requisições e colocar na fila bloqueante; se a fila fica cheia, o thread principal fica bloqueado esperando por espaço, fazendo com que novas conexões tenham que esperar.
Os threads do pool removem uma tarefa da fila, a tratam e, ao final do atendimento,  pegam nova requisição na fila, em um loop infinito; se a fila se esvazia, os threads ficam bloqueados esperando novas requisições.

É possível refinar mais este modelo, quebrando o processamento em vários pools, no que é conhecido como **Staged Event-Driven Architecture**, SEDA.

[![Seda](images/seda1.png)](http://images.cnitblog.com/blog/13665/201306/15180500-a54c8eb3d73246469f1b74ee74f2119b.png)

Nesta abordagem, cada **estágio**,  por ter seu próprio *pool*, pode ser escalado individualmente de acordo com a demanda do estágio.
Esta abordagem também é útil quando múltiplas partes da tarefa consistem em E/S.

[![Seda](images/seda2.png)](http://images.cnitblog.com/blog/13665/201306/15180500-a54c8eb3d73246469f1b74ee74f2119b.png)

Uma extrapolação que pode ser feita aqui, reforçando a observação que problemas (e soluções) de sistemas distribuídos são refletidos em nível de processamento paralelo e concorrente, é que a uma arquitetura SEDA lembra em muito a arquitetura de [micro-serviços](http://muratbuffalo.blogspot.com.br/2011/02/seda-architecture-for-well-conditioned.html).

Para aprender mais sobre SEDA, vá [aqui](http://courses.cs.vt.edu/cs5204/fall05-gback/presentations/SEDA_Presentation_Final.pdf).  


<h1> TODO </h1>


#### Problemas com multithreading

Embora a ideia de usar múltiplos threads seja resolver problemas, fazê-lo efetivamente não trivial.
Vejamos, por exemplo, o problema de definir afinidade entre threads, isto é, de definir quais threads compartilham o mesmo estado de forma que threads afins sejam colocados nos mesmos processadores e compartilhem as mesmas memórias. 
Isto torna muito mais fácil e eficiente o controle de concorrência, do ponto de vista do SO e hardware.

![Multithreaded](images/multithread2.png)

A realidade, contudo, é outra e simplesmente criar múltiplos threads não garante paralelismo perfeito, pois o SO é quem é responsável por escalonar os mesmos, e é difícil determinar (se existir) uma configuração ótima em termos de afinidade  que seja também eficiente.

![Multithreaded](./images/multithreaded.jpg)

Memes bonitinhos à parte, precisamos lidar com estado compartilhado e enfrentar condições de corrida de forma a não levar a **inconsistências** na executação de tarefas, nos referindo a inconsistência aqui como qualquer desvio no comportamento do programa daquilo que foi especificado pelo desenvolvedor.
Para isso, usamos as primitivas de controle de concorrência que estudaram em SO, que também tem seus problemas em potencial, como **deadlocks** e **inanição**.
Veja o seguinte vídeo para uma análise de diversos pontos importantes no uso de multithreads.

<iframe width="560" height="315" src="https://www.youtube.com/embed/JRaDkV0itbM" frameborder="0" allow="accelerometer; autoplay; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>


#### Estado em Servidores

A questão das regiões críticas no servidor está intimamente relacionada à questão da manutenção de estado nos servidores.
Quanto a este respeito, podemos classificar servidores como **stateful** e **stateless**, dois termos que ouvirão frequentemente enquanto trabalhando com SD.

O "state" nos dois nomes se refere ao estado mantido por um serviço para atender a requisições.
Caso mantenha estado, por exemplo informando em quais arquivos o cliente está interessado, fica mais fácil para o servidor continuar o trabalho feito em requisições anteriores.
Imagine por exemplo que um cliente esteja acessando linhas em um banco de dados, de forma paginada: a cada requisição, o cliente recebe $n$ novas linhas para processar e, quando estiver pronto, requisite $n$ novas linhas.
Imagine quão infeficiente seria se o servidor seguisse o seguinte flxo:

1. receba requisição informando a última linha lida
2. recalcule todas as respostas para consulta
3. salte até a linha informada pelo cliente
4. retorne as próximas $n$ linhas para o cliente
5. feche o resultado da consulta.

Se em vez disso o servidor mantiver um mapa com consultas recentes, em que a chave seja algum identificador do cliente e o valor uma *visão*  dos resultados; a cada nova requisição, basta o servidor preparar rapidamente uma nova resposta.
Em contrapartida, considere que múltiplos clientes fazem consultas concorrentemente: quanto recurso seria necessário para que o servidor mantenha a visão de todos os clientes?
Também a complexidade do servidor aumenta, uma vez que ele precisa manter as respostas a novas requisições consistentes com as respostas anteriores e portanto, caso o serviço seja implementado por múltiplos servidores acessíveis ao cliente,  o estado deve ser compartilhado por tais servidores.
Além disso, imagine que o cliente resolva não fazer mais requisições, por exemplo por ter encontrado o que procurava: por quanto tempo o servidor deve manter a visão aberta?

Você já deve ter adivinhado que no primeiro exemplo temos um servidor *stateless* e no segundo um *stateful*, e percebido que cada um tem suas vantagens e desvantagens.
Vejamos mais algumas.

##### Informação sobre Sessão

Essencialmente, o servidor *stateless* não mantem informação sobre a sessão do cliente e requer que a cada nova requisição, quaisquer informações necessárias para realizar a tarefa requisitada sejam novamente fornecidas ao servidor.
No caso *stateful*, o servidor pode se lembrar, como no exemplo anterior, até onde o trabalho já foi executado, quais arquivos o cliente manipulou (e mantê-los abertos), qual o endereço o cliente e enviar-lhe notificações importantes (e.g., "Novo dado inserido!").

##### Tratamento de falhas

Enquanto servidores *stateful* obviamente levam a melhor desempenho no *happy path* (contanto que recursos suficientes sejam providos), no caso de falhas, serviços *stateless* tendem a voltar ao ar mais rapidamente, uma vez que não há estado que precise ser recuperado.
Pela mesma razão, clientes que percebem que um servidor falhou, podem rapidamente se dirigirem a outros servidores e continuar suas requisições de onde estavam, uma vez que são detentores de toda a informação necessária para o próximo passo do processamento.

Lidar com falhas também introduz outro requisito aos servidores: memória estável.
Para que possa o recuperar o estado anterior à falha, o servidor precisa colocar o estado em algum lugar que independa do processo para se manter, por exemplo,
[nvRAM](https://en.wikipedia.org/wiki/Non-volatile_random-access_memory), [SSD](https://en.wikipedia.org/wiki/Solid-state_drive) ou [spindles](https://en.wikipedia.org/wiki/Hard_disk_drive#Spindle).
A perda deste estado implicaria na incapacidade de prover o serviço corretamente.
Um projeto *stateless* não depende deste estado e por isso pode ser mais rapidamente recuperado, replicado ou substituído.

##### Qual é melhor?

Não surpreendentemente, a resposta para "qual abordagem é melhor, *stateful* ou *stateless*?" é **depende**.
Ambos as opções tem suas vantagens e desvantagens e para algums serviços apenas uma opção será viável.
Se seu serviço precisa manter estado (um SGBD, por exemplo), ele terá que manter estado, mesmo que não sobre clientes.
Veja um pequeno comparativo das características das duas abordagens.


| Stateless | Stateful |
|-----------|----------|
| Resultado depende da entrada| Depende do histórico de entradas |
| Qualquer servidor pode atender | Mesmo servidor deve atender |
| Não promete notificar o cliente | Assina contrato com o cliente |
| Repete operações | Aproveita resultados anteriores |
| Não fica inconsistente com relação ao cliente | Pode ficar inconsistente se perder estado ou conexão feita com outro servidor |
| re-autenticação (mesmo que simplficada) a cada requisição | Autentica no começo da sessão |


##### Leia mais

Uma visão interessante sobre estado é apresentada em [On stateless software design](https://leonmergen.com/on-stateless-software-design-what-is-state-72b45b023ba2).
Observe que não necessariamente eu concordo com tudo o que está escrito aqui, principalmente a questão sobre *stateful* ser sempre mais complexo.
A discrepância de visão está no fato de parte da complexidade ser levada para o cliente, no caso dos servidores *stateless*, mas não necessariamente ser eliminada.

[Sobre IO não bloqueante em Java.](https://www.developer.com/java/data/understanding-asynchronous-socket-channels-in-java.html)

### Multithread na prática



#### PThreads

[POSIX Threads](https://en.wikipedia.org/wiki/POSIX_Threads) ou PThreads, são uma definição aberta de como *threads* devem funcionar em sistemas operacionais.
Várias implementações desta especificação estão disponíveis tanto para sistemas Unix, compatíveis com especifições POSIX, mas também para Windows, via subsistemas.
Além disso, mesmo implementações não POSIX tem funcionalidade equivalentes e, por este motivo, entender POSIX servirá de base para entender quaisquer API para programação *multi-threaded*.


##### Função de entrada

Para se definir um *thread*, é necessário definir uma função de entrada, que será para o *thread* como a função `main` é para o processo em si.
No exemplo a seguir a função foi definida com retorno `void *` e com único parâmetro tambem `void *`; esta é uma obrigatoriedade para funções de entrata PThread.
Observe contudo que `void *` pode ser tratado como um blob para mascarar outros tipos de dado, por exemplo um vetor, um enumeração ou uma `struct`.

```c
#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
	
int thread_count;

void* hello(void* rank) {
	long my_rank = (long) rank;
	printf("Hello from thread %ld of %d\n", my_rank, thread_count);
	return NULL;
}
```

##### Criação

Um *thread*  é criado pela função `pthread_create`, que coloca em um `pthread_t` um *handle* para o *thread*.
A função recebe como parâmetros opções para configuração, a função de entrada, e o parâmetro do tipo `void *`.

```c
int main(int argc, char* argv[]) {
	long thread;
	pthread_t* thread_handles;
	
	if(argc < 2) {
		printf("usage: %s <number of threads>", argv[0]); 
		return 1;
	}
	
	thread_count = strtol(argv[1], NULL, 10);
	thread_handles = malloc(thread_count*sizeof(pthread_t));
	
	for (thread = 0; thread < thread_count; thread++)
		pthread_create(&thread_handles[thread], NULL, hello, (void*) thread);
	
	printf("Hello from the main thread\n");
```

##### Destruição

O *handle* do *thread* deve ser alocado previamente à função de criação e liberado após o fim da execução do *thread*.
É possível esperar pelo fim da execução usando o `pthread_join`, que recebe como parâmetro o *handle* do *thread* e um ponteiro para onde o resultado da função de entrada deve ser colocado, do tipo `void **`.

```c
	for (thread = 0; thread < thread_count; thread++)
		pthread_join(thread_handles[thread], NULL);
	
	free(thread_handles);
```

##### Execução

Para executar um programa PThread, compile com
```bash
gcc -pthread teste.c -o teste
```
e execute com
```bash
./teste 5
```
e observe que a saída das threads é *ordenada*. 

Agora experimente
```bash
./teste 200
```
Observe que a saída é desordenada (pode ser necessário executar múltiplas vezes ou aumentar de 200 para, digamos, 1000 para observar a desordem.
Isto acontece porquê a execução das threads independe da ordem de criação.
De fato, usando PThreads, temos pouco controle sobre os threads que criamos. 
Mas isto não quer dizer que estamos "órfãos" de API; várias outras operações podem ser executadas, e podem ser encontradas a partir do [manual de `pthread_create`](http://man7.org/linux/man-pages/man3/pthread_create.3.html). Alguns exemplos interessantes:

* `pthread_tryjoin` - espera thread terminar
* `pthread_exit` - termina a thread e retorna resultado 
   > An implicit call to `pthread_exit()` is made when a thread other than the thread in which `main()` was first invoked returns from the start routine that was used to create it. The function's return value serves as the thread's exit status. [Manual de `pthread_exit`](http://man7.org/linux/man-pages/man3/pthread_exit.3.html).
	
* `pthread_attr_setaffinity_np *` - ajusta afinidade dos threads.

#### Threads Java

Neste tutorial, baseado neste [outro](https://docs.oracle.com/javase/tutorial/essential/concurrency/), exploraremos formas de se obter concorrência em Java. Isto é, exploraremos como iniciar múltiplas linhas de execução de instruções, que podem ou não, ser executadas em paralelo.

Em Java, há essencialmente duas formas de se conseguir concorrência. 
A primeira é via instâncias explícitas da classe `Thread`, e a segunda é via abstrações de mais alto nível, os `Executors`.


Além de formas de definir as linhas de execução, Java provê diversas estruturas para comunicação e coordenação destas linhas, desde de a versão 5 da linguagem, no pacote `java.util.concurrent`.


##### Criação de *Threads* Java
Há duas formas básicas de se usar a classe `Thread`, via extensão ou delegação de um objeto implementando `Runnable`.

###### Extender `Thread`
```Java
public class HelloThread extends Thread {
    public void run() {
        System.out.println("Hello from a thread!");
    }

    public static void main(String args[]) {
        Thread t = new HelloThread();
        t.start();
    }
}
```


###### Implementar `Runnable`
```Java
public class HelloRunnable implements Runnable {
    public void run() {
        System.out.println("Hello from a thread!");
    }

    public static void main(String args[]) {
        Thread t = new Thread(new HelloRunnable());
        t.start();
    }
}
```

Observe que nos dois exemplos, um método `run()` é implementado com o código a ser executado pelo *thread*. Em nenhum dos exemplos, contudo, o método é invocado diretamente. 
Em vez disto, o método `start()`, sim, é invocado. Isto ocorre pq antes de executar as instruções definidas pelo pelo programador no método `run()`,
a máquina virtual precisa executar alguma "mágica" por baixo dos panos como, por exemplo, solicitar ao sistema operacional a criação de um *thread* do SO, que servirá de hospedeiro para o *thread* Java. 
Isto acontece dentro do `start()`, que em algum ponto de sua execução levará à invocação do método `run()`.


##### API Thread
A classe `Thread` também provê uma série de métodos que permitem gerenciar a vida do *thread* criado. 
Por exemplo, o método de classe (`static`) `Thread.sleep()` permite bloquear um *thread*  por um determinado período.

###### Thread.sleep()
```Java
public class HelloRunnable implements Runnable {
    public void run() {
        for (int i = 0; i < 10; i ++)
        {
            System.out.println("Hello at instant " + i);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException ie) {
                System.out.println("awoken");
            }
        }
    }

    public static void main(String args[]) {
        Thread t = new Thread(new HelloRunnable());
        t.start();
    }
}
```

Observe que a chamada a `sleep()` está dentro de um bloco `try/catch`. Isto é necessário pois é permitido à JVM acordar o *thread* em qualquer instante, antes ou após o tempo especificado. Assim, embora normalmente o tempo "dormido" seja próximo ao especificado, se há requisitos de precisão, é necessário que o *thread*, ao acordar, verifique se já dormiu o suficiente.

###### InterruptedException
```Java
public class HelloRunnable implements Runnable {
    public void run() {
        for (int i = 0; i < 10; i ++)
        {
            System.out.println("Hello at instant " + i);
            long before = System.currentTimeMillis();
            long timeout = 1000;
            while(before + timeout > System.currentTimeMillis())
            {
                try {
                    Thread.sleep(Math.max(0,System.currentTimeMillis() - (before + timeout)));
                } catch (InterruptedException ie) {
                    System.out.println("awoken");
                }
            }
        }
    }

    public static void main(String args[]) {
        Thread t = new Thread(new HelloRunnable());
        t.start();
    }
}
```

Quando um *thread* está sendo executado, outros podem ter que esperar até que complete. Por exemplo, no caso de um navegador
Web, o *thread* que faz a renderização da página não pode começar a trabalhar enquanto o *thread*  que solicitou o HTML
do servidor não receber sua resposta. Um *thread* indica a intenção de esperar por outro usando o método `join()`.


###### Thread.join()
```Java
public class HelloRunnable implements Runnable {
    public void run() {
        Random rand = new Random();
        for (int i = 0; i < 10; i ++)
        {
            System.out.println("Hello at instant " + i);
            long before = System.currentTimeMillis();
            long timeout = 901 + rand.nextInt(200);
            while(before + timeout > System.currentTimeMillis())
            {
                try {
                    Thread.sleep(Math.max(0,System.currentTimeMillis() - (before + timeout)));
                } catch (InterruptedException ie) {
                    System.out.println("awoken");
                }
            }
        }
    }

    public static void main(String args[]) {
        Thread t = new Thread(new HelloRunnable());
        //t.setDaemon(true);
        t.start();
        try {
            t.join();
            //t.join(10000);
        } catch (InterruptedException ie) {
            System.out.println("Waiting was interrupted");
        }
        if (t.isAlive())
            System.out.println("Got tired of waiting");
        else
            System.out.println("Wait is over");
    }
}
```

Invocar `t.join()` fará com que o *thread* principal espere indefinidamente até que `t` termine de executar.
Caso seja necessário limitar o tempo de espera, o tempo pode ser especificado como na linha comentada. 
Caso a espera termine por causa de um *timeout*, é possível testar o estado atual do thread com `Thread.isAlive()`.

Outro método interessante, `Thread.setDaemon()`, especifica que o *thread* pode ser terminado quando a *thread* principal terminar. Descomente a invocação e teste o efeito.

##### Exercício

Vejamos um exemplo simples do uso de *threads*.

* Instancie um programa que gere 10 *threads*. 
* Todos os *threads* devem compartilhar **uma mesma instância** de `Counter`
* Cada *thread* deve executar um *loop* em que incrementa o valor do contador 20 vezes 
  * a cada vez, imprime o resultado precedido do identificador do *thread* (use `Thread.getName()` ou `Thread.currentThread().getName()`)
* A *thread*  principal deve esperar todas as outras terminarem antes de terminar (use `Thread.join()`).
* Analise a saída do programa observando a ordem de execução dos *threads*.

##### Counter.java
```Java
class Counter {
    private int c = 0;

    public int increment() {
        return ++c;
    }

    public int decrement() {
        return --c;
    }

    public int value() {
        return c;
    }
}
```


É fácil observar que a saída do programa é aleatória nos identificadores e tende a ser incremental nos contadores, mas nem sempre isso é verdade. 
Como discutido anteriormente, frequentemente *threads* tem que coordenar suas ações para que não pisem uns nos outros, por exemplo decidindo quem deve ser o próximo a entrar em uma região crítica ou será o responsável por uma tarefa. 
Em Java, esta coordenação pode ser feita por diversas abstrações: `synchronized`, `Lock`, variáveis atômicas, ...


##### `synchronized`
Ao definir métodos como `synchronized`, garante-se que os mesmos nunca serão executados concorrentemente. 
Observe a classe a seguir, que modifica o contador do exercício anterior.

```Java
public class SynchronizedCounter {
    private int c = 0;

    public synchronized int increment() {
        return ++c;
    }

    public synchronized int decrement() {
        return --c;
    }

    public synchronized int value() {
        return c;
    }
}
```

Caso dois *threads* invoquem os métodos `increment` e `decrement` ao mesmo tempo, por exemplo, a JVM fará com que um dos *threads* pare sua execução até que o outro tenha completado a invocação.
Isto não quer dizer que executar o exercício anterior com esta versão do contador não levará a saídas com incrementos completamente sequenciais, pois um *thread*  poderia parar de ser executado logo após incrementar o contador, depois de terminado o método `increment`, e só voltar a executar depois que outro tenha incrementado e impresso na tela o valor obtido. 
O que quer dizer é que, mesmo que saídas estranhas existam, cada operação foi executada integralmente antes da operação seguinte.

##### Exercício
Modifique o código do exercício anterior para usar a versão `synchronized` do contador. Depois de executá-lo, adicione um `println("Dentro: " + c)` **dentro** do método de incremento para verificar que estas saídas acontecem ordenadamente.

##### Blocos `synchronized`
`synchronized` funciona porquê limita a concorrência, e é problemático exatamente pela mesma razão. 
Por isso, é essencial que o `synchronized` seja o mais limitado possível em termos de escopo, o que nos leva ao uso de `synchronized` em blocos de código menores que métodos. Por exemplo:

```Java
public class Namer {
    String lastName = null;
    int nameCount = 0;

    public void addName(String name) {
       lastName = name;
        synchronized(this) {
           nameCount++;
        }
        nameList.add(name);
    }
}
```

Neste caso, blocos sincronizados **no mesmo objeto**, não são executados concorrentemente, mas outros blocos sim.

##### Exercício
Neste exercício, use dois objetos para travar o acesso a dois contadores. Instancie um programa com dois *threads*  tal que:

* executem um loop 1000 vezes em que
* o primeiro *thread* primeiro invoca `inc1` e depois `inc2`
* o segundo *thread* primeiro invoca `inc2` e depois `inc1`
* ambos os threads imprimem o valor de `c1` e `c2`

##### synchronized
```Java
public class MsLunch {
    private long c1 = 0;
    private long c2 = 0;
    private Object lock1 = new Object();
    private Object lock2 = new Object();

    public void inc1() {
        synchronized(lock1) {
            c1++;
        }
    }

    public void inc2() {
        synchronized(lock2) {
            c2++;
        }
    }
}
```

##### *Deadlock*
O uso dos "locks" em ordens diferentes pode levar a um deadlock, pois o seguinte grafo de dependência poderá ser gerado:

##### Deadlock

```mermaid
graph LR
      T1 --> lock1 --> T2 --> lock2 --> T1
```

##### Sinalização
Usados corretamente, o bloco `synchronized` é executado de forma atômica, isto é, indivisível.
Algumas operações muito simples são naturalmente atômicas, e não precisam ser "protegidas" pelo `synchronized`.
Por exemplo, leituras e escritas de tipos básicos como (`int`, `char`, `byte`, mas não `long` ou `double`), ou variáveis declaradas `volatile`.

Usando estas variáveis, é possível coordenar *threads*, por exemplo, assim:

##### Espera ocupada

```Java
boolean condicao = false;

...

public void espereCondicao() {
    while(!condicao) {}
    System.out.println("condicao alcancada.");
}

...

public void satisfacaCondicao() {
    condicao = true;
}
```


Embora correto, esta abordagem não é eficiente, pois o primeiro método desperdiça computação. 
Felizmente, em Java, todos os objetos implementam os métodos `wait` e `notify/notifyAll`, que podem ser usados para sincronizar eficientemente *threads*.


##### Wait/Notify

```Java
public class Sync{
   Object synch = new Object();
   boolean condicao = false;
   public void espereCondicao() {
      while(!condicao) {
         try {
            synch.wait();
         } catch (InterruptedException e) {}
      }
      System.out.println("Condicao alcancada");
   }
...
   public void satisfacaCondicao() {
      condicao = true;
      synch.notifyAll();
   }
}
```


##### *Locks*
Outras abstrações para coordenação de *threads* estão disponíveis no pacote `java.util.concurrent`. 
As mais simples delas são `java.util.concurrent.locks.Lock` e `java.util.concurrent.locks.ReentrantLock`. 
Veja um exemplo de uso, notando o idioma de uso dentro de block `try/catch`.

```Java
Lock l = new ReentrantLock();
  l.lock();
  try {
     // access the resource protected by this lock
  } finally {
     l.unlock();
  }
```


##### Executor
Além de *threads*, Java disponibiliza `Executor` como abstração de mais alto nível para execução de tarefas concorrentes.

* `Executor`
* `ExecutorService`
* `ScheduledExecutorService`

```Java
Executor e = ...;
Runnable r = ...;
e.execute(r);
```

Executors normalmente implementam *thread pools*, que podem ser de diferentes tipos. 
O mais simples é o de tamanho fixo em que há um número inicial de *threads* criados e que, no caso de algum ser terminado, por exemplo por causa de uma exceção não tratada, cria substitutos para manter o número constante.

##### ThreadPool
`Executor e = java.util.concurrent.Executors.newFixedThreadPool();`

* `newCachedThreadPool()` - *expandable thread pool*
* `newSingleThreadExecutor()` - *single task at a time*
* e outras versões
* `ForkJoinPool`

###### Fork/Join

```
if (my portion of the work is small enough)
  do the work directly
else
  split my work into two pieces
  invoke the two pieces and wait for the results
```

##### Estrutura para Coordenação de *Threads*

Finalmente, Java também disponibiliza estruturas de dados que podem ser acessadas concorrentemente por múltiplos *threads* 
sem risco de corrupção. 


* `BlockingQueue` - bloquei *threads*  se não houver elementos na filq.
* `ConcurrentMap/ConcurrentHashMap` - operações atômicas;
   * `if (!m.containsKey(k)) m.put(k,v);`
   * `vOld = m.putIfAbsent(k,v);`

##### Tipos Atômicos

```java
import java.util.concurrent.atomic.AtomicInteger;

class AtomicCounter {
    private AtomicInteger c = new AtomicInteger(0);

    public void increment() {
        c.incrementAndGet();
    }

    public void decrement() {
        c.decrementAndGet();
    }

    public int value() {
        return c.get();
    }
}
```


##### ThreadLocal
```java
private static ThreadLocal<Integer> myId = new ThreadLocal<Integer>() {
   public Integer initialValue() {
      return new Random().nexInt();
   } 
};

public static Integer getMyId() { 
    return myId.get();
}
```

##### Leia mais

Para aprender mais, muito mais sobre concorrência em Java, ótimas referências são:

* [Java Concurrency in Practice](http://jcip.net/)
* [The Well-Grounded Java Developer](https://www.manning.com/books/the-well-grounded-java-developer)
* [Concorrência em Java](http://docs.oracle.com/javase/tutorial/essential/concurrency/simple.html)
* [Futures e Promises](http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/)
* [Locks](http://winterbe.com/posts/2015/04/30/java8-concurrency-tutorial-synchronized-locks-examples/)
* [Tipos Atômicos](http://winterbe.com/posts/2015/05/22/java8-concurrency-tutorial-atomic-concurrent-map-examples/)


#### Threads em Python

Em Python, como seria de se esperar, há várias formas de se trabalhar com *threads*.
A seguir são apresentados dois exemplos, usando o pacote `thread` ou `threading`.

```python
#!/usr/bin/python
import thread
import time

# Define a function for the thread
def print_time( threadName, delay):
   count = 0
   while count < 5:
      time.sleep(delay)
      count += 1
      print "%s: %s" % ( threadName, time.ctime(time.time()) )

# Create two threads as follows
try:
   thread.start_new_thread( print_time, ("Thread-1", 2, ) )
   thread.start_new_thread( print_time, ("Thread-2", 4, ) )
except:
   print "Error: unable to start thread"

while True:
   pass
```

Ou

```python
#!/usr/bin/python

import threading
import time

exitFlag = 0

class myThread (threading.Thread):
   def __init__(self, threadID, name, counter):
      threading.Thread.__init__(self)
      self.threadID = threadID
      self.name = name
      self.counter = counter
   def run(self):
      print "Starting " + self.name
      print_time(self.name, self.counter, 5)
      print "Exiting " + self.name

   def print_time(threadName, counter, delay):
      while counter:
         if exitFlag:
            threadName.exit()
         time.sleep(delay)
         print "%s: %s" % (threadName, time.ctime(time.time()))
         counter -= 1

# Create new threads
thread1 = myThread(1, "Thread-1", 1)
thread2 = myThread(2, "Thread-2", 2)

# Start new Threads
thread1.start()
thread2.start()

print "Exiting Main Thread"
```

##### Leia mais

[Threads em Python](https://www.tutorialspoint.com/python/python_multithreading.htm)


### Exercício - Anel Multithread

* Usando uma linguagem de alto-nível como C/C++/Java, escrever um programa que crie 30 threads e faça com que uma mensagem circule entre os mesmos. 
* A mensagem é uma string aleatória de pelo menos 80 caracteres. 
* A cada vez que um thread recebe a mensagem ele a imprime, modifica o primeiro caractere minúsculo para maiúsculo, caso exista, dorme por 1 segundo, e repassa a mensagem. 
* Quando todos os caracteres forem maiúsculos, o processo repassa a mensagem e então termina. 
* Antes de terminar, o processo deve imprimir a mensagem resultante.


