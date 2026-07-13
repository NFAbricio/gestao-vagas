# Gestao de Vagas API

API REST para gestao de vagas, empresas e candidatos, desenvolvida com Spring Boot, PostgreSQL e autenticacao via JWT.

## Sumario

- [Sobre o projeto](#sobre-o-projeto)
- [Tecnologias](#tecnologias)
- [Estrutura do projeto](#estrutura-do-projeto)
- [Como executar localmente](#como-executar-localmente)
- [Configuracao](#configuracao)
- [Autenticacao e seguranca](#autenticacao-e-seguranca)
- [Endpoints](#endpoints)
- [Exemplos de requisicao](#exemplos-de-requisicao)
- [Testes](#testes)
- [Melhorias futuras](#melhorias-futuras)

## Sobre o projeto

Este projeto implementa uma API para:

Antes de tudo, este é um projeto didático para aprendizado de framkeworks e tecnologias

- cadastro de empresas
- cadastro de candidatos
- autenticacao de empresas e candidatos
- criacao de vagas
- consulta de perfil de candidato autenticado

A aplicacao utiliza JPA/Hibernate para persistencia e Spring Security com filtros customizados para validacao de token JWT.

## Tecnologias

- Java 21
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Bean Validation (Jakarta Validation)
- PostgreSQL 13
- Docker e Docker Compose
- Lombok
- java-jwt (Auth0)
- BCrypt

## Estrutura do projeto

```text
src/main/java/br/com/fabricio/gestao_vagas
|- modules
|  |- candidate
|  |  |- controllers
|  |  |- dto
|  |  |- useCases
|  |- company
|     |- controllers
|     |- dto
|     |- entities
|     |- repositories
|     |- useCases
|- security
|- providers
|- exceptions
```

Arquivos importantes:

- `src/main/resources/application.properties`
- `docker-compose.yaml`
- `pom.xml`

## Como executar localmente

### Pre-requisitos

- JDK 21
- Docker e Docker Compose
- Maven (ou uso do Maven Wrapper: `./mvnw`)

### 1. Subir o banco de dados

```bash
docker compose up -d
```

### 2. Executar a aplicacao

Linux/macOS:

```bash
./mvnw spring-boot:run
```

Windows (PowerShell/CMD):

```powershell
mvnw.cmd spring-boot:run
```

Aplicacao disponivel em:

- `http://localhost:8081`

## Configuracao

Configuracao padrao atual:

- API na porta `8081`
- PostgreSQL em `localhost:5480`
- Banco: `gestao_vagas`
- Usuario: `admin`
- Senha: `admin`
- `spring.jpa.hibernate.ddl-auto=update`

Exemplo de propriedades principais:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5480/gestao_vagas
spring.datasource.username=admin
spring.datasource.password=admin
server.port=8081
spring.jpa.hibernate.ddl-auto=update
security.token.secret=...
security.token.secret.candidate=...
```

## Autenticacao e seguranca

- A API utiliza JWT para autenticacao.
- As rotas abaixo sao publicas:
	- `/candidate/`
	- `/company/`
	- `/company/auth`
	- `/candidate/auth`
- Demais rotas exigem autenticacao.
- Senhas sao armazenadas com hash BCrypt.

## Endpoints

Base URL local: `http://localhost:8081`

### Publicos

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| POST | `/candidate/` | Cadastra candidato |
| POST | `/candidate/auth` | Autentica candidato e retorna token |
| POST | `/company/` | Cadastra empresa |
| POST | `/company/auth` | Autentica empresa e retorna token |

Observacao:

- O controller de empresa usa `@RequestMapping("/")` sem metodo explicito em `/company/`. Na pratica, recomenda-se consumir como `POST` para cadastro.

### Protegidos

| Metodo | Endpoint | Descricao |
| --- | --- | --- |
| GET | `/candidate/` | Busca perfil do candidato autenticado |
| POST | `/company/job/` | Cria vaga vinculada a empresa autenticada |
| POST | `/candidate/job/` | Cria vaga via rota de candidato (endpoint existente no codigo) |

## Exemplos de requisicao

### Criar candidato

```bash
curl -X POST http://localhost:8081/candidate/ \
	-H "Content-Type: application/json" \
	-d '{
		"name": "Fabricio",
		"username": "fabricio",
		"email": "fabricio@email.com",
		"password": "12345678",
		"description": "Dev Java",
		"curriculum": "Link do curriculo"
	}'
```

### Autenticar empresa

```bash
curl -X POST http://localhost:8081/company/auth \
	-H "Content-Type: application/json" \
	-d '{
		"username": "empresa1",
		"password": "12345678"
	}'
```

### Criar vaga (empresa autenticada)

```bash
curl -X POST http://localhost:8081/company/job/ \
	-H "Content-Type: application/json" \
	-H "Authorization: Bearer SEU_TOKEN_AQUI" \
	-d '{
		"description": "Desenvolvedor Java Senior",
		"benefits": "VR, VA, Plano de Saude",
		"level": "Senior"
	}'
```

## Testes

Executar testes com:

```bash
./mvnw test
```

No Windows:

```powershell
mvnw.cmd test
```

## Melhorias futuras

- adicionar documentacao OpenAPI/Swagger
- incluir versionamento de schema com Flyway ou Liquibase
- separar ambientes (dev/homolog/prod) via profiles
- melhorar cobertura de testes
- mover secrets para variaveis de ambiente
