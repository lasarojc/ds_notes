# Redes de Computadores

Redes de computadores podem ter diversas topologias e características, por exemplo:

| Ponto-a-ponto  | Barramento Compartilhado | Token Ring |
|----------------|--------------------------|------------|
| Sem colisões   | Com colisões             | Sem colisões|
| Roteamento trivial | Roteamento complexo | Roteamento simples|
| Caro (exponencial) | Barato (linear) | Barato (linear)|

Nas redes atuais, pode se dizer que o meio mais utilizado é provido pela arquitetura **Ethernet**, que trata da comunicação entre nós usando um **barramento compartilhado**, mesmo que este esteja por vezes escondido.
Sobre este meio, são usados protocolos para, por exemplo,

* Controle de acesso ao meio 
* Transmissão de mensagens
* Evitar e tratar colisões

As redes Ethernet, contudo, cobrem pequenas áreas e para se ter conversas mais "abrangentes", é necessário que se conecte diversas destas redes.
A conversa então é feita por meio de intermediários, ***gateways*** que conectam duas ou mais redes, permitindo que mensagens de um interlocutor sejam **roteadas** para o outro, via tais intermediários.

Um exemplo interessante das questões ligadas à manutenção da conversa entre dois pontos é a decisão sobre o uso de **comutação de pacotes** (*packet switching*) ou de **circuitos** (*circuit switching*).

| Comutação de pacotes | Comutação de circuito |
|-|-|
| Cada pacote viaja independentemente | Todo pacote viaja por caminho predefinido|
| Latência variável | Latência mais constante|
| Banda não reservada | Banda reservada |
| Banda não desperdiçada | Banda desperdiçada |


Outro fator importante é a **unidade máxima de transmissão** (*maximum transmission unit*, MTU), o tamanho máximo de um pacote em determinada rede. É necessário entender que qualquer quantidade de dados maior que o MTU precisará ser dividida em múltiplos pacotes. Também é importante perceber que redes são heterogêneas, e que o vários segmentos no caminho entre origem e destino podem ter MTU diferentes, levando à fragmentação de pacotes em trânsito e, possivelmente, entrega desordenada dos mesmos.

Finalmente, há uma questão importante relativa à confiabilidade na transmissão dos elementos da conversa, isto é, se a rede deve garantir ou não que algo "dito" por um interlocutor deve garantidamente ser "ouvido" pelo outro, ou se a **mensagem pode ser perdida** no meio.

Felizmente boa parte da **complexidade da resolução destas questões é abstraída do desenvolvedor dos sistemas distribuídos**, isto é, você, lhe cabendo apenas a decisão de qual protocolo utilizar.
Nas redes atuais, a conversa em componentes será feita, em algum nível, por meio dos protocolos da arquitetura **Internet**.