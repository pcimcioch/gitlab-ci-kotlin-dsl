package pcimcioch.gitlabci.dsl.include

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class IncludeDslTest : DslTestBase<IncludeDsl>(IncludeDsl.serializer()) {

    @Test
    fun `should create empty`() {
        // given
        val testee = IncludeDsl()

        // then
        assertDsl(
            testee,
            """
                    []
                """
        )
    }

    @Test
    fun `should create full`() {
        // given
        val testee = IncludeDsl().apply {
            local("local 1")
            file("project 1", "file 1")
            template("template 1")
            remote("remote 1")
            local("local 2")
            file("project 2", "file 2", "ref 2")
            template("template 2")
            remote("remote 2")
        }

        // then
        assertDsl(
            testee,
            """
                    - local: "local 1"
                    - project: "project 1"
                      file: "file 1"
                    - template: "template 1"
                    - remote: "remote 1"
                    - local: "local 2"
                    - project: "project 2"
                      file: "file 2"
                      ref: "ref 2"
                    - template: "template 2"
                    - remote: "remote 2"
                """
        )
    }

    @Test
    fun `should add using unary plus`() {
        // given
        val local1Dsl = createIncludeLocal("local 1")
        val local2Dsl = createIncludeLocal("local 2") {
            inputs { "a" to "b" }
        }
        val file1Dsl = createIncludeFile("project 1", "file 1")
        val file2Dsl = createIncludeFile("project 2", "file 2", "ref 2")
        val file3Dsl = createIncludeFile("project 3", "file 3") {
            inputs { "c" to "d" }
        }
        val file4Dsl = createIncludeFile("project 4", "file 4", "ref 4") {
            inputs { "e" to "f" }
        }
        val template1Dsl = createIncludeTemplate("template 1")
        val template2Dsl = createIncludeTemplate("template 2") {
            inputs { "g" to "h" }
        }
        val remote1Dsl = createIncludeRemote("remote 1")
        val remote2Dsl = createIncludeRemote("remote 2") {
            inputs { "i" to "j" }
        }
        val testee = IncludeDsl().apply {
            +local1Dsl
            +local2Dsl
            +file1Dsl
            +file2Dsl
            +file3Dsl
            +file4Dsl
            +template1Dsl
            +template2Dsl
            +remote1Dsl
            +remote2Dsl
        }

        // then
        assertDsl(
            testee,
            """
                    - local: "local 1"
                    - local: "local 2"
                      inputs:
                        "a": "b"
                    - project: "project 1"
                      file: "file 1"
                    - project: "project 2"
                      file: "file 2"
                      ref: "ref 2"
                    - project: "project 3"
                      file: "file 3"
                      inputs:
                        "c": "d"
                    - project: "project 4"
                      file: "file 4"
                      ref: "ref 4"
                      inputs:
                        "e": "f"
                    - template: "template 1"
                    - template: "template 2"
                      inputs:
                        "g": "h"
                    - remote: "remote 1"
                    - remote: "remote 2"
                      inputs:
                        "i": "j"
                """
        )
    }

    @Test
    fun `should be equal`() {
        // given
        val testee = IncludeDsl().apply {
            local("local 1")
            file("project 1", "file 1")
            template("template 1")
            remote("remote 1")
            local("local 2")
            file("project 2", "file 2", "ref 2")
            template("template 2")
            remote("remote 2")
        }

        val expected = IncludeDsl().apply {
            local("local 1")
            file("project 1", "file 1")
            template("template 1")
            remote("remote 1")
            local("local 2")
            file("project 2", "file 2", "ref 2")
            template("template 2")
            remote("remote 2")
        }

        // then
        assertEquals(expected, testee)
    }

    @Test
    fun `should serialize includes with inputs`() {
        // given
        val testee = IncludeDsl().apply {
            local("local 1") {
                inputs {
                    "env" to "prod"
                    "retries" to 3
                    "debug" to true
                }
            }
            file("project 1", "file 1") {
                inputs(mapOf("nested" to mapOf("subKey" to "subVal"), "list" to listOf("a", "b")))
            }
            template("template 1") {
                inputs {
                    "param" to "val"
                }
            }
            remote("remote 1") {
                inputs {
                    "active" to false
                }
            }
        }

        // then
        assertDsl(
            testee,
            """
                    - local: "local 1"
                      inputs:
                        "env": "prod"
                        "retries": 3
                        "debug": true
                    - project: "project 1"
                      file: "file 1"
                      inputs:
                        "nested":
                          "subKey": "subVal"
                        "list":
                        - "a"
                        - "b"
                    - template: "template 1"
                      inputs:
                        "param": "val"
                    - remote: "remote 1"
                      inputs:
                        "active": false
                """
        )
    }

    @Test
    fun `should validate empty inputs`() {
        // given
        val testee = IncludeDsl().apply {
            local("local 1") {
                inputs {}
            }
        }

        // then
        assertDsl(
            testee,
            """
                    - local: "local 1"
                      inputs: {}
                """,
            "[include][inputs] inputs map cannot be empty"
        )
    }

    @Test
    fun `should check equality with inputs`() {
        // given
        val testee = IncludeDsl().apply {
            local("local 1") {
                inputs { "a" to "b" }
            }
        }
        val expected = IncludeDsl().apply {
            local("local 1") {
                inputs { "a" to "b" }
            }
        }
        val different = IncludeDsl().apply {
            local("local 1") {
                inputs { "a" to "c" }
            }
        }

        // then
        assertEquals(expected, testee)
        assertEquals(expected.hashCode(), testee.hashCode())
        assertNotEquals(different, testee)
    }
}
