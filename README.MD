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
  <version>1.0.0</version>
</dependency>
```

```
implementation("com.github.pcimcioch:gitlab-ci-kotlin-dsl:1.0.0")
```

# Supported Gitlab CI configuration parameters

| Block          | Supported | Gitlab Documentation link                                            |
| -------------- | --------- |--------------------------------------------------------------------- |
| default        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#setting-default-parameters>     |
| inherit        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#inherit>                        |
| image          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#image>                          |
| services       | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#services>                       |
| script         | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#script>                         |
| before_script  | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#before_script-and-after_script> |
| after_script   | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#before_script-and-after_script> |
| stage          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#stage>                          |
| only           | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic>               |
| except         | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic>               |
| rules          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#rules>                          |
| tags           | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#tags>                           |
| allow_failure  | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#allow_failure>                  |
| when           | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#when>                           |
| environment    | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#environment>                    |
| cache          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#cache>                          |
| artifacts      | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#artifacts>                      |
| dependencies   | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#dependencies>                   |
| needs          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#needs>                          |
| coverage       | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#coverage>                       |
| retry          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#retry>                          |
| timeout        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#timeout>                        |
| parallel       | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#parallel>                       |
| trigger        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#trigger>                        |
| interruptible  | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#interruptible>                  |
| resource_group | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#resource_group>                 |
| include        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#include>                        |
| extends        | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#extends>                        |
| pages          | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#pages>                          |
| variables      | Yes       | <https://docs.gitlab.com/ee/ci/yaml/#variables>                      |

# Documentation

All supported features are documented in [Features Page](/FEATURES.MD)

# How to use

1. One way to use this DSL is to create custom Kotlin project, that will be responsible for creating `.gitlab-ci.yml` file. [Example](examples/gradle-kotlin/README.MD)
1. Also, if you are using gradle, it already supports kotlin, so you can create simple gradle task to generate `.gitlab-ci.yml` file. [Example](examples/pure-gradle/README.MD)
1. GitlabCi can also generate pipeline automatically using child pipelines feature. [Example](examples/child-pipeline/README.MD)