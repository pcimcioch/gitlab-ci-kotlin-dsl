package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import pcimcioch.gitlabci.dsl.DslTestBase
import pcimcioch.gitlabci.dsl.Duration

internal class EnvironmentDslTest : DslTestBase() {

    @Test
    fun `should create environment from name`() {
        // given
        val testee = createEnvironment("production")

        // then
        assertDsl(EnvironmentDsl.serializer(), testee,
                """
                    name: "production"
                """.trimIndent()
        )
    }

    @Test
    fun `should create environment from name and block`() {
        // given
        val testee = createEnvironment("production") {
            url = "https://test.com"
        }

        // then
        assertDsl(EnvironmentDsl.serializer(), testee,
                """
                    name: "production"
                    url: "https://test.com"
                """.trimIndent()
        )
    }

    @Test
    fun `should create environment from block`() {
        // given
        val testee = createEnvironment {
            name = "production"
            url = "https://test.com"
        }

        // then
        assertDsl(EnvironmentDsl.serializer(), testee,
                """
                    name: "production"
                    url: "https://test.com"
                """.trimIndent()
        )
    }

    @Test
    fun `should validate null name`() {
        // given
        val testee = createEnvironment {}

        // then
        assertDsl(EnvironmentDsl.serializer(), testee,
                """
                    {}
                """.trimIndent(),
                "[environment name='null'] name 'null' is incorrect. Cannot be empty"
        )
    }

    @Test
    fun `should validate empty name`() {
        // given
        val testee = createEnvironment("") {}

        // then
        assertDsl(EnvironmentDsl.serializer(), testee,
                """
                    name: ""
                """.trimIndent(),
                "[environment name=''] name '' is incorrect. Cannot be empty"
        )
    }

    @ParameterizedTest
    @ValueSource(strings = ["incorrect|", "name'", "#"])
    fun `should validate incorrect names`(name: String) {
        // given
        val testee = createEnvironment(name) {}

        // then
        assertDsl(EnvironmentDsl.serializer(), testee,
                """
                    name: "$name"
                """.trimIndent(),
                "[environment name='$name'] name '$name' is incorrect. Contains forbidden characters"
        )
    }

    @Test
    fun `should validate correct name`() {
        // given
        val testee = createEnvironment("aB3 _{}-$/") {}

        // then
        assertDsl(EnvironmentDsl.serializer(), testee,
                """
                    name: "aB3 _{}-${'$'}/"
                """.trimIndent()
        )
    }

    @Test
    fun `should create empty`() {
        // given
        val testee = createEnvironment {}

        // then
        assertDsl(EnvironmentDsl.serializer(), testee,
                """
                    {}
                """.trimIndent(),
                "[environment name='null'] name 'null' is incorrect. Cannot be empty"
        )
    }

    @Test
    fun `should create full`() {
        // given
        val testee = createEnvironment {
            name = "production"
            url = "https://test.com"
            onStop = "test"
            action = EnvironmentAction.STOP
            autoStopIn = Duration(minutes = 20)
            kubernetes("kubernetesNamespace")
        }

        // then
        assertDsl(EnvironmentDsl.serializer(), testee,
                """
                    name: "production"
                    url: "https://test.com"
                    on_stop: "test"
                    action: "stop"
                    auto_stop_in: "20 min"
                    kubernetes:
                      namespace: "kubernetesNamespace"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow job in onStop`() {
        // given
        val job = createJob("testJob") {}
        val testee = createEnvironment("production") {
            onStop(job)
        }

        // then
        assertDsl(EnvironmentDsl.serializer(), testee,
                """
                    name: "production"
                    on_stop: "testJob"
                """.trimIndent()
        )
    }

    @Test
    fun `should configure kubernetes from namespace`() {
        // given
        val testee = createEnvironment("production") {
            kubernetes("kubernetesNamespace")
        }

        // then
        assertDsl(EnvironmentDsl.serializer(), testee,
                """
                    name: "production"
                    kubernetes:
                      namespace: "kubernetesNamespace"
                """.trimIndent()
        )
    }

    @Test
    fun `should configure kubernetes from block`() {
        // given
        val testee = createEnvironment("production") {
            kubernetes {
                namespace = "kubernetesNamespace"
            }
        }

        // then
        assertDsl(EnvironmentDsl.serializer(), testee,
                """
                    name: "production"
                    kubernetes:
                      namespace: "kubernetesNamespace"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val kubernetesDsl = createKubernetesEnvironment("kubernetesNamespace")
        val testee = createEnvironment("production") {
            kubernetes = kubernetesDsl
        }

        // then
        assertDsl(EnvironmentDsl.serializer(), testee,
                """
                    name: "production"
                    kubernetes:
                      namespace: "kubernetesNamespace"
                """.trimIndent()
        )
    }
}

internal class KubernetesEnvironmentDslTest : DslTestBase() {

    @Test
    fun `should create from namespace`() {
        // given
        val testee = createKubernetesEnvironment("kubernetesNamespace")

        // then
        assertDsl(KubernetesEnvironmentDsl.serializer(), testee,
                """
                    namespace: "kubernetesNamespace"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from namespace and block`() {
        // given
        val testee = createKubernetesEnvironment("kubernetesNamespace") {
            namespace = "namespace2"
        }

        // then
        assertDsl(KubernetesEnvironmentDsl.serializer(), testee,
                """
                    namespace: "namespace2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create from block`() {
        // given
        val testee = createKubernetesEnvironment {
            namespace = "kubernetesNamespace"
        }

        // then
        assertDsl(KubernetesEnvironmentDsl.serializer(), testee,
                """
                    namespace: "kubernetesNamespace"
                """.trimIndent()
        )
    }

    @Test
    fun `should create empty`() {
        // given
        val testee = createKubernetesEnvironment {}

        // then
        assertDsl(KubernetesEnvironmentDsl.serializer(), testee,
                """
                    {}
                """.trimIndent()
        )
    }
}