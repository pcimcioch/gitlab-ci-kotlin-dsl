# Gradle + Kotlin Example

To generate `.gitlab-ci.yml` file using DSL, you can create simple kotlin project.

In this example it's gradle project, but it doesn't have to be. You can use any technology that supports building kotlin.

To run it, you have to have Java installed (version 1.8+)
```shell script
./gradlew run
```

DSL is defined in [com.example.Main](src/main/kotlin/com/example/Main.kt) file.

This will create or override `.gitlab-ci.yml` file.

After that, you have to copy `.gitlab-ci.yml` file to your project repository that will be using it. Of course, you can also embed this small project in your own project.
