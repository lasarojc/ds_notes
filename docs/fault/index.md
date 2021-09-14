# Tolerância a Faltas

Sistemas distribuídos são usados para resolver problemas de um espectro muito amplo, indo do controle de braços robóticos em cirurgias remotas à sistemas de comércio eletrônico, do controle de usinas hidroelétricas à jogos de truco online.
Cada um destes serviços tem seus próprios requisitos de qualidade de serviço, isto é, enquanto você perder uma partida de truco porquê a conexão caiu pode ter deixar chateado, certamente há de convir que perder a conexão com um robô segurando um bisturi sobre uma pessoa é algo bem mais grave.

Independentemente do problema, um dos objetivos no desenvolvimento é ter o sistema funcional quando precisarmos dele, mesmo quando fontes queimem, discos deixem de girar, fontes parem de alimentar, *coolers* parem de refrigerar, administradores parem de manter o sistema, hackers façam o que fazem, fibras sejam rompidas, satélites saiam de órbita, funcionários demitidos se revoltem, etc.

A lista de problemas que podem afligir sistemas é tão grande, que há uma área da computação (e não somente da computação) dedicada somente a lidar com estes problemas, a área de tolerância a faltas.[^faltas] 


[^faltas]: Ou falhas, dependendo de a quem você perguntar.