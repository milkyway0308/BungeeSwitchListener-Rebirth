plugins {
    id("java")
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "5.2.0"
}

buildscript {
    repositories {
        mavenCentral()
    }
}


group = "skywolf46"
version = properties["version"] as String

tasks {
    processResources {
        filesMatching(listOf("plugin.yml", "bungee.yml")) {
            expand("version" to project.properties["version"])
        }
    }
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.github.com/milkyway0308/CommandAnnotation") {
        credentials {
            username = properties["gpr.user"] as String
            password = properties["gpr.key"] as String
        }
    }
}


dependencies {
    compileOnly("skywolf46:commandannotation:2.0.6")
    implementation(project(":global"))
    implementation(project(":client"))
    implementation(project(":server"))
}
tasks {
    shadowJar {
        archiveClassifier.set("shaded")
    }
}

publishing {
    repositories {
        maven {
            name = "Github"
            url = uri("https://maven.pkg.github.com/milkyway0308/BungeeSwitchListener-Rebirth")
            credentials {
                username = properties["gpr.user"] as String
                password = properties["gpr.key"] as String
            }
        }
    }
    publications {
        create<MavenPublication>("shadowJar") {
            from(components["java"])
            artifact(tasks.shadowJar.get())
            groupId = "skywolf46"
            artifactId = "bsl"
            version = properties["version"] as String
            pom {
                url.set("https://github.com/milkyway0308/BungeeSwitchListener-Rebirth.git")
            }
        }
    }
}
