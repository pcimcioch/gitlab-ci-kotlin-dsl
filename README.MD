# Gitlab CI Kotlin DSL

This is the Kotlin DSL to generate Gitlab CI yaml file

# Example

```kotlin
gitlabCi {
    default {
        image("openjdk:8u162")

        cache("./gradle/wrapper", "./gradle/caches")

        beforeScript("export GRADLE_USER_HOME=\$(pwd)/.gradle")
    }

    stages {
        +"test"
        +"release"
    }

    job("build") {
        stage = "test"
        script("./gradlew clean build")
        artifacts {
            whenUpload = WhenUploadType.ALWAYS
            paths("build/test-results", "build/reports")
            expireIn = Duration(days = 7)
            reports {
                junit("build/test-results/test/TEST-*.xml")
            }
        }
    }

    job("release") {
        stage = "release"
        script("./gradlew publishToMavenLocal")
        only {
            master()
        }
        artifacts {
            whenUpload = WhenUploadType.ON_SUCCESS
            paths("build/libs")
            expireIn = Duration(days = 7)
        }
    }
}
```

# How to download

Library is available in Central Maven Repository
```xml
<dependency>
  <groupId>com.github.pcimcioch</groupId>
  <artifactId>gitlab-ci-kotlin-dsl</artifactId>
  <version>1.2.0</version>
</dependency>
```

```
implementation("com.github.pcimcioch:gitlab-ci-kotlin-dsl:1.2.0")
```

It uses [kotlin-datetime](https://github.com/Kotlin/kotlinx-datetime) api which is in early stage of development and is currently 
only available in [kotlinx](https://bintray.com/kotlin/kotlinx/kotlinx.datetime) bintray repository.

```
repositories {
    maven(url = "https://kotlin.bintray.com/kotlinx/") // soon will be just jcenter()
}
```

# Documentation

All supported features are documented in [Features Page](/FEATURES.MD)

# How to use

1. One way to use this DSL is to create custom Kotlin project, that will be responsible for creating `.gitlab-ci.yml` file. [Example](examples/gradle-kotlin)
1. Also, if you are using gradle, it already supports kotlin, so you can create simple gradle task to generate `.gitlab-ci.yml` file. [Example](examples/pure-gradle)
1. GitlabCi can also generate pipeline automatically using child pipelines feature. [Example](examples/child-pipeline)