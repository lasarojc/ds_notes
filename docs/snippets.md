


```mermaid
graph LR
  A[Cliente] -->|Requisição| B{Monitor de Transações}
  B -->|Resposta| A
  B -->|Requisição| C[(Servidor 1)]
  B -->|Requisição| D[(Servidor 2)]
  B -->|Requisição| E[(Servidor 3)]
		
  C -->|Resposta| B
  D -->|Resposta| B
  E -->|Resposta| B
```


transacionais.
Isto é, eles provêem as garantias na execução de transações conhecidas como propriedades ACID.

!!!note "ACID"
    * Atomicidade: transações são tratadas de forma indivisível, isto é, ou tudo ou nada.
    * Consistência: transações levam banco de um estado consistente a outro. E.g., `x == 2*y`
    * Isolamento: transações não vêem dados não comitados umas das outras.
    * Durabilidade: os efeitos de uma transação comitada devem persistir no sistema a despeito de falhas.


Para relembrar no que implica ACID, considere a seguinte sequência de operações, onde X e Y são valores guardados pelo banco de dados, a, b e c são variáveis definidas no programa, e SELECT e SET são comandos para ler e modificar o banco de dados.

```
1: a = SELECT X
2: c = a * 2
3: b = c + 10
4: SET X=c
5: SET Y=b
```
Suponha duas instâncias desta sequência, $T_1$ e $T_2$, concorrentes, em que as operações escalonadas da seguinte forma.

```
   T1                T2
1: a = SELECT X
2: c = a * 2
3: b = c + 10
4: SET X=c
5:                    a = SELECT X
6:                    c = a * 2
7:                    b = c + 10
8:                    SET X=c
9:                    SET Y=b
10:SET Y=b
```

Ao final da execução, X terá o valor atribuído por $T_2$, mas $Y$ terá o valor de $T_1$. 
Este escalonamento violou a **consistência** do banco de dados por quê as operações não foram executadas **isoladamente**.













, como mostra a hierarquia a seguir, adaptada de [jepsen.io](https://jepsen.io/consistency).

```mermaid
graph BT
    S --> SS[Strict Serializable]
    RU[Read Uncommitted] --> RC[Read Committed]
    RC --> CS[Cursor Stability]
    RC --> MAV[Monotonic Atomic View]
    MAV --> RR[Repeatable Read]
    MAV --> SI[Snapshot Isolation]
    CS --> RR
    SI --> S[Serializable]
    RR --> S
    L[Linearizable] --> SS
    Seq[Sequential] --> L
    C[Causal] --> Seq
    WFR[Writes Follow Reads] --> C
    FIFO --> C
    MR[Monotonic Reads] --> FIFO
    MW[Monotonic Writes] --> FIFO
    RW[Read Your Writes] --> FIFO
```



e é aí que entram os modelos de consistência.