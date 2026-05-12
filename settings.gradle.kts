pluginManagement {
    repositories {
        google()
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
}

rootProject.name = "UltraSwipeRefresh"
include(
    ":app",
    ":refresh",
    ":refresh-indicator-classic",
    ":refresh-indicator-progress",
    ":refresh-indicator-lottie"
)
