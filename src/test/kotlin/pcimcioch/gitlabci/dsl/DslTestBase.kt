package pcimcioch.gitlabci.dsl

import kotlinx.serialization.SerializationStrategy
import org.assertj.core.api.Assertions.assertThat
import java.io.StringWriter

internal abstract class DslTestBase<T: DslBase>(
        private val strategy: SerializationStrategy<T>
) {
    private val writer = StringWriter()

    internal fun assertDsl(testee: T, expectedYaml: String, vararg validationErrors: String) {
        val yaml = serialize(testee)
        assertThat(yaml).isEqualTo(expectedYaml.trimIndent())

        val errors = mutableListOf<String>()
        testee.validate(errors)
        assertThat(errors).containsExactlyInAnyOrder(*validationErrors)
    }

    private fun serialize(value: T): String {
        writer.use { serializeToYaml(strategy, value, writer) }
        return writer.toString()
    }
}