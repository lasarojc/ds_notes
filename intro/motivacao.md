# Motivação

A área de computação e sistemas distribuídos está extremamente em voga e pode ser uma excelente forma de se destacar de seus colegas e competidores.
"Como assim?", você pergunta, já que nunca ouviu falar desta área até ter que se matricular nesta disciplina.
Bem, o desenvolvimento da teoria da computação distribuída, na forma do estudo de algoritmos e técnicas de implementação, e sua colocação em prática, na forma do desenvolvimento de sistemas distribuídos, pode não ser tão falado como outras áreas quentes, e.g. inteligência artificial, ciência de dados e por aí vai.
Acontece que sem a computação distribuída, nenhuma desenvolvimento sério destas outras áreas, sedentas por desempenho, escalaria para problemas reais. Veja por exemplo a seguinte descrição para se trabalha como cientista de dados: [Data science/engineer](https://www.quora.com/What-skills-are-expected-from-a-data-engineer-not-a-data-scientist)

Esta necessidade de escalabilidade vai além destas áreas *mainstream*. 
De fato, há quem diga que [somos todos desenvolvedores de sistemas distribuídos agora](https://devclass.com/2019/08/16/pivotal-cto-kubernetes-means-were-all-distributed-systems-programmers-now/)
Praticamente qualquer sistema de informação que deseje atingir um público considerável, necessitará aplicar técnias de computação distribuída para conseguir **escalar**, isto é, "ser grande", seja no número de clientes que atende (computacionais ou humanos), seja em sua área de cobertura, ou na qualidade do serviço que presta.

Este último ponto, sobre qualidade do serviço, tem a ver com a capacidade que um sistema tem de se manter no ar a despeito de problemas, isto é, de ser tolerante a falhas. 
Tolerância a falhas implica em redundância, em cópias; redundância implica praticamente sempre em distribuição e distribuição implica em, bem, em **distribuição**, como em **Sistemas Distribuídos**.


Se o fato de praticamente todo sistema com o qual você se importa ser distribuído não é motivação o suficiente para você, futuro desenvolver de tecnologias, então eu não sei o que seria.
Talvez financeira? Então veja este vídeo sobre uma das carreiras do [Facebook](https://www.facebook.com/facebookcareers/videos/1747855735501113/)

---
##### Motivação

* Escalabilidade
* Capacidade agregada de processamento.
* Tolerância a falhas
* Carreira promissora
  * [Data science/engineer](https://www.quora.com/What-skills-are-expected-from-a-data-engineer-not-a-data-scientist)
  * [Facebook](https://www.facebook.com/facebookcareers/videos/1747855735501113/)
  * [Facebook](https://www.facebook.com/facebookcareers/videos/1747855735501113/)

---

Seja qual for a sua motivação, neste curso você terá a oportunidade
* de ter uma visão geral da computação distribuída, e visão geral é exatamente o que lhe faz começar a ser um bom "computeiro" (opinião minha)
* e aplicar o que aprendeu fazendo um projeto com (um dos) pés na realidade.

Sendo mais específico, você desenvolverá um projeto em várias etapas que lhe permitirá exercitar os conceitos vistos em aqui e que te levará a:
* programar processos que se comuniquem via redes de computadores;
* conhecer arquiteturas clássicas de sistemas distribuídos (e.g, cliente/servidor, p2p e híbrida), seus usos e limitações;
* escrever programas *multithreaded* simples e a entender como o uso de *multithreading* afeta os componentes de um sistema distribuído;
* entender a problemática da coordenação e do controle de concorrência em sistemas distribuídos;
* entender o uso de sistemas de nomeação em sistemas distribuídos bem como diversas formas de se implementar tais sistemas de nomeação;
* entender os conceitos básicos de replicação e tolerância a falhas;
* entender as implicações da dessincronização de relógios na coordenação, replicação e tolerância a falhas;
* projetar sistemas com componentes geograficamente distantes, fracamente acoplados;
* entender onde os diversos *middleware* podem ser usados para acoplar tais componentes;
* conhecer várias técnicas que controle de concorrência controlar o acesso a um recurso compartilhado;