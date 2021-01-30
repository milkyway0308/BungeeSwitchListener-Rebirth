plugins {
    id("java")
    id("maven-publish")
}

buildscript {
    repositories {
        mavenCentral()
    }
}


group = "skywolf46"
version = properties["version"] as String

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
    compileOnly(project(":global"))
    compileOnly("skywolf46:commandannotation:2.0.6")
    compileOnly(files("V:/API/Java/Minecraft/Bukkit/Spigot/Spigot 1.12.2.jar"))
    testCompile("io.netty:netty-testsuite-http2:4.1.54.Final")

}



tasks {
    jar {
        archiveBaseName.set("BungeeSwitchListener-Client")
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
        create<MavenPublication>("jar") {
            from(components["java"])
            groupId = "skywolf46"
            artifactId = "bsl.client"
            version = properties["version"] as String
            pom {
                url.set("https://github.com/milkyway0308/BungeeSwitchListener-Rebirth.git")
            }
        }
    }
}
