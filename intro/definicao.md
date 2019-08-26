# Sistemas Distribuídos

Infelizmente, programar sistemas distribuídos é muito mais complexo que sistemas centralizados.
De fato, quando começou, você aprendeu que programar era basicamente colar blocos Lego, que se encaixavam perfeitamente.
Bastava importar a biblioteca correta e invocar suas funções.

---
##### O Sonho

![Lego Rainbow](./images/lego0.jpg)

---

Sistemas maiores, contudo, aumentam consideravelmente a complexidade.
Mesmo que haja diferentes bibliotecas a serem usadas, com desenvolvedores e estilos diferentes, bem ou mal testadas, seus encaixes ainda fazem sentido.

---
##### A realidade
![Lego Hell](images/lego3.jpg)

---

Quando partimos para os sistemas distribuídos, a dificuldade aumenta ainda mais.
Agora temos peças que nunca foram pensadas para serem encaixadas e um pouco de *crazy glue* é necessária para encaixá-las.
Além disso, às vezes times diferentes montam partes diferentes do sistema, tendo visões diferentes do que deve fazer e de como as partes se comunicam.

---
##### O que vocês encontrarão aqui.

![Lego SD](images/lego4.jpg)
![Lego SD](images/lego5.jpg)

----

Programar distribuído é como programar com blocos de fabricante diferente, juntando com arame e cola, e com seu irmãozinho passando para lá e para cá, chutando as peças, mordendo algumas, enquanto seus pais brigam com você pq não arrumou o quarto.

Mas esta é a realidade da programação distribuída, por quê fazê-lo?

---

## Por quê?

---

O fato é que computadores individuais tem capacidade limitada de processamento e armazenamento, mas nossa necessidade de poder computacional cresce exponencialmente.

---
##### Crescimento da informação

![Data Growth](images/datagrowth.jpg)

---

Assim, precisamos crescer nosso poder computacional, mas aumentar a capacidade de um dispositivo mesmo de forma linear tem custo exponencial.

---
##### Custo de se "incrementar" seu computador.

![Custo de melhoria](images/exponential.jpg)

---

O que nos resta então é agregar o poder computacional de diversos computadores "baratos" para satisfazer nossas necessidades.
O remédio, contudo, é bem amargo: com muitos computadores conectados, vem a necessidade de coordená-los, de forma a agir de forma coerente, mesmo quando alguns deles falhe, e quanto mais computadores, maior é a probabilidade de que pelo menos um deles tenha uma CPU, disco, fonte, ou que quer que seja, falhando.

---

Computadores [falham](https://www.statista.com/statistics/430769/annual-failure-rates-of-servers/) o tempo todo!

---

Nós precisamos então entender este ambiente,
* qual a probabilidade de um nó falhar
* como os computadores, ou melhor, como os processos se comunicam? mensagens podem ser perdidas, atrasadas, corrompidas?
* os relógios dos computadores são sincronizados?
* quais os padrões de acesso ao serviços? Aumenta à noite? Diminui no verão?
* há agentes maliciosos que possam querer perturbar o sistema?

---
##### Modelos

* Comunicação
* Sincronismo
* Falhas

---

Definido o modelo computacional, podemos distribuir nosso sistema, isto é, dividir a computação/armazenamento em diversas máquinas, e coordenar suas ações para que sejam consistentes com a especificação, de forma a minimizar o tempo que o serviço fica fora do ar, entregando o serviço de acordo com expectativas especificadas. Para isto, precisamos entender 
* como falhas (bugs, por exemplo) afetam a execução de forma a 
* evitar que a falha de algum componente possa levar o sistema a parar como um todo,
* e a garantir que clientes em qualquer lugar do mundo tenham a mesma facilidade em acessar o serviço.

---
##### Expectativa

* Tolerância a falhas
* Desempenho
* Consistência

---

Vejamos algumas exemplos de tarefas executadas por sistemas distribuídos, que você usa hoje.

---
##### Exemplos

* Entregue este email para fulano@knowhere.uni.
* Envie o item X para este endereço, após cobrança de Y dinheiros da conta Z.
* Em um ambiente de simulação de batalhas em 3D, simule o disparo de um projétil nesta direção e sentido, com velocidade v, enquanto movimenta o avatar A para a esquerda.
* Autorize a transferência de X dinheiros da conta C para a conta C'.
* Movimente o braço mecânico que está segurando um bisturi, 3cm à direita, então abaixe-o 3mm, e movimente-o 4cm para a esquerda
* Inclua o comentário ``LOL!!!'' na lista de comentários do item XYZ, com marca de tempo T
* Leia o valor do sensor de temperatura S e, caso seu valor supere V, emita alarme luminoso vermelho intermitente e alarme sonoro

---

Um sistema distribuído implica em algum tipo de colaboração entre componentes, para permitir que recursos de um sejam usados por outro. Por exemplo, capacidade de armazenamento, de processamento, ou conexão física com uma impressora.

---
##### Compartilhar

* memória
* CPU
* impressora
* canal de comunicação
* localização

---

## Definição

Assim, uma possível definição de Sistema Distribuído, que me agrada, é a seguinte.

---
##### Sistema Distribuído

Coleção de sistemas computacionais (software ou hardware), independentes mas com alguma forma de comunicação, que colaboram na execução de alguma tarefa.

---

Uma mais realista talvez seja a de Leslie Lamport, que certa vez disse

> A distributed system is one in which the failure of a computer you didn't even know existed can render your own computer unusable.