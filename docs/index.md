# Prólogo

As áreas ligadas ao desenvolvimentos de sistemas computacionais, como Ciência e Engenharia de Computação e Sistemas de Informação, estão extremamente em voga e tem atraído mais e mais profissionais, mais ou menos qualificados, tornando este **mercado cada vez mais competitivo**.

Ter conhecimentos específicos da subárea de desenvolvimento de **sistemas distribuídos** pode ser uma excelente vantagem e forma de se destacar de seus colegas e competidores, mesmo que você nunca tenha ouvido falar em sistemas distribuídos até ter que se matricular nesta disciplina.
Sendo sincero, o desenvolvimento da teoria da **computação distribuída**, na forma do estudo de algoritmos e técnicas de implementação, e sua colocação em prática, na forma do desenvolvimento de sistemas distribuídos, não é tão "quente" como outras áreas, por exemplo inteligência artificial e ciência de dados.
Mas acontece que sem a computação distribuída, nenhum desenvolvimento sério destas outras áreas, sedentas por desempenho, escalaria para problemas reais.
Veja por exemplo a seguinte descrição dos *skills* necessários para atuar como [cientista de dados](https://www.quora.com/What-skills-are-expected-from-a-data-engineer-not-a-data-scientist) ou como engenheiro no [Facebook](https://www.facebook.com/facebookcareers/videos/1747855735501113/).
É fato que aplicações distribuídos são importantes por já serem parte inexpurgável da infraestrutura computacional que usamos.

Se já estiver convencido de que esta é uma área importante, ótimo! 
Se não estiver, só te peço que dê uma chance ao tópico e leia mais um pouco, pois neste curso apesentaremos uma visão geral do que são sistemas distribuídos, porquês técnicos para os desenvolvermos e como fazê-lo, com uma forte componente prática, por meio do desenvolvimento de um projeto com (um dos) pés na realidade, ou seja, só coisas super divertidas.
Como exatamente faremos isso? Começaremos por uma revisão de conceitos de redes de computadores e sistemas operacionais enquanto falamos sobre a arquitetura mais fundamental de computação distribuída, Cliente/Servidor, e de como é usada para implementar um proto banco de dados distribuído, uma Tabela de Espalhamento Distribuída em memória.
À medida em que apresentamos problemas com o modelo assumido inicialmente e com nossa implementação inicial, buscaremos por soluções enquanto introduzimos novas abstrações, mais poderosas e mais complexas.
Ao final desta jornada, teremos fundamentado a construção de uma Tabela de Espalhamento Distribuído com particionamento de dados entre nós, usando protocolos par-a-par e replicação de máquinas de estados.
Em paralelo, teremos estudado diversos *frameworks* de computação distribuída atuais, como modelo ou bloco de construção para a resolução de nossos problemas.

Se você ainda não está convencido e não percebeu que computação distribuída é mágia, bem, você não tem muita escolha, não é mesmo? 
Então tente aproveitar esta visão geral para praticar um pouco de [programação neuro-liguística](https://pt.wikipedia.org/wiki/Programa%C3%A7%C3%A3o_neurolingu%C3%ADstica) e repita o seguinte mantra: heeeeeeeuuuuummmmmm amo computação distribuída.

Brincadeiras a parte, você desenvolverá um projeto em várias etapas que lhe permitirá exercitar os conceitos vistos aqui e que te levará a:

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

Mãos a obra!

!!!note "Sobre estas notas de aula"
    Estas notas, em sua forma atual, são fortemente baseadas no livro Distributed Systems: Principles and Paradigms[^dspp], mas também em alguns materiais mais recentes disponíveis livremente na Internet.
Referências são apontadas em links nas notas de rodapé. Referências para imagens são dadas como links nas próprias imagens.

!!!note "Atenção!"
    Para navegar no material, utilize o menu à esquerda para Capítulos, à direita para seções, ou os botões abaixo para Anterior e Próximo.

???todo "TODO"
    * Tipografia: Clarificar uso de diferentes fontes.
    * Caixas: Clarificar uso de caixas como esta.
    * Gerar lista de material referenciado no final página em vez de em links embutidos.

[^dspp]: [Distributed Systems: Principles and Paradigms](https://www.amazon.com.br/Distributed-Systems-Principles-Andrew-Tanenbaum/dp/153028175X)
