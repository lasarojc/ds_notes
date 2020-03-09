---
layout: home
title: Prólogo
nav_order: 0
---

# Prólogo

Escrever "bons" sistemas distribuídos é uma tarefa que esbarra em diversos obstáculos, cujo não menos importante é a definição de "bom", o que é extremamente subjetivo. 
Aqui diremos que um sistema é **bom** se está sempre **no ar** (despeito de falhas), com bom **desempenho** (boa experiência de usuário) e com **baixo custo** (usa o mínimo de recursos necessários ao trabalho). Enquanto ainda subjetiva, nossa definção já nos permite estabelecer um pano de fundo para delinear as dificuldades de se implementar tais sistemas.

Na prática, as barreiras vão desde o uso de premissas inválidas, como confiabilidade e custo zero da transmissão de dados via Internet, ao não entendimento de conceitos fundamentais de programação concorrente, da falta de *frameworks* para resolver certos problemas, ao excesso de opções para outros.
Do ponto de vista teórico, a complexidade vai da dificuldade de abstração de problemas ao nível de abstração de problemas bem definidos, da impossibilidade de se resolver problemas aparentemente simples à realização de que com o uso da abstração correta se consegue resolver facilmente problemas complexos.
Para ajudá-lo a navegar neste mar de dificuldades, este conjunto de notas de aulas está sendo organizado para apresentar gradualmente o desenvolvimento de sistemas distribuídos ao estudante.

Iniciamos com um trabalho de convencimento de que tais aplicações são importantes por já serem parte inexpurgável da infraestrutura computacional que usamos.
Revisamos então conceitos de redes de computadores e sistemas operacionais enquanto falamos sobre a arquitetura mais fundamental de computação distribuída, Cliente/Servidor, e de como é usada para implementar um proto banco de dados distribuído, uma Tabela de Espalhamento Distribuída em memória.
À medida em que apresentamos problemas com o modelo assumido inicialmente e com nossa implementação inicial, buscaremos por soluções enquanto introduzimos novas abstrações, mais poderosas e mais complexas.
Ao final desta jornada, teremos fundamentado a construção de uma Tabela de Espalhamento Distribuído com particionamento de dados entre nós, usando protocolos par-a-par e replicação de máquinas de estados.
Em paralelo, teremos estudado diversos *frameworks* de computação distribuída atuais, como modelo ou bloco de construção para a resolução de nossos problemas.

Estas notas, em sua forma atual, são fortemente baseadas em uma literatura já antiquada, como a segunda edição do livro [Distributed Systems: Principles and Paradigms](https://www.amazon.com.br/Distributed-Systems-Principles-Andrew-Tanenbaum/dp/153028175X), de Andrew Tanenbaum, mas também em alguns materiais mais recentes disponíveis livremente na Internet.
Em futuras iterações, as notas serão atualizadas para conter cada vez mais material autoral e referências mais atuais; fique ligado.


!!! note "Atenção"
    Para navegar no material, utilize o menu à esquerda ou os botões abaixo.
