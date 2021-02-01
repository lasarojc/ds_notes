# Prefácio

## Por quê ler estas notas?


???sideslide "Por quê?"
    * Competitividade
    * Área extremamente ativa

As áreas ligadas ao desenvolvimentos de sistemas computacionais, como Ciência e Engenharia de Computação e Sistemas de Informação, estão extremamente em voga e tem atraído mais e mais profissionais, mais ou menos qualificados, tornando este **mercado cada vez mais competitivo**.
Dentro destas grandes áreas, o desenvolvimento de **sistemas distribuídos** é um dos tópicos mais quentes e ter conhecimentos específicos desta subárea pode ser uma excelente vantagem e forma de se destacar de seus colegas e competidores.
Se estiver pensando que sou louco, que uma área sobre a qual talvez você nunca tenha ouvido falar até agora não pode ser mais quente que áreas como
[**aprendizado de máquina**]() e [**ciência de dados**](), então deixe-me explicar o que quero dizer. Sem o desenvolvimento da teoria da **computação distribuída**, na forma do estudo de algoritmos e técnicas de implementação, e sua colocação em prática, na forma do desenvolvimento de sistemas distribuídos, nenhum desenvolvimento sério destas outras áreas, sedentas por desempenho, escalaria para problemas reais.
Veja, por exemplo, a seguinte descrição dos *skills* necessários para se atuar como [cientista de dados](https://www.quora.com/What-skills-are-expected-from-a-data-engineer-not-a-data-scientist) ou como engenheiro de software no [Facebook](https://www.facebook.com/facebookcareers/videos/1747855735501113/).
É fato que aplicações distribuídos são importantes por já serem parte inexpurgável da infraestrutura computacional que usamos para resolver os mais diversos problemas.

Assim, respondendo à pergunta acima, entendo que ler estas notas lhe permitirá mergulhar rapidamente no coração da computação distribuída, para entender os fundamentos de como as grandes infra-estruturas computacionais que usamos hoje funcionam, muito além das anotações do [Springboot]() e dos clientes de bancos de dados.

Para os alunos de GBC074 e GSI028, pelo menos enquanto eu for o professor destas disciplinas, outra razão para ler estas notas é que elas são muito mais simples de digerir quer as fontes onde me baseei para escrevê-las.


## Estrutura

Neste curso apesentaremos uma visão geral do que são sistemas distribuídos, porquês técnicos para os desenvolvermos e como fazê-lo, com uma forte componente prática, por meio do desenvolvimento de um projeto com (um dos) pés na realidade.
Faremos isso começando por uma revisão de conceitos de redes de computadores e sistemas operacionais enquanto falamos sobre a arquitetura mais fundamental de computação distribuída, Cliente/Servidor, e de como é usada para implementar um proto banco de dados distribuído, uma Tabela de Espalhamento Distribuída em memória.
À medida em que apresentamos problemas com o modelo assumido inicialmente e com nossa implementação inicial, buscaremos por soluções enquanto introduzimos novas abstrações, mais poderosas e mais complexas.
Ao final desta jornada, teremos fundamentado a construção de uma Tabela de Espalhamento Distribuído com particionamento de dados entre nós, usando protocolos par-a-par e replicação de máquinas de estados.
Em paralelo, teremos estudado diversos *frameworks* de computação distribuída atuais, como modelo ou bloco de construção para a resolução de nossos problemas.


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

???todo "TODO"
      Estruturar melhor esta seção uma vez que a estrutura do documento tenha estabilizado mais.

Para navegar no material, utilize o menu à esquerda para Capítulos, à direita para seções, ou os botões abaixo para Anterior e Próximo.



## Convenções

???todo "TODO"
    * Tipografia: Clarificar uso de diferentes fontes.
    * Caixas: Clarificar uso de caixas como esta.
    * Bibliografia
         * Gerar lista de material referenciado no final página em vez de em links embutidos.
         * Referências são apontadas em links nas notas de rodapé. Referências para imagens são dadas como links nas próprias imagens.


## Agradecimentos

Gostaria de agradecer ao Prof. Paulo R. S. L. Coelho pelas diversas contribuições feitas a este texto, bem como aos alunos que estão sempre, gentilmente, apresentando oportunidades de melhorias.



## Referencial
Estas notas, em sua forma atual, são fortemente baseadas no livro Distributed Systems: Principles and Paradigms[^dspp], mas também em alguns materiais mais recentes disponíveis livremente na Internet.

[^dspp]: [Distributed Systems: Principles and Paradigms](https://www.amazon.com.br/Distributed-Systems-Principles-Andrew-Tanenbaum/dp/153028175X)
