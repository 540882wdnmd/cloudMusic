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
        maven {
            url=uri("https://jitpack.io")
            url=uri("https://s01.oss.sonatype.org/content/groups/public")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven {
            url=uri("https://jitpack.io")
            url=uri("https://s01.oss.sonatype.org/content/groups/public")
        }
    }
}

rootProject.name = "cloudMusic"
include(":app")
include(":centre")
include(":utils")

