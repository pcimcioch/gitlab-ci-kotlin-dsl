# Pure Gradle Example

To generate `.gitlab-ci.yml` file using DSL, you can create simple gradle project.

As gradle already supports kotlin, we can use this, to create gradle task generating `/gitlab-ci.yml` from kotlin DSL

To run it, you have to have Java installed (version 1.8+)
```shell script
./gradlew generateGitlabCi
```

DSL is defined in gradle task in [build.gradle.kts](build.gradle.kts) file.

This will create or override `.gitlab-ci.yml` file.

After that, you have to copy `.gitlab-ci.yml` file to your project repository that will be using it. Of course, you can also embed this small project in your own project.
