plugins {
    id("refinedarchitect.fabric")
}

repositories {
    maven {
        name = "ModMenu"
        url = uri("https://maven.terraformersmc.com/")
    }
    maven {
        name = "Cloth Config"
        url = uri("https://maven.shedaniel.me/")
    }
}

refinedarchitect {
    modId = "refinedstorage"
    fabric()
    compileWithProject(project(":refinedstorage-common"))
    compileWithProject(project(":refinedstorage-common-api"))
    addProject(project(":refinedstorage-core-api"))
    addProject(project(":refinedstorage-resource-api"))
    addProject(project(":refinedstorage-storage-api"))
    addProject(project(":refinedstorage-network-api"))
    addProject(project(":refinedstorage-network"))
    addProject(project(":refinedstorage-grid-api"))
    addProject(project(":refinedstorage-query-parser"))
    publishing {
        maven = true
        // curseForge = "243076"
        // modrinth = "refined-storage"
    }
}

base {
    archivesName.set("refinedstorage-fabric")
}

dependencies {
    modApi(libs.cloth.config) {
        exclude(group = "net.fabricmc.fabric-api")
    }
    modApi(libs.teamreborn.energy) {
        exclude(group = "net.fabricmc.fabric-api")
    }
    modApi(libs.modmenu)
    include(libs.cloth.config)
    include(libs.teamreborn.energy)
}
