enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}
dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}
//issue: can't pass "compose-barcode", its duplicate name for sub-module and root project
rootProject.name = "compose-barcode-library"
include(":compose-barcode")
include(":sample")