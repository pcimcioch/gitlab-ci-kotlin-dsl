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

TODO supported

## image

<https://docs.gitlab.com/ee/ci/yaml/#image> 

TODO supported

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

TODO supported

## coverage

<https://docs.gitlab.com/ee/ci/yaml/#coverage>  

Not supported yet

## retry

<https://docs.gitlab.com/ee/ci/yaml/#retry> 

TODO supported

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

TODO supported
