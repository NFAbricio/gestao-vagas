Gestão Vagas
===========

Resumo das mudanças recentes
---------------------------
- Atualizado Spring Boot para **4.0.6** (corrige vulnerabilidade no DevTools).
- Fixado o driver PostgreSQL para **org.postgresql:postgresql:42.7.11** (corrige CVE relacionado a SCRAM/PBKDF2).
- Ajustado `java.version` para **21** no `pom.xml` para compatibilidade com o JDK local.

Rotas importantes
-----------------
- Cadastro de vaga da empresa: `POST /company/job/` ([src/main/java/br/com/fabricio/gestao_vagas/modules/company/controllers/JobController.java](src/main/java/br/com/fabricio/gestao_vagas/modules/company/controllers/JobController.java))
- Cadastro de vaga (candidato): `POST /candidate/job/` ([src/main/java/br/com/fabricio/gestao_vagas/modules/candidate/controllers/JobController.java](src/main/java/br/com/fabricio/gestao_vagas/modules/candidate/controllers/JobController.java))

Arquivos alterados
------------------
- `pom.xml` — atualizações de versão e fix do driver ([pom.xml](pom.xml)).
- `src/main/java/.../company/controllers/JobController.java` — rota/bean renomeado.
- `src/main/java/.../candidate/controllers/JobController.java` — rota/bean renomeado.

Como executar localmente
------------------------
Requisitos:

- JDK 21 instalado e ativo.

Comandos úteis:

```bash
java -version
mvn -v
```

Build e testes:

```bash
mvn test
```

Rodar a aplicação:

```bash
mvn spring-boot:run
```

Observações de segurança
-----------------------
- As duas vulnerabilidades inicializadas pelo scanner foram tratadas atualizando as dependências mencionadas acima.
- Mesmo com o driver atualizado, continue a garantir que a aplicação conecte-se apenas a servidores PostgreSQL confiáveis e, quando possível, use TLS com verificação de host/CA configurada.

Próximos passos recomendados
----------------------------
- Revisar endpoints e políticas de autenticação antes de colocar em produção.
- Abrir um PR com estas mudanças e executar o pipeline de CI para validar em ambiente controlado.

Contato
-------
Para qualquer dúvida sobre as mudanças, abra uma issue ou me chame no PR.
