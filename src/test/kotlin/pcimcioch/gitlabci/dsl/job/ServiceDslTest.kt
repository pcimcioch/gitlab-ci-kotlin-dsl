package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class ServiceDslTest : DslTestBase() {

    @Test
    fun `should create service from name`() {
        // given
        val testee = service("ser 1")

        // then
        assertDsl(ServiceDsl.serializer(), testee,
                """
                    name: "ser 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create service from name and block`() {
        // given
        val testee = service("ser 1") {
            alias = "test"
        }

        // then
        assertDsl(ServiceDsl.serializer(), testee,
                """
                    name: "ser 1"
                    alias: "test"
                """.trimIndent()
        )
    }

    @Test
    fun `should create service from block`() {
        // given
        val testee = service {
            name = "test"
        }

        // then
        assertDsl(ServiceDsl.serializer(), testee,
                """
                    name: "test"
                """.trimIndent()
        )
    }

    @Test
    fun `should not validate null name`() {
        // given
        val testee = service {
            alias = "test"
        }

        // then
        assertDsl(ServiceDsl.serializer(), testee,
                """
                    alias: "test"
                """.trimIndent(),
                "[service name='null'] name 'null' is incorrect"
        )
    }

    @Test
    fun `should not validate empty name`() {
        // given
        val testee = service("")

        // then
        assertDsl(ServiceDsl.serializer(), testee,
                """
                    name: ""
                """.trimIndent(),
                "[service name=''] name '' is incorrect"
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = service("name") {
            cmd = listOf("c 1", "c 2")
            entrypoint = listOf("e 1", "e 2")
        }

        // then
        assertDsl(ServiceDsl.serializer(), testee,
                """
                    name: "name"
                    cmd:
                    - "c 1"
                    - "c 2"
                    entrypoint:
                    - "e 1"
                    - "e 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should override command and entrypoint`() {
        // given
        val testee = service("name") {
            cmd("c 1", "c 2")
            entrypoint("e 1", "e 2")
            cmd("c 10", "c 20")
            entrypoint("e 10", "e 20")
        }

        // then
        assertDsl(ServiceDsl.serializer(), testee,
                """
                    name: "name"
                    cmd:
                    - "c 10"
                    - "c 20"
                    entrypoint:
                    - "e 10"
                    - "e 20"
                """.trimIndent()
        )
    }

    @Test
    fun `should create empty command and entrypoint`() {
        // given
        val testee = service("name") {
            cmd()
            entrypoint()
        }

        // then
        assertDsl(ServiceDsl.serializer(), testee,
                """
                    name: "name"
                    cmd: []
                    entrypoint: []
                """.trimIndent()
        )
    }

    @Test
    fun `should create one element command and entrypoint`() {
        // given
        val testee = service("name") {
            cmd("c 1")
            entrypoint("e 1")
        }

        // then
        assertDsl(ServiceDsl.serializer(), testee,
                """
                    name: "name"
                    cmd:
                    - "c 1"
                    entrypoint:
                    - "e 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create multiple elements command and entrypoint`() {
        // given
        val testee = service("name") {
            cmd(listOf("c 1", "c 2"))
            entrypoint(listOf("e 1", "e 2"))
        }

        // then
        assertDsl(ServiceDsl.serializer(), testee,
                """
                    name: "name"
                    cmd:
                    - "c 1"
                    - "c 2"
                    entrypoint:
                    - "e 1"
                    - "e 2"
                """.trimIndent()
        )
    }
}

internal class ServiceListDslTest : DslTestBase() {

    @Test
    fun `should create services from block`() {
        // given
        val testee = services {
            service("service 1")
        }

        // then
        assertDsl(ServiceListDsl.serializer(), testee,
                """
                    - name: "service 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create services from vararg list`() {
        // given
        val testee = services("service 1", "service 2")

        // then
        assertDsl(ServiceListDsl.serializer(), testee,
                """
                    - name: "service 1"
                    - name: "service 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create services from list`() {
        // given
        val testee = services(listOf("service 1", "service 2"))

        // then
        assertDsl(ServiceListDsl.serializer(), testee,
                """
                    - name: "service 1"
                    - name: "service 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create empty`() {
        // given
        val testee = services()

        // then
        assertDsl(ServiceListDsl.serializer(), testee,
                """
                    []
                """.trimIndent()
        )
    }

    @Test
    fun `should validate services`() {
        // given
        val testee = services {
            service("")
            service("service 2")
            service {
                alias = "test"
            }
        }

        // then
        assertDsl(ServiceListDsl.serializer(), testee,
                """
                    - name: ""
                    - name: "service 2"
                    - alias: "test"
                """.trimIndent(),
                "[service name=''] name '' is incorrect",
                "[service name='null'] name 'null' is incorrect"
        )
    }

    @Test
    fun `should allow multiple service creation mechanisms`() {
        // given
        val service = service("test 4")

        val testee = services {
            service {
                name = "test 1"
                alias = "alias 1"
                cmd("c 1", "c 2")
            }

            service("test 2")

            service("test 3") {
                entrypoint("e 1")
            }

            +service
        }

        // then
        assertDsl(ServiceListDsl.serializer(), testee,
                """
                    - name: "test 1"
                      alias: "alias 1"
                      cmd:
                      - "c 1"
                      - "c 2"
                    - name: "test 2"
                    - name: "test 3"
                      entrypoint:
                      - "e 1"
                    - name: "test 4"
                """.trimIndent()
        )
    }
}