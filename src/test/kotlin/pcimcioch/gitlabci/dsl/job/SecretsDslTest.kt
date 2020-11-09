package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class SecretsDslTest: DslTestBase<SecretsDsl>(SecretsDsl.serializer()) {

    @Test
    fun `should create from block`() {
        // given
        val testee = createSecrets {
            add("key1", "value 1")
        }

        // then
        assertDsl(testee,
                """
                    "key1":
                      vault: "value 1"
                """
        )
    }

    @Test
    fun `should create from map`() {
        // given
        val testee = createSecrets(mapOf("key1" to "value 1", "key2" to "value 2"))

        // then
        assertDsl(testee,
                """
                    "key1":
                      vault: "value 1"
                    "key2":
                      vault: "value 2"
                """
        )
    }

    @Test
    fun `should create from map and block`() {
        // given
        val testee = createSecrets(mapOf("key1" to "value 1", "key2" to "value 2")) {
            add("key3", "value 3")
        }

        // then
        assertDsl(testee,
                """
                    "key1":
                      vault: "value 1"
                    "key2":
                      vault: "value 2"
                    "key3":
                      vault: "value 3"
                """
        )
    }

    @Test
    fun `should create from dsl map`() {
        // given
        val value1Dsl = createSecret("value 1")
        val value2Dsl = createSecret {
            path = "path"
            field = "field"
        }
        val testee = createSecrets(mapOf("key1" to value1Dsl, "key2" to value2Dsl))

        // then
        assertDsl(testee,
                """
                    "key1":
                      vault: "value 1"
                    "key2":
                      vault:
                        path: "path"
                        field: "field"
                """
        )
    }

    @Test
    fun `should create from dsl map and block`() {
        // given
        val value1Dsl = createSecret("value 1")
        val value2Dsl = createSecret {
            path = "path"
            field = "field"
        }
        val testee = createSecrets(mapOf("key1" to value1Dsl, "key2" to value2Dsl)) {
            add("key3", "value 3")
        }

        // then
        assertDsl(testee,
                """
                    "key1":
                      vault: "value 1"
                    "key2":
                      vault:
                        path: "path"
                        field: "field"
                    "key3":
                      vault: "value 3"
                """
        )
    }

    @Test
    fun `should create empty secrets`() {
        // given
        val testee = createSecrets {}

        // then
        assertDsl(testee,
                """
                    {}
                """
        )
    }

    @Test
    fun `should create one element secrets`() {
        // given
        val testee = createSecrets {
            add("key1", "value 1")
        }

        // then
        assertDsl(testee,
                """
                    "key1":
                      vault: "value 1"
                """
        )
    }

    @Test
    fun `should create multiple element secrets`() {
        // given
        val testee = createSecrets {
            add("key1", "value 1")
            add("key2", "value 2")
        }

        // then
        assertDsl(testee,
                """
                    "key1":
                      vault: "value 1"
                    "key2":
                      vault: "value 2"
                """
        )
    }

    @Test
    fun `should allow multiple types of adding`() {
        // given
        val value2Dsl = createSecret("value 2")
        val value5Dsl = createSecret("value 5")
        val testee = createSecrets {
            add("key1", "value 1")
            add("key2", value2Dsl)
            add("key3") {
                path = "path 3"
            }
            "key4" to "value 4"
            "key5" to value5Dsl
            "key6" to {
                path = "path 6"
            }
        }

        // then
        assertDsl(testee,
                """
                    "key1":
                      vault: "value 1"
                    "key2":
                      vault: "value 2"
                    "key3":
                      vault:
                        path: "path 3"
                    "key4":
                      vault: "value 4"
                    "key5":
                      vault: "value 5"
                    "key6":
                      vault:
                        path: "path 6"
                """
        )
    }

    @Test
    fun `should create direct access`() {
        // given
        val map = mutableMapOf(
                "key1" to createSecret("value 1"),
                "key2" to createSecret("value 2")
        )

        val testee = createSecrets {
            secrets = map
        }

        // then
        assertDsl(testee,
                """
                    "key1":
                      vault: "value 1"
                    "key2":
                      vault: "value 2"
                """
        )
    }

    @Test
    fun `should override on add`() {
        // given
        val testee = createSecrets {
            add("key1", "value 1")
            add("key1", "value 2")
        }

        // then
        assertDsl(testee,
                """
                    "key1":
                      vault: "value 2"
                """
        )
    }
}

internal class SecretDslTest: DslTestBase<SecretDsl>(SecretDsl.serializer()) {

    @Test
    fun `should crete from string`() {
        // given
        val testee = createSecret("test vault")

        // then
        assertDsl(testee,
                """
                    vault: "test vault"
                """
        )
    }

    @Test
    fun `should crete empty`() {
        // given
        val testee = createSecret()

        // then
        assertDsl(testee,
                """
                    vault: {}
                """
        )
    }

    @Test
    fun `should crete from block`() {
        // given
        val testee = createSecret {
            engine {
                name = "engine name"
                path = "engine path"
            }
            path = "path"
            field = "field"
        }

        // then
        assertDsl(testee,
                """
                    vault:
                      engine:
                        name: "engine name"
                        path: "engine path"
                      path: "path"
                      field: "field"
                """
        )
    }

    @Test
    fun `should crete from dsl`() {
        // given
        val vault = createSecretVault {
            engine {
                name = "engine name"
                path = "engine path"
            }
            path = "path"
            field = "field"
        }
        val testee = createSecret(vault)

        // then
        assertDsl(testee,
                """
                    vault:
                      engine:
                        name: "engine name"
                        path: "engine path"
                      path: "path"
                      field: "field"
                """
        )
    }
}

internal class SecretVaultDslTest: DslTestBase<SecretVaultDsl>(SecretVaultDsl.serializer()) {

    @Test
    fun `should create empty`() {
        // given
        val testee = createSecretVault()

        // then
        assertDsl(testee,
                """
                    {}
                """
        )
    }

    @Test
    fun `should create full`() {
        // given
        val testee = createSecretVault {
            engine {
                name = "engine name"
                path = "engine path"
            }
            path = "path"
            field = "field"
        }

        // then
        assertDsl(testee,
                """
                    engine:
                      name: "engine name"
                      path: "engine path"
                    path: "path"
                    field: "field"
                """
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val engineDsl = createSecretVaultEngine {
            name = "engine name"
            path = "engine path"
        }
        val testee = createSecretVault {
            engine = engineDsl
            path = "path"
            field = "field"
        }

        // then
        assertDsl(testee,
                """
                    engine:
                      name: "engine name"
                      path: "engine path"
                    path: "path"
                    field: "field"
                """
        )
    }
}

internal class SecretVaultEngineDslTest: DslTestBase<SecretVaultEngineDsl>(SecretVaultEngineDsl.serializer()) {

    @Test
    fun `should create empty`() {
        // given
        val testee = createSecretVaultEngine()

        // then
        assertDsl(testee,
                """
                    {}
                """
        )
    }

    @Test
    fun `should create full`() {
        // given
        val testee = createSecretVaultEngine {
            name = "name"
            path = "path"
        }

        // then
        assertDsl(testee,
                """
                    name: "name"
                    path: "path"
                """
        )
    }
}