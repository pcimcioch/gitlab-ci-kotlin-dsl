# List of Gitlab CI features

## Table of contents

- [How to start](#how-to-start)
- [default](#default)
- [inherit](#inherit)
- [image](#image)
- [services](#services)
- [script](#script)
- [before_script](#before_script)
- [after_script](#after_script)
- [stage](#stage)
- [only](#only)
- [except](#except)
- [rules](#rules)
- [tags](#tags)
- [allow_failure](#allow_failure)
- [when](#when)
- [environment](#environment)
- [cache](#cache)
- [artifacts](#artifacts)
- [dependencies](#dependencies)
- [needs](#needs)
- [coverage](#coverage)
- [retry](#retry)
- [timeout](#timeout)
- [parallel](#parallel)
- [trigger](#trigger)
- [interruptible](#interruptible)
- [resource_group](#resource_group)
- [include](#include)
- [extends](#extends)
- [pages](#pages)
- [variables](#variables)
- [release](#release)
- [secrets](#secrets)

## How to start

This simple code will create `.gitlab-ci.yml` file in working directory containing definition for three stages (`test`, `build` and `release`) and three jobs
```kotlin
gitlabCi {
    stages("test", "build", "release")

    job("test application") {
        stage = "test"
        image("openjdk:8-alpine")
        script("./gradlew test")
    }

    job("build application") {
        stage = "build"
        image("openjdk:8-alpine")
        script("./gradlew build")
    }

    job("release application") {
        stage = "release"
        image("openjdk:8-alpine")
        script("./gradlew publish")
    }
}
```

If you want to save generated yaml to different location, you can pass custom writer. Note, that the writer will not be closed automatically
```kotlin
val str = StringWriter()
gitlabCi(writer = str) {
    // definitions
}

println(str)
```

Defined configuration is validated for few simple error cases by default. If configuration is incorrect, `IllegalArgumentException` will be thrown.
Validation can be disabled
```kotlin
val str = StringWriter()
gitlabCi(validate = false) {
    // definitions
}

println(str)
```

Main `gitlabCi` block can be configured using different methods:
```kotlin
gitlabCi {
    workflow {
        rules {
            // more details in 'rules' section of this document
        }
    }

    // local include can be added using varargs
    include("file 1", "file 2")
    // or any iterable
    include(listOf("file 3", "file 4"))
    // or block
    include {
        // more details in 'include' section of this document
    }
    
    // stages can be added using varargs
    stages("stage 1", "stage 2")
    // or any iterable
    stages(listOf("stage 3", "stage 4"))
    // or block
    stages {
        // more details in 'stages' section of this document
    }

    default {
        // more details in other sections of this document
    }

    // job can be added using name and block
    job("jobName2") {
        // more details in other sections of this document
    }
}

// jobs can also be created outside of gitlabCi
val job1 = createJob("jobName") {
    script("command")
}
// it can later be added using unary plus operator
gitlabCi { 
    +job1
    +methodReturningJob()
}
```


## default

<https://docs.gitlab.com/ee/ci/yaml/#setting-default-parameters>

```kotlin
gitlabCi { 
    default { 
        // those elements can be configured in default block
        // more details and configuration options can be found in each element section in this document
        image("openjdk:8-alpine")
        services("service 1", "service 2")
        cache("file 1", "file 2")
        tags("tag 1", "tag 2")
        artifacts("artifact 1", "artifact 2")
        retry(1)
        timeout = Duration(days = 5)
        interruptible = true
        beforeScript("command 1", "command 2")
        afterScript("command 1", "command 2")
    }
}
```

## inherit

<https://docs.gitlab.com/ee/ci/yaml/#inherit>

```kotlin
gitlabCi {
    job("jobName") {
        inherit {
            // default and variables can be configured as boolean attributes
            default(true)
            variables(true)

            // or as collection of specific elements
            // which can be added as vararg
            default(InheritDefaultType.AFTER_SCRIPT, InheritDefaultType.ARTIFACTS)
            variables("variable1", "variable2")
            // or any iterable
            default(listOf(InheritDefaultType.CACHE, InheritDefaultType.IMAGE))
            variables(listOf("variable3", "variable4"))
        }
    }
}

// inherit can also be created outside of gitlabCi
val inherit1 = createInherit { 
    default(true)
}

// it can later be added to any job. It may be useful to reuse inherit blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured inherit in this job
        inherit = inherit1
    }
    job("secondJob") {
        inherit = inherit1
    }
}
```

## image

<https://docs.gitlab.com/ee/ci/yaml/#image> 

```kotlin
gitlabCi {
    job("jobName") {
        // image can be configured as string with image name
        image("alpine")
        // or block with more complex configuration
        image {
            name = "alpine"
            // entrypoint commands can be added as vararg arguments
            entrypoint("cmd1", "cmd2")
            // or any iterable
            entrypoint(listOf("cmd3", "cmd4"))
            // entrypoint can also be accessed directly. Note that this will override any previously configured entrypoint in this image
            entrypoint = mutableListOf("cmd1", "cmd2")
        }
        // or name and block
        image("alpine") {
            entrypoint("cmd1", "cmd2")
        }
    }
}

// image can also be created outside of gitlabCi from name
val image1 = createImage("alpine")
// or block
val image2 = createImage { 
    name = "alpine"
}
// or name and block
val image3 = createImage("alpine") {
    entrypoint("cmd1", "cmd2")
}

// it can later be added to any job. It may be useful to reuse image blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured image in this job
        image = image1
    }
    job("secondJob") {
        image = image1
    }
}
```

## services

<https://docs.gitlab.com/ee/ci/yaml/#services>

```kotlin
gitlabCi {
    job("jobName") {
        // services can be added as vararg arguments
        services("service 1", "service 2")
        // or any iterable
        services(listOf("service 1", "service 2"))
        // or services block can be used for more complex services
        services {
            // just service name can be used
            service("service 1")
            // or block
            service {
                name = "service 2"
                alias = "alias 2"
                // entrypoint commands can be added as vararg arguments
                entrypoint("cmd1", "cmd2")
                // or any iterable
                entrypoint(listOf("cmd3", "cmd4"))
                // entrypoint can also be accessed directly. Note that this will override any previously configured entrypoint in this service
                entrypoint = mutableListOf("cmd1", "cmd2")
                // commands can be added as vararg arguments
                cmd("cmd1", "cmd2")
                // or any iterable
                cmd(listOf("cmd3", "cmd4"))
                // cmd can also be accessed directly. Note that this will override any previously configured cmd in this service
                cmd = mutableListOf("cmd1", "cmd2")
            }
            // or name and block
            service("service 3") {
                alias = "alias 3"
            }
        }
    }
}

// service and services can also be created outside of gitlabCi
// from name
val service1 = createService("service 1")
// or name and block
val service2 = createService("service 2") {
    alias = "alias 2"
}
// or just block
val service3 = createService {
    name = "service 3"
}

// services can also be created outside of gitlabCi from names as vararg
val services1 = createServices("service 1", "service 2")
// or any iterable
val services2 = createServices(listOf("service 3", "service 4"))
// or block
val services3 = createServices {
    // service can be added using unary plus operator
    +service1
}

// it can later be added to any job. It may be useful to reuse services blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured services in this job
        services = services1
    }
    job("secondJob") {
        services = services1
    }
}
```

## script

<https://docs.gitlab.com/ee/ci/yaml/#script>

```kotlin
gitlabCi {
    job("jobName") {
        // commands can be added as vararg arguments
        script("command 1", "command 2")
        // or any iterable
        script(listOf("command 3", "command 4"))
        // or by block
        script {
            // in block commands can be added using 'exec' method
            exec("command 5")
            // or unary plus
            +"command6"
        }
    }
}

// script can also be created outside of gitlabCi from vararg arguments
val script1 = createScript("command 1", "command 2")
// or any iterable
val script2 = createScript(listOf("command 1", "command 2"))
// or block
val script3 = createScript {
    exec("command1")
    exec("command2")
}

// it can later be added to any job. It may be useful to reuse script blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured script in this job
        script = script1
    }
    job("secondJob") {
        script = script1
    }
}
```

## before_script

<https://docs.gitlab.com/ee/ci/yaml/#before_script-and-after_script>

```kotlin
gitlabCi {
    job("jobName") {
        // commands can be added as vararg arguments
        beforeScript("command 1", "command 2")
        // or any iterable
        beforeScript(listOf("command 3", "command 4"))
        // or by block
        beforeScript {
            // in block commands can be added using 'exec' method
            exec("command 5")
            // or unary plus
            +"command6"
        }
    }
}

// beforeScript can also be created outside of gitlabCi from vararg arguments
val before1 = createBeforeScript("command 1", "command 2")
// or any iterable
val before2 = createBeforeScript(listOf("command 1", "command 2"))
// or block
val before3 = createBeforeScript {
    exec("command1")
    exec("command2")
}

// it can later be added to any job. It may be useful to reuse beforeScript blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured beforeScript in this job
        beforeScript = before1
    }
    job("secondJob") {
        beforeScript = before1
    }
}
```

## after_script

<https://docs.gitlab.com/ee/ci/yaml/#before_script-and-after_script>

```kotlin
gitlabCi {
    job("jobName") {
        // commands can be added as vararg arguments
        afterScript("command 1", "command 2")
        // or any iterable
        afterScript(listOf("command 3", "command 4"))
        // or by block
        afterScript {
            // in block commands can be added using 'exec' method
            exec("command 5")
            // or unary plus
            +"command6"
        }
    }
}

// afterScript can also be created outside of gitlabCi from vararg arguments
val after1 = createAfterScript("command 1", "command 2")
// or any iterable
val after2 = createAfterScript(listOf("command 1", "command 2"))
// or block
val after3 = createAfterScript {
    exec("command1")
    exec("command2")
}

// it can later be added to any job. It may be useful to reuse afterScript blocks
gitlabCi { 
    job("firstJob") {
        // Note that this will override any previously configured afterScript in this job
        afterScript = after1
    }
    job("secondJob") {
        afterScript = after1
    }
}
```

## stage

<https://docs.gitlab.com/ee/ci/yaml/#stage>

```kotlin
gitlabCi {
    // stages can be added as vararg arguments
    stages("stage 1", "stage 2")
    // or any iterable
    stages(listOf("stage 3", "stage 4"))
    // or by block
    stages {
        // in block stages can be added using 'stage' method
        stage("stage 5")
        // or unary plus
        +"stage 6"
        // stages can also be accessed directly. Note that this will override any previously configured stages
        stages = mutableListOf("stage 1", "stage 2")
    }

    job("jobName") {
        stage = "stage 1"
    }
}
```

## only

<https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic> 

```kotlin
gitlabCi {
    job("jobName") {
        // only refs can be added as vararg arguments
        only("branch1", "branch2")
        // or any iterable
        only(listOf("branch3", "branch4"))
        // or block
        only {
            kubernetes = KubernetesState.ACTIVE
            // refs, changes and variables can be added as vararg arguments
            refs("branch5", "branch6")
            changes("file 1", "file 2")
            variables("\$VAR1", "\$VAR2 == 'test'")
            // or any iterable
            refs(listOf("branch7", "branch8"))
            changes(listOf("file 3", "file 4"))
            variables(listOf("\$VAR3", "\$VAR4 ~= 'test'"))
            // they can also be accessed directly. Note that this will override any previously configured refs, changes and variables
            refs = mutableSetOf("branch1", "branch2")
            changes = mutableSetOf("file 1", "file 2")
            variables = mutableSetOf("\$VAR1", "\$VAR2 == 'test'")
            // some predefined refs can be easily configured using helper methods
            branches()
            tags()
            api()
            external()
            pipelines()
            pushes()
            schedules()
            triggers()
            web()
            mergeRequests()
            externalPullRequests()
            chat()
            master()
        }
    }
}

// only can also be created outside of gitlabCi from vararg of refs
val only1 = createOnlyExcept("branch1", "branch2")
// or any iterable
val only2 = createOnlyExcept(listOf("branch1", "branch2"))
// or block
val only3 = createOnlyExcept {
    refs("branch1", "branch2")
}

// it can later be added to any job. It may be useful to reuse only blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured only in this job
        only = only1
    }
    job("secondJob") {
        only = only1
    }
}
```

## except

<https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic> 

```kotlin
gitlabCi {
    job("jobName") {
        // except refs can be added as vararg arguments
        except("branch1", "branch2")
        // or any iterable
        except(listOf("branch3", "branch4"))
        // or block
        except {
            kubernetes = KubernetesState.ACTIVE
            // refs, changes and variables can be added as vararg arguments
            refs("branch5", "branch6")
            changes("file 1", "file 2")
            variables("\$VAR1", "\$VAR2 == 'test'")
            // or any iterable
            refs(listOf("branch7", "branch8"))
            changes(listOf("file 3", "file 4"))
            variables(listOf("\$VAR3", "\$VAR4 ~= 'test'"))
            // they can also be accessed directly. Note that this will override any previously configured refs, changes and variables
            refs = mutableSetOf("branch1", "branch2")
            changes = mutableSetOf("file 1", "file 2")
            variables = mutableSetOf("\$VAR1", "\$VAR2 == 'test'")
            // some predefined refs can be easily configured using helper methods
            branches()
            tags()
            api()
            external()
            pipelines()
            pushes()
            schedules()
            triggers()
            web()
            mergeRequests()
            externalPullRequests()
            chat()
            master()
        }
    }
}

// except can also be created outside of gitlabCi from vararg of refs
val except1 = createOnlyExcept("branch1", "branch2")
// or any iterable
val except2 = createOnlyExcept(listOf("branch1", "branch2"))
// or block
val except3 = createOnlyExcept {
    refs("branch1", "branch2")
}

// it can later be added to any job. It may be useful to reuse except blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured except in this job
        except = except1
    }
    job("secondJob") {
        except = except1
    }
}
```

## rules

<https://docs.gitlab.com/ee/ci/yaml/#rules>

```kotlin
gitlabCi {
    job("jobName") {
        rules {
            rule {
                ifCondition = "condition"
                whenRun = WhenRunType.DELAYED
                startIn = Duration(months = 1)
                allowFailure = true
                // changes and exists can be added as vararg arguments
                changes("file 1", "file 2")
                exists("file 3", "file 4")
                // or any iterable
                changes(listOf("file 5", "file 6"))
                exists(listOf("file 7", "file 8"))
                // they can also be accessed directly. Note that this will override any previously configured changes and exists in this rule
                changes = mutableSetOf("file 1", "file 2")
                exists = mutableSetOf("fiule 3", "file 4")
            }
        }
    }
}

// rule and rules can also be created outside of gitlabCi
val rule1 = createRule { 
    ifCondition = "condition"
}

val rules1 = createRules {
    // rule can be added using unary plus operator
    +rule1
}

// it can later be added to any job. It may be useful to reuse rules blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured rules in this job
        rules = rules1
    }
    job("secondJob") {
        rules = rules1
    }
}
```

## tags

<https://docs.gitlab.com/ee/ci/yaml/#tags>  

```kotlin
gitlabCi {
    job("jobName") {
        // tags can be added as vararg arguments
        tags("tag 1", "tag 2")
        // or any iterable
        tags(listOf("tag 3", "tag 4"))
        // tags can also be accessed directly. Note that this will override any previously configured tags
        tags = mutableSetOf("tag 1", "tag 2")
    }
}
```

## allow_failure

<https://docs.gitlab.com/ee/ci/yaml/#allow_failure>

```kotlin
gitlabCi {
    job("jobName") {
        allowFailure = true
    }
}
```

## when

<https://docs.gitlab.com/ee/ci/yaml/#when> 

```kotlin
gitlabCi {
    job("jobName") {
        whenRun = WhenRunType.DELAYED
        startIn = Duration(hours = 1)
    }
}
```

## environment

<https://docs.gitlab.com/ee/ci/yaml/#environment>  

```kotlin
gitlabCi {
    job("jobName") {
        // environment can be configured by name
        environment("dev")
        // or by block
        environment {
            name = "staging"
            url = "https://test.com"
            onStop = "job2"
            action = EnvironmentAction.STOP
            autoStopIn = Duration(minutes = 20)
            // kubernetes can be created from namespace
            kubernetes("namespace")
            // or block
            kubernetes {
                namespace = "namespace"
            }
            // or name and block
            kubernetes("namespace1") {
                namespace = "namespace2"
            }
        }
        // or by name and block
        environment("production") {
            url = "https://prod.test.com"
        }
    }

    // instead of operating on strings, onStop can also operate on JobDsl
    val job1 = job("job 1") {}
    job("lastJob") {
        environment("test") {
            onStop(job1)
        }
    }
}

// environment can also be created outside of gitlabCi from name
val env1 = createEnvironment("dev")
// or block
val env2 = createEnvironment {
    name = "staging"
}
// or name and block
val env3 = createEnvironment("production") {
    url = "https://test.com"
}

// it can later be added to any job. It may be useful to reuse environment blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured environment in this job
        environment = env1
    }
    job("secondJob") {
        environment = env1
    }
}
```

## cache

<https://docs.gitlab.com/ee/ci/yaml/#cache> 

Github supports multiple caches <https://docs.gitlab.com/ee/ci/caching/#use-multiple-caches>. Each `cache` method will add new cache

```kotlin
gitlabCi { 
    job("jobName") {
        // paths to cache can be added as vararg arguments
        cache("file1", "file2")
        // or any iterable
        cache(listOf("file3", "file4"))
        // or by block
        cache { 
            untracked = true
            policy = CachePolicy.PULL
            whenCache = WhenCacheType.ALWAYS
            // paths can be added as vararg arguments
            paths("file5", "file6")
            // or any iterable
            paths(listOf("file7", "file8"))
            // paths can also be accessed directly. Note that this will override any previously defined paths in this cache
            paths = mutableSetOf("file1", "file2")
            
            // path key can be string
            key("someKey")
            // or more complex object
            key { 
                prefix = "p_"
                // key files can be added as vararg arguments
                files("f1", "f2")
                // or any iterable
                files("f3", "f4")
                // key files can also be accessed directly. Note that this will override any previously defined files in this key
                files = mutableSetOf("f1", "f2")
            }
        }
    }
}

// cache can also be created outside of gitlabCi from vararg of paths
val cache1 = createCache("file1", "file2")
// or any iterable
val cache2 = createCache(mutableSetOf("file3", "file4"))
// or using block
val cache3 = createCache {
    paths("f1", "f2")
    policy = CachePolicy.PULL
}

// it can later be added to any job. It may be useful to reuse cache blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured cache in this job
        cache = mutableListOf(cache1)
    }
    job("secondJob") {
        cache = mutableListOf(cache1)
    }
    job("thirdJob") {
        // unary plus can be used
        +cache3
    }
}

// cache key can also be created outside of gitlabCi from block
val key1 = createCacheKey {
    files("file1", "file2")
    prefix = "pref"
}

// it can later be added to any job. It may be useful to reuse cache key blocks
gitlabCi {
    job("firstJob") {
        cache {
          key(key1)
        }
    }
    job("secondJob") {
        cache {
          key(key1)
        }
    }
}
```

## artifacts

<https://docs.gitlab.com/ee/ci/yaml/#artifacts> 

```kotlin
gitlabCi {
    job("jobName") {
        // paths to artifacts can be added as vararg arguments
        artifacts("file1", "file2")
        // or any iterable
        artifacts(listOf("file3", "file4"))
        // or by block
        artifacts {
            name = "art1"
            exposeAs = "My artefact"
            untracked = true
            whenUpload = WhenUploadType.ON_FAILURE
            expireIn = Duration(months = 1)
            // paths can be added as vararg arguments
            paths("file5", "file6")
            // or any iterable
            paths(listOf("file7", "file8"))
            // paths can also be accessed directly. Note that this will override any previously defined paths in this artifacts
            paths = mutableSetOf("file1", "file2")

            // exclude can be added as vararg arguments
            exclude("exclude1", "exclude2")
            // or any iterable
            exclude(listOf("exclude3", "exclude4"))
            // paths can also be accessed directly. Note that this will override any previously defined paths in this artifacts
            exclude = mutableSetOf("exclude1", "exclude2")

            reports {
                // every predefined report files can be configured as varargs or any iterable
                junit("junit 1", "junit 2")
                junit(listOf("junit 3", "junit 4"))
                // they can also be accessed directly. Note that this will override any previously defined paths
                junit = mutableSetOf("junit 1", "junit 2")

                dotenv("dotenv 1", "dotenv 2")
                dotenv(listOf("dotenv 3", "dotenv 4"))
                dotenv = mutableSetOf("dotenv 1", "dotenv 2")

                codequality("codequality 1", "codequality 2")
                codequality(listOf("codequality 3", "codequality 4"))
                codequality = mutableSetOf("codequality 1", "codequality 2")

                sast("sast 1", "sast 2")
                sast(listOf("sast 3", "sast 4"))
                sast = mutableSetOf("sast 1", "sast 2")

                dependencyScanning("dependencyScanning 1", "dependencyScanning 2")
                dependencyScanning(listOf("dependencyScanning 3", "dependencyScanning 4"))
                dependencyScanning = mutableSetOf("dependencyScanning 1", "dependencyScanning 2")

                containerScanning("containerScanning 1", "containerScanning 2")
                containerScanning(listOf("containerScanning 3", "containerScanning 4"))
                containerScanning = mutableSetOf("containerScanning 1", "containerScanning 2")

                dast("dast 1", "dast 2")
                dast(listOf("dast 3", "dast 4"))
                dast = mutableSetOf("dast 1", "dast 2")

                licenseManagement("licenseManagement 1", "licenseManagement 2")
                licenseManagement(listOf("licenseManagement 3", "licenseManagement 4"))
                licenseManagement = mutableSetOf("licenseManagement 1", "licenseManagement 2")

                licenseScanning("licenseScanning 1", "licenseScanning 2")
                licenseScanning(listOf("licenseScanning 3", "licenseScanning 4"))
                licenseScanning = mutableSetOf("licenseScanning 1", "licenseScanning 2")

                performance("performance 1", "performance 2")
                performance(listOf("performance 3", "performance 4"))
                performance = mutableSetOf("performance 1", "performance 2")

                metrics("metrics 1", "metrics 2")
                metrics(listOf("metrics 3", "metrics 4"))
                metrics = mutableSetOf("metrics 1", "metrics 2")

                cobertura("cobertura 1", "cobertura 2")
                cobertura(listOf("cobertura 3", "cobertura 4"))
                cobertura = mutableSetOf("cobertura 1", "cobertura 2")

                loadPerformance("loadPerformance 1", "loadPerformance 2")
                loadPerformance(listOf("loadPerformance 3", "loadPerformance 4"))
                loadPerformance = mutableSetOf("loadPerformance 1", "loadPerformance 2")

                terraform("terraform 1", "terraform 2")
                terraform(listOf("terraform 3", "terraform 4"))
                terraform = mutableSetOf("terraform 1", "terraform 2")
            }
        }
    }
}

// artifacts can also be created outside of gitlabCi from vararg of paths
val artifacts1 = createArtifacts("file1", "file2")
// or any iterable
val artifacts2 = createArtifacts(mutableSetOf("file3", "file4"))
// or using block
val artifacts3 = createArtifacts {
    paths("f1", "f2")
    whenUpload = WhenUploadType.ALWAYS
}

// it can later be added to any job. It may be useful to reuse artifacts blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured artifacts in this job
        artifacts = artifacts1
    }
    job("secondJob") {
        artifacts = artifacts1
    }
}
```

## dependencies

<https://docs.gitlab.com/ee/ci/yaml/#dependencies> 

```kotlin
gitlabCi {
    job("jobName") {
        // dependencies can be added as vararg arguments
        dependencies("job 1", "job 2")
        // or any iterable
        dependencies("job 3", "job 4")
        // dependencies can also be accessed directly. Note that this will override any previously configured dependencies
        dependencies = mutableSetOf("job 1", "job 2")
    }

    // instead of operating on strings, dependencies can also operate on JobDsl
    val job1 = job("job 1") {}
    val job2 = job("job 2") {}
    val job3 = job("job 3") {}
    val job4 = job("job 4") {}
    
    job("lastJob") {
        // dependencies can be added as vararg arguments
        dependencies(job1, job2)
        // or any iterable
        dependencies(listOf(job3, job4))
    }
}
```

## needs

<https://docs.gitlab.com/ee/ci/yaml/#needs>    

```kotlin
gitlabCi {
    job("jobName") {
        // needs can be added as vararg arguments
        needs("job 1", "job 2")
        // or any iterable
        needs(listOf("job 1", "job 2"))
        // or needs block can be used for more complex needs
        needs {
            // just job name can be used
            needJob("job 1")
            // or block
            needJob { 
                job = "job2"
                ref = "ref"
                project = "project"
                artifacts = true
            }
            // or name and block
            needJob("job3") {
                artifacts = true
            }
        }
    }

    // instead of operating on strings, needs can also operate on JobDsl
    val job1 = job("job 1") {}
    val job2 = job("job 2") {}
    val job3 = job("job 3") {}
    val job4 = job("job 4") {}
    job("lastJob") {
        // needs can be added as vararg arguments
        needs(job1, job2)
        // or any iterable
        needs(listOf(job3, job4))
        // or in block
        needs {
            // using just job
            needJob(job1)
            // or block for more complex setup
            needJob(job2) {
                artifacts = true
            }
        }
    }
}

// needJob and needs can also be created outside of gitlabCi
val job1 = createJob("job1") {}
val job2 = createJob("job2") {}
// from job
val needJob1 = createNeedJob(job1)
// or job and block
val needJob2 = createNeedJob(job2) {
    artifacts = true
}
// or name
val needJob3 = createNeedJob("job3")
// or name and block
val needJob4 = createNeedJob("job4") {
    artifacts = true
}
// or just block
val needJob5 = createNeedJob { 
    job = "job5"
}

// needs can also be created outside of gitlabCi from names as vararg
val needs1 = createNeeds("job1", "job2")
// or any iterable
val needs2 = createNeeds(listOf("job3", "job4"))
// or job vararg
val needs3 = createNeeds(job1, job2)
// or any iterable
val needs4 = createNeeds(listOf(job1, job2))
// or block
val needs5 = createNeeds {
    // needJobs can be added using unary plus operator
    +needJob1
}

// it can later be added to any job. It may be useful to reuse needs blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured needs in this job
        needs = needs1
    }
    job("secondJob") {
        needs = needs2
    }
}
```

## coverage

<https://docs.gitlab.com/ee/ci/yaml/#coverage>  

```kotlin
gitlabCi {
    job("jobName") {
        coverage = "/Code coverage: \\d+\\.\\d+/"
    }
}
```

## retry

<https://docs.gitlab.com/ee/ci/yaml/#retry> 

```kotlin
gitlabCi {
    job("jobName") {
        // retry can be configured from int
        retry(2)
        // or using block
        retry {
            max = 3
            // when can be added as vararg arguments
            whenRetry(WhenRetryType.SCHEDULER_FAILURE, WhenRetryType.STALE_SCHEDULE)
            // or any iterable
            whenRetry(listOf(WhenRetryType.JOB_EXECUTION_TIMEOUT, WhenRetryType.ARCHIVED_FAILURE))
            // when can also be accessed directly. Note that this will override any previously configured when
            whenRetry = mutableSetOf(WhenRetryType.UNMET_PREREQUISITES, WhenRetryType.SCRIPT_FAILURE)
        }
        // or using int and block
        retry(2) {
            whenRetry(WhenRetryType.ALWAYS)
        }
    }
}

// retry can also be created outside of gitlabCi from int
val retry1 = createRetry(2)
// or block
val retry2 = createRetry {
    max = 2
}
// or int and block
val retry3 = createRetry(2) {
    whenRetry(WhenRetryType.ALWAYS)
}

// it can later be added to any job. It may be useful to reuse retry blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured retry in this job
        retry = retry1
    }
    job("secondJob") {
        retry = retry1
    }
}
```

## timeout

<https://docs.gitlab.com/ee/ci/yaml/#timeout> 

```kotlin
gitlabCi {
    job("jobName") {
        timeout = Duration(minutes = 30)
    }
}
```

## parallel

<https://docs.gitlab.com/ee/ci/yaml/#parallel> 

```kotlin
gitlabCi {
    job("jobName") {
        parallel = 2
    }
}
```

Note: `parallel:matrix` is currently not supported

## trigger

<https://docs.gitlab.com/ee/ci/yaml/#trigger>  

```kotlin
gitlabCi { 
    job("jobName") {
        // trigger can be created from project name
        trigger("test/project")
        // optionally, branch and strategy can be passed
        trigger("test/project", "test-branch", TriggerStrategy.DEPEND)
        // or project name and block
        trigger("test/project") {
            branch = "test-branch"
        }
        // optionally, branch and strategy can be passed
        trigger("test/project", "test-branch", TriggerStrategy.DEPEND) {
            branch = "test-branch-2"
        }
        // or from block
        trigger {
            project = "test/project"
            branch = "test-branch"
            strategy = TriggerStrategy.DEPEND
            include { 
                local("localFile")
                file("project 1", "file 1", "ref 1")
                file("project 2", "file 2")
                artifact("file", "job1")
            }
        }
    }
    
    // include artifact in trigger also accepts job
    val job = job("job1") {}
    job("lastJob") {
        trigger {
            include {
                artifact("file", job)
            }
        }
    }
}

// include can also be created outside of gitlabCi block
val include1 = createTriggerInclude { 
    local("localFile")
}

// trigger can also be created outside of gitlabCi
// from project name
val trigger1 = createTrigger("test/project")
// optionally, branch and strategy can be passed
val trigger2 = createTrigger("test/project", "test-branch", TriggerStrategy.DEPEND)
// or project name and block
val trigger3 = createTrigger("test/project") {
    branch = "test-branch"
}
// optionally, branch and strategy can be passed
val trigger4 = createTrigger("test/project", "test-branch", TriggerStrategy.DEPEND) {
    branch = "test-branch-2"
}
// or block
val trigger5 = createTrigger {
    // include can be accessed directly
    include = include1
}

// it can later be added to any job. It may be useful to reuse trigger blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured trigger in this job
        trigger = trigger1
    }
    job("secondJob") {
        trigger = trigger1
    }
}
```

## interruptible

<https://docs.gitlab.com/ee/ci/yaml/#interruptible> 

```kotlin
gitlabCi {
    job("jobName") {
       interruptible = true
    }
}
```

## resource_group

<https://docs.gitlab.com/ee/ci/yaml/#resource_group>

```kotlin
gitlabCi {
    job("jobName") {
       resourceGroup = "group1"
    }
}
```

## include

<https://docs.gitlab.com/ee/ci/yaml/#include>  

```kotlin
gitlabCi {
    // local include can be added using varargs
    include("file 1", "file 2")
    // or any iterable
    include(listOf("file 3", "file 4"))
    // or block
    include {
        local("localFile")
        file("project", "file")
        file("project", "file", "ref")
        template("template")
        remote("remoteFile")
    }
}
```

## extends

<https://docs.gitlab.com/ee/ci/yaml/#extends>   

```kotlin
gitlabCi {
    job("jobName") {
        // extends can be added as vararg arguments
        extends("job 1", "job 2")
        // or any iterable
        extends("job 3", "job 4")
        // extends can also be accessed directly. Note that this will override any previously configured extends
        extends = mutableListOf("job 1", "job 2")
    }

    // instead of operating on strings, extends can also operate on JobDsl
    val job1 = job("job 1") {}
    val job2 = job("job 2") {}
    val job3 = job("job 3") {}
    val job4 = job("job 4") {}
    job("lastJob") {
        // extends can be added as vararg arguments
        extends(job1, job2)
        // or any iterable
        extends(listOf(job3, job4))
    }
}
```

## pages

<https://docs.gitlab.com/ee/ci/yaml/#pages> 

```kotlin
gitlabCi {
    // this will create job names "pages", with artifacts.paths=["public"] and only.refs="master"
    pages {
        script("command")
    }
}
```

## variables

<https://docs.gitlab.com/ee/ci/yaml/#variables> 

```kotlin
gitlabCi {
    job("jobName") {
        // variables can be added as map of string -> any
        variables(mapOf("variable1" to "value1"))
        // or map of enum -> any
        variables(mapOf(RunnerSettingsVariables.GIT_DEPTH to 2))
        // or block
        variables {
            // single variable can be added using add
            add("variable2", "value2")
            // enum version is also available
            add(RunnerSettingsVariables.GET_SOURCES_ATTEMPTS, 4)
            // infix notation to can be used
            "variable3" to "value3"
            // enum version is also available
            RunnerSettingsVariables.RESTORE_CACHE_ATTEMPTS to 3
            // methods for runner specific environment variables can be used
            gitStrategy(GitStrategyType.CLONE)
            gitSubmoduleStrategy(GitSubmoduleStrategyType.RECURSIVE)
            gitCheckout(true)
            gitClean("-ffdx")
            getResourcesAttempts(3)
            artifactDownloadAttempts(2)
            restoreCacheAttempts(1)
            gitDepth(4)
            gitClonePath("path/to/dir")
            disableGitClean()
            gitFetchExtraFlags("extra")
            executorJobSectionAttempts(5)
            cacheFallbackKey("key")
        }
        // you can also mix map and block
        variables(mapOf("variable4" to "value4")) {
            "variable5" to "value5"
        }
        variables(mapOf(RunnerSettingsVariables.GIT_DEPTH to 2)) {
            "variable6" to "value6"
        }
        // variables can also be accessed directly. Note that this will override any previously configured variables in this release
        variables = mutableMapOf("variable1", "variable2")
    }
}

// variables can also be created outside of gitlabCi from string -> any map
val variables1 = createVariables(mapOf("variable1" to "value1"))
// or enum -> any map
val variables2 = createVariables(mapOf(RunnerSettingsVariables.GIT_DEPTH to 2))
// or block
val variables3 = createVariables {
    "variable1" to "key1"
}
// or map and block
val variables4 = createVariables(mapOf("variable1" to "value1")) {
    "variable2" to "value2"
}
val variables5 = createVariables(mapOf(RunnerSettingsVariables.GIT_DEPTH to 2)) {
    "variable1" to "key1"
}

// it can later be added to any job. It may be useful to reuse variables blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured variables in this job
        variables = variables1
    }
    job("secondJob") {
        variables = variables1
    }
}
```

## release

<https://docs.gitlab.com/ee/ci/yaml/#release>

```kotlin
gitlabCi {
    job("jobName") {
        // release can be configured as string with tag name
        release("version-1.2.3")
        // or block with more complex configuration
        release {
            tagName = "version-1.2.3"
            name = "Release 1.2.3"
            ref = "master" 
            // milestones can be added as vararg arguments
            milestones("milestone 1", "milestone 2")
            // or any iterable
            milestones(listOf("milestone 3", "milestone 4"))
            // milestones can also be accessed directly. Note that this will override any previously configured milestones in this release
            milestones = mutableSetOf("milestone 1", "milestone 2")
        }
        // or tag name and block
        release("version-1.2.3") {
            name = "Release 1.2.3"
        }
    }
}

// release can also be created outside of gitlabCi from tag name
val release1 = createRelease("version-1.2.3")
// or block
val release2 = createRelease { 
    tagName = "version-1.2.3"
}
// or tag name and block
val release3 = createRelease("version-1.2.3") {
    name = "Release 1.2.3"
}

// it can later be added to any job. It may be useful to reuse release blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured release in this job
        release = release1
    }
    job("secondJob") {
        release = release1
    }
}

// also, releaseImage() can be used to automaticaly set job image to "registry.gitlab.com/gitlab-org/release-cli:latest"
gitlabCi {
    job("someJob") {
        releaseImage()
        script("echo Releasing")
        release("version-1.2.3")
    }
}
```

## secrets

<https://docs.gitlab.com/ee/ci/yaml/#secrets

```kotlin
gitlabCi {
    job("jobName") {
        // secrets can be added as map of string -> string
        secrets(mapOf("variable1" to "vault1"))
        // or block
        secrets {
            // single secret can be added using add
            add("variable2", "vault2")
            // block version is also available
            add("variable3") {
              engine {
                name = "name"
                path = "path"
              }
              path = "path"
              fileld = "field"
            }
            // infix notation to can be used
            "variable4" to "vault4"
            // block version is also available
            "variable5" to {
              engine {
                name = "name2"
                path = "path2"
              }
              path = "path2"
              fileld = "field2"
            }
        }
        // you can also mix map and block
        secrets(mapOf("variable6" to "vault6")) {
            "variable7" to "vault7"
        }
    }
}

// secrets can also be created outside of gitlabCi from string -> string map
val secrets1 = createSecrets(mapOf("variable1" to "vault1"))
// or block
val secrets2 = createSecrets {
    "variable1" to "vault1"
}
// or map and block
val secrets4 = createSecrets(mapOf("variable1" to "vault1")) {
    "variable2" to "vault2"
}

// it can later be added to any job. It may be useful to reuse secrets blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured secrets in this job
        secrets = secrets1
    }
    job("secondJob") {
        secrets = secrets1
    }
}

// single secret can also be created outside of gitlabCi from string
val secret1 = createSecret("vault1")
// or block
val secret2 = createSecret {
  engine {
    name = "name"
    path = "path"
  }
  path = "path"
  fileld = "field"
}
val secret3 = createSecret("vault3")

// it can later be added to any secrets. It may be useful to reuse secret blocks
gitlabCi {
    job("jobName") {
        // existing secrets can be added as map of string -> SecretDsl
        secrets(mapOf("variable1" to secret1)) {
            // single secret can be added using add
            add("variable2", secret2)
            // infix notation to can be used
            "variable3" to secret3
            // secrets can also be accessed directly. Note that this will override any previously configured secrets in this release
            secrets = mutableMapOf("variable1", secret1)
        }
    }
}

// also secrets can also be created outside of gitlabCi from string -> SecretDsl map
val secrets5 = createSecrets(mapOf("variable1" to secret1))
// or map and block
val secrets6 = createSecrets(mapOf("variable1" to secret1)) {
  "variable2" to secret2
}

// vault can also be created outside of gitlabCi
val vault1 = createSecretVault {
  engine {
    name = "name"
    path = "path"
  }
  path = "path"
  fileld = "field"
}

// it can later be added to any secret. It may be useful to reuse vault blocks
val secret4 = createSecret(vault1)

// vault engine can also be created outside of gitlabCi
val vaultEngine1 = createSecretVaultEngine {
  name = "name"
  path = "path"
}

// it can later be added to any vault. It may be useful to reuse engine blocks
val vault2 = createSecretVault {
  // Note that this will override any previously configured engine in this vault
  engine = vaultEngine1
  path = "path"
  fileld = "field"
}
```