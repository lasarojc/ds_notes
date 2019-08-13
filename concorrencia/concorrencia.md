# Concorrência



## Laboratório: Concorrência em Java

Neste tutorial, baseado neste [outro](https://docs.oracle.com/javase/tutorial/essential/concurrency/), exploraremos formas de se obter concorrência em Java. Isto é, exploraremos como iniciar múltiplas linhas de execução de instruções, que podem ou não, ser executadas em paralelo.

Em Java, há essencialmente duas formas de se conseguir concorrência. A primeira é via instâncias explícitas da classe `Thread`, e a segunda é via abstrações de mais alto nível, os `Executors`.

---
* Thread
* Executor
---

Além de formas de definir as linhas de execução, Java provê diversas estruturas para comunicação e coordenação destas linhas, desde de a versão 5 da linguagem, no pacote `java.util.concurrent`.


### *Threads*
Há duas formas básicas de se usar a classe `Thread`: extensão ou delegação de um objeto implementando `Runnable`.


---
##### Estender Thread
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
---


---
##### Implementar Runnable
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
---

Observe que nos dois exemplos, um método `run()` é implementado com o código a ser executado pelo *thread*. Em nenhum dos exemplos, contudo, o método é invocado diretamente. 
Em vez disto, o método `start()`, sim, é invocado. Isto ocorre pq antes de executar as instruções definidas pelo pelo programador no método `run()`,
a máquina virtual precisa executar alguma "mágica" por baixo dos panos como, por exemplo, solicitar ao sistema operacional a criação de um *thread* do SO, que servirá de hospedeiro para o *thread* Java. 
Isto acontece dentro do `start()`, que em algum ponto de sua execução levará a invocação do método `run()`.

A classe `Thread` também provê uma série de métodos que permitem gerenciar a vida do *thread* criado. 
Por exemplo, o método de classe (`static`) `Thread.sleep()` permite bloquear um *thread*  por um determinado período.


---
##### Thread.sleep()
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
---

Observe que a chamada a `sleep()` está dentro de um bloco `try/catch`. Isto é necessário pois é permitido à JVM acordar o *thread* em qualquer instante, antes ou após o tempo especificado. Assim, embora normalmente o tempo "dormido" seja próximo ao especificado, se há requisitos de precisão, é necessário que o *thread*, ao acordar, verifique se já dormiu o suficiente.

---
##### InterruptedException
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
---

Quando um *thread* está sendo executado, outros podem ter que esperar até que complete. Por exemplo, no caso de um navegador
Web, o *thread* que faz a renderização da página não pode começar a trabalhar enquanto o *thread*  que solicitou o HTML
do servidor não receber sua resposta. Um *thread* indica a intenção de esperar por outro usando o método `join()`.



---
##### Thread.join()
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
---

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

---
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


#### `synchronized`
Ao definir métodos como `synchronized`, garante-se que os mesmos nunca serão executados concorrentemente. 
Observe a classe a seguir, que modifica o contador do exercício anterior.

---
##### synchronized
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
---

Caso dois *threads* invoquem os métodos `increment` e `decrement` ao mesmo tempo, por exemplo, a JVM fará com que um dos *threads* pare sua execução até que o outro tenha completado a invocação.
Isto não quer dizer que executar o exercício anterior com esta versão do contador não levará a saídas com incrementos completamente sequenciais, pois um *thread*  poderia parar de ser executado logo após incrementar o contador, depois de terminado o método `increment`, e só voltar a executar depois que outro tenha incrementado e impresso na tela o valor obtido. 
O que quer dizer é que, mesmo que saídas estranhas existam, cada operação foi executada integralmente antes da operação seguinte.

##### Exercício
Modifique o código do exercício anterior para usar a versão `synchronized` do contador. Depois de executá-lo, adicione um `println("Dentro: " + c)` **dentro** do método de incremento para verificar que estas saídas acontecem ordenadamente.

#### Blocos `synchronized`
`synchronized` funciona porquê limita a concorrência, e é problemático exatamente pela mesma razão. 
Por isso, é essencial que o `synchronized` seja o mais limitado possível em termos de escopo, o que nos leva ao uso de `synchronized` em blocos de código menores que métodos. Por exemplo:

---
##### blocos `synchronized`
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

---
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

#### *Deadlock*
O uso dos "locks" em ordens diferentes pode levar a um deadlock, pois o seguinte grafo de dependência poderá ser gerado:

---
##### Deadlock
```plantuml
digraph Test {
T1 -> lock1
lock1 -> T2
T2 -> lock2
lock2 -> T1
}
```
---

#### Sinalização
Usados corretamente, o bloco `synchronized` é executado de forma atômica, isto é, indivisível.
Algumas operações muito simples são naturalmente atômicas, e não precisam ser "protegidas" pelo `synchronized`.
Por exemplo, leituras e escritas de tipos básicos como (`int`, `char`, `byte`, mas não `long` ou `double`), ou variáveis declaradas `volatile`.

Usando estas variáveis, é possível coordenar *threads*, por exemplo, assim:

---
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
---


Embora correto, esta abordagem não é eficiente, pois o primeiro método desperdiça computação. 
Felizmente, em Java, todos os objetos implementam os métodos `wait` e `notify/notifyAll`, que podem ser usados para sincronizar eficientemente *threds*.


---
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
---


#### *Locks*
Outras abstrações para coordenação de *threads* estão disponíveis no pacote `java.util.concurrent`. 
As mais simples delas são `java.util.concurrent.locks.Lock` e `java.util.concurrent.locks.ReentrantLock`. 
Veja um exemplo de uso, notando o idioma de uso dentro de block `try/catch`.

---
##### Lock
```Java
Lock l = new ReentrantLock();
  l.lock();
  try {
     // access the resource protected by this lock
  } finally {
     l.unlock();
  }
```
---


### Executor
Além de *threads*, Java disponibiliza `Executor` como abstração de mais alto nível para execução de tarefas concorrentes.

---
##### Executor
* `Executor`
* `ExecutorService`
* `ScheduledExecutorService`

```
Executor e = ...;
Runnable r = ...;
e.execute(r);
```
---

Executors normalmente implementam *thread pools*, que podem ser de diferentes tipos. 
O mais simples é o de tamanho fixo em que há um número inicial de *threads* criados e que, no caso de algum ser terminado, por exemplo por causa de uma exceção não tratada, cria substitutos para manter o número constante.

---
##### ThreadPool
`Executor e = java.util.concurrent.Executors.newFixedThreadPool();`

* `newCachedThreadPool()` - *expandable thread pool*
* `newSingleThreadExecutor()` - *single task at a time*
* e outras versões
* `ForkJoinPool`
---

---
##### Fork/Join
```
if (my portion of the work is small enough)
  do the work directly
else
  split my work into two pieces
  invoke the two pieces and wait for the results
```
---

### Estrutura para Coordenação de *Threads*
Finalmente, Java também disponibiliza estruturas de dados que podem ser acessadas concorrentemente por múltiplos *threads* 
sem risco de corrupção. 

---
##### Alguns tipos interessantes
* `BlockingQueue` - bloquei *threads*  se não houver elementos na filq.
* `ConcurrentMap/ConcurrentHashMap` - operações atômicas;
   * `if (!m.containsKey(k)) m.put(k,v);`
   * `vOld = m.putIfAbsent(k,v);`
---

---
##### Tipos Atômicos
```Java
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
---


---
##### ThreadLocal
```Java
private static ThreadLocal<Integer> myId = new ThreadLocal<Integer>() {
   public Integer initialValue() {
      return new Random().nexInt();
   } 
};

public static Integer getMyId() { 
    return myId.get();
}
```
---

Para aprender mais, muito mais sobre concorrência em Java, ótimas referências são:

* [Java Concurrency in Practice](http://jcip.net/)
* [The Well-Grounded Java Developer](https://www.manning.com/books/the-well-grounded-java-developer)

















