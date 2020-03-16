package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class CacheDslTest : DslTestBase() {

    @Test
    fun `should create empty cache`() {
        // given
        val testee = cache {}

        // then
        assertDsl(CacheDsl.serializer(), testee,
                """
                    {}
                """.trimIndent()
        )
    }

    @Test
    fun `should create full cache`() {
        // given
        val testee = cache {
            paths("path1", "path2")
            untracked = true
            policy = CachePolicy.PULL_PUSH
            key("key1")
        }

        // then
        assertDsl(CacheDsl.serializer(), testee,
                """
                    paths:
                    - "path1"
                    - "path2"
                    untracked: true
                    policy: "pull-push"
                    key: "key1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create cache from block`() {
        // given
        val testee = cache {
            paths("path")
        }

        // then
        assertDsl(CacheDsl.serializer(), testee,
                """
                    paths:
                    - "path"
                """.trimIndent()
        )
    }

    @Test
    fun `should create cache from vararg paths`() {
        // given
        val testee = cache("path/1", "path/2")

        // then
        assertDsl(CacheDsl.serializer(), testee,
                """
                    paths:
                    - "path/1"
                    - "path/2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create cache from list paths`() {
        // given
        val testee = cache(listOf("path/1", "path/2"))

        // then
        assertDsl(CacheDsl.serializer(), testee,
                """
                    paths:
                    - "path/1"
                    - "path/2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create string key`() {
        // given
        val testee = cache {
            paths("path1", "path2")
            key("key1")
        }

        // then
        assertDsl(CacheDsl.serializer(), testee,
                """
                    paths:
                    - "path1"
                    - "path2"
                    key: "key1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create object key`() {
        // given
        val testee = cache {
            paths("path1", "path2")
            key {
                files("file1", "file2")
                prefix = "pref"
            }
        }

        // then
        assertDsl(CacheDsl.serializer(), testee,
                """
                    paths:
                    - "path1"
                    - "path2"
                    key:
                      prefix: "pref"
                      files:
                      - "file1"
                      - "file2"
                """.trimIndent()
        )
    }

    @Test
    fun `should validate key`() {
        // given
        val testee = cache {
            paths("path1", "path2")
            key {
                files("file1", "file2")
                prefix = "pref/ref"
            }
        }

        // then
        assertDsl(CacheDsl.serializer(), testee,
                """
                    paths:
                    - "path1"
                    - "path2"
                    key:
                      prefix: "pref/ref"
                      files:
                      - "file1"
                      - "file2"
                """.trimIndent(),
                "[cache][key] prefix value 'pref/ref' can't contain '/' nor '%2F'"
        )
    }

    @Test
    fun `should create cache with empty paths`() {
        // given
        val testee = cache {
            paths()
        }

        // then
        assertDsl(CacheDsl.serializer(), testee,
                """
                    paths: []
                """.trimIndent()
        )
    }

    @Test
    fun `should create cache with single element paths`() {
        // given
        val testee = cache {
            paths("path1")
        }

        // then
        assertDsl(CacheDsl.serializer(), testee,
                """
                    paths:
                    - "path1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create cache with multiple element paths`() {
        // given
        val testee = cache {
            paths("path1", "path2")
        }

        // then
        assertDsl(CacheDsl.serializer(), testee,
                """
                    paths:
                    - "path1"
                    - "path2"
                """.trimIndent()
        )
    }

    @Test
    fun `should merge paths`() {
        // given
        val testee = cache {
            paths("path1", "path2")
            paths(listOf("path3", "path4"))
        }

        // then
        assertDsl(CacheDsl.serializer(), testee,
                """
                    paths:
                    - "path1"
                    - "path2"
                    - "path3"
                    - "path4"
                """.trimIndent()
        )
    }
}

internal class CacheKeyDslTest : DslTestBase() {

    @Test
    fun `should create empty key`() {
        // given
        val testee = cacheKey {}

        // then
        assertDsl(CacheKeyDsl.serializer(), testee,
                """
                    {}
                """.trimIndent(),
                "[key] files list can't be empty"
        )
    }

    @Test
    fun `should create full key`() {
        // given
        val testee = cacheKey {
            prefix = "test"
            files("file 1", "file 2")
        }

        // then
        assertDsl(CacheKeyDsl.serializer(), testee,
                """
                    prefix: "test"
                    files:
                    - "file 1"
                    - "file 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create key with empty files`() {
        // given
        val testee = cacheKey {
            files()
        }

        // then
        assertDsl(CacheKeyDsl.serializer(), testee,
                """
                    files: []
                """.trimIndent(),
                "[key] files list can't be empty"
        )
    }

    @Test
    fun `should create key with one file`() {
        // given
        val testee = cacheKey {
            files("file 1")
        }

        // then
        assertDsl(CacheKeyDsl.serializer(), testee,
                """
                    files:
                    - "file 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create key with multiple file`() {
        // given
        val testee = cacheKey {
            files("file 1", "file 2")
        }

        // then
        assertDsl(CacheKeyDsl.serializer(), testee,
                """
                    files:
                    - "file 1"
                    - "file 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should merge files`() {
        // given
        val testee = cacheKey {
            files("file 1", "file 2")
            files(listOf("file 3"))
        }

        // then
        assertDsl(CacheKeyDsl.serializer(), testee,
                """
                    files:
                    - "file 1"
                    - "file 2"
                    - "file 3"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = cacheKey {
            files = mutableSetOf("file 1", "file 2")
        }

        // then
        assertDsl(CacheKeyDsl.serializer(), testee,
                """
                    files:
                    - "file 1"
                    - "file 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should validate dot prefix`() {
        // given
        val testee = cacheKey {
            prefix = "."
            files("file")
        }

        // then
        assertDsl(CacheKeyDsl.serializer(), testee,
                """
                    prefix: "."
                    files:
                    - "file"
                """.trimIndent(),
                "[key] prefix value '.' can't be '.' nor '%2E'"
        )
    }

    @Test
    fun `should validate escaped dot prefix`() {
        // given
        val testee = cacheKey {
            prefix = "%2E"
            files("file")
        }

        // then
        assertDsl(CacheKeyDsl.serializer(), testee,
                """
                    prefix: "%2E"
                    files:
                    - "file"
                """.trimIndent(),
                "[key] prefix value '%2E' can't be '.' nor '%2E'"
        )
    }

    @Test
    fun `should validate escaped lowercase dot prefix`() {
        // given
        val testee = cacheKey {
            prefix = "%2e"
            files("file")
        }

        // then
        assertDsl(CacheKeyDsl.serializer(), testee,
                """
                    prefix: "%2e"
                    files:
                    - "file"
                """.trimIndent(),
                "[key] prefix value '%2e' can't be '.' nor '%2E'"
        )
    }

    @Test
    fun `should validate prefix containing slash`() {
        // given
        val testee = cacheKey {
            prefix = "contains/slash"
            files("file")
        }

        // then
        assertDsl(CacheKeyDsl.serializer(), testee,
                """
                    prefix: "contains/slash"
                    files:
                    - "file"
                """.trimIndent(),
                "[key] prefix value 'contains/slash' can't contain '/' nor '%2F'"
        )
    }

    @Test
    fun `should validate prefix containing escaped slash`() {
        // given
        val testee = cacheKey {
            prefix = "contains%2Fslash"
            files("file")
        }

        // then
        assertDsl(CacheKeyDsl.serializer(), testee,
                """
                    prefix: "contains%2Fslash"
                    files:
                    - "file"
                """.trimIndent(),
                "[key] prefix value 'contains%2Fslash' can't contain '/' nor '%2F'"
        )
    }

    @Test
    fun `should validate prefix containing escaped lowercase slash`() {
        // given
        val testee = cacheKey {
            prefix = "contains%2fslash"
            files("file")
        }

        // then
        assertDsl(CacheKeyDsl.serializer(), testee,
                """
                    prefix: "contains%2fslash"
                    files:
                    - "file"
                """.trimIndent(),
                "[key] prefix value 'contains%2fslash' can't contain '/' nor '%2F'"
        )
    }
}