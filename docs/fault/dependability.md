# Dependabilidade

Da necessidade de se poder depender de um sistema, surge a ideia de **dependabilidade**, isto é, de um sistema ter a propriedade de se poder depender do mesmo.

Um pouco mais formalmente, dizemos que um componente $C$ **depende de um componente** $C'$ se a corretude do comportamento de $C$ depende da corretude do componente $C'$ e dizemos também que um componente é "dependável" (*dependable*) na medida em que outros podem depender dele.
A dependabilidade é essencial aos componentes de sistemas distribuídos, pois como diz o ditado, uma corrente é tão forte quanto seu elo mais fraco.

Esta propriedade por ser dividida em outras propriedades mais "simples":[^avizienis]

* Disponibilidade (*Availability*) - Prontidão para uso.
* Confiabilidade/Fiabilidade (*Reliability*) - Continuidade do serviço.
* Segurança (*Safety*) - Tolerância a catástrofes.
* Integridade (*Integrity*) - Tolerância a modificações.
* Manutenabilidade (*Maintainability*) - Facilidade de reparo.

[^avizienis]: [Basic Concepts and Taxonomy of Dependable and Secure Computing](https://www.nasa.gov/pdf/636745main_day_3-algirdas_avizienis.pdf).

###### Disponibilidade
A **disponibilidade** (em inglês, ***availability***) especifica o quão pronto para uso o sistema está.

!!!quote "Disponibilidade"
    The term 'availability' means ensuring timely and reliable access to and use of information.
    
    [NIST SP 800-59](https://doi.org/10.6028/NIST.SP.800-59), no termo Availability [44 U.S.C., Sec. 3542 (b)(1)(C))](https://www.gpo.gov/fdsys/granule/USCODE-2011-title44/USCODE-2011-title44-chap35-subchapIII-sec3542)

Na prática, esta medida é dada como a percentagem de tempo que o sistema está disponível para uso e, embora qualquer percentagem seja válida, normalmente se usa valores muito próximos a 100% e descritos em termos de "noves".
Por exemplo, três 9 quer disponibilidade de 99,9% e cinco 9 quer dizer 99,999%.
A seguinte tabela resume o que significa na práticas os valores mais comuns de disponibilidade:[^daniels]

[^daniels]: [Availability Service Level 9’s And What they Equate To](https://web.archive.org/web/20180728204314/https://www.digitaldaniels.com/availability-service-level-9s-equate/)

!!!note inline end "Disponibilidade"
     A porcentagem do tempo que infraestrutura, sistema ou solução está operacional sob circunstâncias normais ou a probabilidade do sistema estar operacional em um certo instante.

| Disponibilidade (%) | "noves" |  *downtime* anual | *downtime* mensal | *downtime* semanal |
|---------------------|---------|-------------------|-------------------|--------------------|
|90                   | 1       | 36,5 dias         | 72 horas          | 16,8 horas         |
|99                   | 2       | 3,65 dias         | 7,2 horas         | 1,68 horas         |
|99,9                 | 3       | 8,76 horas        | 43,8 minutos      | 10.1 minutos       |
|99,99                | 4       | 52,56 minutos     | 4,38 minutos      | 1,01 minutos       |
|99,999               | 5       | 5,26 minutos      | 25,9 segundos     | 6,06 segundos      |
|99,9999              | 6       | 31,5 segundos     | 2,59 segundos     | 0,605 segundos     |
|99,99999             | 7       | 3,15 segundos     | 0,259 segundos    | 0,0605 segundos    |

Apesar da precisão dos valores, o que exatamente esta disponibilidade significa é variável.
Se você contratar algum serviço na nuvem, por exemplo, o fornecedor pode garantir 99,99% de disponibilidade, mas especificar que o serviço considerado disponível desde que 80% das requisições sejam atendidas em menos de 1s.
Os detalhes do que exatamente é considerado são especificados em um acordo de nível de serviço, ou SLA (do inglês, *service level agreement*).[^sla]
O valor final da disponibilidade é dado pela seguinte fórmula, onde

[^sla]: A SLA especifica, por exemplo, como a qualidade do serviço é medida, qual o nível de serviço almejado  (SLO, do inglês, *service level objetive*), e penalidades caso o SLO não seja alcançado.

* **Tempo de disponibilidade acordado** é o tempo que o serviço deve ficar no ar, de acordo com a SLA; e,
* **Tempo de indisponibilidade** é o tempo que o serviço ficou fora do ar.


$$
\text{Disponbilidade} = \frac{\text{Tempo de disponibilidade acordado}-{\text{Tempo de indisponibilidade}}}{\text{Tempo de disponibilidade acordado}}
$$


###### Confiabilidade
A **confiabilidade** é uma métrica frequentemente confundida com a disponibilidade, mas enquanto esta última mede a prontidão para uso em algum instante, a primeira mede a prontidão para uso por um período, tal que durante tal período o sistema não precise cesse o funcionamento e mantenha um nível de desempenho previamente acordado.

Para se falar em confiabilidade é necessário pensar em termos **defeitos**, da frequência com que acontecem e dos atrasos que causam.
Mais adiante discutiremos defeitos no contexto de sistemas distribuídos, mas por enquanto podemos pensar em defeitos simplesmente como **manifestações de problemas do sistema que o impede de executar as operações para as quais foi construído**; se seu sistema é uma lâmpada, então um defeito pode ser a lâmpada queimar ou emitir menos luz do que o necessário.
Com esta visão de defeitos, podemos definir quatro métricas

* Tempo médio para falha, MTTF (do inglês, *mean time to failure*) é a expectativa de quanto tempo resta até que o sistema apresente o próximo defeito (relevante);
* Tempo médio para diagnóstico, MTTD (do inglês, *mean time to diagnose*), é a expectativa de quanto tempo leva perceber que o sistema apresentou um defeito e iniciar sua correção;
* Tempo médio para reparo, MTTR (do inglês, *mean time to repair*) é a expectativa de quanto tempo leva para corrigir o sistema e retorná-lo ao estado funcional, uma vez que o defeito foi percebido
* Tempo médio entre defeitos, MTBF (do inglês, *mean time between failures*) é a expectativa de quanto tempo transcorre entre falhas.

[![System Reliability & Availability Calculations](../drawings/mtbf.drawio)](https://www.bmc.com/blogs/system-reliability-availability-calculations/)

A frequência de falhas de um único componente é dada por $\lambda = \frac{1}{MTBF}$ e, dada a taxa de falhas de um componente, é possível calcular sua confiabilidade em um certo instante $t$ como $R(t) = e^{-\lambda t}$.
Calcular estas métricas para sistemas com múltiplos componentes, como um sistema distribuído, é possível mas requer uma imersão em probabilidade condicional.


###### Segurança (*Safety*)

!!!todo "TODO"

###### Integridade

!!!todo "TODO"

###### Manutenabilidde

!!!todo "TODO"

###### Segurança (*Security*)

Além da dependabilidade, outra propriedade importante e desejável para os sistemas é a **Confidencialidade**, que quando combinada à **Integridade** é também chamada de **Segurança** (*Security*). 

* Confidencialidade (*Confidentiality*) -- informação somente é acessível a quem é devido.

Mais especificamente, sobre como manter um sistema online para que possa responder a requisições, mesmo quando problemas aparecem. Mas para isso, primeiro precisamos entender os tipos de problemas que aparecem em vários níveis, desde o seu desenvolvimento até seu uso.