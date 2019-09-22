
# Multiprogramação e *Multithreading* em Sistemas Distribuídos

É impossível pensar em sistemas distribuídos sem pensar em concorrência na forma de múltiplos processos executando, normalmente, em hosts distintos.
De fato, os exemplos que apresentamos até agora consistem todos em um processo cliente requisitando ações de algum processo servidor.
Apesar disso, a interação entre tais processos aconteceu sempre de forma sincronizada, *lock-step*, em que o cliente requisitava o serviço e ficava bloqueado esperando a resposta do servidor, para então prosseguir em seu processamento. O servidor, de sua parte, fica bloqueado esperando requisições, que atende e então volta a dormir.
Assim, apesar do uso de processadores distintos e da concorrência na execução dos processos, temos um baixo grau de efetivo paralelismo.

![Request/Response](./images/02-03.png)

Para usarmos melhor os recursos disponíveis, uma das razões de ser da computação distribuída, temos então que pensar em termos eventos sendo disparados entre os componentes, que devem ser tratados assim que recebidos ou tão logo quanto haja recursos para fazê-lo. 
Estes eventos correspondem tanto a requisições quanto a respostas (efetivamente tornando difícil a distinção).
Além disso, sempre que possível, um componente não deve ficar exclusivamente esperando por eventos, aproveitando a chance executar outras tarefas até que eventos sejam recebidos.
Dada que processos interagem com a rede usando sockets, cuja operação de leitura é bloqueante, para aumentar a concorrência em um processo, precisamos falar de multi-threading.

Há duas razões claras para estudarmos multi-threading. A primeira, é a discutida acima: permitir o desenvolvimento de componentes que utilizem "melhormente" os recursos em um host.
A segunda é o fato que muitos dos problemas que aparecem em programação multi-thread, aparecem em programação multi-processo (como nos sistemas distribuídos), apenas em um grau de complexidade maior.

## Threads x Processos

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


## Threads em SD

### Cliente multithreaded

Vantagens similares a usar em sistemas centralizado.

* Lida com várias tarefas concorrentemente.
* Esconde latência.
* Separa código em blocos/módulos.


---

### Servidor multithreaded

Há diversas possibilidades de uso de threads em servidores. A mais simples é usar apenas um, com temos feito até agora.

\includegraphics[width=.5\textwidth]{images/singlethreadedserver}

---

Outra opção é criar um novo tthread para cada nova requisição e atende múltiplas requisições concorrentemente.

\includegraphics[width=.5\textwidth]{images/multithreadedserver}


* Permite tratar requisições enquanto faz IO.
* Número de threads é limitado. %Pelo framework em uso.
* Número de threads deve ser limitado. %Para não saturar o servidor.
* Criação e destruição de threads é cara.

Assim, temos uma outra oção que também usa múltiplos threads.

\includegraphics[width=.6\textwidth]{images/poolthreadedserver}

\href{https://www3.nd.edu/~dthain/courses/cse30341/spring2009/project4/project4.html}{Fonte}
\end{frame}


\includegraphics[width=.98\textwidth]{images/seda1}

%\href{http://images.cnitblog.com/blog/13665/201306/15180500-a54c8eb3d73246469f1b74ee74f2119b.png}{Fonte}

E se quebrarmos o processamento em vários pools?

Esta abordagem é conhecida como **Staged Event-Driven Architecture**

\includegraphics[width=.6\textwidth]{images/seda2}

Que lembra arquitetura de [micro-serviços](http://muratbuffalo.blogspot.com.br/2011/02/seda-architecture-for-well-conditioned.html)

Se você quiser ler mais sobre SEDA, vá [aqui](http://courses.cs.vt.edu/cs5204/fall05-gback/presentations/SEDA_Presentation_Final.pdf).



\begin{frame}
\includegraphics[width=\textwidth]{images/multithreaded}
\end{frame}


\begin{frame}
\includegraphics[width=\textwidth]{images/multithread2}
\end{frame}


\begin{frame}{Problemas}

\includegraphics[width=\textwidth]{images/multithread3}
\href{https://www.youtube.com/watch?v=JRaDkV0itbM}{Fonte}


\begin{itemize}
	\item Inconsistências
	\item  Deadlock
	\item Inanição
\end{itemize}
\end{frame}


\subsection{Estado em Servidores}

\begin{frame}{Stateless Servers} 
Não mantém informação após terminar de tratar requisições.
\begin{itemize}
	\item Fecha todos os arquivos abertos
	\item Não faz promessas de atualização ao cliente
	\item Clientes e servidores são independentes
	
	\item Pouca ou nenhuma inconsistência causada por falhas
	\item Perda de desempenho (e.g., abertura do mesmo arquivo a cada requisição.)
\end{itemize}
\end{frame}

\begin{frame}{Stateful Servers} 
Mantém informação dos clientes entre requisições.
\begin{itemize}
	\item Mantem arquivos abertos
	\item Sabe quais dados o cliente tem em cache
	
	\item Possíveis inconsistência causada por falhas (cliente se conecta a servidor diferente)
	\item Melhor desempenho
	
	\item Maior consumo de recursos
\end{itemize}
\end{frame}

\begin{frame}{Impacto na Concorrência}
\begin{small}
	\begin{multicols}{2}
		\begin{block}{Stateless}
		\begin{itemize}
		\item Resultado depende da entrada
		\item Qualquer servidor pode atender
		\end{itemize}
		\end{block}
		\begin{block}{Stateful}
			\begin{itemize}
				\item Depende do histórico de entradas		
				\item Mesmo servidor deve atender
				\end{itemize}
				\end{block}
	\end{multicols}
\end{small}
\end{frame}





## Multithread na prática

### PThreads

#### Função de entrada

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

#### Criação

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

#### Destruição

```c
	for (thread = 0; thread < thread_count; thread++)
		pthread_join(thread_handles[thread], NULL);
	
	free(thread_handles);
```

#### Execução

Compile com
```bash
gcc -pthread teste.c -o teste
````

e execute com 
```bash
./teste 5
```

Observe que a saída das threads é *ordenada*. Agora experimente
```bash
./teste 200
```

Isto acontece porquê a execução das threads independe da ordem de criação. De fato, usando PThreads, temos pouco controle sobre os threads que criamos. Mas isto não quer dizer que estamos "órfãos" de API.

* `pthread_create` - cria novo thread
   * passagem de parâmetros
   * opções
* `pthread_join` - espera thread terminar
   * recebe resultado da thread
* `pthread_tryjoin` - espera thread terminar
* `pthread_exit` - termina a thread e retorna resultado 
   > An implicit call to \lstinline|pthread_exit()| is made when a thread other than the thread in which \lstinline|main()| was first invoked returns from the start routine that was used to create it. The function's return value serves as the thread's exit status. (manual do \lstinline|pthread_exit|)}
	
* pthread_attr_setaffinity_np* - ajusta afinidade dos threads.

#### Threads Java

##### Estender Thread
```java
public class HelloThread extends Thread {
   public void run() {
      System.out.println("Hello from a thread!");
   }

   public static void main(String args[]) {
      Thread t = new HelloThread();
      t.start()
      t.join()
   }
}
```

##### Implementar Runnable

```java
public class HelloRunnable implements Runnable {
   public void run() {
      System.out.println("Hello from a thread!");
   }

   public static void main(String args[]) {
      Thread t = new Thread(new HelloRunnable());
      t.start();
      t.join();
   }
}	
```

##### Executors

```java
ExecutorService executor = Executors.newSingleThreadExecutor();
executor.submit(() -> {
   String threadName = Thread.currentThread().getName();
   System.out.println("Hello " + threadName);
});
```

Você pode aprender mais sobre executores [aqui](http://docs.oracle.com/javase/tutorial/essential/concurrency/executors.html).

Algumas funções não são óbvias. É preciso estudar suas especificações. Veja o seguinte exemplo, onde o `sleep` não é garantido dormir pelo tempo especificado, 4000ms.

```java
try {
   Thread.sleep(4000);
} catch (InterruptedException e) {
   return;
}
```

Isso ocorre porquê seria complicado implementar um timer perfeito. Algo semelhante acontece quando se faz um `lock.await()`


#### Threads em Python

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

Um tipo interessante em linguagens como Java e C++ é o ThreadLocal. Veja um exemplo em Java.

```java
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadId {
   // Atomic integer containing the next thread ID to be assigned
   private static final AtomicInteger nextId = new AtomicInteger(0);

   // Thread local variable containing each thread's ID
   private static final ThreadLocal<Integer> threadId = new ThreadLocal<Integer>() {
      @Override 
	  protected Integer initialValue() {
         return nextId.getAndIncrement();
      }
   };

   // Returns the current thread's unique ID, assigning it if necessary
   public static int get() {
      return threadId.get();
   }
}
```

Uma boa explicação se encontra [aqui](
https://docs.oracle.com/javase/7/docs/api/java/lang/ThreadLocal.html)

### Exercício - Anel Multithread

* Usando uma linguagem de alto-nível como C/C++/Java, escrever um programa que crie 30 threads e faça com que uma mensagem circule entre os mesmos. 
* A mensagem é uma string aleatória de pelo menos 80 caracteres. 
* A cada vez que um thread recebe a mensagem ele a imprime, modifica o primeiro caractere minúsculo para maiúsculo, caso exista, dorme por 1 segundo, e repassa a mensagem. 
* Quando todos os caracteres forem maiúsculos, o processo repassa a mensagem e então termina. 
* Antes de terminar, o processo deve imprimir a mensagem resultante.


Aqui vão alguns ponteiros para ler mais

[Concorrência em Java](http://docs.oracle.com/javase/tutorial/essential/concurrency/simple.html)

[Futures e Promises](http://winterbe.com/posts/2015/04/07/java8-concurrency-tutorial-thread-executor-examples/)

[Locks](http://winterbe.com/posts/2015/04/30/java8-concurrency-tutorial-synchronized-locks-examples/)

[Tipos Atômicos](http://winterbe.com/posts/2015/05/22/java8-concurrency-tutorial-atomic-concurrent-map-examples/)

[Threads em Python](https://www.tutorialspoint.com/python/python_multithreading.htm)