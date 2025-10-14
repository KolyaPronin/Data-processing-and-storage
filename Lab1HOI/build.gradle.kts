plugins {
    id("java")
    id("application")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.bouncycastle:bcprov-jdk18on:1.78.1")
    implementation("org.bouncycastle:bcpkix-jdk18on:1.78.1")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

application {
    mainClass.set("main.Main")
}

tasks.test {
    useJUnitPlatform()
}

// --- fat jar ---
tasks.register<Jar>("fatJar") {
    group = "build"
    description = "Собирает jar со всеми зависимостями"
    archiveClassifier.set("all")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

    // Основной код проекта
    from(sourceSets.main.get().output)

    // Все runtime зависимости
    from({
        configurations.runtimeClasspath.get()
            .filter { it.name.endsWith("jar") }
            .map { zipTree(it) }
    }) {
        // ИСКЛЮЧАЕМ подписи из META-INF, чтобы не было SecurityException
        exclude("META-INF/*.SF", "META-INF/*.DSA", "META-INF/*.RSA")
    }

    // Манифест
    manifest {
        attributes["Main-Class"] = "main.Main"
    }
}

