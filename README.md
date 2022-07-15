# Distributed and Partitioned Key-Value Store

Compilar o projeto:

`$ make`

Executar um nó:

`$ java Store <IP_mcast_addr> <IP_mcast_port> <IP_addr>  <Store_port>`

Enviar um comando:

`$ java TestClient <node_ap> <operation> [<opnd>]`

Encerrar todos os servidores criados de uma só vez:

`$ make terminate`

Limpar todos os arquivos gerados após testes:

`$ make clean`
