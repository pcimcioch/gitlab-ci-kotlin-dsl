package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class InheritDslTest : DslTestBase<InheritDsl>(InheritDsl.serializer()) {

    @Test
    fun `should create empty inherit`() {
        // given
        val testee = createInherit {}

        // then
        assertDsl(
            testee,
            """
                    {}
                """
        )
    }

    @Test
    fun `should create inherit with boolean values`() {
        // given
        val testee = createInherit {
            default(true)
            variables(false)
        }

        // then
        assertDsl(
            testee,
            """
                    default: true
                    variables: false
                """
        )
    }

    @Test
    fun `should create inherit with multivalued lists`() {
        // given
        val testee = createInherit {
            default(InheritDefaultType.AFTER_SCRIPT, InheritDefaultType.ARTIFACTS)
            variables("var1", "var2")
        }

        // then
        assertDsl(
            testee,
            """
                    default:
                    - "after_script"
                    - "artifacts"
                    variables:
                    - "var1"
                    - "var2"
                """
        )
    }

    @Test
    fun `should create inherit with empty lists`() {
        // given
        val testee = createInherit {
            default()
            variables()
        }

        // then
        assertDsl(
            testee,
            """
                    default: []
                    variables: []
                """
        )
    }

    @Test
    fun `should create inherit with single element lists`() {
        // given
        val testee = createInherit {
            default(InheritDefaultType.BEFORE_SCRIPT)
            variables("var1")
        }

        // then
        assertDsl(
            testee,
            """
                    default:
                    - "before_script"
                    variables:
                    - "var1"
                """
        )
    }

    @Test
    fun `should merge lists`() {
        // given
        val testee = createInherit {
            default(InheritDefaultType.CACHE, InheritDefaultType.IMAGE)
            variables("var1", "var2")
            default(listOf(InheritDefaultType.RETRY))
            variables(listOf("var3"))
        }

        // then
        assertDsl(
            testee,
            """
                    default:
                    - "cache"
                    - "image"
                    - "retry"
                    variables:
                    - "var1"
                    - "var2"
                    - "var3"
                """
        )
    }

    @Test
    fun `should create with boolean and list`() {
        // given
        val testee = createInherit {
            default(true)
            variables("var1", "var2")
        }

        // then
        assertDsl(
            testee,
            """
                    default: true
                    variables:
                    - "var1"
                    - "var2"
                """
        )
    }

    @Test
    fun `should allow override list to boolen and boolean to list`() {
        // given
        val testee = createInherit {
            default(true)
            variables("var1", "var2")
            default(InheritDefaultType.SERVICES, InheritDefaultType.TIMEOUT)
            variables(false)
        }

        // then
        assertDsl(
            testee,
            """
                    default:
                    - "services"
                    - "timeout"
                    variables: false
                """
        )
    }

    @Test
    fun `should be equal`() {
        // given
        val testee = createInherit {
            default(InheritDefaultType.CACHE, InheritDefaultType.IMAGE)
            variables("var1", "var2")
        }

        val expected = createInherit {
            default(InheritDefaultType.CACHE, InheritDefaultType.IMAGE)
            variables("var1", "var2")
        }

        // then
        assertEquals(expected, testee)
    }

    @Test
    fun `should be equal 2`() {
        // given
        val testee = createInherit {
            default(false)
            variables(true)
        }

        val expected = createInherit {
            default(false)
            variables(true)
        }

        // then
        assertEquals(expected, testee)
    }
}