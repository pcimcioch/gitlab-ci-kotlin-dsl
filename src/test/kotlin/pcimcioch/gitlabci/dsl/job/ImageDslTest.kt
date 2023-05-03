package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class ImageDslTest : DslTestBase<ImageDsl>(ImageDsl.serializer()) {

    @Test
    fun `should create image from name`() {
        // given
        val testee = createImage("image 1")

        // then
        assertDsl(
            testee,
            """
                    name: "image 1"
                """
        )
    }

    @Test
    fun `should create image from name and block`() {
        // given
        val testee = createImage("image 1") {
            entrypoint("cmd 1", "cmd 2")
        }

        // then
        assertDsl(
            testee,
            """
                    name: "image 1"
                    entrypoint:
                    - "cmd 1"
                    - "cmd 2"
                """
        )
    }

    @Test
    fun `should create image from block`() {
        // given
        val testee = createImage {
            name = "image 1"
            entrypoint(listOf("cmd 1"))
            pullPolicy(PullPolicy.IF_NOT_PRESENT)
        }

        // then
        assertDsl(
            testee,
            """
                    name: "image 1"
                    entrypoint:
                    - "cmd 1"
                    pull_policy:
                    - "if-not-present"
                """
        )
    }

    @Test
    fun `should create image with empty entrypoint`() {
        // given
        val testee = createImage("image 1") {
            entrypoint()
        }

        // then
        assertDsl(
            testee,
            """
                    name: "image 1"
                    entrypoint: []
                """
        )
    }

    @Test
    fun `should create image with one element entrypoint`() {
        // given
        val testee = createImage("image 1") {
            entrypoint("cmd 1")
        }

        // then
        assertDsl(
            testee,
            """
                    name: "image 1"
                    entrypoint:
                    - "cmd 1"
                """
        )
    }

    @Test
    fun `should create image with multiple elements entrypoint`() {
        // given
        val testee = createImage("image 1") {
            entrypoint("cmd 1", "cmd 2")
        }

        // then
        assertDsl(
            testee,
            """
                    name: "image 1"
                    entrypoint:
                    - "cmd 1"
                    - "cmd 2"
                """
        )
    }

    @Test
    fun `should merge entrypoint`() {
        // given
        val testee = createImage("image 1") {
            entrypoint("cmd 1", "cmd 2")
            entrypoint("cmd 3", "cmd 4", "cmd 5")
        }

        // then
        assertDsl(
            testee,
            """
                    name: "image 1"
                    entrypoint:
                    - "cmd 1"
                    - "cmd 2"
                    - "cmd 3"
                    - "cmd 4"
                    - "cmd 5"
                """
        )
    }

    @Test
    fun `should not validate null name`() {
        // given
        val testee = createImage {
            entrypoint("cmd 1")
        }

        // then
        assertDsl(
            testee,
            """
                    entrypoint:
                    - "cmd 1"
                """,
            "[image] name 'null' is incorrect"
        )
    }

    @Test
    fun `should not validate empty name`() {
        // given
        val testee = createImage("") {
            entrypoint("cmd 1")
        }

        // then
        assertDsl(
            testee,
            """
                    name: ""
                    entrypoint:
                    - "cmd 1"
                """,
            "[image] name '' is incorrect"
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = createImage("image 1") {
            entrypoint = mutableListOf("cmd 1", "cmd 2")
        }

        // then
        assertDsl(
            testee,
            """
                    name: "image 1"
                    entrypoint:
                    - "cmd 1"
                    - "cmd 2"
                """
        )
    }

    @Test
    fun `should be equal`() {
        // given
        val testee = createImage("image 1") {
            entrypoint("cmd 1", "cmd 2")
            entrypoint("cmd 3", "cmd 4", "cmd 5")
            pullPolicy(PullPolicy.ALWAYS)
        }

        val expected = createImage("image 1") {
            entrypoint("cmd 1", "cmd 2")
            entrypoint("cmd 3", "cmd 4", "cmd 5")
            pullPolicy(PullPolicy.ALWAYS)
        }

        // then
        assertEquals(expected, testee)
    }
}