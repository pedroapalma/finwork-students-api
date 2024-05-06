package com.ppalma.studentsapi;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.classes;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import org.junit.jupiter.api.Test;

public class MyArchitectureRules {

  public static final String BASE_PACKAGE = "com.ppalma.studentsapi";

  public static final String DOMAIN_PACKAGE = "com.ppalma.studentsapi.domain";

  public static final String APPLICATION_PACKAGE = "com.ppalma.studentsapi.application";

  public static final String INFRASTRUCTURE_PACKAGE = "com.ppalma.studentsapi.infrastructure";

  @Test
  void dependencyArchitectureTest() {

    JavaClasses importedClasses = new ClassFileImporter()
        .importPackages(BASE_PACKAGE);

    ArchRule domainRule = classes()
        .that()
        .resideInAPackage(DOMAIN_PACKAGE)
        .should()
        .onlyDependOnClassesThat()
        .resideInAPackage(DOMAIN_PACKAGE)
        .allowEmptyShould(true);

    ArchRule applicationRule = classes()
        .that()
        .resideInAPackage(APPLICATION_PACKAGE)
        .should()
        .onlyDependOnClassesThat()
        .resideInAnyPackage(DOMAIN_PACKAGE, APPLICATION_PACKAGE)
        .allowEmptyShould(true);

    ArchRule infrastructureRule = classes()
        .that()
        .resideInAPackage(INFRASTRUCTURE_PACKAGE)
        .should()
        .dependOnClassesThat()
        .resideInAnyPackage(BASE_PACKAGE)
        .allowEmptyShould(true);

    domainRule.check(importedClasses);
    applicationRule.check(importedClasses);
    infrastructureRule.check(importedClasses);

  }

}

