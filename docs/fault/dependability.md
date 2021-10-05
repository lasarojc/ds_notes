# Dependabilidade

Da necessidade de se poder depender de um sistema, surge a ideia de **dependabilidade**, isto é, de um sistema ter a propriedade de se poder depender do mesmo.

Um pouco mais formalmente, dizemos que um componente $C$ **depende de um componente** $C'$ se a corretude do comportamento de $C$ depende da corretude do componente $C'$ e dizemos também que um componente é "**dependável**" (*dependable*) na medida em que outros podem depender dele.
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
Se você contratar algum serviço na nuvem, por exemplo, o fornecedor pode garantir 99,99% de disponibilidade, mas especificar que o serviço é considerado disponível desde que 80% das requisições sejam atendidas em menos de 1s.
Os detalhes do que exatamente é considerado são especificados em um acordo de nível de serviço, ou SLA (do inglês, *service level agreement*).[^sla]
O valor final da disponibilidade é dado pela seguinte fórmula, onde

[^sla]: A SLA especifica, por exemplo, como a qualidade do serviço é medida, qual o nível de serviço almejado  (SLO, do inglês, *service level objetive*), e penalidades caso o SLO não seja alcançado.

* **Tempo de disponibilidade acordado** é o tempo que o serviço deve ficar no ar, de acordo com a SLA; e,
* **Tempo de indisponibilidade** é o tempo que o serviço ficou fora do ar.


$$
\text{Disponbilidade} = \frac{\text{Tempo de disponibilidade acordado}-{\text{Tempo de indisponibilidade}}}{\text{Tempo de disponibilidade acordado}}
$$


###### Confiabilidade
A **confiabilidade** é uma métrica frequentemente confundida com a disponibilidade, mas enquanto esta última mede a prontidão para uso em algum instante, a primeira mede a prontidão para uso por um período, tal que durante tal período o sistema não precise cessar o funcionamento e mantenha um nível de desempenho previamente acordado.

Para se falar em confiabilidade é necessário pensar em termos de **falhas**, da frequência com que acontecem e dos atrasos que causam.
Mais adiante discutiremos falhas no contexto de sistemas distribuídos, mas por enquanto podemos pensar em falhas simplesmente como **manifestações de problemas do sistema que o impede de executar as operações para as quais foi construído**; se seu sistema é uma lâmpada, então um defeito pode ser a lâmpada queimar ou emitir menos luz do que o necessário.
Com esta visão de falhas, podemos definir quatro métricas

* Tempo médio para falha, MTTF (do inglês, *mean time to failure*) é a expectativa de quanto tempo resta até que o sistema apresente o próximo defeito (relevante);
* Tempo médio para diagnóstico, MTTD (do inglês, *mean time to diagnose*), é a expectativa de quanto tempo leva perceber que o sistema apresentou um defeito e iniciar sua correção;
* Tempo médio para reparo, MTTR (do inglês, *mean time to repair*) é a expectativa de quanto tempo leva para corrigir o sistema e retorná-lo ao estado funcional, uma vez que o defeito foi percebido
* Tempo médio entre falhas, MTBF (do inglês, *mean time between failures*) é a expectativa de quanto tempo transcorre entre falhas.

[![System Reliability & Availability Calculations](../drawings/mtbf.drawio)](https://www.bmc.com/blogs/system-reliability-availability-calculations/)

A frequência de falhas de um único componente é dada por $\lambda = \frac{1}{MTBF}$ e, dada a taxa de falhas de um componente, é possível calcular sua confiabilidade em um certo instante $t$ como $R(t) = e^{-\lambda t}$.
Calcular estas métricas para sistemas com múltiplos componentes, como um sistema distribuído, é possível mas requer uma imersão em probabilidade condicional.


###### Manutenabilidde

Manutenabilidade é uma medida da expectativa do quão rápido um sistema é reparado uma vez identificado sua falha.
Uma manutenabilidade de 95% para 1 hora implica que com 95% de probabilidade chance o sistema voltará a estar funcional dentro de 1 hora.
A manutenabilidade está intimamente ligada ao tempo médio de reparo (MTTR), mencionado acima.


###### Segurança (*Safety*)

!!!todo "TODO"

###### Integridade

!!!todo "TODO"


###### Segurança (*Security*)

Além da dependabilidade, outra propriedade importante e desejável para os sistemas é a **Confidencialidade**, que quando combinada à **Integridade** é também chamada de **Segurança** (*Security*). 

* Confidencialidade (*Confidentiality*) - informação somente é acessível a quem é devido.
* Integridade (*Integrity*) - Ausência de corrupções.

## Cálculo da Dependabilidade
A dependabilidade é facilmente definida mas não facilmente calculada.
Simplifiquemos o problema e consideremos um único computador, muito mais simples que um sistema distribuído. 
O quanto você pode confiar neste computador para desempenhar suas tarefas? Este computador tem memórias, CPU, barramentos, discos, etc. Cada um falha independentemente e por isso a dependabilidade do computador é uma combinação da dependabilidade de todos os componentes do mesmo.

Simplifiquemos mais ainda e consideremos apenas um tipo de componente, por exemplo um chip de memória.
Consideremos também apenas a confiabilidade, uma das componentes da dependabilidade.
Digamos que experimentos foram feitos em chips individuais e que assim se determinou que cada chip tem uma confiabilidade de 99%. Wow!
Agora suponha que os chips foram organizados em série, isto é, que para que os dados de um chip sejam acessíveis e manipuláveis, os dados de todos os outros também devem estar acessíveis e manipuláveis.
A probabilidade do sistema estar funcionando é a probabilidade de o primeiro da série estar funcionando, e o segundo da série estar funcionando, e o terceiro da série..., bem, você entendeu a ideia.
Assim, se a sua configuração tem 10 chips, então a confiabilidade do conjunto é de $0,99^10 = 0,9034$. Se fossem 20 chips, a confiabilidade seria de $0,99^10 = 0,81$.

![](../drawings/reliability.drawio#0)

Se os componentes estivessem ligados em paralelo, ou seja, propiciando formas redundantes de uso, o cálculo seria diferente.
Neste caso, a probabilidade do sistema não estar funcional seria a probabilidade de nenhum deles estar funcional; dado que um componente não está funcional com probabilidade $1-0,99 = 0,01$, todos não estariam funcionais com probabilidade $0,01* 0,01 * 0,01 \ldots$. Já a probabilidade do sistema estar funcional, sua confiabilidade, é de 1 menos a probabilidade de não estar funcional, isto é, $1 - (0,01*0,01*0,01\ldots)$.
Se forem 4 componentes, temos $1 - 0,01^4 = 0,9999999$.

![](../drawings/reliability.drawio#1)


Por isso, não basta ter dispositivos individualmente confiáveis; a forma como os mesmos são combinados tem um grande impacto no resultado final.

![](../drawings/reliability.drawio#1)
 
Embora seja óbvio que quanto mais redundância (caminhos paralelos) tivermos no sistema, maior será a confiabilidade, o que pode lhe induzir a querer aumentar a redundância, lembre-se de que redundância tem um custo, e este custo pode chegar a ser maior que o benefício que esta redundância lhe trará.

