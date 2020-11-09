package pcimcioch.gitlabci.dsl.include

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class IncludeDslTest : DslTestBase<IncludeDsl>(IncludeDsl.serializer()) {

    @Test
    fun `should create empty`() {
        // given
        val testee = IncludeDsl()

        // then
        assertDsl(testee,
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
        assertDsl(testee,
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
        val localDsl = createIncludeLocal("local 1")
        val file1Dsl = createIncludeFile("project 1", "file 1")
        val file2Dsl = createIncludeFile("project 2", "file 2", "ref 2")
        val templateDsl = createIncludeTemplate("template 1")
        val remoteDsl = createIncludeRemote("remote 1")
        val testee = IncludeDsl().apply {
            +localDsl
            +file1Dsl
            +file2Dsl
            +templateDsl
            +remoteDsl
        }

        // then
        assertDsl(testee,
                """
                    - local: "local 1"
                    - project: "project 1"
                      file: "file 1"
                    - project: "project 2"
                      file: "file 2"
                      ref: "ref 2"
                    - template: "template 1"
                    - remote: "remote 1"
                """
        )
    }
}