import kr.entree.spigradle.kotlin.*

plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    id("kr.entree.spigradle") version "2.2.4"
    kotlin("jvm") version "1.6.10"
}

group = "io.github.cloudate9.instaminedeepslate"
version = "2.1.0"

repositories {
    mavenCentral()
    codemc()
    papermc()
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.18.2-R0.1-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:3.0.0")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }

    compileJava {
        options.release.set(17)
    }

    shadowJar {
        archiveFileName.set(rootProject.name + "-" + rootProject.version + ".jar")
        relocate("kotlin", "${rootProject.group}.dependencies.kotlin")
        relocate("org.bstats", "${rootProject.group}.dependencies.org.bstats")
        relocate("org.intellij", "${rootProject.group}.dependencies.org.intellij")
        relocate("org.jetbrains", "${rootProject.group}.dependencies.org.jetbrains")
    }
}

spigot {
    authors = listOf("Cloudate9")
    apiVersion = "1.18"
    description = "Makes mining deepslate with a Netherite pickaxe, eff 5 and haste II instant."
    website = "https://cloudate9.github.io/"
    commands {
        create("instaminedeepslate") {
            aliases = listOf("imd", "imds")
            description = "Toggle the breaking speed of deepslate."
            usage = "/imd [enable/disable]"
        }
    }
    permissions {
        create("instaminedeepslate.configure") {
            description = "Configure IMD settings, such as if deepslate can be broken immediately, given the right conditions."
            defaults = "op"
        }

        create("instaminedeepslate.eligible") {
            description = "If someone is eligible to instantly break deepslate, given the right conditions."
            defaults = "true"
        }

        create("instaminedeepslate.updater") {
            description = "If someone is allowed to see when the plugin has updates"
            defaults = "op"
        }

    }
}
