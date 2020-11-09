package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class ServiceDslTest : DslTestBase<ServiceDsl>(ServiceDsl.serializer()) {

    @Test
    fun `should create service from name`() {
        // given
        val testee = createService("ser 1")

        // then
        assertDsl(testee,
                """
                    name: "ser 1"
                """
        )
    }

    @Test
    fun `should create service from name and block`() {
        // given
        val testee = createService("ser 1") {
            alias = "test"
        }

        // then
        assertDsl(testee,
                """
                    name: "ser 1"
                    alias: "test"
                """
        )
    }

    @Test
    fun `should create service from block`() {
        // given
        val testee = createService {
            name = "test"
        }

        // then
        assertDsl(testee,
                """
                    name: "test"
                """
        )
    }

    @Test
    fun `should not validate null name`() {
        // given
        val testee = createService {
            alias = "test"
        }

        // then
        assertDsl(testee,
                """
                    alias: "test"
                """,
                "[service name='null'] name 'null' is incorrect"
        )
    }

    @Test
    fun `should not validate empty name`() {
        // given
        val testee = createService("")

        // then
        assertDsl(testee,
                """
                    name: ""
                """,
                "[service name=''] name '' is incorrect"
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = createService("name") {
            command = mutableListOf("c 1", "c 2")
            entrypoint = mutableListOf("e 1", "e 2")
        }

        // then
        assertDsl(testee,
                """
                    name: "name"
                    command:
                    - "c 1"
                    - "c 2"
                    entrypoint:
                    - "e 1"
                    - "e 2"
                """
        )
    }

    @Test
    fun `should merge command and entrypoint`() {
        // given
        val testee = createService("name") {
            cmd("c 1", "c 2")
            entrypoint("e 1", "e 2")
            cmd("c 10", "c 20")
            entrypoint("e 10", "e 20")
        }

        // then
        assertDsl(testee,
                """
                    name: "name"
                    command:
                    - "c 1"
                    - "c 2"
                    - "c 10"
                    - "c 20"
                    entrypoint:
                    - "e 1"
                    - "e 2"
                    - "e 10"
                    - "e 20"
                """
        )
    }

    @Test
    fun `should create empty command and entrypoint`() {
        // given
        val testee = createService("name") {
            cmd()
            entrypoint()
        }

        // then
        assertDsl(testee,
                """
                    name: "name"
                    command: []
                    entrypoint: []
                """
        )
    }

    @Test
    fun `should create one element command and entrypoint`() {
        // given
        val testee = createService("name") {
            cmd("c 1")
            entrypoint("e 1")
        }

        // then
        assertDsl(testee,
                """
                    name: "name"
                    command:
                    - "c 1"
                    entrypoint:
                    - "e 1"
                """
        )
    }

    @Test
    fun `should create multiple elements command and entrypoint`() {
        // given
        val testee = createService("name") {
            cmd(listOf("c 1", "c 2"))
            entrypoint(listOf("e 1", "e 2"))
        }

        // then
        assertDsl(testee,
                """
                    name: "name"
                    command:
                    - "c 1"
                    - "c 2"
                    entrypoint:
                    - "e 1"
                    - "e 2"
                """
        )
    }
}

internal class ServiceListDslTest : DslTestBase<ServiceListDsl>(ServiceListDsl.serializer()) {

    @Test
    fun `should create services from block`() {
        // given
        val testee = createServices {
            service("service 1")
        }

        // then
        assertDsl(testee,
                """
                    - name: "service 1"
                """
        )
    }

    @Test
    fun `should create services from vararg list`() {
        // given
        val testee = createServices("service 1", "service 2")

        // then
        assertDsl(testee,
                """
                    - name: "service 1"
                    - name: "service 2"
                """
        )
    }

    @Test
    fun `should create services from list`() {
        // given
        val testee = createServices(listOf("service 1", "service 2"))

        // then
        assertDsl(testee,
                """
                    - name: "service 1"
                    - name: "service 2"
                """
        )
    }

    @Test
    fun `should create empty`() {
        // given
        val testee = createServices()

        // then
        assertDsl(testee,
                """
                    []
                """
        )
    }

    @Test
    fun `should validate services`() {
        // given
        val testee = createServices {
            service("")
            service("service 2")
            service {
                alias = "test"
            }
        }

        // then
        assertDsl(testee,
                """
                    - name: ""
                    - name: "service 2"
                    - alias: "test"
                """,
                "[service name=''] name '' is incorrect",
                "[service name='null'] name 'null' is incorrect"
        )
    }

    @Test
    fun `should allow multiple service creation mechanisms`() {
        // given
        val service = createService("test 4")

        val testee = createServices {
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
        assertDsl(testee,
                """
                    - name: "test 1"
                      alias: "alias 1"
                      command:
                      - "c 1"
                      - "c 2"
                    - name: "test 2"
                    - name: "test 3"
                      entrypoint:
                      - "e 1"
                    - name: "test 4"
                """
        )
    }
}