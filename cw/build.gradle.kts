plugins {
    kotlin("jvm")

    application
}

group = "ru.eltech.sapr"
version = "1.0"

repositories {
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation("com.gitlab.dheid.fontchooser:fontchooser:fontchooser-parent-2.4")
}

application {
    mainClass.set("MainKt")
}

tasks.jar {
    manifest {
        attributes["Main-Class"] = "MainKt"
    }
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)

    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE

}
