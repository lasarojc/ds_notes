\section{Blockchain}

\subsection{Introdução}
\begin{frame}{Mercado}
	\begin{itemize}
		\item Clientes, consumidores, fornecedores, vendedores
		\item Bens, serviços, contratos
		\item Mercado público (feira livre, supermercados) e Privado (supply chain)
	\end{itemize}
\end{frame}

\begin{frame}{Asset -- Bem}
	Tudo que tem valor e pode pertencer a alguém.
	\begin{itemize}
		\item Casa, empréstimo (tangível e intangível)
		\item Patente
		\item Ação
		\item Dinheiro -- é anônimo
	\end{itemize}
\end{frame}

\begin{frame}{Ledger}
	Livro registro
	
	\begin{itemize}
		\item 1000 reais pagos ao funcionário F
		\item Carro X vendido ao João por 50k
		\item Cada entidade mantém o seu, privadamente.
		\item Ineficiente -- retrabalho e lento
		\item Caro -- Retrabalho
		\item Vulnerável -- hack, erros, e modificações maliciosas
	\end{itemize}
\end{frame}

\begin{frame}{Blockchain}
Ledger distribuído e centralizado
	\begin{itemize}
		\item Replicado usando P2P
		\item Só envolvidos tem acesso ao registro
		\item Consenso -- acordo na transação
		\item Proveniência -- todo o histórico de um asset é mantido na blockchain.
		\item Imutabilidade -- entradas não podem ser alteradas
		\item Finalidade -- entradas não podem ser refutadas
	\end{itemize}
\end{frame}

\begin{frame}{Bitcoin}
Primeira aplicação da blockchain
\begin{itemize}
	\item Bitcoin
	\begin{itemize}
		\item Moeda é o asset
		\item Anonimidade
		\item Proof of work
	\end{itemize}
	\item Negócios	
	\begin{itemize}
		\item Qualquer coisa é asset
		\item Identificação das partes
		\item Selective endorsement
	\end{itemize}
\end{itemize}
\end{frame}

\begin{frame}{Smart Contracts}
	Os termos do negócio são mantidos na blockchain: "Se na data X a entidade E não tiver transferido D dinheiros para a entidade F, então transfira o asset A de E para F."
	
	Verificável, assinável e \emph{executável}.	
\end{frame}


\frame{}

\begin{frame}{Bitcoin}
\includegraphics[width=\textwidth]{images/bitcoin_jun_2018}
\end{frame}

\begin{frame}{Bitcoin}
\includegraphics[width=\textwidth]{images/bitcoin_dec_2018}
\end{frame}\section{A Small Piece of Big Data}

\subsection{Introdução}

\frame{
	\begin{block}{Big-Data}
``Big data is a term for data sets that are so large or complex that traditional data processing application software is inadequate to deal with them.''
	\end{block}

Ciclo convencional:
\begin{itemize}
	\item Coleta
	\item Armazenamento
	\item Análise
	\item Consulta
	\item Compartilhamento
	\item Visualização
	\item Atualização
	\item ...
\end{itemize}

\href{https://en.wikipedia.org/wiki/Big_data}{Fonte}
}

\begin{frame}{Áreas}
Grandes massas de dados:
\begin{itemize}
	\item Propaganda
	\item Astronomia
	\item Ciência
	\item e-governos
	\item meteorologia
	\item \emph{genomics}
	\item ...
\end{itemize}
\end{frame}

\begin{frame}{Dados}
\begin{itemize}
	\item Internet das coisas
	\item sensoriamento remoto
	\item suas fotos
	\item logs de software
	\item RFID
	\item redes de sensores
	\item ...
\end{itemize}
\end{frame}

\begin{frame}{O quê?}
	Quão grande é ``big'' o suficiente? \pause Depende dos dados, ferramentas, e capacidade de manipulá-los. \pause Uma vez dado um passo, o alvo passa a ser o próximo passo. \pause Isso quer dizer que vai de alguns TB até Petabytes, dependendo do problema.
\end{frame}


\begin{frame}{O quê?}
\begin{block}{Gartner, 2012}
Big data is high volume, high velocity, and/or high variety information assets that require new forms of processing to enable enhanced decision making, insight discovery and process optimization.
\end{block}

\begin{itemize}
	\item Volume: incapacidade de armazenar todos os dados; apenas observe e guarde conclusões
	\item Velocidade: dados passando em ``tempo real''
	\item Variedade: imagens, vídeos, áudio, temperatura,...
	\pause
	\item Machine learning para automação de extração de informação, por exemplo, detecção de padrões, sem se preocupar com o porquê dos mesmos.
\end{itemize}
\end{frame}



\begin{frame}{Como lidar?}
\begin{itemize}
	\item Bancos de dados colunares
	\item Stream DBs
	\item ...
	\item \alert{MapReduce}
\end{itemize}
\end{frame}

\subsection{Google FS}

\begin{frame}{Google FS}
\begin{itemize}
	\item Google, 2003
	\item File System
	\item Dados recuperados da Internet usados em consultas
	\item Milhões de arquivos de múltiplos GB
	\item Chunks de 64MB (``blocos do disco'')
	\item Operações comuns são appends ou reads
	\item Servidores/discos/memórias estão sempre falhando
	\item Centenas de clientes concorrentes no mesmo arquivo
\end{itemize}
\includegraphics[width=.6\textwidth]{images/gfs3}
\end{frame}

\begin{frame}{Google FS}
\includegraphics[width=.7\textwidth]{images/gfs2}

\begin{itemize}
\item Clusters de nós ``comuns''
\item Master node: metadata
\item Chunk servers: data
\item Permite usar um cluster como um único HD elástico na rede.
\end{itemize}

\href{https://www.cs.rutgers.edu/~pxk/417/lectures/l-dfs.html}{Fonte}
\end{frame}

\begin{frame}{Google FS}
\includegraphics[width=.7\textwidth]{images/gfs5}

\begin{itemize}
	\item Apps recebem \emph{leases} de acesso direto aos dados
	\item Atomic commitment garante consistência entre réplicas
\end{itemize}

\href{http://google-file-system.wikispaces.asu.edu/}{Fonte}
\end{frame}

\begin{frame}[fragile,allowframebreaks]{Google FS: Consistência }
\includegraphics[width=\textwidth]{images/gfs6}

\framebreak
\begin{enumerate}
\item Application sends the file name and data to the GFS client.
\item GFS Client send the file name and chunk index to master
\item Master sends the identity of the primary and other secondary replicas to the client.
\item Client caches this information. Client contacts master again only when primary is unreachable or it sends a reply saying it does not holds the lease anymore.
\item Considering the network topology the client sends the data to all the replicas.This improves performance. GFS separates data flow from the control flow. Replicas store the data in their LRU buffers till it is used.
\item After all replicas receiving of the data, client sends write request to the primary. Primary decides the mutation order. It applies this order to its local copy.
\item Primary sends the write request to all the secondary replicas. They perform write according to serial order decided by the primary.
\item After completing the operation all secondary acknowledge primary.
\item Primary replies the client about completion of the operation. In case of the errors that is when some of the secondary fail to write client request is supposed to be fail.This leaves modified chunk inconsistent. \item Client handles this by retrying the failed mutation. 
\end{enumerate}

\href{http://google-file-system.wikispaces.asu.edu/}{Fonte}
\end{frame}

\begin{frame}{Map Reduce}
\begin{itemize}
	\item Google, 2004
	\item Processamento distribuído
	\item Processa arquivos no Google FS
\end{itemize}
\includegraphics[width=.6\textwidth]{images/gfs4}
\end{frame}

\frame{\alert{leases?}}

\begin{frame}{Chubby}
	\begin{itemize}
		\item Google, 2006
	\end{itemize}
	\includegraphics[width=.6\textwidth]{images/chubby1}
\end{frame}





\begin{frame}{Hadoop}
\begin{itemize}
	\item HDFS: Hadoop Distributed File System
	\item Map Reduce
	\item Yahoo!
	\item Open source em 2011, 1.0.0
	\item 2012, 2.0.0,
	\item 2017, 3.0.0
	\item nov 2018, 2.9.2
\end{itemize}
\end{frame}


\begin{frame}{Hadoop Ecosystem}
\begin{itemize}
	\item Hive: data warehouse
	\item Spark: 
	\item Kafka
	\item Yarn
	\item Pig: linguagem para especificação de data flow.
	\item HBase: banco de dados estruturado
	\item Sqoop
	\item Flume
	\item Oozie
	\item Avro: serialização
	\item Mahout: machine learning
\end{itemize}
\end{frame}



\begin{frame}{HDFS}
\begin{itemize}
	\item Distribuído
	\item Escalável
	\item Cost effective
	\item Tolerante a falhas
	\item Alta vazão
\end{itemize}
\end{frame}


\begin{frame}{Arquitetura}
\begin{itemize}
	\item Rack e rack failure
	\item Top of rack switch
	\item Core switch
	\item Name Node: nomes das pastas e arquivos
	\item Data Node: conteúdo dos arquivos
	\item Cliente
\end{itemize}
\end{frame}

\begin{frame}{Arquitetura}
\begin{itemize}
	\item Crie arquivo: cliente -> name node
	\item Escreva um block (e.g., 128MB): cliente
	\item Aloque block: cliente -> name node
	\item Salve os dados: cliente -> data node
	\item Heartbeat block report: data node -> name node
	\item Dados são replicados (RF configurado por arquivo): Data node -> data node
\end{itemize}
\end{frame}



\begin{frame}{Name node}
Dados em memory e edit log.

\begin{itemize}
	\item Name node é um SPOF? 
	\item Quorum Journal Manager replica edit log.
	\item Standby Name Node
	\item Zookeeper usado para decidir quem é o líder
	\item Secondary Name Node replica checkpoint da imagem em memória.
\end{itemize}
\end{frame}

\subsection{MapReduce}
\begin{frame}{MapReduce}
	\begin{itemize}
		\item Programação funcional
		\item Map: (map length (() (a) (a b c)) = (0 1 3))
		\item Fold/Reduce: (reduce + (1 2 3)) = 6
	\end{itemize}
\end{frame}


\begin{frame}{MapReduce}
	\begin{itemize}
		\item Não há dependência entre os dados
		\item Dados divididos em \emph{shards}
		\item Execução paralela e distribuída
		\item Trabalhador recebe um shard
		\item Mestre agrega valores
		\item Milhares de processos
		\item Petabytes de dados
	\end{itemize}
\end{frame}

\begin{frame}{MapReduce}
	\begin{itemize}
		\item Shards são arquivos do GFS/HDFS/EC2
		\item Função mapeada a cada shard
		\item Resultado é lista de chaves e valores
		\item Agregação acontece por chaves
		\item Resultado são arquivos no GFS/HDFS/EC2
	\end{itemize}
\end{frame}

\begin{frame}{MapReduce}
	\includegraphics[width=.8\textwidth]{images/mapreduce1}
\end{frame}

\begin{frame}{MapReduce}
	\includegraphics[width=.8\textwidth]{images/mapreduce2}
\end{frame}


\subsection{Laboratório}

\begin{frame}[fragile]{Exemplo}
\begin{lstlisting}[language=java]
import ...

public class WordCount 
{
 public static class TokenizerMapper 
 extends Mapper<Object, Text, Text, IntWritable>
 {
  private final static IntWritable one = new IntWritable(1);
  private Text word = new Text();

  public void map(Object key, Text value, Context context) 
   throws IOException, InterruptedException 
  {
   StringTokenizer itr = new StringTokenizer(value.toString());
   while (itr.hasMoreTokens()) 
   {
    word.set(itr.nextToken());
    context.write(word, one);
   }
  } 
 }
...
\end{lstlisting}
\end{frame}

\begin{frame}[fragile]{Exemplo}
\begin{lstlisting}[language=java]
...
 public static class IntSumReducer 
  extends Reducer<Text,IntWritable,Text,IntWritable> 
 { 
  private IntWritable result = new IntWritable();
  public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException 
  {
   int sum = 0;
   for (IntWritable val : values) 
    sum += val.get();
   result.set(sum);
   context.write(key, result);
  }
 }
	
 public static void main(String[] args) throws Exception 
 {
  ...
 }
}
	
\end{lstlisting}
\href{https://hadoop.apache.org/docs/current/hadoop-mapreduce-client/hadoop-mapreduce-client-core/MapReduceTutorial.html#Example:_WordCount_v1.0}{Fonte}
\end{frame}

\frame{\url{https://youtu.be/DJPwV2ge9m0?list=PLkz1SCf5iB4dw3jbRo0SYCk2urRESUA3v}}




\section{Estudo de caso: Kafka}

\subsection{Introdução}



\begin{frame}
\includegraphics[width=\textwidth]{images/kafka0}
\end{frame}

\begin{frame}{Apache Kafka}

``Kafka is a distributed streaming platform.''

\begin{itemize}
	\item LinkedIn
	\item OpenSource em 2011
	\item Projeto Apache em ????
\end{itemize}
\end{frame}


\begin{frame}{O quê?}
\includegraphics[width=.6\textwidth]{images/kafka1}
\end{frame}

\begin{frame}{Usos}
\includegraphics[width=\textwidth]{images/kafka2}

\pause record ~= message

\pause
\begin{block}{Enterprise Messaging System}
	Producers x Message Broker x Consumers
\end{block}
\end{frame}

\begin{frame}{Componentes}
\includegraphics[width=\textwidth]{images/kafka3}

\begin{itemize}
	\item Produtores: enviam dados/mensagens/records (array de bytes)
	\item Consumidores: recebem dados
	\item Cluster/Broker: distribuído e tolerantes a falhas.
	\item Conectores: integração simplificada com outras aplicações 
	\item Stream processors: spark ou outros frameworks; transformam dados
\end{itemize}
\end{frame}


\begin{frame}{Apache Kafka}
\begin{itemize}
	\item Brokers
	\item Cluster de brokers
	\item Distribuído
	\item Tolerante a falhas
	\item Desacoplamento espacial
	\item Desacoplamento temporal
	\item Tópicos, não endereços
\end{itemize}
\end{frame}

\begin{frame}{Tópicos}
\begin{itemize}
	\item Nome de uma stream de dados: ordem de serviço, exame de sangue, MSFT
	\item Quantidade pode ser imensa.
\end{itemize}
\end{frame}

\begin{frame}{Partição}
\begin{itemize}
	\item Subdivisões de tópicos
	\item Número de partições é definido por usuário
	\item Cada partição está associada a um único servidor
\end{itemize}
\end{frame}

\begin{frame}{Offset}
\begin{itemize}
	\item Índice de uma mensagem em uma partição
	\item Índices atribuídos na ordem de chegada
	\item Offsets são locais às partições
	\item Mensagens são unicamente identificadas por (tópico, partição, índice)
\end{itemize}

\includegraphics[width=.6\textwidth]{images/kafka4}

\end{frame}


\begin{frame}{Consumer group}
\begin{itemize}
	\item Carga pode ser muito grande para um consumidor
	\item Compartilham o processamento de um tópico
	\item Cada mensagem é processada por um membro do grupo
	\item A mesma mensagem pode ser processada por múltiplos grupos
	\item Número de consumidores $\leq$ partições no tópico
	\item Máximo de dois consumidores por partição (mantem pos. de cada um)
\end{itemize}

\includegraphics[width=.6\textwidth]{images/kafka5}
\end{frame}

\subsection{Quickstart}

\begin{frame}{Baixar e Executar}
Siga o tutorial em \url{http://kafka.apache.org/quickstart}, até o passo 5.

\begin{itemize}
	\item Baixe e descompacte
	\item Rode o zookeeper (Terminal 1)
	\item Rode o Kafka (Terminal 2)
	\item Crie um tópico (Terminal 3)\\
		Mais de uma partição em um servidor
	\item \alert{Conecte-se ao Zookeeper e dê uma olhada. O que está vendo?}
	\item Liste os tópicos criados
	\item Envie algumas mensagens
	\item Inicie um consumidor (Terminal 4)
\end{itemize}
\end{frame}



\subsection{Tolerância a Falhas}
\begin{frame}{O quê?}
Manter dados/serviços disponíveis a despeito de falhas.
\end{frame}

\begin{frame}{Replicação}
No Kafka, o \alert{Replication Factor} determina quantas cópias de cada tópico (todas as partições no tópico).
\end{frame}

\begin{frame}{Líder e Seguidor}
\begin{itemize}
	\item Produtor conversa com líder. Líder grava localmente e envia ack ao produtor.
	\item Consumidor conversa com líder. Líder envia dados ao consumidor.
	\item Líder replica dados para seguidores.
\end{itemize}
\end{frame}

\begin{frame}{Replicar}
Passo 6  ensina a criar um sistema com múltiplos brokers.

\begin{itemize}
	\item Identificador
	\item Porta (mesmo servidor)
	\item \alert{Log directory}
\end{itemize}
\end{frame}

\begin{frame}{Replicar}
\begin{itemize}
	\item Crie um novo tópico, com RF = 3 e duas partições
	\item \lstinline|bin/kafka-topics.sh --list --zookeeper localhost:2181 --describe --topic <topico>|
	\item Lista de réplicas
	\item Lista de réplicas sincronizadas: \emph{list of \alert{i}n \alert{s}ync \alert{r}eplicas}
\end{itemize}
\end{frame}


\begin{frame}{Zookeeper}
\begin{itemize}
	\item Permite que nós do cluster se descubram
	\item Elege líder
\end{itemize}
\end{frame}

\begin{frame}{Armazenamento}
\begin{itemize}
	\item Dado deve ser removido depois de um tempo de ``retenção''
	\item Pode definir retenção por tamanho (por partição, não tópico)
\end{itemize}
\end{frame}


\subsection{Produtor}

\begin{frame}{Produtor}
\begin{itemize}
	\item Produtor envia mensagens para os brokers
	\item Producer API
	\item \href{https://github.com/LearningJournal/ApacheKafkaTutorials}{Learning Journal}
\end{itemize}
\end{frame}

\begin{frame}[fragile]{SimpleProducer.java}
\begin{lstlisting}[language=Java]
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

\end{lstlisting}
\end{frame}

\begin{frame}{Workflow}
\includegraphics[width=.8\textwidth]{images/kafka6}

\begin{itemize}
	\item Particionador default
	\begin{itemize}
		\item Partition
		\item Hash da ``chave''
		\item Round robin
	\end{itemize}
	\item Retry automático
\end{itemize}
\end{frame}


\begin{frame}{Fire and Forget}
Envia a mensagem e não se importa com o resultado.
\end{frame}

\begin{frame}[fragile]{Synchronous Call}
Envia a mensagem e espera para saber se foi entregue ou não.

\begin{lstlisting}[language=Java]
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
\end{lstlisting}
\begin{itemize}
	\item Future
\end{itemize}
\end{frame}

\begin{frame}[fragile]{Callback}
Envia a mensagem e é invocado depois de receber um ACK

\begin{lstlisting}[language=Java]
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
\end{lstlisting}
\begin{itemize}
	\item max.in.flight.requests.per.connection
\end{itemize}
\end{frame}


\begin{frame}{Default Partitioner}
\includegraphics[width=.8\textwidth]{images/kafka6}

\begin{itemize}
	\item Partition
	\item Hash da ``chave'' \% \#partition
	\item Round robin
\end{itemize}

\href{https://github.com/LearningJournal/ApacheKafkaTutorials/blob/master/ProducerExamples/SensorPartitioner.java}{Exemplo de Custom Partitioner}
\end{frame}

\subsection{Consumidor}

\begin{frame}{Consumer Groups}
\begin{itemize}
	\item Múltiplos consumidores processam dados em paralelo
	\item Grupo de consumidores de tópicos
	\item Grupo pertence à mesma aplicação
	\includegraphics[width=.6\textwidth]{images/kafka7}
	\item Duplicate reads? Consumidores não compartilham partições
	\item Group coordinator (broker eleito): lista de consumidores
	\item Group líder: rebalanceamento
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
	\item Se não definir grupo, será novo grupo, e lerá todas as mensagens disponíveis
\end{itemize}
\end{frame}


\begin{frame}{Poll}
\begin{itemize}
	\item poll também envia hearbeat
	\item executar a cada 3s, no mínimo	
	\item Current offset: a cada poll, broker incrementa current offset
	\item Commited offset: o consumidor informa quais índices foram processados
	\begin{itemize}
		\item Auto Commit
		\begin{itemize}
			\item enable.auto.commit
			\item auto.commit.interval.ms
			\item Pode causar reprocessamento de mensagens
		\end{itemize}
		\item Manual Commit
		\begin{itemize}
			\item CommitSync
			\item CommitAsync
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


\subsection{Data Streams}
\subsection{Connectors}

%\begin{frame}{Data streams}
%\begin{itemize}
%	\item 
%\end{itemize}
%\end{frame}

