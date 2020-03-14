package pcimcioch.gitlabci.dsl

import pcimcioch.gitlabci.dsl.job.job

fun main() {
    val j1 = job("test") {
        name = "override"
        script {
            exec("test")
        }
    }

    gitlabCi {
        job("test") {
            script {
                exec("one")
                exec("two")
            }
        }
        +j1
    }
}