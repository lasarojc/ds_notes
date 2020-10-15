#Tolerância a Falhas

Neste capítulo discutiremos o quê são sistemas distribuídos, por quê os desenvolvemos, e damos uma visão geral de como isto é feito.
## Dependabilidade

Ao escrevermos nossos softwares, queremos que sejam usados para resolver problemas, mesmo que importância do problema esteja em um espectro bem vasto, indo, por exemplo, da execução de um cirurgia ocular remota, ao controle de uma usina hidrelétrica, à jogar truco contra um computador. Independentemente do problema sendo resolvido, gostaríamos de poder contar com o sistema, de poder depender nele para executar sua tarefa.
Desta situação, surge a ideia de dependabilidade, isto é, de um sistema ter a propriedade de que podemos depender do mesmo.

Em computação distribuída, componentes dependem uns dos outros para a realização de tarefas. Assim, componentes que quer ser "dependáveis" (do inglês, *dependable*), pois se não o forem, os demais componentes não poderão executar suas tarefas, rendendo o sistema como um todo inútil.

Assim, dizemos que um componente $C$ depende de um componente $C'$ se a corretude do comportamento de $C$ depende da corretude do componente $C'$.
E que um componente é ``dependável'' (\emph{dependable}) na medida que outros podem depender dele.



De acordo com [Laprie et al](https://ieeexplore.ieee.org/document/1335465?arnumber=1335465), tem-se dependabilidade quando os seguintes atributos estão presentes.

* Disponibilidade (*Availability*) - Prontidão para uso.
* Confiabilidade/Fiabilidade (*Reliability) - Continuidade do serviço.
* Manutenabilidade (*Maintainability*) - Facilidade de reparo.
* Segurança (*Safety*) - Tolerância a catástrofes.
* Integridade (*Integrity*) - Tolerância a modificações.
* Confidencialidade (*Confidentiality) - Informação somente a quem devido.

A combinção das três últimas propriedades é também chamadas de Segurança (*Security*).

Como obstáculos para se conseguir estes atributos estão os seguintes obstáculos, ou ameaças:

* Fault - Falha (Falta): bug -- \lstinline|<=| em vez de \lstinline|<| (pode nunca afetar a execução).
* Error - Erro  (Erro): manifestação do bug -- iteração passa do ponto. (Pode não ser observável pelo usuário.)
* Failure - Defeito (Falha): problema visível -- tela azul

## Modelos 

\section{Dependabilidade}
\subsection{Introdução}


\begin{frame}{Dependabilidade}
\begin{block}{}
	Um componente provê serviços a um cliente.\\ 
	Para prover o serviço, o componente pode \alert{depender} de outros.
\end{block}

\begin{block}{}
	Um componente $C$ depende de um componente $C'$ se a corretude do comportamento de $C$ depende da corretude do componente $C'$.
\end{block}

\begin{block}{}
Um componente é ``dependável'' (\emph{dependable}) na medida que outros podem depender dele.
\end{block}
\end{frame}

\begin{frame}{Dependabilidade}
O que queremos (Atributos)
\begin{itemize}
	\item Disponibilidade (\emph{Availability})-- Prontidão para uso.
	\item Confiabilidade/Fiabilidade (\emph{Reliability}) -- Continuidade do serviço.
	\item Manutenabilidade (\emph{Maintainability}) -- Facilidade de reparo.	\\~ \\~\\
	
	\item Segurança (\emph{Safety}) -- Tolerância a catástrofes.
	\item Integridade (\emph{Integrity}) -- Tolerância a modificações
	\item Confidencialidade (\emph{Confidentiality}) -- Informação somente a quem devido.
	\item Segurança (\emph{Security}) -- Soma dos três anteriores.
\end{itemize}
\end{frame}


\begin{frame}[fragile]{Dependabilidade}
O que gostaríamos de evitar (Ameaças)
\begin{itemize}
	\item Fault -- Falha (Falta): bug -- \lstinline|<=| em vez de \lstinline|<| (pode nunca afetar a execução).
	\item Error -- Erro  (Erro): manifestação do bug -- iteração passa do ponto. (Pode não ser observável pelo usuário.)
	\item Failure -- Defeito (Falha): problema visível -- tela azul
\end{itemize}
\end{frame}


\begin{frame}{Ariane 5}
``The Explosion of the Ariane 5

On June 4, 1996 an unmanned Ariane 5 rocket launched by the European Space Agency exploded just forty seconds after its lift-off [...] after a decade of development costing \$7B. The destroyed rocket and its cargo were valued at \$500M. [...] the failure was a software error [...] a 64 bit floating point number [...] was converted to a 16 bit signed integer. The number was larger than 32,767, the largest integer storeable in a 16 bit signed integer, and thus the conversion failed.''

\includegraphics[width=.7\textwidth]{images/ariane5}

\href{http://www-users.math.umn.edu/~arnold/disasters/ariane.html}{Fonte}
\end{frame}

O erro gerado foi tratado como input, causando outros erros, que geraram instabilidade e que levou o sistema a se auto-destruir.

\begin{frame}{Dependabilidade}
Como evitar (Meios)
\begin{itemize}
	\item Prevenção de falhas: \pause escreva sem bugs!\\
	Especificações formais; prova de corretude; model checkers;...

	\item Remoção de falhas: \pause resolva seus bugs!\\
	Testes; manutenção.	

	\item Previsão de falhas: \pause estime quando elas se manifestarão!
	Reinicie processos frequentemente.

	\item Tolerância a falhas: \pause conviva e mascare falhas.
\end{itemize}
\end{frame}

\begin{frame}{Subaru SUVs -- 2018}
\includegraphics[width=.45\textwidth]{images/subaru}

\href{https://spectrum.ieee.org/riskfactor/computing/it/coding-error-leads-293-subaru-ascents-to-the-car-crusher}{Fonte}
\end{frame}

\begin{frame}{Car Hack -- 2017}
\includegraphics[width=.4\textwidth]{images/carhack}

\href{https://www.wired.com/story/car-hack-shut-down-safety-features/}{Fonte}
\end{frame}




\begin{frame}{Tolerância a falhas}
Dependendo dos efeitos e tratamentos.
\begin{itemize}
	\item Fail safe -- defeito não leva a comportamento inseguro (sistema de entretenimento no avião)
	\item Fail soft -- graceful degradation (sistema de controle de vôo)
	\item Fail fast -- para o fluxo de defeitos (e possível reinício)

\vspace{1cm}

	\item Robusto -- erros não atrapalham execução (tratamento de exceções)
	\item Quebradiço (\emph{brittle}) -- não resiliente a falhas
	
\end{itemize}
\end{frame}


\begin{frame}{Fail Fast -- Cadeia de supervisão}
\includegraphics[width=.7\textwidth]{images/httpatomoreillycomsourceoreillyimages300817}
\end{frame}


\begin{frame}{Tolerância a Falhas -- Exemplos}
\begin{itemize}
	\item Pneu estepe
	\item Gerador de eletricidade em casa
	\item Calça extra na mala
	\item Uber, em vez do ônibus
\end{itemize}
\pause O que há de comum?
\end{frame}

\begin{frame}{Redundância}
Componentes extra, para uso em caso de defeitos. Ausência de um SPOF -- \emph{Single Point of Failure}
\pause
\begin{itemize}
	\item Mais projeto
	\item Mais testes
	\item Mais custo
	\item Mais tempo
	\item Peso extra
\end{itemize}

\pause Custo x Beneficio -- Custo da redundância, Probabilidade de falha, beneficio.\\
\pause Pneu x Motor x Radio



\end{frame}

\begin{frame}[allowframebreaks]{Tipos de defeitos}
\begin{itemize}
	\item Quebra/Crash: componente para de funcionar.
	\begin{itemize}
		\item Fail-stop -- defeito é detectável (timeout, por exemplo).\\
		E se o problema for a rede?
		\item Fail-silent -- defeito pode não ser notável.
		\item Fail-recover -- voltam a executar.
	\end{itemize}

	\item Omissão: componente não executa ações.
	\begin{itemize}
		\item Requisição não atendida
		\item Mensagem não transmitida
	\end{itemize}

	\item Temporização: prazos não são respeitados.
		
	\item Arbitrária: qualquer coisa pode acontecer.
	\begin{itemize}
		\item Resposta: ações são executadas incorretamente mas sem maldade.
		\item Arbitrária com detecção por falha de identificação
	\end{itemize}
\end{itemize}

Fail-stop $\in$ Quebra $\in$ Omissão $\in$ Temporização $\in$ Arbitrária

\end{frame}


\begin{frame}{Resposta}
\begin{itemize}
	\item ALU defeituosa
\end{itemize}
\end{frame}

\begin{frame}{Arbitrária}
\begin{itemize}
	\item Bug
	\item Hacking
	\item Vírus
\end{itemize}
\end{frame}


\begin{frame}{Prepare-se para a vida de desenvolvedor}
\begin{itemize}
	\item Falhas intermitentes -- e.g., picos de energia, comportamento emergente, 
	\item Heisenbugs -- inobservável
	\item Schroedinbugs	-- inexistente até que observado
\end{itemize}
\end{frame}

Heisenbug
The name may seem to rhyme well with Heisenberg, but the Heisenbug is actually "a bug that disappears or alters its behavior when one attempts to probe or isolate it." The Freenet Project describes a Heisenbug in certain Java virtual machines.

Bohrbug
The Bohrbug is a sort of antonym of the Heisenbug, as this bug does not disappear or alter its characteristics when it is researched.

Mandelbug
The Mandelbug, named after Benoit Mandelbrot (think Mandelbrot set), is a bug whose underlying causes are so complex and obscure as to make its behavior appear chaotic.

Schroedinbug
The Schroedinbug is a design or implementation bug in a program that doesn't manifest until someone reading source or using the program in an unusual way notices that it never should have worked, at which point the program promptly stops working for everybody until fixed. Here, an Office developer describes "stupid SQL tricks" to get rid of a "classic Schroedinbug." 



\begin{frame}{Correlação entre falhas?}
N-Version programming
\begin{itemize}
	\item Múltiplos times
	\item Múltiplas implementações do mesmo sistema
	\item Falhas independentes
	\vspace{1cm}
	\item Custo maior
	\item Erros de especificação são reproduzidos
	\item Times diferentes, mas erros iguais
\end{itemize}
\end{frame}



\begin{frame}{Foco}
Falhas do tipo crash.
\end{frame}

\begin{frame}{Redundância de Processos}
Como lidar com falhas de processos? 

Tenha múltiplos, tal que se um falha, outros podem continuar executando o serviço.

\begin{itemize}
	\item Ativo/Ativo
	\item Mestre/Escravo
	\item Replicação em cadeia
	\item ...
\end{itemize}
\end{frame}





\subsection{Coordenação}

\begin{frame}{Coordenação}
Para replicar, precisamos coordenar as execuções dos processos, mas como?
\end{frame}

\begin{frame}{Uma história de três exércitos}
	\begin{itemize}
		\item A e B deve atacar C
		\item Se A e B atacam juntos, ganham
		\item Se atacarem separados, são ambos derrotados.
		\item Comunicação por mensageiros, 
		\begin{itemize}
			\item que podem se perder e levar muito tempo para chegar
			\item podem ser mortos no caminho
		\end{itemize}
	\pause
		\item Atacamos ao amanhecer! (relógios sincronizados)	
	\end{itemize}
\end{frame}

\begin{frame}{Uma história de três exércitos}
Será que A não mandou uma resposta? Será que A foi destruído? Será que o mensageiro morreu? Será que parou em um inferninho?
\end{frame}

\begin{frame}{Uma história de três exércitos}
Como saber se o outro recebeu a mensagem e irá atacar ao mesmo tempo?

\pause
Mensagem de confirmação.
\end{frame}

\begin{frame}{Uma história de três exércitos}
Como saber se o um recebeu a confirmação?

\pause
Confirmação da confirmação!
\end{frame}


\begin{frame}{Uma história de três exércitos}
Como saber se o outro/um ...?
\end{frame}


\begin{frame}{Uma história de três exércitos}
Nesse cenário, até simples, coordenar os dois processos é \alert{impossível}!

\pause É impossível garantir que chegarão a um \emph{acordo}.
\end{frame}


\section{Acordo}

\subsection{Comunicação em Grupo}
\begin{frame}{Acordo}
Dependendo do modelo, pode ser muito fácil ou impossível fazer com que um grupo de processos concorde sobre como agir/entre em acordo.
\end{frame}

Um dos fatores é como o grupo é organizado.

\begin{frame}{Grupos de Processos}
Grupos podem ser organizados de diferentes formas, dependendo da aplicação.

\includegraphics[width=.5\textwidth]{images/08-03}
\includegraphics[width=.45\textwidth]{images/chain}

Assumindo um grupo estático (sem entrada e saída de processos), em que todos conversam com todos diretamente (grafo completo).
\end{frame}

É mais fácil, mas não trivial neste modelo. Vejamos o problema de difusão confiável. Mas antes, vamos definir.

\begin{frame}{Correto x Falho}
Um processo é correto se ele não falha.
\end{frame}

\begin{frame}{Difusão Confiável}
\begin{itemize}
	\item Corretude: Se um processo \alert{correto} $p$ difunde uma mensagem $m$ para processos no grupo $G$, então todos os processos corretos em $G$ entregam a mensagem.
	
	\item Acordo: Se um processo correto em $G$ entrega uma mensagem $m$, então todo processo correto em $G$ entrega $m$.
	
	\item Validade: Somente mensagens difundidas são entregues.
\end{itemize}
\end{frame}

\begin{frame}{Terminologia}
\begin{itemize}
	\item Enviar/Receber: rede
	\item Difundir/Entregar: difusão
	
	\item Corretude x Progresso
\end{itemize}
\end{frame}



\begin{frame}{Difusão Confiável}
Algoritmo?
\end{frame}


\begin{frame}{Difusão Confiável}
Para $p$ difundir $m$ para $G$
\begin{itemize}
\item $p$ envia $m$ para todo $q \in G$
\item Todo processo $q\in G$ que receber a mensagem, envia $m_{ack}$ para $p$
\item Ao receber ack de todos os processos $q \in G$, $p$ para de retransmitir e entrega $m$.
\end{itemize}

\pause Confiável? Isto é, satisfaz as duas propriedades?

\pause e se um dos receptores falhar?

\pause Assumamos detector de falhas perfeito.
\end{frame}

\begin{frame}{Difusão Confiável}
Para $p$ difundir $m$ para $G$
\begin{itemize}
	\item $p$ envia $m$ para todo $q \in G$
	\item Todo processo $q\in G$ que receber a mensagem, envia $m_{ack}$ para $p$
	\item Ao receber ack de todos os processos \alert{corretos} $q \in G$, $p$ para de retransmitir e entrega $m$.
\end{itemize}

\pause Pq $p$ só entrega $m$ no final?
\end{frame}


\begin{frame}{Difusão Confiável}
Posso assumir TCP como protocolo de comunicação?

\begin{itemize}
	\item $p$ envia $m$ para $G$
	\item $p$ entrega $m$
\end{itemize}

\pause Não! TCP não é confiável neste sentido.
\end{frame}

\begin{frame}{Difusão Confiável}
Assuma máximo de $f$ falhas, fail stop

\begin{itemize}
\item $p$ envia $m$ para processos em $G$.
\item Todo processo $q \in G$ que receber $m$
\begin{itemize}
	\item envia $m_{ack}$
	\item repassa $m$ para $G$ processos.
\end{itemize}
\item Mensagens são retransmitidas de tempos em tempos.
\item Ao receber $f+1$ acks, entrega $m$.
\end{itemize}
%https://www.youtube.com/watch?v=uzcALT7sHew
\end{frame}

\begin{frame}{Difusão Confiável}
Assuma máximo de $f$ falhas e uso de TCP
\begin{itemize}
	\item $p$ envia $m$ para processos em $G$.
	\item Em caso de quebra/falha de conexão, substitua destinatário.
	\item Todo processo $q \in G$ que receber $m$, repassa $m$ para os outros  processos.
	\item Entrega $m$.
\end{itemize}

\pause Confiável? Somente se conexões quebradas for reestabelecidas e mensagens reenviadas.

\pause Escalável?
\end{frame}

\begin{frame}{Difusão Confiável FIFO}
\begin{itemize}
	\item Corretude.
	\item Acordo.	
	\item Validade.
	\item FIFO: Se $p$ difunde $m$ e então $n$, e se $q$ entrega $n$, então $q$ entrega $m$ antes $n$.
\end{itemize}
\end{frame}

\begin{frame}{Difusão Totalmente Ordenada}
\begin{itemize}
\item Corretude: Se um processo $p$ difunde uma mensagem $m$ para processos no grupo $G$, e se $p$ não falha, então todos os processos corretos em $G$ entregam $m$

\item Acordo: Se um processo correto $q$ em $G$ entrega uma mensagem $m$, então todo processo correto em $G$ entrega $m$.

\item Ordenação: Se um processo entrega mensagem $m$ e depois $n$, então qualquer processo que entregue a mensagem $n$ deve primeiro entregar $m$.

\item Validade: Somente mensagens difundidas são entregues.
\end{itemize}

\pause Algoritmo?
\pause Resolver diretamente este problema não é trivial. Por isso, veremos primeiro o problema do Consenso Distribuído.
\end{frame}


\begin{frame}{Consenso}
Sejam vários processos. Cada um propõe um único valor por \emph{instância de consenso}. O objetivo é decidir um dentre os valores propostos:
\begin{itemize}
	\item Validade: Somente um valor proposto pode ser decidido.
	\item Terminação: Todo processo não falho decide-se.
	\item Acordo: Se um processo decide-se por $v$ e outro por $w$, então $v = w$
\end{itemize}
\end{frame}

\begin{frame}{Consenso}
É impossível resolver deterministicamente o problema do consenso em sistema assíncrono sujeito a falhas. \[Fischer, Lynch, Patterson, 85\]

\pause Mas o consenso é resolvido frequentemente em sistemas assíncronos sujeitos a falhas. Isso porque normalmente estes sistemas se comportam sincronamente.
\end{frame}



\begin{frame}{Consenso}
Há diversos algoritmos de consenso que terminam quando o sistema se comporta bem. O mais famoso, atualmente, é o Paxos.


\href{http://paxos.systems/index.html}{Leia mais aqui}
%TODO: Synod
%TODO: Paxos

\end{frame}

\begin{frame}{Primitivas}
\begin{itemize}
	\item send\&receive/enviar\&receber -- rede
	\item propose\&decide/propor\&decidir -- consenso
	\item broadcast\&deliver/difundir\&entregar -- difusão
\end{itemize}
\end{frame}


\begin{frame}{Difusão Totalmente Ordenada}
Dado infinitas instâncias de consenso, pode-se usá-las para resolver difusão atômica:
\begin{itemize}
\item Ordene as instâncias de consenso.
\item Para difundir mensagem $m$, proponha a mensagem na menor instância $i$ em que não tiver visto uma decisão.
\item Se a decisão de $i$ não é $m$, volte para o passo anterior.
\item Entregue as decisões na ordem das instâncias.
\end{itemize}
\end{frame}


\subsection{Replicação de Máquinas de Estados}

\begin{frame}{Máquina de Estados Replicada}
Ativo/Ativo -- Se todos os processos executam a mesma sequência de comandos, todos avançam pelos mesmos estados.

\begin{itemize}
	\item Mesmo estado inicial
	\item Comandos determinísticos
	\item Comandos causalmente relacionados são executados em mesma ordem
\end{itemize}
\end{frame}

\begin{frame}{Máquina de Estados Replicada}
Possíveis ordens com mesmo efeito?
\begin{itemize}
\item \lstinline|touch /tmp/file1|
\item \lstinline|echo "teste testando" $>>$ /tmp/file2|
\item \lstinline|rm /tmp/file1|
\item \lstinline|mkdir /dir1|
\end{itemize}
\end{frame}


\begin{frame}{Difusão Causal}
Há diversos algoritmos, mas o mais recente e interessante é denominado Generalized Paxos.

Para saber mais, leia minha dissertação!
\end{frame}

\begin{frame}{Frameworks para Coordenação}
Diversos sistemas abstraem problemas de coordenação em sistemas distribuídos.

Estudaremos alguns nas aulas seguintes.
\end{frame}

Antes, vamos nos aprofundar no estudo de um protocolo de Consenso/Difusão atômica.

\subsection{RSM: Estudo de Caso do Raft}
\begin{frame}{Raft}
\url{http://thesecretlivesofdata.com/raft/}
\end{frame}







\section{Estudo de Caso: Paxos}


\begin{frame}{Paxos}
%\begin{itemize}
%	\item Sínodo (Synod): consenso
%	\item Paxos: Difusão Atômica
%\end{itemize}
\end{frame}

\section{Estudo de caso: Atomix Copycat}
\begin{frame}{Atomix Copycat}
\begin{itemize}
\item Framework de replicação de máquinas de estados implementada pela Atomix. 
\item Implementação do Raft
\item API simples
\item Java 8 (lambdas e futures)
\item \url{http://atomix.io/copycat/}
\end{itemize}
\end{frame}

\begin{frame}[fragile,allowframebreaks]{Lambda}
\begin{itemize}
	\item Classe com um único método.
	\begin{lstlisting}[language=java]
class Tarefa implements Runnable {
  public void run(){
    while (true)
      System.out.println("Bem vindo a um loop infinito");
    }   
}

new Thread(new Tarefa()).start();
	\end{lstlisting}

\framebreak

	\item Classe anônima -- uso único
\begin{lstlisting}[language=java]
new Thread( new Runnable() {
  public void run(){
    while (true)
      System.out.println("Bem vindo a um loop infinito");
  }   
}).start();
\end{lstlisting}

\framebreak

	\item Lambda
\begin{lstlisting}[language=java]
new Thread(() -> {
                     while (true)
                       System.out.println("Bem vindo a um loop infinito");
                   }).start();
\end{lstlisting}

\framebreak
   \item Encadeamento (fluent)
\begin{lstlisting}[language=java]
   Collection<Pessoa> c = ...;
   c.stream()
    .filter(p -> p.idade > 33)
    .map(Pessoa::sobrenomeNome)//.map(p -> p.sobrenomeNome())
    .forEach(s -> System.out.println(s));
\end{lstlisting}
\end{itemize}
\end{frame}

\begin{frame}[fragile]{Future}
\begin{itemize}
	\item Promessa de computação e resultado.
\begin{lstlisting}[language=java]
ExecutorService executor = Executors.newSingleThreadExecutor();
Future<Integer> futFib = executor.submit(() -> { return Fibonacci(217)};
\end{lstlisting}

	\item Quando será executado? \pause Em algum momento.
	\item Como pegar o resultado? 

\begin{lstlisting}[language=java]
while (!futFib.isDone())
  System.out.println("tah calculando...");

int fib217 = futFib.get();
\end{lstlisting}

	\item Em qual thread? \pause Em algum thread. Depende do Executor Service usado.	
\end{itemize}
\end{frame}


\begin{frame}{Atomix-Raft}
\begin{itemize}
	\item Versão >= 2 do copycat
	\item Melhor desempenho
	\item Documentação ruim
	\item \url{https://github.com/atomix/atomix}
\end{itemize}
\end{frame}

\begin{frame}{Lab}
\begin{itemize}
	\item Versão 1.1.4
	\item Baseado em \url{http://atomix.io/copycat/docs/getting-started/}
	\item Código funcional em \url{https://github.com/pluxos/atomix_labs}
\end{itemize}
\end{frame}

\begin{frame}{Clone e compile o projeto}
\begin{itemize}
	\item Instale dependências: git, maven e JDK >= 1.8 (lembre-se que gRPC precisa de JDK <= 1.8)
	\item git clone https://github.com/pluxos/atomix\_labs
	%https://www.baeldung.com/atomix
	\item cd atomix\_labs
	\item cd replication
	\item mvn compile
	\item mvn test
\end{itemize}
\end{frame}

\begin{frame}[fragile]{mvn test}
Resultado esperado.
\begin{verbatim}
Tests run: 1, Failures: 0, Errors: 0, Skipped: 0

[INFO] ---------------------------------------
[INFO] BUILD SUCCESS
[INFO] ---------------------------------------
[INFO] Total time: 6.898 s
[INFO] Finished at: 2017-10-25T08:38:08-02:00
[INFO] Final Memory: 15M/159M
[INFO] ---------------------------------------
\end{verbatim}
\end{frame}

\begin{frame}{Estrutura}
Explore o projeto. Na pasta/URL \url{https://github.com/pluxos/atomix_labs/tree/master/replication/src/main/java/atomix_lab/state_machine}

Há três pastas. Analise-as nesta ordem
\begin{itemize}
	\item type -- tipos dos dados mantidos pela replica (Edge e Vertex)\\
	Os tipos são serializable para que o Java saiba como transformá-los em bytes.
	\item command -- estruturas que contêm informações para modificar os tipos\\
	Os comandos serão enviadas do cliente para o cluster e são naturalmente serializable.
	\item client -- cria comandos e os envia para serem executados no cluster\\
	Respostas podem ser esperadas síncrona ou assincronamente.
	\item server -- recebe os comandos na ordem definida pelo Raft e os executa
\end{itemize}
\end{frame}


\begin{frame}{Lab}
O projeto foi construído seguindo as instruções no tutorial mencionado antes, saltando-se a parte dos snapshots, isto é:
\begin{itemize}
	\item crie um projeto maven\\
	eclipse tem template para isso
	\item adicione dependências no pom.xml\\
	como so criei um projeto, coloquei as dependências tanto do cliente quando do servidor
	\item defina Command que modifiquem o estado das réplicas
	\item defina Queries que consultem o estado das réplicas
	\item implemente a réplica para lidar com os comandos
	\item implemente o cliente para emitir comandos
\end{itemize}
\end{frame}

\begin{frame}{Lab}
Para executar um servidor, você precisa passar como parâmetro
\begin{itemize}
	\item identificador do processo (inteiro)
	\item IP do processo com identificador 0
	\item porta do processo com identificar 0
	\item IP do processo com identificador 1
	\item porta do processo com identificar 1
	\item ...
\end{itemize}

Sabendo seu identificador, o servidor sabe em qual porta escutar e em quais IP/porta se conectar.
\end{frame}

\begin{frame}[fragile]{Lab}
Execute três servidores. Usando o maven, da linha de comando, fica assim:
\begin{tiny}
\begin{verbatim}
mvn exec:java \\
  -Dexec.mainClass="atomix_lab.state_machine.server.GraphStateMachine" \\
  -Dexec.args="0 127.0.0.1 5000 127.0.0.1 5001 127.0.0.1 5002"
	
mvn exec:java \\
  -Dexec.mainClass="atomix_lab.state_machine.server.GraphStateMachine" \\
  -Dexec.args="1 127.0.0.1 5000 127.0.0.1 5001 127.0.0.1 5002"
	
mvn exec:java \\
  -Dexec.mainClass="atomix_lab.state_machine.server.GraphStateMachine" \\
  -Dexec.args="2 127.0.0.1 5000 127.0.0.1 5001 127.0.0.1 5002"
\end{verbatim}
\end{tiny}
%As instruções, sem quebra, estão no README do repositório.
\end{frame}


\begin{frame}[fragile]{Lab}
O cliente não precisa de um identificador, apenas dos pares IP/porta dos servidores.
\begin{itemize}
	\item IP do processo com identificador 0
	\item porta do processo com identificar 0
	\item IP do processo com identificador 1
	\item porta do processo com identificar 1
	\item ...
\end{itemize}

Para executá-lo, use o comando
\begin{tiny}
\begin{verbatim}
mvn exec:java 
  -Dexec.mainClass="atomix_lab.state_machine.client.GraphClient"
  -Dexec.args="127.0.0.1 5000 127.0.0.1 5001 127.0.0.1 5002"
\end{verbatim}
\end{tiny}

\end{frame}

\begin{frame}{Exercício}
Uma vez executado o projeto, modifique-o para incluir uma nova operação (Command) e nova consulta (Query). 
\end{frame}
\section{Serviços de Coordenação}

\begin{frame}{Serviços de Coordenação}
\begin{itemize}
	\item Zookeeper
	\item Atomix
	\item OpenReplica
\end{itemize}
\end{frame}

\subsection{Estudo de Caso: ZooKeeper}

\subsubsection{Visão Geral}
\begin{frame}{ZooKeeper}
\begin{center}
\includegraphics{images/zklogo}
\end{center}
\url{http://zookeeper.apache.org/}
\end{frame}


\begin{frame}{ZooKeeper}
\begin{block}{Zoo?}
Porquê sistemas distribuídos são como zoológicos, com animais de diversas espécies, sendo obrigados a conviver de forma anti-natural.
\end{block}
\end{frame}

\begin{frame}{ZooKeeper}
\begin{block}{O quê?}
ZooKeeper is a \alert{centralized} service for maintaining \alert{configuration} information, \alert{naming}, providing distributed \alert{synchronization}, and providing \alert{group services}. All of these kinds of services are used in some form or another by \alert{distributed applications}. Each time they are implemented there is a lot of work that goes into fixing the bugs and race conditions that are inevitable. Because of the difficulty of implementing these kinds of services, applications initially usually skimp on them, which make them brittle in the presence of change and difficult to manage. Even when done correctly, different implementations of these services lead to management \alert{complexity} when the applications are deployed.
\end{block}
\end{frame}


\begin{frame}{ZooKeeper}
\begin{block}{O quê?}
	ZooKeeper is a \alert{distributed}, open-source \alert{coordination service for distributed applications}. It exposes a \alert{simple set of primitives} that distributed applications can build upon to implement higher level services for synchronization, configuration maintenance, and groups and naming. It is designed to be easy to program to, and uses a data model styled after the familiar \alert{directory tree structure of file systems}. It runs in Java and has bindings for both \alert{Java} and \alert{C}.
\end{block}
\end{frame}

\begin{frame}{ZooKeeper}
\begin{block}{Por quê?}
	Coordination services are notoriously hard to get right. They are especially prone to errors such as race conditions and deadlock. The motivation behind ZooKeeper is to relieve distributed applications the responsibility of implementing coordination services from scratch.
\end{block}
\end{frame}


\begin{frame}{ZooKeeper}
\begin{block}{Como?}
	ZooKeeper allows distributed processes to coordinate with each other through a \alert{shared hierarchal namespace which is organized similarly to a standard file system}. The name space consists of data registers - called \alert{znodes}, in ZooKeeper parlance - and these are \alert{similar to files and directories}. Unlike a typical file system, which is designed for storage, ZooKeeper data is kept \alert{in-memory}, which means ZooKeeper can achieve \alert{high throughput and low latency} numbers.
\end{block}

\end{frame}

\begin{frame}{ZooKeeper}
\includegraphics[width=.7\textwidth]{images/zknamespace}
\end{frame}

\begin{frame}{ZooKeeper}
\begin{block}{Como?}
	ZooKeeper is replicated.\\
	ZooKeeper is ordered.
\end{block}

\includegraphics[width=1\textwidth]{images/zkservice}
\end{frame}

\begin{frame}{ZooKeeper}
\includegraphics[width=1\textwidth]{images/zkcomponents}
\end{frame}

\begin{frame}{ZooKeeper}
\begin{block}{Como?}
	ZooKeeper is fast [...] and it performs best where reads are more common than writes, at ratios of around 10:1.
\end{block}
\end{frame}

\begin{frame}{Desempenho}
	\includegraphics[width=1\textwidth]{images/zkperfRW_3_2}
\end{frame}


\subsubsection{Uso}
\begin{frame}{ZNodes}
\includegraphics[width=.6\textwidth]{images/zknamespace}

\begin{itemize}
	\item Arquivo e diretório ao mesmo tempo.
	\item São (devem ser) pequenos.
	\item Operados atomicamente: todo o dado é lido/escrito.
	\item API simples
		\begin{itemize}
			\item C: create
			\item R: get
			\item U: set
			\item D: delete
			\item *: get children
		\end{itemize}
\end{itemize}
\end{frame}


\begin{frame}[fragile]{Lab}
\begin{itemize}
	\item Download: wget \url{www-eu.apache.org/dist/zookeeper/zookeeper-3.4.10}
	\item Unpack: tar xvzf zookeeper*.tgz
	\item Config: \verb|conf/zoo.cfg| $\Leftarrow$ Copie o exemplo\\
	%\verb|tickTime=2000|\\
	%\verb|dataDir=/tmp/seuNome/zk0| $\Leftarrow$\\
	%\verb|clientPort=2181| $\Leftarrow$
	\item \verb|./bin/zkServer.sh start-foreground|
	\item \verb|./bin/zkCli.sh -server 127.0.0.1:2181| $\Leftarrow$
\end{itemize}
\end{frame}


\frame{Exemplo}

\begin{frame}{ZNodes}
\begin{itemize}
	\item Stat(istics): versão, ACL, timestamps.
\end{itemize}
\end{frame}

\frame{Exemplo}

\begin{frame}{ZNodes}
\begin{itemize}
	\item Updates condicionais.
\end{itemize}
\end{frame}

\frame{Exemplo}

\begin{frame}{ZNodes}
\begin{itemize}
	\item Nós efêmeros: presentes enquanto a sessão que os criou estiver ativa.
\end{itemize}
\end{frame}

\frame{Exemplo}

\begin{frame}{ZNodes}
\begin{itemize}
	\item Watches: notificam clientes de mudanças no nó ou em seus filhos.
	\item Uso único.
\end{itemize}
\end{frame}

\frame{Exemplo}

\begin{frame}{Durabilidade?}
Todas as operações são colocadas em um log em disco.
\end{frame}


\subsubsection{Receitas}
\begin{frame}{Receitas}
É possível resolver diversos problemas encontrados em sistemas distribuídos usando-se o ZooKeeper.
\end{frame}

\begin{frame}{Rendezvous}
Ponto de encontro de processos. \pause

\begin{itemize}
	\item Defina um zNode raiz a ser usado: /rendezvous/app1/ \pause
	\item Cada filho de /rendezvous/app1 corresponde a um processo:
		\begin{itemize}
			\item IP
			\item Porta
			\item Número de processadores
			\item ...
		\end{itemize}
	\item Processo p ao ser iniciado:
		\begin{itemize}
			\item procura /rendezvous/app1/p
			\begin{itemize}
				\item se achar, continua
				\item se não achar, cria /rendezvous/app1/p
			\end{itemize}
			\item lista os filhos de /rendezvous/app1
		\end{itemize}
\end{itemize}
\end{frame}

\begin{frame}{Como lidar com saída de processos?}
\pause Faça todos os zNodes são efêmeros. \\
Quando um nó é desconectado, o zNode correspondente será destruído.
\end{frame}

\begin{frame}{Como detectar mudanças no grupo de processos?}
Monitore os filhos de /rendezvous/app1\\
Sempre que receber notificações, refaça o cálculo do \emph{membership}.
\end{frame}

\begin{frame}{Eleição de Líderes}
\pause Rendezvous.\\
\pause Faça os zNodes sequenciais.
\pause Ordene os zNodes e escolha o primeiro.
\pause Monitore o zNode. Se ele sumir, eleja outro líder.
\end{frame}

\begin{frame}{Exclusão Mútua}
Construa uma fila usando nós efêmeros e sequenciais. O processo na cabeça da fila tem direito de acesso. Em caso de falhas, o processo é removido da cabeça da fila.
\end{frame}

\begin{frame}{Compartilhamento de Parâmetros de Configuração}
Pronto!
\end{frame}

\begin{frame}{Receitas}
\begin{itemize}
	\item Lock distribuído
	\item Filas, e.g. de prioridades
	\item Barreira
	\item Serviço de nomes
	\item Terminação em duas fases
	\item Contador atômico
\end{itemize}

\url{http://zookeeper.apache.org/doc/trunk/recipes.html}
\end{frame}

\begin{frame}{Curator}
\includegraphics{images/curator-logo}

Um livro de receitas implementadas em ZK.

\url{http://curator.apache.org}
\end{frame}

\begin{frame}{Lab}
	\begin{itemize}
		\item Crie um zNode /teste
		\item Debaixo de /teste, crie três outros, sequenciais
	\end{itemize}
\end{frame}

\begin{frame}{Lab}
\begin{itemize}
	\item Crie um zNode /teste2
	\item Crie um zNode efêmero
	\item Conecte-se com outro cliente
	\item Coloque um watch em /teste2
	\item Desconecte o primeiro cliente
	\item Observe o evento gerado no segundo cliente
	\item Reconecte o primeiro cliente
\end{itemize}
\end{frame}

\begin{frame}[fragile]{Multi-node}
Crie três arquivos, zoo1.cfg, zoo2.cfg e zoo3.cfg.\\Por exemplo, zoo1.cfg fica assim:
\begin{itemize}
	\item \verb|dataDir=/tmp/lasaro/zoo1| $\Leftarrow$ Diretórios distintos.
	\item \verb|server.1=zoo1:2888:3888| $\Leftarrow$ Portas distintas.
	\item \verb|server.2=zoo2:2889:3889|
	\item \verb|server.3=zoo3:2890:3890|
	\item \verb|clientPort=2181| $\Leftarrow$ Portas distintas.
	
\end{itemize}

Crie diretórios e arquivos de identificação.
\begin{itemize}
	\item \verb|mkdir /tmp/lasaro/zoo1|
	\item \verb|echo 1 > /tmp/lasaro/zoo1/myid|
\end{itemize}

Execute servidores.
\begin{itemize}
	\item \verb|./bin/zkServer.sh start conf/zoo1.cfg|
\end{itemize}

Use \verb|start-foreground| para acompanhar a execução.

\end{frame}

\begin{frame}{Lab}
\begin{itemize}
	\item Crie um znode /contador com valor 0
	\item Descreva como fazer para que os clientes incrementem atomicamente o valor de /contador.
\end{itemize}
\end{frame}
\subsection{Falhas Bizantinas}

\begin{frame}{Uma história de três exércitos -- Versão 2}
Exércitos estão às portas de Bizâncio, aka Constantinopla, aka Istambul.

Todos os exércitos tem que atacar em conjunto ou se retirar em conjunto.

Cada exército é comandado por um General. Alguns destes preferem atacar, enquanto outros preferem se retirar.

Alguns generais podem ter sido comprados, e mandar mensagens discrepantes para os outros, ou simplesmente não mandar mensagens.

Fonte: \href{http://research.microsoft.com/en-us/um/people/lamport/pubs/byz.pdf}{Lamport, L.; Shostak, R.; Pease, M. (1982). "The Byzantine Generals Problem" (PDF). ACM Transactions on Programming Languages and Systems. 4 (3): 382–401. doi:10.1145/357172.357176.}
\end{frame}

\begin{frame}{Generais e Tenentes}
Problema pode ser mudado para:
\begin{itemize}
	\item Comandante envia ordem.
	\item Todos os tenentes leais executam ordem recebida.
	\item Comandante pode ser traidor.
\end{itemize}
\end{frame}


\begin{frame}{Generais e Tenentes}
Suponha 3 exércitos. \\
Comandante (traidor) diz "Ataque!" Tenente A e "Retirada!" tenente B.\\
Ou \\
Comandante diz "Ataque!" a ambos. Tenente A segue a ordem mas B se retira.

\pause E se os tenentes trocarem informações?

\pause Como diferenciar casos em que Comandante ou Tenente é traidor?
\end{frame}




\begin{frame}{Generais e Tenentes}
Só há solução se mais de $\frac{2}{3}$ dos Generais/Tenentes são leais.
\end{frame}

%http://www.drdobbs.com/cpp/the-byzantine-generals-problem/206904396?pgno=5

\begin{frame}{Comunicação}
\begin{itemize}
	\item Toda mensagem enviada é entregue corretamente.
	\item A ausência de mensagem pode ser detectada (mensagem Null é entregue no lugar) (Sistema síncrono)
\end{itemize}
\end{frame}


\begin{frame}{4/0}
General manda ordens.

Ausência de ordem = Retirada

Tenente repassa ordens

Maioria de comandos é comando a ser seguido
\end{frame}


\begin{frame}{4/0}
General manda ordens.

Ausência de ordem = Retirada

Tenente repassa ordens

Maioria de comandos é comando a ser seguido
\end{frame}


\begin{frame}{Comunicação}
\begin{itemize}
	\item Toda mensagem enviada é entregue corretamente.
	\item Toda mensagem é assinada.
	\item A ausência de mensagem pode ser detectada (mensagem Null é entregue no lugar) (Sistema síncrono)
\end{itemize}

\pause É possível detectar inconsistências e processos bizantinos.
\end{frame}


%http://cs.brown.edu/courses/cs138/s16/lectures/19consen-notes.pdf
\section{Outros tópicos}

%TODO \subsection{Detectores de Falhas}




\subsection{Reconfiguração}

\begin{frame}{Reconfiguração da Aplicação}
Na segunda entrega do projeto, você distribuiu a carga do seu banco de dados entre vários nós. Caso um nó falhe, parte dos seus dados será perdida.

Para corrigir esta deficiência, na terceira entrega, cada nó será replicado em três vias e, assim, caso um nó falhe, outros dois continuarão a manter o dado.

\end{frame}

\begin{frame}{Reconfiguração da Aplicação}
Ainda assim, há problemas. E se mais de um, de um mesmo conjunto de réplicas, falhar? 

\pause Embora seja pequena a probabilidade de dois nós de um mesmo grupo falharem em instantes próximos, dado tempo suficiente, qualquer evento com probabilidade diferente de 0 acontecerá.

\pause Precisamos de uma forma de trocar nós da aplicação que falharam por novos nós. 

Este é problema denominado Pertinência de Grupo ou \emph{Group Membership}
\end{frame}


\begin{frame}{Group Membership}
Para não correr o risco, retire o processo falhos do grupo e coloque outro no lugar!

I.e., mude a visão que o sistema de quem é o grupo.
\end{frame}



\begin{frame}{Visões}
\includegraphics[width=.7\textwidth]{images/vc}

Fonte: \href{https://www.cs.rutgers.edu/~pxk/417/notes/virtual_synchrony.html}{Paul Krzyzanowski}

$G$ é o grupo de processos participando do sistema, é a Visão do Sistema.
\end{frame}

Inicialmente, $G$ consiste de apenas o processo $p$, como o processo que cria o cluster no Atomix. Na sequência, outros processo vão se unindo ao grupo através de View Changes. Uma vez que $p$ e $q$ estão no grupo, inicia-se a comunicação entre eles. Quando $r, s$ e $t$ aparecem, também entram no grupo por meio de uma nova visão.

Finalmente, quando ambos $p$ e $q$ falham, os outros processo os excluem da visão, e continuam funcionando normalmente.


\begin{frame}{Impossibilidade de Detecção de Falhas}
Em um sistema distribuído assíncrono, é impossível distinguir com toda certeza um processo falho (parou de funcionar) de um que está lento.

\pause Como decidir se mudar ou não de visão?
\end{frame}

Ou aceita a imprecisão e muda quando suspeitar de uma falha, ou corre o risco de ficar esperando \emph{ad eternum} e não mudar, mesmo quando uma falha aconteceu.

\begin{frame}{Uma ``solução''!}
Quando suspeitar de falha, reporte suspeita a outros processos, que também passarão a suspeitar.

Tome decisão baseado na suspeita, isto é, troque de visão quando houver suspeita.

Pague o preço de uma suspeita errada, isto é, quando um processo for removido da visão indevidamente, adicione-o novamente.
\end{frame}


\begin{frame}{Sincronismos Virtual}
Gerenciamento de Grupo/Group Membership e Comunicação em Grupo
\begin{itemize}
	\item Processos se unem ao grupo
	\item Processos saem do grupo
	\item Processos enviam mensagens para o grupo
	\item Diferentes ordenações
		\begin{itemize}
			\item Atomic Multicast
		\end{itemize}
\end{itemize}
\end{frame}

\begin{frame}{Visão de Grupo}
\begin{itemize}
	\item Visão: conjunto de processos no sistema.
	\item Multicast feito para processos na visão.
	\item Visão é consistente entre os processos.
	\item Entrada e saída de processos muda a visão.
\end{itemize}
\end{frame}

\begin{frame}{Eventos}
\begin{itemize}
	\item Mensagem
	\item Mudança de Visão
	\item Checkpoint
\end{itemize}
\end{frame}

\begin{frame}{Visões}
\includegraphics[width=.7\textwidth]{images/vc}

Fonte: \href{https://www.cs.rutgers.edu/~pxk/417/notes/virtual_synchrony.html}{Paul Krzyzanowski}
\end{frame}

\begin{frame}{Sincronismo Virtual}
Deve satisfazer
\begin{itemize}
	\item Se uma mensagem é enviada em uma visão, ela só pode ser entregue naquela visão.
	\item Se uma mensagem é entregue a um processo correto em uma visão, então é entregue a todos os processos corretos naquela visão.
	\item Se um processo não recebe a mensagem, ele não estará na próxima visão.
	\item Ao entrar em uma visão, o processo recebe o estado dos outros processos e seu estado se torna equivalente ao de um processo que recebeu todas as mensagens já entregues.
\end{itemize}

A troca de Visão é uma barreira.
\end{frame}

\begin{frame}{ISIS Toolkit}
	Sistema de Sincronismo Virtual tolerante a falhas desenvolvido por Ken Birman, Cornell University (\url{http://www.cs.cornell.edu/Info/Projects/Isis/})\\
	ISIS: An Environment for Constructing Fault-Tolerant Distributed Systems. Kenneth Birman, D. Skeen, A. El Abbadi, W. C. Dietrich and T. Raeuchle. May 1983.
	
\begin{itemize}
	\item 100.000's/s
	\item Em uso até 2009
	\item NY Stock Exchange
	\item Swiss Exchange
	\item US Navy
	\item Precursos de sistemas como Zookeeker
	\item Totem, ISIS, Horus, Transis (Partições), \alert{Spread}, \alert{Ensamble}, \alert{JGroups}, Appia, QuickSilver, vSynch (née ISIS 2)
\end{itemize}
\end{frame}

\begin{frame}{Difusão Totalmente Ordenada}
\begin{itemize}
	\item Corretude: Se um processo $p$ envia uma mensagem $m$ para processos no grupo $G$, então se $p$ não falha, todos os processos corretos em $G$ recebem a mensagem.
	
	\item Acordo: Se um processo correto em $G$ recebe uma mensagem $m$, então todo processo correto em $G$ recebe $m$
	
	\item Ordenação: Se um processo recebe mensagem $m$ e depois $n$, então qualquer processo que receba a mensagem $n$ deve primeiro receber $m$
	
	\item Validade: Somente mensagens difundidas são entregues.
\end{itemize}

E se mandarmos mensagens do tipo ``A partir da entrega desta mensagem, o grupo de processos é $G$.''
\end{frame}

\begin{frame}{Sincronismo Virtual}
Deve satisfazer
\begin{itemize}
	\item Se uma mensagem é enviada em uma visão, ela só pode ser entregue naquela visão.\\
	Mensagens de troca de visão podem incrementar um contador\\
	Mensagens normais carregam o valor atual do contador\\
	Mensagem descartada se valor na mensagem é maior contador no destinatário
	
	\item Se uma mensagem é entregue a um processo correto em uma visão, então é entregue a todos os processos corretos naquela visão.\\
	Pela difusão, se a mensagem de troca for entregue para um processo, será entregue para todos os corretos, na mesma ordem
	Se mensagem comum for entregue antes para algum, será entregue ante para todos.
	
	\item Se um processo não recebe a mensagem, ele não estará na próxima visão.\\
	Se um processo não recebe uma mensagem comum que foi entregue pelos outros, então ele não troca de visão.

	\item Ao entrar em uma visão, o processo recebe o estado dos outros processos e seu estado se torna equivalente ao de um processo que recebeu todas as mensagens já entregues.\\
	Caso contrário, não haveria porquê trocar os processos
\end{itemize}
\end{frame}

\begin{frame}{State Transfer}
\includegraphics[width=.7\textwidth]{images/state_transfer}


\href{http://www.gsd.inesc-id.pt/~ler/docencia/tfd0405/bib/BSRNA.pdf}{Building Secure and Reliable Network Applications}
\end{frame}


\begin{frame}{Difusão Atômica $\equiv$ Sincronismo Virtual?}
Seria uma boa aproximação, mas que poderia ser relaxada. 

Em certas aplicações, FIFO ou Causal seriam suficientes dentro da visão, desde que a mensagem de mudança da visão seja totalmente ordenada com as comuns.
\end{frame}


\begin{frame}{Particionamento}
E se dois subconjuntos mutuamente exclusivos se formarem e criarem visões independentes?

\pause \emph{Primary Partition Model} -- Somente a partição primária pode mudar de visão.

\pause Lembram-se que no Raft somente uma partição com uma maioria de processo pode decidir? É exatamente a mesma situação, pois os processos estão chegando a um Consenso sobre quem é a nova visão.
\end{frame}


\begin{frame}{Extended Virtual Synchrony}
\emph{Primary Partition Model} -- Não é adequado a uma rede geograficamente distribuída (Internet scale).

\pause Lembram-se que no Raft somente uma partição com uma maioria de processo pode decidir? É exatamente a mesma situação, pois os processos estão chegando a um Consenso sobre quem é a nova visão.
\end{frame}

É possível que no trabalho dois, alguns de vocês tenham tentado gerar locks do sistema para manipular objetos distribuídos no sistema. Esse locks são perigosos por quê processos pode travar/quebrar/falhar e nunca liberarem os locks. O uso de um algoritmo VS poderia ser usado para resolver o problema.\right 




[Swim](https://asafdav2.github.io/2017/swim-protocol/)



[Lista de Falhas Reais](https://github.com/danluu/post-mortems)
