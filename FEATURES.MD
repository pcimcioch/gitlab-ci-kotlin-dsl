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

## How to start

TODO

## default

<https://docs.gitlab.com/ee/ci/yaml/#setting-default-parameters>

Not supported yet

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
            // entrypoint commands can be appended as vararg arguments
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
        // Note that this will override any previously configured inherit in this job
        image = image1
    }
    job("secondJob") {
        image = image1
    }
}
```

## services

<https://docs.gitlab.com/ee/ci/yaml/#services>

TODO supported

## script

<https://docs.gitlab.com/ee/ci/yaml/#script>

```kotlin
gitlabCi {
    job("jobName") {
        // commands can be appended as vararg arguments
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
        // commands can be appended as vararg arguments
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
        // commands can be appended as vararg arguments
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
    // stages can be appended as vararg arguments
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

Not supported yet

## except

<https://docs.gitlab.com/ee/ci/yaml/#onlyexcept-basic> 

Not supported yet

## rules

<https://docs.gitlab.com/ee/ci/yaml/#rules>

Not supported yet

## tags

<https://docs.gitlab.com/ee/ci/yaml/#tags>  

```kotlin
gitlabCi {
    job("jobName") {
        // tags can be appended as vararg arguments
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

Not supported yet

## cache

<https://docs.gitlab.com/ee/ci/yaml/#cache> 

TODO supported

## artifacts

<https://docs.gitlab.com/ee/ci/yaml/#artifacts> 

TODO supported

## dependencies

<https://docs.gitlab.com/ee/ci/yaml/#dependencies> 

```kotlin
gitlabCi {
    job("jobName") {
        // dependencies can be appended as vararg arguments
        dependencies("job 1", "job 2")
        // or any iterable
        dependencies("job 3", "job 4")
        // dependencies can also be accessed directly. Note that this will override any previously configured dependencies
        dependencies = mutableSetOf("job 1", "job 2")
    }

    // instead of operating on strings, dependencies can also operate on JobDsl
    val job1 = job("job 1")
    val job2 = job("job 2")
    val job3 = job("job 3")
    val job4 = job("job 4")
    
    job("lastJob") {
        // dependencies can be appended as vararg arguments
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
        // needs can be appended as vararg arguments
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
    val job1 = job("job 1")
    val job2 = job("job 2")
    val job3 = job("job 3")
    val job4 = job("job 4")
    job("lastJob") {
        // needs can be appended as vararg arguments
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
val needJob2 = createNeedJob(job2)
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
    // needJobs can be appended using unary plus operator
    +needJob1
}

// it can later be added to any job. It may be useful to reuse needs blocks
gitlabCi {
    job("firstJob") {
        // Note that this will override any previously configured retry in this job
        needs = needs1
    }
    job("secondJob") {
        needs = needs2
    }
}
```

## coverage

<https://docs.gitlab.com/ee/ci/yaml/#coverage>  

Not supported yet

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
            // when can be appended as vararg arguments
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

## trigger

<https://docs.gitlab.com/ee/ci/yaml/#trigger>  

Not supported yet

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

Not supported yet

## extends

<https://docs.gitlab.com/ee/ci/yaml/#extends>   

```kotlin
gitlabCi {
    job("jobName") {
        // extends can be appended as vararg arguments
        extends("job 1", "job 2")
        // or any iterable
        extends("job 3", "job 4")
        // extends can also be accessed directly. Note that this will override any previously configured extends
        extends = mutableListOf("job 1", "job 2")
    }

    // instead of operating on strings, extends can also operate on JobDsl
    val job1 = job("job 1")
    val job2 = job("job 2")
    val job3 = job("job 3")
    val job4 = job("job 4")
    job("lastJob") {
        // extends can be appended as vararg arguments
        extends(job1, job2)
        // or any iterable
        extends(listOf(job3, job4))
    }
}
```

## pages

<https://docs.gitlab.com/ee/ci/yaml/#pages> 

Not supported yet

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
        }
        // you can also mix map and block
        variables(mapOf("variable4" to "value4")) {
            "variable5" to "value5"
        }
        variables(mapOf(RunnerSettingsVariables.GIT_DEPTH to 2)) {
            "variable6" to "value6"
        }
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
