import java.io.File

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

rootProject.name = "android-tv-search-keyboard"
include(":sample")

/**
 * AGP reads sdk.dir or ANDROID_HOME. A clean shell often has neither exported, even when the
 * SDK is installed in the platform default directory. Write local.properties only when it is
 * missing so a checked-out copy can run ./gradlew.
 */
fun ensureAndroidSdkLocation() {
    val localProperties = file("local.properties")
    if (localProperties.exists()) return
    val home = System.getProperty("user.home")
    val candidates = listOf(
        System.getenv("ANDROID_HOME"),
        System.getenv("ANDROID_SDK_ROOT"),
        "$home/Library/Android/sdk",
        "$home/Android/Sdk",
    )
    val sdk = candidates
        .filterNotNull()
        .map { File(it) }
        .firstOrNull { it.resolve("platforms").isDirectory }
        ?: return
    val sdkDir = sdk.absolutePath.replace("\\", "\\\\")
    localProperties.writeText("sdk.dir=$sdkDir${System.lineSeparator()}")
}

ensureAndroidSdkLocation()
