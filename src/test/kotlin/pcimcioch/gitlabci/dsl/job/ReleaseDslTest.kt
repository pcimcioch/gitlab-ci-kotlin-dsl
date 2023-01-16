package pcimcioch.gitlabci.dsl.job

import kotlinx.datetime.Instant
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class ReleaseDslTest : DslTestBase<ReleaseDsl>(ReleaseDsl.serializer()) {

    @Test
    fun `should create full release`() {
        // given
        val testee = createRelease("tag 1") {
            name = "name 1"
            description = "desc 1"
            ref = "ref 1"
            milestones("mile 1", "mile 2")
            releasedAt = Instant.fromEpochSeconds(1604869132)
        }

        // then
        assertDsl(
            testee,
            """
                    tag_name: "tag 1"
                    name: "name 1"
                    description: "desc 1"
                    ref: "ref 1"
                    milestones:
                    - "mile 1"
                    - "mile 2"
                    released_at: "2020-11-08T20:58:52Z"
                """
        )
    }

    @Test
    fun `should create release from tag name`() {
        // given
        val testee = createRelease("tag 1")

        // then
        assertDsl(
            testee,
            """
                    tag_name: "tag 1"
                """
        )
    }

    @Test
    fun `should create release from tag name and block`() {
        // given
        val testee = createRelease("tag 1") {
            name = "name 1"
        }

        // then
        assertDsl(
            testee,
            """
                    tag_name: "tag 1"
                    name: "name 1"
                """
        )
    }

    @Test
    fun `should create release from block`() {
        // given
        val testee = createRelease {
            name = "name 1"
            tagName = "tag 1"
        }

        // then
        assertDsl(
            testee,
            """
                    tag_name: "tag 1"
                    name: "name 1"
                """
        )
    }

    @Test
    fun `should create release with empty milestones`() {
        // given
        val testee = createRelease("tag 1") {
            milestones()
        }

        // then
        assertDsl(
            testee,
            """
                    tag_name: "tag 1"
                    milestones: []
                """
        )
    }

    @Test
    fun `should create release with one element milestones`() {
        // given
        val testee = createRelease("tag 1") {
            milestones("mile 1")
        }

        // then
        assertDsl(
            testee,
            """
                    tag_name: "tag 1"
                    milestones:
                    - "mile 1"
                """
        )
    }

    @Test
    fun `should create release with multiple elements milestones`() {
        // given
        val testee = createRelease("tag 1") {
            milestones("mile 1", "mile 2")
        }

        // then
        assertDsl(
            testee,
            """
                    tag_name: "tag 1"
                    milestones:
                    - "mile 1"
                    - "mile 2"
                """
        )
    }

    @Test
    fun `should merge milestones`() {
        // given
        val testee = createRelease("tag 1") {
            milestones("mile 1", "mile 2")
            milestones("mile 3", "mile 4", "mile 5")
        }

        // then
        assertDsl(
            testee,
            """
                    tag_name: "tag 1"
                    milestones:
                    - "mile 1"
                    - "mile 2"
                    - "mile 3"
                    - "mile 4"
                    - "mile 5"
                """
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = createRelease("tag 1") {
            milestones = mutableSetOf("mile 1", "mile 2")
        }

        // then
        assertDsl(
            testee,
            """
                    tag_name: "tag 1"
                    milestones:
                    - "mile 1"
                    - "mile 2"
                """
        )
    }

    @Test
    fun `should be equal`() {
        // given
        val testee = createRelease("tag 1") {
            name = "name 1"
            description = "desc 1"
            ref = "ref 1"
            milestones("mile 1", "mile 2")
            releasedAt = Instant.fromEpochSeconds(1604869132)
        }

        val expected = createRelease("tag 1") {
            name = "name 1"
            description = "desc 1"
            ref = "ref 1"
            milestones("mile 1", "mile 2")
            releasedAt = Instant.fromEpochSeconds(1604869132)
        }

        // then
        assertEquals(expected, testee)
    }
}