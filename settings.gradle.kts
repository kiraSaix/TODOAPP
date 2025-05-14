pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    versionCatalogs {
        create("libs") {
            library("androidx-core-ktx", "androidx.core:core-ktx:1.12.0")
            library("androidx-appcompat", "androidx.appcompat:appcompat:1.6.1")
            library("material", "com.google.android.material:material:1.11.0")
            library("androidx-constraintlayout", "androidx.constraintlayout:constraintlayout:2.1.4")
            library("androidx-navigation-fragment-ktx", "androidx.navigation:navigation-fragment-ktx:2.7.7")
            library("androidx-navigation-ui-ktx", "androidx.navigation:navigation-ui-ktx:2.7.7")
            library("gson", "com.google.code.gson:gson:2.10.1")
            library("junit", "junit:junit:4.13.2")
            library("androidx-test-ext-junit", "androidx.test.ext:junit:1.1.5")
            library("androidx-test-espresso-core", "androidx.test.espresso:espresso-core:3.5.1")

            plugin("android-application", "com.android.application").version("8.1.0")
            plugin("kotlin-android", "org.jetbrains.kotlin.android").version("1.9.0")
        }
    }
}

rootProject.name = "todo"
include(":app")
