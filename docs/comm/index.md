
???inline info end "Comunicação"
    * Canal
    * Protocolo

A pedra fundamental da construção de sistemas distribuídos é a capacidade de comunicação entre seus componentes e, para que os componentes de um sistema distribuído se comuniquem, é necessário que seus *hosts* possuam tenham algum **canal de comunicação** que os conecte e que se estabeleça um **protocolo de comunicação**, que define as regras para que a comunicação aconteça.
Por exemplo, quando você fala com uma pessoa, cara-a-cara, o canal de comunicação é o ar e o protocolo utilizado é a linguagem conhecida pelas duas partes.
Se o canal não está presente ou se o protocolo não é bem definido, a comunicação não acontece.

As camadas de abstração mais básicas do desenvolvimento de SD estão nas **redes de computadores**, que servem de substrato a todo e qualquer sistema distribuído, afinal.
Por isso, neste capítulo revisaremos rapidamente como as redes funcionam, relembraremos como sockets são usados e, na sequência, subiremos algumas camadas de abstração.