plugins {
    id("refinedarchitect.common")
}

refinedarchitect {
    common()
    testing()
    javadoc()
    publishing {
        maven = true
    }
}

base {
    archivesName.set("refinedstorage-platform-common")
}

dependencies {
    api(project(":refinedstorage-platform-api"))
    api(project(":refinedstorage-core-api"))
    api(project(":refinedstorage-resource-api"))
    api(project(":refinedstorage-storage-api"))
    api(project(":refinedstorage-network-api"))
    api(project(":refinedstorage-network"))
    api(project(":refinedstorage-query-parser"))
    api(project(":refinedstorage-grid-api"))
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testImplementation(libs.assertj)
    testImplementation(libs.equalsverifier)
    testRuntimeOnly(libs.junit.engine)
}
