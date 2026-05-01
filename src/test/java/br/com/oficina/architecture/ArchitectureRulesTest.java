package br.com.oficina.architecture;

import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;

/**
 * Garante fronteiras de camada alinhadas ao DDD pragmático do monólito:
 * pacotes {@code domain} não referenciam HTTP, persistência concreta nem infraestrutura técnica.
 */
@AnalyzeClasses(packages = "br.com.oficina")
class ArchitectureRulesTest {

    @ArchTest
    static final ArchRule bounded_context_domain_independent_of_infrastructure =
            noClasses()
                    .that().resideInAnyPackage(
                            "..cadastros..domain..",
                            "..catalogo..domain..",
                            "..ordemservico.domain.."
                    )
                    .should().dependOnClassesThat().resideInAnyPackage(
                            "br.com.oficina..adapters..",
                            "br.com.oficina..api..",
                            "br.com.oficina..infra.."
                    )
                    .because("domínio não deve depender de adaptadores de entrada/saída, controllers ou infraestrutura");

    @ArchTest
    static final ArchRule domain_has_no_jakarta_persistence =
            noClasses()
                    .that().resideInAnyPackage(
                            "..cadastros..domain..",
                            "..catalogo..domain..",
                            "..ordemservico.domain..",
                            "..shared.domain.."
                    )
                    .should().dependOnClassesThat().resideInAnyPackage("jakarta.persistence")
                    .because("entidades JPA ficam em infra/adapters; o núcleo de domínio permanece sem anotações de persistência");
}
