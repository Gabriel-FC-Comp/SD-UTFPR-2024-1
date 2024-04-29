# Sistemas Distribuídos - Engenharia de Computação - UTFPR Apucarana

Este repositório contém códigos desenvolvidos na disciplina de Sistemas Distribuídos (SDCO8A) do Curso de Engenharia de Computação da Universidade Tecnológica Federal do Paraná (UTFPR), Campus Apucarana.

## Descrição

Os códigos neste repositório são relacionados ao desenvolvimento de sistemas distribuídos, abordando conceitos como comunicação remota, programação cliente-servidor, uso de RMI (Remote Method Invocation), entre outros tópicos relevantes para a disciplina de Sistemas Distribuídos.

## Estrutura do Repositório

- [`Lista1_Ex09_SDCO8A`](./Lista1_Ex09_SDCO8A): Neste laboratório, foi desenvolvida uma aplicação em Java para realizar operações básicas em um banco de dados não distribuído. A aplicação é capaz de ler uma fortuna aleatória do arquivo "fortune-br.txt" e escrever uma nova mensagem nesse arquivo. O formato dos dados no arquivo segue um padrão, com cada mensagem de fortuna encerrando com um sinal de porcentagem (%). O laboratório foi concluído como parte do curso de Sistemas Distribuídos, fornecendo uma introdução ao conceito de banco de dados não distribuído.
- [`Lista1_Ex10_SDCO8A`](./Lista1_Ex10_SDCO8A): Neste laboratório de Sistemas Distribuídos, foi desenvolvida uma aplicação cliente-servidor em Java para interagir com um banco de dados de fortunas no servidor. Utilizando um protocolo de comunicação baseado em mensagens JSON, o cliente pode enviar solicitações de leitura e escrita de fortunas, enquanto o servidor processa essas requisições e retorna as respostas correspondentes. O sistema implementado segue o modelo cliente-servidor, permitindo que múltiplos clientes se comuniquem com o servidor central para acessar ou modificar os dados da base de dados de forma síncrona.
- [`Lista2_Ex09_SDCO8A`](./Lista2_Ex09_SDCO8A): Neste laboratório de Preparação de uma Arquitetura Peer-to-Peer em Sistemas Distribuídos, continuamos o código do laboratório anterior, focando na implementação de uma arquitetura RPC (Remote Procedure Call) utilizando o middleware RMI (Remote Method Invocation) da linguagem Java. O objetivo é lidar com situações onde pares arbitrariamente se juntam ou deixam o sistema distribuído. A aplicação desenvolvida permite que clientes realizem chamadas de métodos remotos para ler e escrever no arquivo de fortunas no servidor. O protocolo de comunicação entre cliente RMI e servidor RMI é baseado em mensagens JSON, onde cada mensagem possui um campo 'method' que identifica o tipo da operação (leitura ou escrita) e um campo 'args' que contém os argumentos da operação. Após a execução das operações de leitura e escrita, o servidor retorna as respostas no formato JSON, garantindo que cada mensagem gravada ou retornada termine com um caracter de nova linha ('\n'). A implementação é dividida entre os arquivos ClienteRMI.java, ServidorImpl.java, IMensagem.java, Mensagem.java e Principal.java, e ao final do laboratório, os participantes são capazes de realizar operações de leitura e escrita com chamadas de métodos remotos, preparando o terreno para uma arquitetura peer-to-peer mais complexa no futuro.
- `Lista2_Ex10_SDCO8A` (A implementar): Neste laboratório de Descoberta de Pares em uma Arquitetura Peer-to-Peer em Sistemas Distribuídos, avançamos no código do laboratório anterior para implementar a descoberta de servidores, onde cada servidor que ingressa no sistema registra seu nome no registro para que o cliente possa se conectar a qualquer servidor disponível. A aplicação continua utilizando o middleware RMI (Remote Method Invocation) da linguagem Java para realizar chamadas de métodos remotos. O protocolo de comunicação entre cliente RMI e servidor RMI permanece o mesmo, baseado em mensagens JSON. Os arquivos relevantes deste laboratório são Peer.java, ClienteRMI.java, ServidorImpl.java, IMensagem.java, Mensagem.java e Principal.java. As tarefas incluem obter a lista de pares disponíveis do arquivo Peer.java, conectar-se a um servidor aleatório como cliente, realizar operações de leitura e escrita com chamadas de métodos remotos nos servidores aleatórios, e implementar o método 'parser' no servidor para processar as operações e retornar respostas no formato JSON. Ao final do laboratório, os participantes são capazes de se conectar a pares aleatórios e realizar operações de leitura e escrita com chamadas de métodos remotos, preparando o terreno para uma arquitetura peer-to-peer mais complexa.
 
## Autor
Gabriel Finger Conte

## Licença

Este projeto está licenciado sob a [Licença MIT](LICENSE).
