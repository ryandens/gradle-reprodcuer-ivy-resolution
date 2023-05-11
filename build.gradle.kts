import org.gradle.internal.os.OperatingSystem


plugins {
    java
}

val os = OperatingSystem.current()
val arch = providers.systemProperty("os.arch")

val driverOsFilenamePart = when {
    os.isMacOsX && arch.get().contains("aarch64") -> "macOS-ARM64"
    os.isMacOsX && !arch.get().contains("aarch64") -> "macOS-64bit"
    // os.nativePrefix only contains arch metadata for non-mac unix systems
    os.isLinux && os.nativePrefix.contains("arm64") -> "Linux-ARM64"
    os.isLinux && os.nativePrefix.contains("64") -> "Linux-64bit"
    else -> "Linux-32bit"
}


repositories {
    exclusiveContent {
        forRepository {
            ivy {
                url = uri("https://github.com/aquasecurity/")
                patternLayout {
                    artifact("[module]/releases/download/v[revision]/[artifact]_[revision]_[classifier].[ext]")
                }
                metadataSources {
                    artifact()
                }
            }
        }
        filter {
            includeModule("aquasecurity", "trivy")
        }
    }
}

val trivy by configurations.creating {
    isCanBeResolved = true
    isCanBeConsumed = false
}

dependencies {
    trivy("aquasecurity:trivy:0.41.0:$driverOsFilenamePart@tar.gz")
}

tasks.register<Sync>("syncTrivy") {
    from({ tarTree(trivy.singleFile) })
    into(project.layout.buildDirectory.dir("security-scanner/trivy/"))
}
