# Prefácio

## Por quê ler estas notas?

???+ sideslide "Computação distribuída"
    * Área extremamente ativa
    * Diferencial

As áreas ligadas ao desenvolvimentos de sistemas computacionais, como Ciência e Engenharia de Computação e Sistemas de Informação, estão extremamente em voga e tem atraído mais e mais profissionais, mais ou menos qualificados, tornando este **mercado cada vez mais competitivo**.
Dentro destas grandes áreas, o desenvolvimento de **sistemas distribuídos** é um dos tópicos mais "quentes" e ter conhecimentos específicos desta subárea pode ser uma excelente vantagem e forma de se destacar de seus colegas e competidores.

???+ sideslide "Fundamental a outras áreas"
    * Aprendizado de máquina
    * Ciência de dados
    * Computação gráfica

Se estiver se perguntando do que estou falando, sobre como posso dizer que é quente uma área sobre a qual talvez você nunca tenha ouvido falar, ao contrário de áreas como [**aprendizado de máquina**]() e [**ciência de dados**](), então deixe-me explicar o que quero dizer. Sem o desenvolvimento da teoria da **computação distribuída**, na forma do estudo de algoritmos e técnicas de implementação, e sua colocação em prática, na forma do desenvolvimento de sistemas distribuídos, nenhum desenvolvimento sério destas outras áreas, sedentas por desempenho, escalaria para problemas reais.
Veja, por exemplo, a seguinte descrição dos *skills* necessários para se atuar como [cientista de dados](https://www.quora.com/What-skills-are-expected-from-a-data-engineer-not-a-data-scientist) ou como engenheiro de software no [Facebook](https://www.facebook.com/facebookcareers/videos/1747855735501113/).
É fato que aplicações distribuídos já são parte inexpurgável da infraestrutura computacional que usamos para resolver os mais diversos problemas.

???+ sideslide ""
    * A teoria por baixo dos *frameworks* que **já** usam

Assim, respondendo à pergunta acima, entendo que ler estas notas lhe permitirá mergulhar rapidamente no coração da computação distribuída, para entender os fundamentos de como as grandes infra-estruturas computacionais que usamos hoje funcionam, muito além das anotações do [Springboot]() e dos clientes de bancos de dados.
Isso, de uma forma muito direta e mais simples de digerir quer as fontes onde me baseei para escrevê-las, além de usar diversos materiais disponíveis mais recentes que a bibliografia básica.


## Estrutura

???+ sideslide "Por quê?"
    * Visão geral
    * Teoria
    * Prática
    * Cenário atual


Neste curso apesentaremos uma visão geral do que são sistemas distribuídos, porquês técnicos para os desenvolvermos e como fazê-lo, com uma forte componente prática, por meio do desenvolvimento de um projeto com (um dos) pés na realidade.
Faremos isso começando por uma revisão de conceitos de redes de computadores e sistemas operacionais enquanto falamos sobre a arquitetura mais fundamental de computação distribuída, Cliente/Servidor, e de como é usada para implementar um proto banco de dados distribuído, uma Tabela de Espalhamento Distribuída em memória.
À medida em que apresentamos problemas com o modelo assumido inicialmente e com nossa implementação inicial, buscaremos por soluções enquanto introduzimos novas abstrações, mais poderosas e mais complexas.
Ao final desta jornada, teremos fundamentado a construção de uma Tabela de Espalhamento Distribuído com particionamento de dados entre nós, usando protocolos par-a-par e replicação de máquinas de estados.
Em paralelo, teremos estudado diversos *frameworks* de computação distribuída atuais, como modelo ou bloco de construção para a resolução de nossos problemas.
Em resumo, durante este curso você irá:

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

???todo "TODO"
      Estruturar melhor esta seção uma vez que a estrutura do documento tenha estabilizado mais.


## Convenções

Neste documento, usamos diversos recursos visuais com diferentes propósitos.

* *itálico* indica termos em outras línguas, como *framework* ou *middleware*. Alguns termos, contudo, são tão corriqueiramente usados que me escapam quando escrevendo e acabam não grafados corretamente.
* **negrito** indica a introdução de termos e conceitos importantes, como **escalabilidade** e **falha**.
* Apontadores indicam um sítio relacionado ao termo, por exemplo, como criar um repositório no [Github](http://github.com), e cuja leitura é sugerida ao final da aula.
* Notas de rodapé, indicam uma observação importante sobre o que está sendo apresentado, cuja leitura é sugerida ao final do parágrafo.[^foot] Estas notas incluem referenciais teóricos importantes, com detalhes da publicação e apontadores para onde a publicação pode ser lida, por exemplo, para o livro Distributed Systems: Principles and Paradigms[^dspp] no qual estas notas são fortemente baseadas; este uso deverá ser migrado para uma forma mais canônica de referências.
* Imagens não autorais são também apontadores para onde são encontradas e tem como texto alternativo as informações da autoria.
* Caixas alinhadas à esquerda são usadas para várias finalidades. Por exemplo, para apresentar exercícios, destacar especificações, apontar tarefas a serem executas por mim... Os diversos usos são indicados nos ícones e cores das caixas.
    
    !!!exercise "Exercício"
        Isso é um exercício!

???+ sideslide "Resumo"
    * Elementos visuais

* Caixas alinhadas à direita podem ser vistas como um sumário executivo do que está sendo apresentado no texto próximo.

[^foot]: Exemplo de nota de rodapé.


???todo "TODO"
    * Diferenciar os usos de negrito,
    * Ativar plugin bibtex
    * Diferenciar caixas


## Agradecimentos

Agradeço ao Prof. Paulo R. S. L. Coelho pelas diversas contribuições feitas a este texto.
Agradeço também aos diversos alunos estão sempre, gentilmente, apresentando oportunidades de melhorias. Caso queira sugerir correções, faça um *pull request* a apontando a correção no *branch* main, a partir do qual eu atualizarei o HTML.

???todo "TODO"
    * Adicionar guia de sugestões.

## Referencial
Estas notas, em sua forma atual, são fortemente baseadas no livro Distributed Systems: Principles and Paradigms[^dspp], mas também em alguns materiais mais recentes disponíveis livremente na Internet.

[^dspp]: [Distributed Systems: Principles and Paradigms](https://www.amazon.com.br/Distributed-Systems-Principles-Andrew-Tanenbaum/dp/153028175X)