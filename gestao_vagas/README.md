Gestão Vagas
===========

Visão geral
-----------
O projeto é uma API REST em Spring Boot para cadastro de candidatos, empresas e vagas, com persistência em PostgreSQL e autenticação baseada em JWT.

Tecnologias utilizadas
---------------------
- Java 21
- Spring Boot 4.0.6
- Spring Web MVC
- Spring Data JPA
- Spring Security
- Bean Validation (`jakarta.validation`)
- Hibernate
- PostgreSQL
- Docker e Docker Compose
- Lombok
- Auth0 Java JWT
- BCrypt para hash de senha

Banco de dados
--------------
- Banco utilizado: PostgreSQL 13.
- Ambiente local: exposto na porta `5480` e mapeado para `localhost:5480`.
- Banco criado pelo Docker Compose: `gestao_vagas`.
- Usuário: `admin`.
- Senha: `admin`.
- A aplicação usa `spring.jpa.hibernate.ddl-auto=update`, então o schema é ajustado automaticamente a partir das entidades JPA.

Configuração local
-----------------
Requisitos:

- JDK 21.
- Maven disponível no ambiente.
- Docker e Docker Compose para subir o banco.

Comandos úteis:

```bash
docker compose up -d
mvn test
mvn spring-boot:run
```

Aplicação:

- Porta da API: `8081`
- String de conexão: `jdbc:postgresql://localhost:5480/gestao_vagas`

Docker
------
O arquivo [docker-compose.yaml](docker-compose.yaml) sobe apenas o PostgreSQL:

```yaml
services:
	postgres:
		image: postgres:13
		ports:
			- 5480:5432
		environment:
			- POSTGRES_USER=admin
			- POSTGRES_PASSWORD=admin
			- POSTGRES_DB=gestao_vagas
```

Autenticação e segurança
-----------------------
- A segurança usa Spring Security com um filtro próprio em [SecurityFilter.java](src/main/java/br/com/fabricio/gestao_vagas/security/SecurityFilter.java).
- O token JWT é assinado com HMAC usando as chaves definidas em `application.properties`.
- O fluxo de autenticação de empresa gera um token com validade de 2 horas.
- O fluxo de autenticação de candidato existe na camada de use case, mas não há controller exposto para ele no estado atual do projeto.
- Senhas são armazenadas com hash BCrypt, nunca em texto puro.

Endpoints expostos
------------------
### Público

| Método | Endpoint | Descrição | Corpo esperado |
| --- | --- | --- | --- |
| `POST` | `/candidate/` | Cadastra um candidato. | `CandidateEntity` |
| `POST` | `/company/` | Cadastra uma empresa. | `CompanyEntity` |
| `POST` | `/auth/company` | Autentica uma empresa e retorna JWT. | `AuthCompanyDTO` |

### Protegido por JWT

| Método | Endpoint | Descrição | Corpo esperado |
| --- | --- | --- | --- |
| `POST` | `/company/job/` | Cria uma vaga associada à empresa autenticada. | `CreateJobDTO` |

### Observações sobre as rotas

- As rotas `/candidate/`, `/company/` e `/auth/company` estão liberadas na configuração de segurança.
- A criação de vaga em `/company/job/` depende do header `Authorization: Bearer <token>`.
- O `company_id` é extraído do JWT e usado para associar a vaga à empresa logada.
- O endpoint de criação de vaga em `/candidate/job/` existe no código, mas não parece fazer parte do fluxo principal do domínio; ele grava um `JobEntity` diretamente.

Modelos persistidos
-------------------
### `CandidateEntity`
- Tabela/JPA entity: `candiadates`
- Campos principais: `id`, `name`, `username`, `email`, `password`, `description`, `curriculum`, `createdAt`

### `CompanyEntity`
- Tabela/JPA entity: `company`
- Campos principais: `id`, `username`, `email`, `password`, `website`, `name`, `description`, `createdAt`

### `JobEntity`
- Tabela/JPA entity: `job`
- Campos principais: `id`, `benefits`, `level`, `description`, `company_id`, `createdAt`
- Relacionamento: `ManyToOne` com `CompanyEntity` via `company_id`

Regras de negócio observadas
---------------------------
- Cadastro de candidato e empresa valida duplicidade por `username` e `email`.
- Senha de candidato e empresa é criptografada antes do `save`.
- A autenticação de empresa compara a senha enviada com o hash salvo e emite token JWT com `subject` igual ao `id` da empresa.
- O filtro de segurança rejeita requisições com token inválido.
- O tratamento global de validação retorna erros de campo em `400 Bad Request`.

Estrutura geral do projeto
-------------------------
- `modules/candidate`: cadastro e autenticação de candidato, além de entidades e DTOs ligados ao candidato.
- `modules/company`: cadastro, autenticação e criação de vagas da empresa.
- `security`: configuração e filtro de autenticação.
- `providers`: geração e validação de token JWT.
- `exceptions`: tratamento global de erros e mensagens de validação.

Observações finais
------------------
- O projeto já está orientado para execução local com PostgreSQL em container.
- Não há migrations explícitas com Flyway/Liquibase; a modelagem depende do JPA/Hibernate para atualizar o schema.
- Se a intenção for publicar em produção, vale revisar expiração de tokens, proteção das credenciais e externalização das secrets.
