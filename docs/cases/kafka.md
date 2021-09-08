
https://kafka.apache.org/

???todo "TODO"



<!--
O quê?
* Manter dados/serviços disponíveis a despeito de falhas.

Replicação
* No Kafka, o \alert{Replication Factor} determina quantas cópias de cada tópico (todas as partições no tópico).

Líder e Seguidor
*  Produtor conversa com líder. Líder grava localmente e envia ack ao produtor.
*  Consumidor conversa com líder. Líder envia dados ao consumidor.
*  Líder replica dados para seguidores.

Replicar
* Passo 6  ensina a criar um sistema com múltiplos brokers.

*  Identificador
*  Porta (mesmo servidor)
*  \alert{Log directory}

Replicar
*  Crie um novo tópico, com RF = 3 e duas partições
*  \lstinline|bin/kafka-topics.sh --list --zookeeper localhost:2181 --describe --topic <topico>|
*  Lista de réplicas
*  Lista de réplicas sincronizadas: \emph{list of \alert{i}n \alert{s}ync \alert{r}eplicas}


Zookeeper
*  Permite que nós do cluster se descubram
*  Elege líder

Armazenamento
*  Dado deve ser removido depois de um tempo de ``retenção''
*  Pode definir retenção por tamanho (por partição, não tópico)


Produtor

*  Produtor envia mensagens para os brokers
*  Producer API
*  [Learning Journal](https://github.com/LearningJournal/ApacheKafkaTutorials)

SimpleProducer.java

```java
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;

public class SimpleProducer {
 public static void main(String[] args) {
  String topicName = "SimpleProducerTopic";
  String key = "Chave";
  String value = "Valor";
  Properties props = new Properties();
  props.put("bootstrap.servers", "localhost:9092, localhost:9093");
  props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
  props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

  Producer<String, String> producer = new KafkaProducer<String, String>(props);

  ProducerRecord<String, String> record = new ProducerRecord<String, String>(topicName, key, value);

  producer.send(record);
  producer.close();

  System.out.println("SimpleProducer Completed.");
 }
}
```

Workflow
![]()images/kafka6.png)

*  Particionador default
	*  Partition
	*  Hash da ``chave''
	*  Round robin
*  Retry automático


Fire and Forget
* Envia a mensagem e não se importa com o resultado.

Synchronous Call
* Envia a mensagem e espera para saber se foi entregue ou não.

```java
try{
 RecordMetadata metadata = producer.send(record).get();
 System.out.println("Message is sent to Partition no " + metadata.partition() + " and offset " + metadata.offset());
 System.out.println("SynchronousProducer Completed with success.");
}catch (Exception e) {
 e.printStackTrace();
 System.out.println("SynchronousProducer failed with an exception");
}finally{
 producer.close();
}
```

*  Future

Callback
Envia a mensagem e é invocado depois de receber um ACK

```
producer.send(record, new MyProducerCallback());

...

class MyProducerCallback implements Callback{
 @Override
 public  void onCompletion(RecordMetadata recordMetadata, Exception e) {
  if (e != null)
   System.out.println("AsynchronousProducer failed with an exception");
  else
   System.out.println("AsynchronousProducer call Success:");
 }
}
```

*  max.in.flight.requests.per.connection


Default Partitioner
![](../images/kafka6.png)

*  Partition
*  Hash da ``chave'' \% \#partition
*  Round robin

\href{https://github.com/LearningJournal/ApacheKafkaTutorials/blob/master/ProducerExamples/SensorPartitioner.java}{Exemplo de Custom Partitioner}
\end{frame}

\subsection{Consumidor}

\begin{frame}{Consumer Groups}
\begin{itemize}
*  Múltiplos consumidores processam dados em paralelo
*  Grupo de consumidores de tópicos
*  Grupo pertence à mesma aplicação
	\includegraphics[width=.6\textwidth]{images/kafka7}
*  Duplicate reads? Consumidores não compartilham partições
*  Group coordinator (broker eleito): lista de consumidores
*  Group líder: rebalanceamento
\end{itemize}
\end{frame}

\begin{frame}[fragile, allowframebreaks]{Consumer}
\begin{lstlisting}[language=Java]
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

public class SimpleConsumer {
 public static void main(String[] args) throws IOException {
  String topicName = "SimpleProducerTopic";
  String groupName = "SupplierTopicGroup";

  Properties props = new Properties();
  props.put("bootstrap.servers", "localhost:9092,localhost:9093");
  props.put("group.id", groupName);
  props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
  props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

  KafkaConsumer<String, String> consumer = null;

  try {
   consumer = new KafkaConsumer<String, String>(props);
   consumer.subscribe(Arrays.asList(topicName));

   while (true) {
    ConsumerRecords<String,String> records = consumer.poll(100);

    for (ConsumerRecord<String, String> record: records)
     System.out.println("Key = " + record.key() + " Value = " + record.value());
   }
  } catch (Exception ex) {
   ex.printStackTrace();
  } finally {
   consumer.close();
  }
 }
}
\end{lstlisting}

\begin{itemize}
*  Se não definir grupo, será novo grupo, e lerá todas as mensagens disponíveis
\end{itemize}
\end{frame}


\begin{frame}{Poll}
\begin{itemize}
*  poll também envia hearbeat
*  executar a cada 3s, no mínimo	
*  Current offset: a cada poll, broker incrementa current offset
*  Commited offset: o consumidor informa quais índices foram processados
	\begin{itemize}
	*  Auto Commit
		\begin{itemize}
		*  enable.auto.commit
		*  auto.commit.interval.ms
		*  Pode causar reprocessamento de mensagens
		\end{itemize}
	*  Manual Commit
		\begin{itemize}
		*  CommitSync
		*  CommitAsync
		\end{itemize}
	\end{itemize}
\end{itemize}
\end{frame}


\subsection{Arquitetura}
%\begin{frame}{Líder}

%\end{frame}

%mensagens são ack depois de copiadas para todas as réplicas
%replicas lentas são removidas se lentas ou falhas
%at least once, at most once, exactly one (nao suportado)
%rolling upgrade
%tls security
%rest
%CRUD




-->

