# Padrões de arquitetura e desenvolvimento

Este documento fixa o que o repositório trata como **referência normativa**.

## Fronteiras de camada (DDD no monólito)

- **Domínio** (`..domain..` em cada bounded context): regras de negócio, invariantes de agregado e VOs **sem** `jakarta.persistence`. Entidades JPA (`*Entity`) vivem em `infra.persistence.entity` ou `adapters.out.persistence.entity`, com mappers para o modelo de domínio.
- **Aplicação**: orquestra casos de uso, transações e portas (`application.port` / interfaces). Pode usar Spring (`@Service`, `@Transactional`) e repositórios de infra quando ainda não existir porta dedicada; evoluções futuras extraem portas para reduzir acoplamento.
- **Adaptadores de entrada**: Spring MVC (`adapters.in.web`, `..api..`) — HTTP, DTOs e mapeamento; sem regra de negócio além de validação de contrato.
- **Adaptadores de saída**: JPA, SMTP, etc. — implementam portas ou expõem detalhes de persistência atrás da porta quando aplicável.

### Verificação automática

- Testes **ArchUnit** em `src/test/java/br/com/oficina/architecture/ArchitectureRulesTest.java` falham o build se o domínio passar a depender de `br.com.oficina..adapters..`, `br.com.oficina..api..` ou `br.com.oficina..infra..`, ou de pacotes `jakarta.persistence` nos pacotes `..domain..`.

## Qualidade e testes

- **JaCoCo**: cobertura mínima de instruções nos pacotes `**/domain/**` (ver `pom.xml`).
- **Testes WebMvc** (`@WebMvcTest`) para contratos HTTP e segurança; integração com **Testcontainers** quando o fluxo exige DB real (excluídos no perfil `ci` igual ao GitHub Actions).
- Perfil Maven **`ci`**: `mvn -B -Pci verify` — sem Docker para Testcontainers.

## Segurança e API

- API stateless com **OAuth2 Resource Server** (JWT); rotas administrativas com role `ADMIN`, públicas sem credencial, cliente com `ROLE_CLIENTE` conforme `SecurityConfig`.
- **Actuator**: exposição apenas de `health`, `info`, `prometheus` (métricas para observabilidade).

## Observabilidade

- Logs estruturados em perfil Kubernetes; métricas Micrometer/Prometheus; correlação via `X-Correlation-Id` (filtro global).

## Convenções de código

- **`.editorconfig`**: UTF-8, indentação consistente, newline final.
- Mensagens de commit e PRs: descrevem a alteração de forma objetiva.
