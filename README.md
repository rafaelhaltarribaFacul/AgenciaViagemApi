# Agência Viagem API

## Descrição
API REST para gerenciar destinos de viagem e reservas.

## Tecnologias
- Java 21
- Spring Boot
- Maven

## Pré-requisitos
- JDK 21 instalado
- Maven
- IDE (IntelliJ, VSCode, etc)

## Como Executar
1. Clone o repositório.
2. Abra o projeto na sua IDE.
3. Execute o comando:
   ```
   ./gradlew clean build
   ./gradlew bootRun
   ```
4. Certifique-se de que o PostgreSQL esteja rodando na porta padrao (5432) e que o banco `agenciaviagem` esteja criado. O usuario e senha configurados sao `postgres`.
5. Acesse a API em `http://localhost:8080`.

## Endpoints

- **POST /destinos**  
  Cadastra um novo destino.  
  **Body JSON**:
  ```json
  {
    "nome": "string",
    "local": "string",
    "descricao": "string"
  }
  ```

- **GET /destinos**  
  Lista todos os destinos.

- **GET /destinos/buscar?nome={nome}&local={local}**  
  Busca destinos filtrando por nome e/ou local.

- **GET /destinos/{id}**  
  Retorna detalhes de um destino específico, incluindo reservas.

- **POST /destinos/{id}/avaliacao/{nota}**  
  Avalia um destino com nota de 1 a 10.

- **DELETE /destinos/{id}**  
  Exclui um destino.

- **POST /destinos/{id}/reservas**  
  Cria uma nova reserva para o destino especificado.  
  **Body JSON**:
  ```json
  {
    "nomeCliente": "string",
    "quantidadePessoas": "number",
    "dataViagem": "YYYY-MM-DD"
  }
  ```

- **GET /destinos/{id}/reservas**  
  Lista todas as reservas de um destino.

- **DELETE /destinos/{id}/reservas/{idReserva}**  
  Exclui uma reserva específica por ID.

## Exemplos de curl

```bash
curl -X POST http://localhost:8080/destinos \
  -u admin:password \
  -H "Content-Type: application/json" \
  -d '{"nome":"Praia","local":"Brasil","descricao":"Destino de teste"}'
```

```bash
curl -u admin:password http://localhost:8080/destinos
```

```bash
curl -u admin:password "http://localhost:8080/destinos/buscar?nome=Praia&local=Brasil"
```

```bash
curl -u admin:password http://localhost:8080/destinos/1
```

```bash
curl -X POST -u admin:password http://localhost:8080/destinos/1/avaliacao/8
```

```bash
curl -X DELETE -u admin:password http://localhost:8080/destinos/1
```

```bash
curl -X POST http://localhost:8080/destinos/1/reservas \
  -u admin:password \
  -H "Content-Type: application/json" \
  -d '{"nomeCliente":"Fulano","quantidadePessoas":2,"dataViagem":"2024-07-01"}'
```

```bash
curl -u admin:password http://localhost:8080/destinos/1/reservas
```

```bash
curl -X DELETE -u admin:password http://localhost:8080/destinos/1/reservas/10
```

## Observações
- Os dados agora são armazenados em um banco PostgreSQL.
- O script `data.sql` cria o usuario `admin` com senha `password` para acesso via Basic Auth.
