import org.sonarqube.gradle.SonarExtension

plugins {
    id("refinedarchitect.root")
    id("refinedarchitect.base")
}

refinedarchitect {
    sonarQube("refinedmods_refinedstorage2", "refinedmods")
}

subprojects {
    group = "com.refinedmods.refinedstorage"
}

project.extensions.getByType<SonarExtension>().apply {
    properties {
        property(
            "sonar.coverage.exclusions",
            "refinedstorage-neoforge/**/*,refinedstorage-fabric/**/*,refinedstorage-common/**/*,refinedstorage-common-api/**/*"
        )
    }
}
