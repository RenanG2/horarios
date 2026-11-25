# API de Horários de Aula

API REST para organização e gerenciamento de horários de aula de professores.
Projeto da Demonstração de Competência – Back End (Unisales).

**Tecnologias:** Java 17, Spring Boot 3.3, Spring Data JPA (Hibernate), Spring Security (HTTP Basic),
Bean Validation, springdoc-openapi (Swagger UI), H2 (padrão) / PostgreSQL (opcional), JUnit 5 + MockMvc.

## Como executar

Requisitos: JDK 17+ e Maven 3.9+ (ou abrir o projeto no IntelliJ/Eclipse/VS Code e rodar `HorariosApplication`).

```bash
mvn spring-boot:run
```

- Swagger UI: http://localhost:8080/swagger-ui.html
- Autenticação (HTTP Basic): usuário `admin` / senha `admin123` (altere em `application.properties`)
- No Swagger, clique em **Authorize**, informe usuário/senha e use **Try it out**.
- Na primeira execução o banco recebe dados de exemplo (`app.seed.enabled=true`).

Testes: `mvn test`

Para usar PostgreSQL: crie o banco `horarios` e rode com `--spring.profiles.active=postgres`
(ajuste `application-postgres.properties`).

## Arquitetura (MVC em camadas)

```
controller  -> recebe HTTP, valida entrada (@Valid), delega ao service
service     -> regras de negócio e transações
repository  -> acesso a dados (Spring Data JPA / ORM)
model       -> entidades JPA (Professor, Disciplina, Turma, Horario)
dto         -> objetos de entrada/saída (a entidade nunca é exposta)
exception   -> exceções de negócio e tratamento global (@RestControllerAdvice)
config      -> segurança, OpenAPI e carga de dados inicial
```

## Modelo de dados

`Horario` (dia da semana, hora início, hora fim) possui `@ManyToOne` para `Professor`, `Disciplina` e `Turma`.

## Endpoints

| Recurso | Operações |
|---|---|
| `/api/professores` `/api/disciplinas` `/api/turmas` `/api/horarios` | GET, GET/{id}, POST, PUT/{id}, DELETE/{id} |
| `GET /api/professores/{id}/horarios` | disciplinas, turmas, dias e horários do professor |
| `GET /api/turmas/{id}/horarios` | professores, disciplinas, dias e horários da turma |

## Regras de negócio

- Hora de fim deve ser posterior à hora de início (400).
- Não permite dois horários sobrepostos para o mesmo professor ou a mesma turma no mesmo dia (409).
- Não permite excluir professor, disciplina ou turma que tenham horários (409).
- E-mail de professor é único (409).

## Exemplo com curl

```bash
curl -u admin:admin123 http://localhost:8080/api/professores/1/horarios

curl -u admin:admin123 -X POST http://localhost:8080/api/horarios \
  -H "Content-Type: application/json" \
  -d '{"diaSemana":"SATURDAY","horaInicio":"08:00","horaFim":"09:40","professorId":1,"disciplinaId":1,"turmaId":1}'
```
