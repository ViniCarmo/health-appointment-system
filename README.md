# Health Appointment System

Vinicius Oliveira do Carmo — Pós Tech 12ADJT
Tech Challenge Fase 3 — Arquitetura e Desenvolvimento Java

Sistema de agendamento de consultas para um ambiente hospitalar, com controle de acesso por papel (médico, enfermeiro, paciente), histórico de consultas via GraphQL e comunicação assíncrona entre serviços via Kafka. Construído com **Java 21**, **Spring Boot** e **Clean Architecture**, distribuído em dois microsserviços independentes.

---

## Sumário

- [Sobre o projeto](#sobre-o-projeto)
- [Tecnologias](#tecnologias)
- [Arquitetura](#arquitetura)
- [Como executar](#como-executar)
- [Variáveis de ambiente](#variáveis-de-ambiente)
- [Endpoints](#endpoints)
- [Autenticação e autorização](#autenticação-e-autorização)
- [Decisões técnicas](#decisões-técnicas)
- [Testes](#testes)
- [Collection Postman](#collection-postman)
- [Estrutura de pastas](#estrutura-de-pastas)

---

## Sobre o projeto

O sistema permite o agendamento de consultas médicas, com três perfis de usuário: **médicos** (visualizam e editam o histórico), **enfermeiros** (registram consultas e acessam o histórico) e **pacientes** (visualizam apenas as próprias consultas).

O projeto é composto por dois serviços:

- **`scheduling-service`** — cadastro de usuários e gerenciamento de consultas (REST + GraphQL).
- **`notification-service`** — consome eventos de consulta via Kafka e simula o envio de lembretes aos pacientes.

## Tecnologias

| Tecnologia | Finalidade |
|---|---|
| Java 21 | Linguagem principal |
| Spring Boot | Framework de aplicação (3.5.0 no scheduling, 4.1.1 no notification) |
| Spring Security + JWT | Autenticação stateless e autorização |
| Spring Data JPA | Persistência |
| Spring for GraphQL | Consultas flexíveis de histórico |
| Apache Kafka | Comunicação assíncrona entre os serviços |
| PostgreSQL | Banco de dados relacional |
| Flyway | Versionamento do schema do banco |
| JUnit 5 + Mockito | Testes unitários |
| Docker + Docker Compose | Containerização e orquestração |
| Maven | Build e gerenciamento de dependências |

## Arquitetura

Cada serviço segue **Clean Architecture**, organizada por módulo de domínio (*Package by Feature*):

```
modulo/
├── domain/            # Entidades, repositórios e exceções — Java puro
├── application/       # Casos de uso
├── infrastructure/    # JPA, Kafka, Security
└── interfaces/        # Controllers, DTOs, resolvers GraphQL
```

A regra central: **as dependências apontam sempre para dentro**. O `domain` não conhece Spring, JPA, Kafka ou HTTP. Os dois serviços não se chamam diretamente — toda comunicação entre eles acontece via Kafka, publicando/consumindo eventos no tópico `appointment-events`.

## Como executar

Projeto totalmente dockerizado — não é necessário instalar Java, Maven, PostgreSQL ou Kafka localmente.

```bash
git clone https://github.com/ViniCarmo/health-appointment-system.git
cd health-appointment-system
docker compose up -d --build
```

A API estará disponível em `http://localhost:8081` (REST e GraphQL). Para encerrar:

```bash
docker compose down
```

## Variáveis de ambiente

| Variável | Padrão | Descrição |
|---|---|---|
| `DB_HOST` | `localhost` | Host do PostgreSQL |
| `DB_PORT` | `5432` | Porta do PostgreSQL |
| `DB_NAME` | `scheduling` | Nome do banco de dados |
| `DB_USERNAME` | `postgres` | Usuário do banco |
| `DB_PASSWORD` | `postgres` | Senha do banco |
| `KAFKA_BOOTSTRAP_SERVERS` | `localhost:9092` | Endereço do broker Kafka |
| `JWT_SECRET` | *(chave de exemplo)* | Segredo usado para assinar os tokens JWT — **substitua em produção** |
| `SERVER_PORT` | `8081` / `8082` | Porta HTTP (scheduling / notification) |

No `docker-compose.yml`, essas variáveis já são definidas com os valores corretos para comunicação entre os containers.

## Endpoints

Todos os endpoints REST seguem o prefixo `/api/v1`. Collection Postman completa disponível — veja [Collection Postman](#collection-postman).

### Auth

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| POST | `/auth/login` | Autentica o usuário e retorna um JWT | Público |

### Users

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| POST | `/users` | Cria um novo usuário | Público |
| GET | `/users/{id}` | Busca usuário por id | DOCTOR, NURSE, PATIENT |
| GET | `/users/email?email=` | Busca usuário por email | DOCTOR, NURSE, PATIENT |
| PUT | `/users/{id}` | Atualiza dados de contato | Próprio usuário ou staff |
| PUT | `/users/{id}/password` | Atualiza a senha | Próprio usuário ou staff |
| DELETE | `/users/{id}` | Exclui um usuário | Próprio usuário ou staff |

### Appointments

| Método | Endpoint | Descrição | Acesso |
|---|---|---|---|
| POST | `/appointments` | Cria uma nova consulta | DOCTOR, NURSE |
| GET | `/appointments/{id}` | Busca consulta por id | DOCTOR, NURSE, PATIENT (própria) |
| GET | `/appointments` | Lista consultas | DOCTOR, NURSE (todas), PATIENT (próprias) |
| PUT | `/appointments/{id}` | Edita data/notas | DOCTOR, NURSE |
| POST | `/appointments/{id}/cancel` | Cancela a consulta | DOCTOR, NURSE |
| POST | `/appointments/{id}/complete` | Marca como concluída | DOCTOR, NURSE |

### GraphQL (`POST /graphql`)

| Query | Descrição | Acesso |
|---|---|---|
| `appointmentsByPatient(patientId)` | Histórico completo de um paciente | DOCTOR, NURSE, PATIENT (próprio) |
| `upcomingAppointmentsByPatient(patientId)` | Apenas consultas futuras | DOCTOR, NURSE, PATIENT (próprio) |

### Kafka (assíncrono, sem endpoint)

Ao criar ou editar uma consulta, o `scheduling-service` publica um evento no tópico `appointment-events`, consumido pelo `notification-service`, que registra em log a simulação do lembrete ao paciente.

## Autenticação e autorização

A autenticação é **stateless**, via JWT:

1. `POST /auth/login` com email e senha retorna um token.
2. Envie o token no header `Authorization: Bearer {token}` nas requisições seguintes.

A autorização acontece em dois níveis:

- **Role** (Spring Security) — apenas `DOCTOR`/`NURSE` criam, editam, cancelam ou completam consultas.
- **Posse do recurso** (casos de uso) — um paciente só acessa as próprias consultas e só altera a própria conta, validado via `AuthenticatedUserProvider`. Médicos e enfermeiros não têm essa restrição.

## Decisões técnicas

- **JWT em vez de Basic Auth** — stateless, mais adequado à comunicação assíncrona entre microsserviços.
- **GraphQL embutido no `scheduling-service`** — sem `history-service` separado (opcional no enunciado), evitando duplicar dados entre bancos.
- **Tópico único no Kafka** (`appointment-events`) — diferenciado por `eventType`, em vez de um tópico por tipo de evento.
- **`AppointmentEvent` duplicado entre os serviços** — cada serviço mantém sua própria cópia do contrato; o JSON trafegado é o contrato real, não uma classe Java compartilhada.
- **`Appointment` sem FK/relacionamento JPA com `User`** — guarda apenas os UUIDs; a composição de nomes é feita na camada de aplicação (`AppointmentDetailsAssembler`).
- **Guarda de transição de estado** — `cancel()`/`complete()`/`reschedule()` só a partir de `SCHEDULED`, retornando `409` em caso de violação.
- **UUID gerado no domínio** — exigiu remover `@GeneratedValue` das entidades JPA, já que o id chega preenchido pela aplicação, não pelo banco.

> **Limitação conhecida:** qualquer `DOCTOR`/`NURSE` pode editar/cancelar/completar qualquer consulta, não apenas as que atende diretamente.

## Testes

```bash
cd scheduling-service && ./mvnw test
cd notification-service && ./mvnw test
```

73 testes (JUnit 5 + Mockito), cobrindo entidades de domínio, todos os casos de uso dos dois serviços e a camada de segurança (JWT e extração do usuário autenticado).

## Collection Postman

Collection disponível na raiz do repositório, cobrindo Auth, Users, Appointments e GraphQL, incluindo cenários de erro (401, 403, 409).

Para importar: Postman → **Import** → selecione o arquivo `.json`.

## Estrutura de pastas

```
health-appointment-system/
├── scheduling-service/
│   ├── src/main/java/com/fiap/scheduling_service/
│   │   ├── user/
│   │   ├── appointment/
│   │   └── shared/
│   │       ├── messaging/     # Kafka
│   │       └── security/      # JWT, SecurityConfig
│   ├── src/main/resources/
│   │   ├── db/migration/      # Flyway
│   │   └── graphql/           # schema.graphqls
│   └── Dockerfile
├── notification-service/
│   ├── src/main/java/com/fiap/notification_service/
│   │   └── notification/
│   └── Dockerfile
└── docker-compose.yml
```

---

**Autor:** Vinicius Carmo
[LinkedIn](https://www.linkedin.com/in/viniciuscarmoo/) · [GitHub](https://github.com/ViniCarmo)
