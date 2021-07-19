### Pub/Sub: MosQuiTTo

>[Eclipse Mosquitto](https://mosquitto.org) is an open source (EPL/EDL licensed) message broker that implements the MQTT protocol versions 5.0, 3.1.1 and 3.1. Mosquitto is lightweight and is suitable for use on all devices from low power single board computers to full servers.

MQTT é um protocolo de transporte para publish/subscribe do tipo cliente-servidor.
É leve, aberto e fácil de implementar, ideal para comunicação *Machine to Machine* (M2M) e uso no contexto de Internet das Coisas (*Internet of Things - I0T*).

>MQTT is a very light weight and binary protocol, and due to its minimal packet overhead, MQTT excels when transferring data over the wire in comparison to protocols like HTTP. Another important aspect of the protocol is that MQTT is extremely easy to implement on the client side. Ease of use was a key concern in the development of MQTT and makes it a perfect fit for constrained devices with limited resources today.

O padrão é definido pela OASIS, uma organização aberta responsável por padrões como SAML e DocBook. A especificação atual é a de número 5, lançada em março de 2019.



###### Instalação

* Ubuntu: `apt-get install mosquitto`
* MacOS: brew install mosquitto
* Windows: baixe [mosquitto-2.0.9a-install-windows-x64.exe](http://mosquitto.org/download)

###### Inicializando o serviço

O arquivo `mosquito.conf` contém as configurações para o *broker*. 
As configurações funcionam bem para o nosso caso. O *broker* aceita requisições na porta 1883 e *publishers* e *subscribers* também utilizam essa porta por padrão.
Basta iniciar o *broker* com a opção `-v` para ter mais detalhes sobre o que ocorre internamente.

* Ubuntu: `mosquitto -v`
* MacOS: `/usr/local/sbin/mosquitto -c /usr/local/etc/mosquitto/mosquitto.conf`


###### Publicando

Para publicar uma mensagem, o *publisher* deve indicar um host, porta, tópico e mensagem. Caso o host e porta sejam omitidos, assume-se `localhost:1883`.
No MacOS, adicione `/usr/local/opt/mosquitto/bin/mosquitto_sub` ao *path*.

```bash
# publicando valor de 40 para tópicos 'sensor/temperature/1' e 'sensor/temperature/2'
mosquitto_pub -t sensor/temperature/1 -m 40
mosquitto_pub -t sensor/temperature/2 -m 32
```

Caso o *subscriber* não esteja em execução, adicione a opção `-r` para que o broker retenha a mensagem.

###### Consumindo

O consumidor funciona de maneira semelhante, informando o tópico de interesse:

```bash
# consumindo mensagens de tópico /sensor/temperature/*
mosquito_pub -t sensor/temperature/+
```

###### Programando

Existem também APIs em diversas linguagem para desenvolvimento de aplicações que utilizem o Mosquitto.
A biblioteca pode ser baixada [aqui](https://repo.eclipse.org/content/repositories/paho-snapshots/org/eclipse/paho/org.eclipse.paho.client.mqttv3/1.2.6-SNAPSHOT/org.eclipse.paho.client.mqttv3-1.2.6-20200715.040602-1.jar).


```java
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttPublishSample {

  public static void main(String[] args) {
    String topic        = "MQTT Examples";
    String content      = "Message from MqttPublishSample";
    int qos             = 2;
    String broker       = "tcp://mqtt.eclipse.org:1883";
    String clientId     = "JavaSample";
    MemoryPersistence persistence = new MemoryPersistence();

    try {
      MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
      MqttConnectOptions connOpts = new MqttConnectOptions();
      connOpts.setCleanSession(true);
      System.out.println("Connecting to broker: "+broker);
      sampleClient.connect(connOpts);
      System.out.println("Connected");
      System.out.println("Publishing message: "+content);
      MqttMessage message = new MqttMessage(content.getBytes());
      message.setQos(qos);
      sampleClient.publish(topic, message);
      System.out.println("Message published");
      sampleClient.disconnect();
      System.out.println("Disconnected");
      System.exit(0);
    } catch(MqttException me) {
      System.out.println("reason "+me.getReasonCode());
      System.out.println("msg "+me.getMessage());
      System.out.println("loc "+me.getLocalizedMessage());
      System.out.println("cause "+me.getCause());
      System.out.println("excep "+me);
      me.printStackTrace();
    }
  }
}
```

???todo "Hive"
     * `/usr/local/opt/mosquitto/bin/mosquitto_sub -h broker.hivemq.com  -p 1883 -t esportes/+/flamengo`
     * `/usr/local/opt/mosquitto/bin/mosquitto_pub -t esportes/nadacao/flamengo -m "perdeu mais uma vez" -r -h broker.hivemq.com`


<!-- Distinction from message queues
There is a lot of confusion about the name MQTT and whether the protocol is implemented as a message queue or not. We will try to shed some light on the topic and explain the differences. In our last post, we mentioned that MQTT refers to the MQseries product from IBM and has nothing to do with “message queue“. Regardless of where the name comes from, it’s useful to understand the differences between MQTT and a traditional message queue:

A message queue stores message until they are consumed When you use a message queue, each incoming message is stored in the queue until it is picked up by a client (often called a consumer). If no client picks up the message, the message remains stuck in the queue and waits to be consumed. In a message queue, it is not possible for a message not to be processed by any client, as it is in MQTT if nobody subscribes to a topic.

A message is only consumed by one client 
Another big difference is that in a traditional message queue a message can be processed by one consumer only. The load is distributed between all consumers for a queue. In MQTT the behavior is quite the opposite: every subscriber that subscribes to the topic gets the message.

Queues are named and must be created explicitly 
A queue is far more rigid than a topic. Before a queue can be used, the queue must be created explicitly with a separate command. Only after the queue is named and created is it possible to publish or consume messages. In contrast, MQTT topics are extremely flexible and can be created on the fly.

If you can think of any other differences that we overlooked, we would love to hear from you in the comments. -->






!!! question "Exercícios - RPC e Publish/Subscribe"
    * Usando *thrift* e a linguagem Java, estenda o serviço ChaveValor para retornar o valor antigo de uma determinada chave na operação `setKV()`  caso a chave já exista.
    * Usando o *broker* mosquitto instalado localmente, faça em Java um *publisher* que simula um sensor de temperatura e publica valores aleatórios entre 15 e 45 a cada segundo.
    * Faça o *subscriber* que irá consumir esses dados de temperatura.





## Referências
* [What is gRPC: introduction](https://grpc.io/docs/what-is-grpc/introduction/)
* [Thrift: the missing guide](https://diwakergupta.github.io/thrift-missing-guide/)
* [MQTT Essentials](https://www.hivemq.com/mqtt-essentials/)
* [MQTT Client in Java](https://www.baeldung.com/java-mqtt-client)
* [Thrift Tutorial](http://thrift-tutorial.readthedocs.org/en/latest/index.html)
