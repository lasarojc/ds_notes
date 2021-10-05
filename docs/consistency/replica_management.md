# Gerenciamento de Réplicas

### Posicionamento

Onde colocar réplicas para conseguir melhor escalabilidade do sistema? Menor custo de comunicação?

* Objetos (código/dados)
* Permanente
* Sob demanda do servidor -- por exemplo em uma CDN
* Sob demanda do cliente -- por exemplo um cache.

![](../images/07-17.png)

###### Sob demanda do Servidor

* $Q$ conta acessos ao arquivo $F$
* Agrega acessos por possível réplica mais próxima ($P$)
* Número de acessos acima de limiar $R$, replica para $P$
* Número de acessos abaixo de $D$, apaga de $P$
* $D < R$
* Se não é alto o suficiente para replicar nem baixo o suficiente para ignorar (entre $D$ e $R$), considera migrar.

![](../images/07-18.png)

### Propagação de Atualizações
Réplicas precisam ser atualizadas.

* Propagar dados -- não reexecuta operações.
* Propagar operações -- não copia todos os dados modificados.
* Propagar notificações -- réplica precisa solicitar atualização.<br/> Usado em caches.

Melhor opção depende do custo das operações, dados manipulados, e taxa de leitura/escrita dos dados.

* Propagar dados
    * razão leitura/escrita é grande
    * operações são caras
* Propagar operações
    * razão leitura/escrita é grande
    * operações são baratas
* Propagar notificações
    * razão leitura/escrita é pequena
    * pouco uso da rede
	
###### Proativo/Push ou Reativo/Pull

* Proativo
    * Mantém réplicas consistentes
    * Desnecessário se leitura $<<$ escrita.

* Reativo
    * Réplicas só se tornam consistentes quando necessário.
    * Lento se leitura $>>$ escrita

*Qual é melhor?*

###### Híbrido: Lease

* Réplica se registra para receber atualizações/notificações por um período.
* Estado sobre réplicas é mantido enquanto possível, pelo período contratado.
* Em caso de sobrecarga, deixa de mandar atualizações/notificações.
* Em caso de lease antigo não renovado, deixa de mandar atualizações/notificações.
* Em caso de renovações frequentes, aumenta o período do lease.