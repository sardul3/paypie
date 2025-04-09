package io.github.sardul3.expense.architecture;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.core.importer.ImportOption;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses;
import static com.tngtech.archunit.library.dependencies.SlicesRuleDefinition.slices;

public class ArchitectureTest {
    private static final String DOMAIN_MODEL = "..domain.model..";
    private static final String DOMAIN_SERVICE = "..domain.service..";
    private static final String ADAPTER_IN = "..adapter.in..";
    private static final String ADAPTER_OUT = "..adapter.out..";
    private static final String PORT_IN = "..application.port.in..";
    private static final String PORT_OUT = "..application.port.out..";
    private static final String APPLICATION = "..application..";

    private static JavaClasses classes;

    @BeforeAll
    static void setup() {
        classes = new ClassFileImporter()
                .withImportOption(new ImportOption.DoNotIncludeTests())
                .importPackages("io.github.sardul3.expense");
    }

    @Test
    void domainEntitiesShouldBeInDomainModel() {
        classes()
                .that().areAnnotatedWith(io.github.sardul3.expense.domain.common.annotation.DomainEntity.class)
                .should().resideInAPackage(DOMAIN_MODEL)
                .check(classes);
    }


    @Test
    void primaryAdaptersShouldOnlyDependOnInputPorts() {
        noClasses()
                .that().resideInAPackage(ADAPTER_IN)
                .should().dependOnClassesThat().resideInAnyPackage(ADAPTER_OUT, DOMAIN_MODEL, DOMAIN_SERVICE)
                .check(classes);
    }

    @Test
    void domainShouldNotDependOnAdapters() {
        noClasses()
                .that().resideInAPackage("..domain..")
                .should().dependOnClassesThat().resideInAnyPackage("..adapter..")
                .check(classes);
    }

    @Test
    void portsShouldNotDependOnAdapters() {
        noClasses()
                .that().resideInAnyPackage(PORT_IN, PORT_OUT)
                .should().dependOnClassesThat().resideInAnyPackage("..adapter..")
                .check(classes);
    }

    @Test
    void noCyclicDependencies() {
        slices()
                .matching("io.github.sardul3.expense.(*)..")
                .should().beFreeOfCycles()
                .check(classes);
    }

    @Test
    void controllerMustNotAccessDomainDirectly() {
        noClasses()
                .that().areAnnotatedWith(io.github.sardul3.expense.adapter.common.PrimaryAdapter.class)
                .should().dependOnClassesThat().resideInAnyPackage(DOMAIN_MODEL, DOMAIN_SERVICE)
                .check(classes);
    }

    @Test
    void repositoriesMustNotAccessInputPorts() {
        noClasses()
                .that().areAnnotatedWith(io.github.sardul3.expense.adapter.common.SecondaryAdapter.class)
                .should().dependOnClassesThat().resideInAnyPackage(PORT_IN)
                .check(classes);
    }

    @Test
    void onlyAnnotatedDomainEntitiesInDomainModel() {
        classes()
                .that().resideInAPackage(DOMAIN_MODEL)
                .should().beAnnotatedWith(io.github.sardul3.expense.domain.common.annotation.DomainEntity.class)
                .orShould().beAnnotatedWith(io.github.sardul3.expense.domain.common.annotation.AggregateRoot.class)
                .check(classes);
    }

    @Test
    void primaryShouldNotUseSecondaryAndViceVersa() {
        noClasses()
                .that().areAnnotatedWith(io.github.sardul3.expense.adapter.common.PrimaryAdapter.class)
                .should().dependOnClassesThat().areAnnotatedWith(io.github.sardul3.expense.adapter.common.SecondaryAdapter.class)
                .check(classes);

        noClasses()
                .that().areAnnotatedWith(io.github.sardul3.expense.adapter.common.SecondaryAdapter.class)
                .should().dependOnClassesThat().areAnnotatedWith(io.github.sardul3.expense.adapter.common.PrimaryAdapter.class)
                .check(classes);
    }


}
