---
layout: default
title: Por quê?
parent: Introdução
nav_order: 1
---


Todos estamos a par de que aplicações importantes nos dias de hoje são aplicações distribuídas rodando em grandes *data centers* com [milhares de máquinas](https://youtu.be/D77WDo881Pc).
Alguns exemplos óbvios são 
* [Aamazon](https://www.amazon.com), 
* [Facebook](https://www.facebook.com), e 
* [GMail](https://www.gmail.com).
Mas as razões que levam a este cenário são válidas para diversas outras aplicações.
De fato, praticamente qualquer sistema de informação que precisa atingir um público considerável, necessitará aplicar técnicas de computação distribuída para conseguir **escalar**, isto é, "ser grande", seja no número de clientes que atende (computacionais ou humanos), seja em sua área de cobertura, ou na qualidade do serviço que presta, mesmo que não cheguem a estas escalas. 

Este último ponto, sobre qualidade do serviço, tem a ver com a capacidade de um sistema se manter no ar a despeito de problemas, isto é, de ser tolerante a falhas. 
Tolerância a falhas implica em redundância, em cópias, o que fatidicamente implica em **distribuição** e em **Sistemas Distribuídos**.

De fato, há quem diga que [somos todos desenvolvedores de sistemas distribuídos agora](https://devclass.com/2019/08/16/pivotal-cto-kubernetes-means-were-all-distributed-systems-programmers-now/)


---
---
Em conclusão, as principais razões para se desenvolver sistemas distribuídos são duas, ambas resultantes da **agregação** (correta) do poder computacional de múltiplas máquinas:
* escalabilidade e
* tolerância a falhas.

---
---
