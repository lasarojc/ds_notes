---
layout: default
title: Tolerância a Falhas
nav_order: 6
has_children: true
---

Neste capítulo discutiremos o quê são sistemas distribuídos, por quê os desenvolvemos, e damos uma visão geral de como isto é feito.
---
layout: default
title: Dependabilidade
parent: Tolerância a Falhas
nav_order: 1
---

# Dependabilidade

Ao escrevermos nossos softwares, queremos que sejam usados para resolver problemas, mesmo que importância do problema esteja em um espectro bem vasto, indo, por exemplo, da execução de um cirurgia ocular remota, ao controle de uma usina hidrelétrica, à jogar truco contra um computador. Independentemente do problema sendo resolvido, gostaríamos de poder contar com o sistema, de poder depender nele para executar sua tarefa.
Desta situação, surge a ideia de dependabilidade, isto é, de um sistema ter a propriedade de que podemos depender do mesmo.

Em computação distribuída, componentes dependem uns dos outros para a realização de tarefas. Assim, componentes que quer ser "dependáveis" (do inglês, *dependable*), pois se não o forem, os demais componentes não poderão executar suas tarefas, rendendo o sistema como um todo inútil.

---
##### Definição

Assim, dizemos que

    um componente $C$ depende de um componente $C'$ se a corretude do comportamento de $C$ depende da corretude do componente $C'$.

e que

    um componente é ``dependável'' (\emph{dependable}) na medida que outros podem depender dele.

---


De acordo com [Laprie et al](https://ieeexplore.ieee.org/document/1335465?arnumber=1335465), tem-se dependabilidade quando os seguintes atributos estão presentes.

---
##### Atributos

* Disponibilidade (*Availability*) - Prontidão para uso.
* Confiabilidade/Fiabilidade (*Reliability) - Continuidade do serviço.
* Manutenabilidade (*Maintainability*) - Facilidade de reparo.
* Segurança (*Safety*) - Tolerância a catástrofes.
* Integridade (*Integrity*) - Tolerância a modificações.
* Confidencialidade (*Confidentiality) - Informação somente a quem devido.

A combinção das três últimas propriedades é também chamadas de Segurança (*Security*).

---

Como obstáculos para se conseguir estes atributos estão os seguintes obstáculos, ou ameaças:

---
##### Ameaças

* Fault - Falha (Falta): bug -- \lstinline|<=| em vez de \lstinline|<| (pode nunca afetar a execução).
* Error - Erro  (Erro): manifestação do bug -- iteração passa do ponto. (Pode não ser observável pelo usuário.)
* Failure - Defeito (Falha): problema visível -- tela azul

---
layout: default
title: Modelos 
parent: Tolerância a Falhas
nav_order: 2
---

