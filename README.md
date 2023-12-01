
# Descrição de Tarefa - Projeto Multithread HTTP com Criptografia

## Objetivo
Desenvolver dois programas interconectados para demonstrar o uso de Multicast e HTTP em um ambiente multithread. Um programa será responsável por receber solicitações M-SEARCH e fornecer um servidor HTTP com API, enquanto o outro realizará o M-SEARCH, chamando a API e exibindo o valor recebido.

## Funcionalidades

### Programa do Servidor
1. **Recepção Multicast:** O programa deve ser capaz de escutar solicitações Multicast.
2. **Resposta ao M-SEARCH:** Ao receber um comando M-SEARCH, o servidor deve fornecer a rota/endpoint da API.

### API (Servidor HTTP Multithread)
1. **Envio de Nome por GET ou POST:** A API deve aceitar o envio de um nome por meio de solicitações GET ou POST.
2. **Resposta da API:** A resposta pode ser um erro ou um sinal de sucesso, acompanhado pelo nome criptografado.

### Programa do Cliente
1. **Realização do M-SEARCH:** O programa deve realizar um M-SEARCH para obter a rota/endpoint da API.
2. **Chamada da API:** Usando a rota obtida, o cliente deve chamar a API, enviando um nome por GET ou POST.
3. **Exibição da Resposta:** O cliente deve exibir a resposta recebida da API, que pode ser um erro ou um sucesso com o nome criptografado.

### Criptografia (Métodos Diferentes por Equipe)
1. **Requisição RMI:** O servidor HTTP, ao receber um nome da API, deve fazer uma requisição RMI à máquina local/remota para invocar o método de criptografia.
2. **Devolução do Nome Criptografado:** O servidor HTTP deve retornar o nome criptografado ao solicitante.

## Implementação
Certifique-se de que cada equipe utilize um método de criptografia diferente para garantir diversidade.

## Execução
Toda essa execução é feita de maneira automatizada no arquivo main. Portanto você para testar apenas precisa fazer um POST para http://localhost:8080/
1. Inicie o programa do servidor para escutar as solicitações Multicast e fornecer a rota/endpoint da API.
2. Inicie a API (servidor HTTP multithread) para aceitar solicitações GET ou POST e fornecer respostas com nomes criptografados.
3. Execute o programa do cliente para realizar o M-SEARCH, chamar a API e exibir a resposta recebida.

## Observações
1. Certifique-se de que as dependências necessárias estejam instaladas antes da execução.
2. O Body para a requisão é:
```
{
	"endpoint": "/api/exemplo/"
}
```
3. Exemplo de Curl:
```
curl --request POST \
  --url http://localhost:8080/ \
  --header 'Content-Type: application/json' \
  --data '{
	"endpoint": "/api/exemplo/"
}'
```

**Muito Obrigado!🚀**
