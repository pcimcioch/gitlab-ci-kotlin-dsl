package pcimcioch.gitlabci.dsl.stage

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase
import pcimcioch.gitlabci.dsl.gitlabCi

internal class StagesDslTest : DslTestBase() {

    @Test
    fun `should create stages from block`() {
        // given
        gitlabCi(writer = writer) {
            stages {
                stage("stage 1")
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "stages":
                    - "stage 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create stages from vararg`() {
        // given
        gitlabCi(writer = writer) {
            stages("stage 1", "stage 2")
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "stages":
                    - "stage 1"
                    - "stage 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create stages from list`() {
        // given
        gitlabCi(writer = writer) {
            stages(listOf("stage 1", "stage 2"))
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "stages":
                    - "stage 1"
                    - "stage 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create stages with multiple commands`() {
        // given
        gitlabCi(writer = writer) {
            stages {
                stage("stage 1")
                stage("stage 2")
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "stages":
                    - "stage 1"
                    - "stage 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create stages with one command`() {
        // given
        gitlabCi(writer = writer) {
            stages {
                stage("stage 1")
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "stages":
                    - "stage 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create stages with no commands`() {
        // given
        gitlabCi(writer = writer) {
            stages {}
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "stages": []
                """.trimIndent()
        )
    }

    @Test
    fun `should add stage with unary plus`() {
        // given
        gitlabCi(writer = writer) {
            stages {
                stage("stage 1")
                +"stage 2"
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "stages":
                    - "stage 1"
                    - "stage 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        gitlabCi(writer = writer) {
            stages {
                stages = mutableListOf("stage 1", "stage 2")
            }
        }

        // then
        assertThat(writer.toString()).isEqualTo(
                """
                    "stages":
                    - "stage 1"
                    - "stage 2"
                """.trimIndent()
        )
    }
}