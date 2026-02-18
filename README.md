# Planner API

API REST em Java para organização de viagens, participantes, atividades e links relacionados ao planejamento.

## Resumo do projeto

Este projeto implementa um backend com Spring Boot para gerenciamento de viagens em grupo. A API cobre o ciclo principal de planejamento:

- cadastro e gerenciamento de viagens;
- cadastro de participantes por viagem;
- organização de atividades;
- registro de links úteis.

A aplicação segue uma estrutura por domínio (`trip`, `participant`, `activity`, `link`), com separação entre camadas de controller, service, repository e DTOs.

## Stack e arquitetura

- Java 21
- Spring Boot
- Spring Web
- Spring Data JPA
- Maven Wrapper (`mvnw` / `mvnw.cmd`)
- Migrações SQL versionadas em `src/main/resources/db/migration`
- Docker (build multi-stage)

Arquitetura utilizada:

- `Controller`: expõe os endpoints HTTP;
- `Service`: concentra regras de negócio;
- `Repository`: acesso a dados com abstração JPA;
- `Entity`: representação das tabelas do banco;
- `Payloads/Responses`: contratos de entrada e saída da API.

## Conceitos técnicos abordados em Java

- Programação orientada a objetos aplicada ao domínio de negócio.
- Uso de records/DTOs para transporte de dados entre camadas.
- Injeção de dependências com Spring.
- Tratamento global de exceções com `@RestControllerAdvice`.
- Serialização e desserialização JSON no ciclo de requisição/resposta.
- Mapeamento objeto-relacional com JPA.
- Organização de projeto por pacotes e responsabilidade única.
- Build reproduzível com Maven Wrapper.

## Estrutura do projeto

```text
src/main/java/com/planner/
  activity/
  config/
  exceptions/
  link/
  participant/
  trip/
  utils/

src/main/resources/
  application.properties
  db/migration/
```

## Como executar localmente

Pré-requisitos:

- JDK 21 instalado
- PowerShell ou terminal compatível

Executar em desenvolvimento:

```bash
./mvnw spring-boot:run
```

No Windows (PowerShell):

```powershell
.\mvnw.cmd spring-boot:run
```

Gerar build:

```bash
./mvnw clean package
```

No Windows (PowerShell):

```powershell
.\mvnw.cmd clean package
```

## Docker

Build da imagem:

```bash
docker build -t planner-api .
```

Execução:

```bash
docker run --rm -p 8080:8080 planner-api
```

O `Dockerfile` usa build multi-stage:

1. compila a aplicação com Maven;
2. gera imagem final enxuta com JRE 21 Alpine.

## Migrações de banco

As migrações SQL estão em `src/main/resources/db/migration`:

- `V1__create-table-trips.sql`
- `V2__create-table-participants.sql`
- `V3__create-table-activities.sql`
- `V4__create-table-links.sql`

Esse versionamento facilita evolução do schema de forma controlada.

## Objetivo educacional

O projeto é um bom exemplo prático para estudar desenvolvimento de APIs em Java com Spring Boot, cobrindo desde modelagem de domínio e persistência até empacotamento para produção com Docker.
