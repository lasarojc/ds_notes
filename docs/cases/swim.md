# Gerenciamento de Grupos/Detecção de Falhas: SWIM

SWIM, ou _**S**calable **W**eakly-Consistent **I**nfection-Style Group-**M**embership_, é um sistema para gerenciamento de pertinência de nós a um grupo de nós que usa protocolos epidêmicos como fundação para comunicação.

O sistema tem duas partes fundamentais, detecção de falhas e propagação de informação por *gossip*.
Embora haja a divisão, as partes se integram, pois o detector de falhas propaga suas suspeitas usando o protocolo de *gossiping*  e o protocolo de •gossiping* coloca suas mensagens em *piggyback*  nos pings.


<iframe width="560" height="315" src="https://www.youtube.com/embed/0bAJ4iNnf5M" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture" allowfullscreen></iframe>





