package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase

internal class CacheDslTest : DslTestBase<CacheDsl>(CacheDsl.serializer()) {

    @Test
    fun `should create empty cache`() {
        // given
        val testee = createCache {}

        // then
        assertDsl(
            testee,
            """
                    {}
                """
        )
    }

    @Test
    fun `should create full cache`() {
        // given
        val testee = createCache {
            paths("path1", "path2")
            untracked = true
            unprotect = false
            policy = CachePolicy.PULL_PUSH
            whenCache = WhenCacheType.ON_SUCCESS
            key("key1")
        }

        // then
        assertDsl(
            testee,
            """
                    paths:
                    - "path1"
                    - "path2"
                    untracked: true
                    unprotect: false
                    policy: "pull-push"
                    when: "on_success"
                    key: "key1"
                """
        )
    }

    @Test
    fun `should create cache from block`() {
        // given
        val testee = createCache {
            paths("path")
        }

        // then
        assertDsl(
            testee,
            """
                    paths:
                    - "path"
                """
        )
    }

    @Test
    fun `should create cache from vararg paths`() {
        // given
        val testee = createCache("path/1", "path/2")

        // then
        assertDsl(
            testee,
            """
                    paths:
                    - "path/1"
                    - "path/2"
                """
        )
    }

    @Test
    fun `should create cache from list paths`() {
        // given
        val testee = createCache(listOf("path/1", "path/2"))

        // then
        assertDsl(
            testee,
            """
                    paths:
                    - "path/1"
                    - "path/2"
                """
        )
    }

    @Test
    fun `should create string key`() {
        // given
        val testee = createCache {
            paths("path1", "path2")
            key("key1")
        }

        // then
        assertDsl(
            testee,
            """
                    paths:
                    - "path1"
                    - "path2"
                    key: "key1"
                """
        )
    }

    @Test
    fun `should create object key`() {
        // given
        val testee = createCache {
            paths("path1", "path2")
            key {
                files("file1", "file2")
                prefix = "pref"
            }
        }

        // then
        assertDsl(
            testee,
            """
                    paths:
                    - "path1"
                    - "path2"
                    key:
                      prefix: "pref"
                      files:
                      - "file1"
                      - "file2"
                """
        )
    }

    @Test
    fun `should create existing object key`() {
        // given
        val key = createCacheKey {
            files("file1", "file2")
            prefix = "pref"
        }
        val testee = createCache {
            paths("path1", "path2")
            key(key)
        }

        // then
        assertDsl(
            testee,
            """
                    paths:
                    - "path1"
                    - "path2"
                    key:
                      prefix: "pref"
                      files:
                      - "file1"
                      - "file2"
                """
        )
    }

    @Test
    fun `should validate key`() {
        // given
        val testee = createCache {
            paths("path1", "path2")
            key {
                files("file1", "file2")
                prefix = "pref/ref"
            }
        }

        // then
        assertDsl(
            testee,
            """
                    paths:
                    - "path1"
                    - "path2"
                    key:
                      prefix: "pref/ref"
                      files:
                      - "file1"
                      - "file2"
                """,
            "[cache][key] prefix value 'pref/ref' can't contain '/' nor '%2F'"
        )
    }

    @Test
    fun `should create cache with empty paths`() {
        // given
        val testee = createCache {
            paths()
        }

        // then
        assertDsl(
            testee,
            """
                    paths: []
                """
        )
    }

    @Test
    fun `should create cache with single element paths`() {
        // given
        val testee = createCache {
            paths("path1")
        }

        // then
        assertDsl(
            testee,
            """
                    paths:
                    - "path1"
                """
        )
    }

    @Test
    fun `should create cache with multiple element paths`() {
        // given
        val testee = createCache {
            paths("path1", "path2")
        }

        // then
        assertDsl(
            testee,
            """
                    paths:
                    - "path1"
                    - "path2"
                """
        )
    }

    @Test
    fun `should merge paths`() {
        // given
        val testee = createCache {
            paths("path1", "path2")
            paths(listOf("path3", "path4"))
        }

        // then
        assertDsl(
            testee,
            """
                    paths:
                    - "path1"
                    - "path2"
                    - "path3"
                    - "path4"
                """
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = createCache {
            paths = mutableSetOf("path")
        }

        // then
        assertDsl(
            testee,
            """
                    paths:
                    - "path"
                """
        )
    }

    @Test
    fun `should be equal`() {
        // given
        val testee = createCache {
            paths = mutableSetOf("path1", "path2")
            untracked = true
            policy = CachePolicy.PULL_PUSH
            whenCache = WhenCacheType.ALWAYS
            key {
                prefix = "gottem"
                files = mutableSetOf("file1", "file2")
            }
        }

        val expected = createCache {
            paths = mutableSetOf("path1", "path2")
            untracked = true
            policy = CachePolicy.PULL_PUSH
            whenCache = WhenCacheType.ALWAYS
            key {
                prefix = "gottem"
                files = mutableSetOf("file1", "file2")
            }
        }

        // then
        assertEquals(expected, testee)
    }

    @Test
    fun `should be equal 2`() {
        // given
        val testee = createCache {
            paths = mutableSetOf("path1", "path2")
            untracked = true
            policy = CachePolicy.PULL_PUSH
            whenCache = WhenCacheType.ALWAYS
            key("k")
        }

        val expected = createCache {
            paths = mutableSetOf("path1", "path2")
            untracked = true
            policy = CachePolicy.PULL_PUSH
            whenCache = WhenCacheType.ALWAYS
            key("k")
        }

        // then
        assertEquals(expected, testee)
    }

    @Test
    fun `should not be equal 1`() {
        // given
        val testee = createCache {
            paths = mutableSetOf("path2", "path1")
            untracked = true
            policy = CachePolicy.PULL_PUSH
            whenCache = WhenCacheType.ALWAYS
            key {
                prefix = "gottem"
                files = mutableSetOf("file1", "file2")
            }
        }

        val expected = createCache {
            paths = mutableSetOf("path1", "path2")
            untracked = true
            policy = CachePolicy.PULL_PUSH
            whenCache = WhenCacheType.ALWAYS
            key {
                prefix = "gottem"
                files = mutableSetOf("file1", "file2")
            }
        }

        // then
        assertEquals(expected, testee)
    }

    @Test
    fun `should not be equal 2`() {
        // given
        val testee = createCache {
            paths = mutableSetOf("path1", "path2")
            untracked = true
            policy = CachePolicy.PULL_PUSH
            whenCache = WhenCacheType.ALWAYS
            key {
                prefix = "gottem"
                files = mutableSetOf("file2", "file1")
            }
        }

        val expected = createCache {
            paths = mutableSetOf("path1", "path2")
            untracked = true
            policy = CachePolicy.PULL_PUSH
            whenCache = WhenCacheType.ALWAYS
            key {
                prefix = "gottem"
                files = mutableSetOf("file1", "file2")
            }
        }

        // then
        assertEquals(expected, testee)
    }
}

internal class CacheKeyDslTest : DslTestBase<CacheKeyDsl>(CacheKeyDsl.serializer()) {

    @Test
    fun `should create empty key`() {
        // given
        val testee = createCacheKey {}

        // then
        assertDsl(
            testee,
            """
                    {}
                """,
            "[key] files list can't be empty"
        )
    }

    @Test
    fun `should create full key`() {
        // given
        val testee = createCacheKey {
            prefix = "test"
            files("file 1", "file 2")
        }

        // then
        assertDsl(
            testee,
            """
                    prefix: "test"
                    files:
                    - "file 1"
                    - "file 2"
                """
        )
    }

    @Test
    fun `should create key with empty files`() {
        // given
        val testee = createCacheKey {
            files()
        }

        // then
        assertDsl(
            testee,
            """
                    files: []
                """,
            "[key] files list can't be empty"
        )
    }

    @Test
    fun `should create key with one file`() {
        // given
        val testee = createCacheKey {
            files("file 1")
        }

        // then
        assertDsl(
            testee,
            """
                    files:
                    - "file 1"
                """
        )
    }

    @Test
    fun `should create key with multiple file`() {
        // given
        val testee = createCacheKey {
            files("file 1", "file 2")
        }

        // then
        assertDsl(
            testee,
            """
                    files:
                    - "file 1"
                    - "file 2"
                """
        )
    }

    @Test
    fun `should merge files`() {
        // given
        val testee = createCacheKey {
            files("file 1", "file 2")
            files(listOf("file 3"))
        }

        // then
        assertDsl(
            testee,
            """
                    files:
                    - "file 1"
                    - "file 2"
                    - "file 3"
                """
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = createCacheKey {
            files = mutableSetOf("file 1", "file 2")
        }

        // then
        assertDsl(
            testee,
            """
                    files:
                    - "file 1"
                    - "file 2"
                """
        )
    }

    @Test
    fun `should validate dot prefix`() {
        // given
        val testee = createCacheKey {
            prefix = "."
            files("file")
        }

        // then
        assertDsl(
            testee,
            """
                    prefix: "."
                    files:
                    - "file"
                """,
            "[key] prefix value '.' can't be '.' nor '%2E'"
        )
    }

    @Test
    fun `should validate escaped dot prefix`() {
        // given
        val testee = createCacheKey {
            prefix = "%2E"
            files("file")
        }

        // then
        assertDsl(
            testee,
            """
                    prefix: "%2E"
                    files:
                    - "file"
                """,
            "[key] prefix value '%2E' can't be '.' nor '%2E'"
        )
    }

    @Test
    fun `should validate escaped lowercase dot prefix`() {
        // given
        val testee = createCacheKey {
            prefix = "%2e"
            files("file")
        }

        // then
        assertDsl(
            testee,
            """
                    prefix: "%2e"
                    files:
                    - "file"
                """,
            "[key] prefix value '%2e' can't be '.' nor '%2E'"
        )
    }

    @Test
    fun `should validate prefix containing slash`() {
        // given
        val testee = createCacheKey {
            prefix = "contains/slash"
            files("file")
        }

        // then
        assertDsl(
            testee,
            """
                    prefix: "contains/slash"
                    files:
                    - "file"
                """,
            "[key] prefix value 'contains/slash' can't contain '/' nor '%2F'"
        )
    }

    @Test
    fun `should validate prefix containing escaped slash`() {
        // given
        val testee = createCacheKey {
            prefix = "contains%2Fslash"
            files("file")
        }

        // then
        assertDsl(
            testee,
            """
                    prefix: "contains%2Fslash"
                    files:
                    - "file"
                """,
            "[key] prefix value 'contains%2Fslash' can't contain '/' nor '%2F'"
        )
    }

    @Test
    fun `should validate prefix containing escaped lowercase slash`() {
        // given
        val testee = createCacheKey {
            prefix = "contains%2fslash"
            files("file")
        }

        // then
        assertDsl(
            testee,
            """
                    prefix: "contains%2fslash"
                    files:
                    - "file"
                """,
            "[key] prefix value 'contains%2fslash' can't contain '/' nor '%2F'"
        )
    }

    @Test
    fun `should be equal`() {
        // given
        val testee = createCacheKey {
            prefix = "contains%2fslash"
            files("file", "file2")
        }

        val expected = createCacheKey {
            prefix = "contains%2fslash"
            files("file", "file2")
        }

        // then
        assertEquals(expected, testee)
    }

    @Test
    fun `should not be equal`() {
        // given
        val testee = createCacheKey {
            prefix = "contains%2fslash"
            files("file", "file2")
        }

        val expected = createCacheKey {
            prefix = "contains%2fslash"
            files("file2", "file")
        }

        // then
        assertEquals(expected, testee)
    }
}