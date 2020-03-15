package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class ImageDslTest : DslTestBase() {

    @Test
    fun `should create image from name`() {
        // given
        val testee = image("image 1")

        // then
        assertDsl(ImageDsl.serializer(), testee,
                """
                    name: "image 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create image from name and block`() {
        // given
        val testee = image("image 1") {
            entrypoint("cmd 1", "cmd 2")
        }

        // then
        assertDsl(ImageDsl.serializer(), testee,
                """
                    name: "image 1"
                    entrypoint:
                    - "cmd 1"
                    - "cmd 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create image from block`() {
        // given
        val testee = image {
            name = "image 1"
            entrypoint(listOf("cmd 1"))
        }

        // then
        assertDsl(ImageDsl.serializer(), testee,
                """
                    name: "image 1"
                    entrypoint:
                    - "cmd 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create image with empty entrypoint`() {
        // given
        val testee = image("image 1") {
            entrypoint()
        }

        // then
        assertDsl(ImageDsl.serializer(), testee,
                """
                    name: "image 1"
                    entrypoint: []
                """.trimIndent()
        )
    }

    @Test
    fun `should create image with one element entrypoint`() {
        // given
        val testee = image("image 1") {
            entrypoint("cmd 1")
        }

        // then
        assertDsl(ImageDsl.serializer(), testee,
                """
                    name: "image 1"
                    entrypoint:
                    - "cmd 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create image with multiple elements entrypoint`() {
        // given
        val testee = image("image 1") {
            entrypoint("cmd 1", "cmd 2")
        }

        // then
        assertDsl(ImageDsl.serializer(), testee,
                """
                    name: "image 1"
                    entrypoint:
                    - "cmd 1"
                    - "cmd 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should override entrypoint`() {
        // given
        val testee = image("image 1") {
            entrypoint("cmd 1", "cmd 2")
            entrypoint("cmd 3", "cmd 4", "cmd 5")
        }

        // then
        assertDsl(ImageDsl.serializer(), testee,
                """
                    name: "image 1"
                    entrypoint:
                    - "cmd 3"
                    - "cmd 4"
                    - "cmd 5"
                """.trimIndent()
        )
    }

    @Test
    fun `should not validate null name`() {
        // given
        val testee = image {
            entrypoint("cmd 1")
        }

        // then
        assertDsl(ImageDsl.serializer(), testee,
                """
                    entrypoint:
                    - "cmd 1"
                """.trimIndent(),
                "[image] name 'null' is incorrect"
        )
    }

    @Test
    fun `should not validate empty name`() {
        // given
        val testee = image("") {
            entrypoint("cmd 1")
        }

        // then
        assertDsl(ImageDsl.serializer(), testee,
                """
                    name: ""
                    entrypoint:
                    - "cmd 1"
                """.trimIndent(),
                "[image] name '' is incorrect"
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = image("image 1") {
            entrypoint = listOf("cmd 1", "cmd 2")
        }

        // then
        assertDsl(ImageDsl.serializer(), testee,
                """
                    name: "image 1"
                    entrypoint:
                    - "cmd 1"
                    - "cmd 2"
                """.trimIndent()
        )
    }
}