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
   mvn clean install
   mvn spring-boot:run
   ```
4. Acesse a API em `http://localhost:8080`.

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
  -H "Content-Type: application/json" \
  -d '{"nome":"Baku","local":"Azerbaijão","descricao":"Inimigos da Armênia"}'
```

```bash
curl http://localhost:8080/destinos
```

```bash
curl http://localhost:8080/destinos/buscar?local=França
```

## Observações
- Dados mantidos em memória (sem persistência).
