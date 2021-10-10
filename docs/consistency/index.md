# Consistência
Quando implementamos um sistema distribuído, precisamos estar cientes de que **há custos inerentes** desta distribuição, introduzidos pela coordenação das partes que formam o todo.
E também precisamos estar cientes de é possível pensar em **diferentes níveis de coordenação**, com diferentes custos.

???sideslide "CDN"
    * Conteúdo é colocado próximo aos clientes. 
    * Conteúdo estático ou majoritariamente determinístico. 
    * Um pequeno atraso na replicação é tolerado.
    * Atualização acontece infrequentemente.

Considere, por exemplo, uma ***Content Delivery Network*** (CDN), um sistema que replica os dados de um "provedor de conteúdo" para colocar tal conteúdo próximo aos clientes.
A replicação acontece de forma adaptativa, dependendo de onde os clientes estiverem e dos custos envolvidos no processo.

[![](../images/cdn.jpeg)](https://www.creative-artworks.eu/why-use-a-content-delivery-network-cdn/)


Se o conteúdo é majoritariamente estático, entregar esta funcionalidade é "simples", implicando apenas em um pequeno atraso entre a publicação de novo conteúdo e sua disponibilização para os usuários.
Neste caso, um protocolo de difusão totalmente ordenada, como o RAFT, pode ser usado para garantir que todos os servidores vejam as mesmas mudanças, na mesma ordem, e que alcancem o mesmo estado final em algum momento.

Entretanto, os protocolos de difusão totalmente ordenada tem um alto custo para atualizar as réplicas e este custo pode ser demasiado para a aplicação.
Neste caso, podemos tentar relaxar os requisitos da aplicação, permitindo que atualizações sejam vistas em ordens diferentes por diferentes "réplicas", agora com aspas.
Por exemplo, se em vez de difusão atômica usássemos IP-Multicast, teríamos uma atualização mais barata dos dados, mas alguns valores escritos poderiam nunca ser vistos.
Dependendo de como estes relaxamentos sejam feitos, serão implementados diferentes **modelos de consistência**.