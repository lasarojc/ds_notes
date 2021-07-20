
# Representação de dados

O desenvolvimento de sistemas distribuídos usando diretamente Sockets como forma de comunicação entre componentes não é para os fracos de coração.
Sua grande vantagem está no **acesso baixo nível à rede**, e todo o ganho de desempenho que isso pode trazer.
Suas desvantagens, entretanto, são várias:

* interface de "arquivo" para se ler e escrever bytes;
* controle de fluxo de "objetos" é por conta da aplicação, isto é, a aplicação precisa sinalizar quantos bytes serão escritos de um lado, para que o outro saiba quanto ler para obter um "objeto" correto;
* logo, a serialização e desserialização de objetos é também por conta da aplicação;
* tratamento de desconexões e eventuais reconexões também é gerenciado pela aplicação e nem a tão famosa confiabilidade do TCP ajuda.

Enquanto se poderia argumentar que algumas destas desvantagens podem ser descartadas em função da discussão de incluir ou não API na comunicação [fim-a-fim](http://web.mit.edu/Saltzer/www/publications/endtoend/endtoend.pdf), é certo que algumas funcionalidades são ubíquas em aplicações distribuídas.
Foquemo-nos agora na necessidade de representar dados complexos em formato inteligível pelos vários componentes da aplicação distribuída.


Exceto por aplicações muito simples, processos em um sistema distribuído trocam dados complexos, por exemplo estruturas ou classes com diversos campos, incluindo valores numéricos de diversos tipos, strings e vetores de bytes, com diversos níveis de aninhamento e somando vários KB.
Neste cenário, vários fatores precisam ser levados em consideração na hora de colocar esta estrutura *no fio*, como:

* variações de definições de tipos, por exemplo, `inteiro`: 8: 16, 32, ou 64 bits?
* variações na representação de dados complexos: classe x estrutura   
* conjunto de caracteres diferentes: ASCII x UTF
* little endian, como x64 e IA-32, ou  big endian como SPARC (< V9), Motorola e PowerPC? ou ainda, flexível como  ARM,  MIPS ou  IA-64?
* fim de linha com crlf (DOS) x lf (Unix)?
* fragmentação de dados na rede   
  [![Fragmentação](../images/ipfrag.png)](http://www.acsa.net/IP/)

###### Representação Textual

Uma abordagem comumente usada é a representação em formato textual "amigável a humanos".
Veja o exemplo de como o protocolo HTTP requisita e recebe uma página HTML.
```HTML
telnet www.google.com 80
Trying 187.72.192.217...
Connected to www.google.com.
Escape character is '^]'.
GET / HTTP/1.1
host: www.google.com
                        <=== Linha vazia!
```
As linhas 5 e 6 são entradas pelo cliente para requisitar a página raiz do sítio [www.google.com](https://www.google.com).
A linha 7, vazia, indica ao servidor que a requisição está terminada.

Em resposta a esta requisição, o servidor envia o seguinte, em que as primeiras linhas trazem metadados da página requisitada e, após a linha em branco, vem a resposta em HTML à requisição.

```HTML
HTTP/1.1 302 Found
Location: http://www.google.com.br/?gws_rd=cr&ei=HTDqWJ3BDYe-wATs_a3ACA
Cache-Control: private
Content-Type: text/html; charset=UTF-8
P3P: CP="This is not a P3P policy! See https://www.google.com/support/accounts/answer/151657?hl=en for more info."
Date: Sun, 09 Apr 2017 12:59:09 GMT
Server: gws
Content-Length: 262
X-XSS-Protection: 1; mode=block
X-Frame-Options: SAMEORIGIN
Set-Cookie: NID=100=NB_AruuFWL0hXk2-h7VDduHO_UkjAr6RaqgG7VbccTsfLzFfhxEKx21Xpa2EH7IgshgczE9vU4W1TyKsa07wQeuZosl5DbyZluR1ViDRf0C-5lRpd9cCpCD5JXXjy-UE; expires=Mon, 09-Oct-2017 12:59:09 GMT; path=/; domain=.google.com; HttpOnly

<HTML><HEAD><meta http-equiv="content-type" content="text/html;charset=utf-8">
<TITLE>302 Moved</TITLE></HEAD><BODY>
<H1>302 Moved</H1>
The document has moved
<A HREF="http://www.google.com.br/?gws_rd=cr&amp;ei=HTDqWJ3BDYe-wATs_a3ACA">here</A>.
</BODY></HTML>
```

Representações textuais são usadas em diversos protocolos como SMTP, POP, e telnet.
Algumas destas representações seguem padrões formalizados, o que facilita a geração e interpretação dos dados. 
Dois padrões bem conhecidas são XML e JSON.

[XML](https://xml.org) é o acrônimo para *Extensible Markup Language*, ou seja, uma linguagem marcação que pode ser estendida para representar diferentes tipos de informação.
A HTML, por exemplo, é uma instância de XML destinada à representação de hipertexto (A bem da verdade, XML foi uma generalização de HTML).

Por exemplo, para representarmos os dados relativos à uma pessoa, podemos ter uma instância XML assim:

```xml
<person>
    <name>John Doe</name>
    <id>112234556</id>
    <email>jdoe@example.com</email>
    <telephones>
       <telephone type="mobile">123 321 123</telephone>
       <telephone type="home">321 123 321</telephone>
    </telephones>
</person>
```

Uma das grandes vantagens do uso de XML é a possibilidade de se formalizar o que pode ou não estar em um arquivo para um certo domínio utilizando um [XML *Domain Object Model*](https://docs.microsoft.com/pt-br/dotnet/standard/data/xml/xml-document-object-model-dom). Há, por exemplo, modelos para representação de documentos de texto, governos eletrônicos, representação de conhecimento, [etc](http://www.xml.org/).
Sua maior desvantagem é que é muito verborrágico e por vezes complicado de se usar, abrindo alas para o seu mais famoso concorrente, JSON.


[JSON](http://json.org/) é o acrônimo de *Javascript Object Notation*, isto é, o formato para representação de objetos da linguagem Javascript.
Devido à sua simplicidade e versatilidade, entretanto, foi adotado como forma de representação de dados em sistemas desenvolvidos nas mais diferentes linguagens.
O mesmo exemplo visto anteriormente, em XML, é representado em JSON assim:

```json
{
    "name": "John Doe",
    "id": 112234556,
    "email": "jdoe@example.com",
    "telephones": [
        { "type": "mobile", "number": "123 321 123"},
        { "type": "home", "number": "321 123 321"},
    ]
}
```

Em Python, por exemplo, JSON são gerados e interpretados nativamente, sem a necessidade de *frameworks* externos, facilitando seu uso.
Mas de fato, a opção final por XML ou JSON é questão de preferência, uma vez que os dois formatos são, de fato, equivalentes na questão da representação de informação.

Outros formatos, binários, oferecem vantagens no uso de espaço para armazenar e transmitir dados, e por isso são frequentemente usados como forma de *serialização* de dados em sistemas distribuídos, isto é, na transformação de TAD para sequências de bytes que seguirão "no fio".

* ASN.1 (Abstract Syntax Notation), pela ISO
* XDR (eXternal Data Representation)
* Java serialization
* Google Protocol Buffers
* Thrift

ASN.1 e XDR são de interesse histórico, mas não os discutiremos aqui.
Quanto à serialização feita nativamente pelo Java, por meio de `ObjectOutputStreams`, como neste [exemplo](https://www.tutorialspoint.com/java/java_serialization.htm), embora seja tentadora para quem usa Java, é necessário saber que ela é restrita à JVM e que usa muito espaço, embora minimize riscos de uma desserialização para uma classe diferente.

Nos foquemos nas outras alternativas listadas, protobuf e Thrift, que podem levar a representações binárias e textuais.

###### Protocol Buffers

Nas palavras dos [criadores](https://developers.google.com/protocol-buffers/),
> Protocol buffers are a language-neutral, platform-neutral extensible mechanism for serializing structured data.

Por meio de protobuf, é possível estruturar dados e gerar o código correspondente em diversas linguagens, for forma compartilhável entre as mesmas. Veja o exemplo a seguir, que especifica os dados referentes a uma pessoa. 
Observe a presença de campos de preenchimento opcional (**optional**), de enumerações (**enum**), e de coleções (**repeated**).

```protobuf
message Person {
	required string name = 1;
	required int32 id = 2;
	optional string email = 3;
	enum PhoneType {
		MOBILE = 0;
		HOME = 1;
		WORK = 2;
	}
	message PhoneNumber {
		required string number = 1;
		optional PhoneType type = 2 [default = HOME];
	}
	repeated PhoneNumber phone = 4;
}
```

Além dos tipos usados no exemplo, diversos outros tipos primitivos estão disponíveis:

* `bool`: boolean (true/false)
* `double`: 64-bit; ponto-flutuante 
* `float`: 32-bit; ponto-flutuante 
* `i32`: 32-bit; inteiro sinalizado 
* `i64`: 64-bit; inteiro sinalizado
* `siXX`: signed
* `uiXX`: unsigned
* `sfixedXX`: codificação de tamanho fixo
* `bytes`: 8-bit; inteiro sinalizado
* `string`: string UTF-8 ou ASCII 7-bit

Além destes, também pode ser usado um tipo indefinido e adaptável, [`Any`](https://developers.google.com/protocol-buffers/docs/proto3#any), bem como coleções.

A especificação protobuf pode ser traduzida para [múltiplas linguagens](https://developers.google.com/protocol-buffers/docs/proto3)
Por exemplo, se a tradução for feita para C++, o tipo `message` resulta em uma classe de mesmo nome, com funcionalidades para serialização e desserialização do objeto, como no exemplo a seguir.

```c++
//Instancia person e salva conteúdo em arquivo
Person person;
person.set_name("John Doe");
person.set_id(1234);
person.set_email("jdoe@example.com");
fstream output("myfile", ios::out | ios::binary);
person.SerializeToOstream(&output);

//Instancia Person e o inicializa com dados do arquivo
fstream input("myfile", ios::in | ios::binary);
Person person;
person.ParseFromIstream(&input);
cout << "Name: " << person.name() << endl;
cout << "E-mail: " << person.email() << endl;
```

De acordo com *benchmarks* do próprio [projeto](https://developers.google.com/protocol-buffers/docs/overview), a operação em XML seria mais ordens de grandeza mais lenta e ocuparia mais espaço.

> When this message is encoded to the protocol buffer binary format, it would probably be 28 bytes long and take around 100-200 nanoseconds to parse. The XML version is at least 69 bytes if you remove whitespace, and would take around 5,000-10,000 nanoseconds to parse.

###### Thrift

Originalmente desenvolvido pela Facebook, [Apache Thrift](https://thrift.apache.org/) é um arcabouço desenvolvimento de serviços multi-linguagens. Isto, mesmo que por enquanto nos foquemos no aspecto da representação de dados desta tecnologia, veremos depois que pode ser usado para executar a troca de dados entre processos.[^fbthrift]
Comparado ao protobuf, ele possui praticamente as mesmas funcionalidades, i.e., a definição de estruturas de dados complexos e geração de código para serialização e desserialização de instâncias destas estruturas.
O mesmo exemplo acima, que define uma estrutura para representar pessoas e seus contatos, ficaria assim em thrift.

[^fbthrift]: O Facebook, insatisfeito com os progressos da versão Apache, acabou fazendo um novo *fork*  do projeto, [fbthrift](https://github.com/facebook/fbthrift), também de código livre, mas que tem evoluído de forma desconexa do projeto Apache. Contudo, no escopo do nosso estudo, as duas versões são essencialmente iguais.

```thrift
enum PhoneType {
    MOBILE = 0;
    HOME = 1;
    WORK = 2;
}

struct PhoneNumber {
    1: required string number;
    2: optional PhoneType type = 2 PhoneType.HOME;
}

struct Person {
	1: required string name;
	2: required i32 id;
	3: optional string email;
    4: list<PhoneNumber> phone;
} 

exception PessoaNaoEncontrada {
   1:i64 hora;
   2:string chaveProcurada;
}
```

Usar a classe correspondente em Java, depois da geração de código pelo compilador `thriftc`, é bem simples.

```Java
Person p = new Person("John Doe",112234556,"jdoe@example.com", Collections.emptyList())
```

Observe que além do uso de coleções e enumerações, demonstradas no exemplo, os mesmos tipos básicos também estão disponíveis.

* bool: boolean (true/false)
* byte: 8-bit; inteiro sinalizado
* i16: 16-bit; inteiro sinalizado
* i32: 32-bit; inteiro sinalizado
* i64: 64-bit; inteiro sinalizado
* double: 64-bit; ponto-flutuante 
* string: string UTF-8
* binary: sequência de bytes
* coleções: List, Map, Set


Uma vez que tenhamos facilidades para representar dados complexos e transformá-los em sequências de bytes, e de volta, pensemos em como podemos definir, de forma simplificada, serviços que manipulam estes dados.
Estas funcionalidades são normalmente implementadas por *frameworks* de comunicação de mais alto nível que, jargão da área de sistemas distribuídos, são denominados ***middleware***.



