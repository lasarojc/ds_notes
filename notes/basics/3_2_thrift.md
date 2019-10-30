---
layout: default
title: Estudo de Caso - Thrift
parent: RPC
grand_parent: Comunicação
nav_order: 2
---

# Estudo de Caso RPC: Thrift

[Thrift](https://thrift.apache.org/)

## Instalação

* [Baixe](http://www.apache.org/dyn/closer.cgi?path=/thrift/0.10.0/thrift-0.10.0.tar.gz) e compile o thrift
* ou instale-o usando apt-get, por exemplo. `apt-get install thrift-compiler`
* execute "thrift" na linha de comando.
* Para thrift com Java, também precisarão dos seguintes arquivos
  * [slf4j](http://mvnrepository.com/artifact/org.slf4j/slf4j-api/1.7.21)
  * [libthrift0.9.3.jar](https://sites.google.com/site/lasaro/sistemasdistribuidos)
  * coloque-os na pasta `jars`



## IDL Thrift
*  Tipos básicos
    * bool: boolean (true/false)
    * byte: 8-bit; inteiro sinalizado
	* i16: 16-bit; inteiro sinalizado
	* i32: 32-bit; inteiro sinalizado
	* i64: 64-bit; inteiro sinalizado
	* double: 64-bit; ponto-flutuante 
	* string: string UTF-8
	* binary: sequência de bytes

* Estruturas
 ```
struct Example {
    1:i32 number,
    2:i64 bigNumber,
    3:double decimals,
    4:string name="thrifty"
}
```	

* Serviços
```
service ChaveValor {
    void set(1:i32 key, 2:string value),
    string get(1:i32 key) throws (1:KeyNotFound knf),
    void delete(1:i32 key)
}
```
* **Não se pode retornar NULL!!!**
* Exceções
```
exception KeyNotFound {
   1:i64 hora r,
   2:string chaveProcurada="thrifty"
}
```
*  Containers
    * List
	* Map
	* Set


Exemplo: chavevalor.thrift

```Thrift
namespace java chavevalor
namespace py chavevalor


exception KeyNotFound
{
}


service ChaveValor
{
    string getKV(1:i32 key) throws (1:KeyNotFound knf),
    bool setKV(1:i32 key, 2:string value),
    void delKV(1:i32 key)
}  
``` 	

Compilação

`thrift --gen java chavevalor.thrift`

`thrift --gen py chavevalor.thrift`

ChaveValorHandler.java
```Java
namespace java chavevalor
namespace py chavevalor


exception KeyNotFound
{
}


service ChaveValor
{
    string getKV(1:i32 key) throws (1:KeyNotFound knf),
    bool setKV(1:i32 key, 2:string value),
    void delKV(1:i32 key)
}  
 	
package chavevalor;

import org.apache.thrift.TException;
import java.util.HashMap;
import chavevalor.*;

public class ChaveValorHandler implements ChaveValor.Iface {
   private HashMap<Integer,String> kv = new HashMap<>();
   @Override
   public String getKV(int key) throws TException {
       if(kv.containsKey(key))
          return kv.get(key);
       else
          throw new KeyNotFound();
   }
   @Override
   public boolean setKV(int key, String valor) throws TException {
       kv.put(key,valor);
       return true;
   }
   @Override
   public void delKV(int key) throws TException {
       kv.remove(key);
   }    
}
```

## Arquitetura 

* Runtime library -- componentes podem ser selecionados em tempo de execução e implementações podem ser trocadas
* Protocol -- responsável pela serializaçãoo dos dados
    * TBinaryProtocol
	* TJSONProtocol
	* TDebugProtocol
	* ...
* Transport -- I/O no ``fio''
    * TSocket
	* TFramedTransport (non-blocking server)
	* TFileTransport
	* TMemoryTransport
* Processor -- Conecta protocolos de entrada e saída com o \emph{handler}
		
* Handler -- Implementação das operações oferecidas
* Server -- Escuta portas e repassa dados (protocolo) para o processors
    * TSimpleServer
	* TThreadPool
	* TNonBlockingChannel



\subsubsection{Exemplo}
\begin{frame}[fragile,allowframebreaks]{ChaveValorServer.java}
	\lstinputlisting[language=Java]{../lab/thrift/ChaveValorServer.java}
\end{frame}


\begin{frame}[fragile,allowframebreaks]{ChaveValorClient.java}
	\lstinputlisting[language=Java]{../lab/thrift/ChaveValorClient.java}
\end{frame}


## Classpath

```bash
javac  -cp jars/libthrift0.9.3.jar:jars/slf4japi1.7.21.jar:gen-java  -d . *.java 
	
java -cp jars/libthrift0.9.3.jar:jars/slf4japi1.7.21.jar:gen-java:. chavevalor.ChaveValorServer
	
java -cp jars/libthrift0.9.3.jar:jars/slf4japi1.7.21.jar:gen-java:. chavevalor.ChaveValorClient	
```

## Referências

[Tutorial](http://thrift-tutorial.readthedocs.org/en/latest/index.html)
