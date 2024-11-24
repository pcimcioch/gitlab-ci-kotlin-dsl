package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase
import pcimcioch.gitlabci.dsl.Duration

internal class JobDslTest : DslTestBase<JobDsl>(JobDsl.serializer()) {

    @Test
    fun `should create job from name and block`() {
        // given
        val testee = createJob("test") {
            script("test command")
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should validate empty name`() {
        // given
        val testee = createJob("") {
            script("test command")
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                """,
            "[job name=''] name '' is incorrect"
        )
    }

    @Test
    fun `should validate restricted name`() {
        // given
        val testee = createJob("image") {
            script("test command")
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                """,
            "[job name='image'] name 'image' is incorrect"
        )
    }

    @Test
    fun `should validate startIn without when`() {
        // given
        val testee = createJob("test") {
            script("test command")
            startIn = Duration(minutes = 10)
        }

        // then
        assertDsl(
            testee,
            """
                    start_in: "10 min"
                    script:
                    - "test command"
                """,
            "[job name='test'] startIn can be used only with when=delayed jobs"
        )
    }

    @Test
    fun `should validate startIn without delayed when`() {
        // given
        val testee = createJob("test") {
            script("test command")
            whenRun = WhenRunType.ALWAYS
            startIn = Duration(minutes = 10)
        }

        // then
        assertDsl(
            testee,
            """
                    when: "always"
                    start_in: "10 min"
                    script:
                    - "test command"
                """,
            "[job name='test'] startIn can be used only with when=delayed jobs"
        )
    }

    @Test
    fun `should validate too small parallel`() {
        // given
        val testee = createJob("test") {
            script("test command")
            parallel = 1
        }

        // then
        assertDsl(
            testee,
            """
                    parallel: 1
                    script:
                    - "test command"
                """,
            "[job name='test'] parallel must be in range [2, 50]"
        )
    }

    @Test
    fun `should validate too big parallel`() {
        // given
        val testee = createJob("test") {
            script("test command")
            parallel = 51
        }

        // then
        assertDsl(
            testee,
            """
                    parallel: 51
                    script:
                    - "test command"
                """,
            "[job name='test'] parallel must be in range [2, 50]"
        )
    }

    @Test
    fun `should validate nested objects`() {
        val testee = createJob("test") {
            beforeScript("before 1", "before 2")
            script("script 1", "script 2")
            afterScript("after 1", "after 2")

            inherit {
                default(true)
            }
            retry(5)
            image("")
            services("service 1", "")
            needs("job 2", "")
            variables {}
            cache {
                paths("path")
                key {
                    prefix = "pre/fix"
                    files("file")
                }
            }
            artifacts("artifact")
            rules {
                rule {
                    whenRun = WhenRunType.MANUAL
                    startIn = Duration(hours = 1)
                }
            }
            environment("env#")
        }

        // then
        assertDsl(
            testee,
            """
                    image:
                      name: ""
                    inherit:
                      default: true
                    retry:
                      max: 5
                    services:
                    - name: "service 1"
                    - name: ""
                    needs:
                    - job: "job 2"
                    - job: ""
                    cache:
                    - paths:
                      - "path"
                      key:
                        prefix: "pre/fix"
                        files:
                        - "file"
                    artifacts:
                      paths:
                      - "artifact"
                    rules:
                    - when: "manual"
                      start_in: "1 hr"
                    before_script:
                    - "before 1"
                    - "before 2"
                    script:
                    - "script 1"
                    - "script 2"
                    after_script:
                    - "after 1"
                    - "after 2"
                    environment:
                      name: "env#"
                    variables: {}
                """,
            "[job name='test'][retry] max attempts must be in range [0, 2]",
            "[job name='test'][image] name '' is incorrect",
            "[job name='test'][service name=''] name '' is incorrect",
            "[job name='test'][need job=''] job '' is incorrect",
            "[job name='test'][variables] variables map cannot be empty",
            "[job name='test'][cache][key] prefix value 'pre/fix' can't contain '/' nor '%2F'",
            "[job name='test'][rule] startIn can be used only with when=delayed jobs",
            "[job name='test'][environment name='env#'] name 'env#' is incorrect. Contains forbidden characters"
        )
    }

    @Test
    fun `should create empty job`() {
        // given
        val testee = createJob("") {}

        // then
        assertDsl(
            testee,
            """
                    {}
                """,
            "[job name=''] name '' is incorrect"
        )
    }

    @Test
    fun `should create full job`() {
        // given
        val testee = createJob("test") {
            script("test command")

            extends("testExtends")
            image("testImage")
            stage = "testStage"
            tags("testTag")
            inherit {
                default(true)
            }
            allowFailure = true
            whenRun = WhenRunType.DELAYED
            startIn = Duration(minutes = 10)
            timeout = Duration(hours = 2)
            retry(2)
            interruptible = true
            parallel = 2
            resourceGroup = "test resource"
            services("testService")
            needs("testNeeds")
            dependencies("testDep")
            cache("testCache")
            artifacts("testArt")
            only("onlyBranch")
            except("exceptBranch")
            rules {
                rule {
                    ifCondition = "condition"
                }
            }
            beforeScript("before")
            afterScript("after")
            release("tag 1")
            trigger("testProject")
            coverage = "/Code coverage: \\d+\\.\\d+/"
            environment("env")
            variables {
                add("KEY1", "VALUE1")
            }
            secrets {
                add("KEY2", "VALUE2")
            }
        }

        // then
        assertDsl(
            testee,
            """
                    extends:
                    - "testExtends"
                    image:
                      name: "testImage"
                    stage: "testStage"
                    tags:
                    - "testTag"
                    inherit:
                      default: true
                    allow_failure: true
                    when: "delayed"
                    start_in: "10 min"
                    timeout: "2 hr"
                    retry:
                      max: 2
                    interruptible: true
                    parallel: 2
                    resource_group: "test resource"
                    services:
                    - name: "testService"
                    needs:
                    - job: "testNeeds"
                    dependencies:
                    - "testDep"
                    cache:
                    - paths:
                      - "testCache"
                    artifacts:
                      paths:
                      - "testArt"
                    only:
                      refs:
                      - "onlyBranch"
                    except:
                      refs:
                      - "exceptBranch"
                    rules:
                    - if: "condition"
                    before_script:
                    - "before"
                    script:
                    - "test command"
                    after_script:
                    - "after"
                    release:
                      tag_name: "tag 1"
                    trigger:
                      project: "testProject"
                    coverage: "/Code coverage: \\d+\\.\\d+/"
                    environment:
                      name: "env"
                    variables:
                      "KEY1": "VALUE1"
                    secrets:
                      "KEY2":
                        vault: "VALUE2"
                """
        )
    }

    @Test
    fun `should create job with empty collections`() {
        // given
        val testee = createJob("test") {
            script("test command")

            extends(listOf<String>())
            tags()
            dependencies(listOf<String>())
        }

        // then
        assertDsl(
            testee,
            """
                    extends: []
                    tags: []
                    dependencies: []
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should create job with single element collections`() {
        // given
        val testee = createJob("test") {
            script("test command")

            extends("extend 1")
            tags("tag 1")
            dependencies("depend 1")
        }

        // then
        assertDsl(
            testee,
            """
                    extends:
                    - "extend 1"
                    tags:
                    - "tag 1"
                    dependencies:
                    - "depend 1"
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should create job with multiple element collections`() {
        // given
        val testee = createJob("test") {
            script("test command")

            extends("extend 1", "extend 2")
            tags("tag 1", "tag 2")
            dependencies("depend 1", "depend 2")
        }

        // then
        assertDsl(
            testee,
            """
                    extends:
                    - "extend 1"
                    - "extend 2"
                    tags:
                    - "tag 1"
                    - "tag 2"
                    dependencies:
                    - "depend 1"
                    - "depend 2"
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should merge collections`() {
        // given
        val testee = createJob("test") {
            script("test command")

            extends("extend 1", "extend 2")
            extends(listOf("extend 3", "extend 4"))
            tags("tag 1", "tag 2")
            tags(listOf("tag 3", "tag 4"))
            dependencies("depend 1", "depend 2")
            dependencies(listOf("depend 3", "depend 4"))
        }

        // then
        assertDsl(
            testee,
            """
                    extends:
                    - "extend 1"
                    - "extend 2"
                    - "extend 3"
                    - "extend 4"
                    tags:
                    - "tag 1"
                    - "tag 2"
                    - "tag 3"
                    - "tag 4"
                    dependencies:
                    - "depend 1"
                    - "depend 2"
                    - "depend 3"
                    - "depend 4"
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should allow objects in collections`() {
        // given
        val job1 = createJob("job1") {}
        val job2 = createJob("job2") {}
        val job3 = createJob("job3") {}
        val job4 = createJob("job4") {}

        val testee = createJob("test") {
            script("test command")

            extends(job1, job2)
            extends(listOf(job3, job4))
            dependencies(job1, job2)
            dependencies(listOf(job3, job4))
        }

        // then
        assertDsl(
            testee,
            """
                    extends:
                    - "job1"
                    - "job2"
                    - "job3"
                    - "job4"
                    dependencies:
                    - "job1"
                    - "job2"
                    - "job3"
                    - "job4"
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should allow different script options`() {
        // given
        val testee = createJob("test") {
            script {
                +"command 1"
            }
            script("command 2")
            script(listOf("command 3"))
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "command 1"
                    - "command 2"
                    - "command 3"
                """
        )
    }

    @Test
    fun `should allow different beforeScript options`() {
        // given
        val testee = createJob("test") {
            script("test command")

            beforeScript {
                +"command 1"
            }
            beforeScript("command 2")
            beforeScript(listOf("command 3"))
        }

        // then
        assertDsl(
            testee,
            """
                    before_script:
                    - "command 1"
                    - "command 2"
                    - "command 3"
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should allow different afterScript options`() {
        // given
        val testee = createJob("test") {
            script("test command")

            afterScript {
                +"command 1"
            }
            afterScript("command 2")
            afterScript(listOf("command 3"))
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                    after_script:
                    - "command 1"
                    - "command 2"
                    - "command 3"
                """
        )
    }

    @Test
    fun `should allow image by name configuration `() {
        // given
        val testee = createJob("test") {
            script("test command")

            image("image:1")
        }

        // then
        assertDsl(
            testee,
            """
                    image:
                      name: "image:1"
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should allow image by block configuration `() {
        // given
        val testee = createJob("test") {
            script("test command")

            image {
                name = "image:1"
            }
        }

        // then
        assertDsl(
            testee,
            """
                    image:
                      name: "image:1"
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should allow image by name and block configuration `() {
        // given
        val testee = createJob("test") {
            script("test command")

            image("image:1") {
                entrypoint("entry", "point")
            }
        }

        // then
        assertDsl(
            testee,
            """
                    image:
                      name: "image:1"
                      entrypoint:
                      - "entry"
                      - "point"
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should create release image`() {
        // given
        val testee = createJob("test") {
            script("test command")

            releaseImage()
        }

        // then
        assertDsl(
            testee,
            """
                    image:
                      name: "registry.gitlab.com/gitlab-org/release-cli:latest"
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should allow release by tag name configuration `() {
        // given
        val testee = createJob("test") {
            script("test command")

            release("tag 1")
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                    release:
                      tag_name: "tag 1"
                """
        )
    }

    @Test
    fun `should allow release by block configuration `() {
        // given
        val testee = createJob("test") {
            script("test command")

            release {
                tagName = "tag 1"
            }
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                    release:
                      tag_name: "tag 1"
                """
        )
    }

    @Test
    fun `should allow release by tag name and block configuration `() {
        // given
        val testee = createJob("test") {
            script("test command")

            release("tag 1") {
                name = "name 1"
            }
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                    release:
                      tag_name: "tag 1"
                      name: "name 1"
                """
        )
    }

    @Test
    fun `should allow different services options`() {
        // given
        val testee = createJob("test") {
            script("test command")

            services("ser1")
            services(listOf("ser2"))
            services {
                service("ser3")
            }
        }

        // then
        assertDsl(
            testee,
            """
                    services:
                    - name: "ser1"
                    - name: "ser2"
                    - name: "ser3"
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should allow different needs options`() {
        // given
        val job3 = createJob("job3") {}
        val job4 = createJob("job4") {}
        val testee = createJob("test") {
            script("test command")

            needs("job1")
            needs(listOf("job2"))
            needs(job3)
            needs(listOf(job4))
            needs {
                needJob("job5")
            }
        }

        // then
        assertDsl(
            testee,
            """
                    needs:
                    - job: "job1"
                    - job: "job2"
                    - job: "job3"
                    - job: "job4"
                    - job: "job5"
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should allow retry from number`() {
        // given
        val testee = createJob("test") {
            script("test command")

            retry(2)
        }

        // then
        assertDsl(
            testee,
            """
                    retry:
                      max: 2
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should allow retry from block`() {
        // given
        val testee = createJob("test") {
            script("test command")

            retry {
                max = 2
            }
        }

        // then
        assertDsl(
            testee,
            """
                    retry:
                      max: 2
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should allow retry from number and block`() {
        // given
        val testee = createJob("test") {
            script("test command")

            retry(2) {
                whenRetry(WhenRetryType.API_FAILURE)
            }
        }

        // then
        assertDsl(
            testee,
            """
                    retry:
                      max: 2
                      when:
                      - "api_failure"
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should allow different variables options`() {
        // given
        val testee = createJob("test") {
            script("test command")

            variables {
                "k1" to "v1"
            }
            variables(mapOf("k2" to "v2"))
            variables(mapOf("k3" to "v3")) {
                "k4" to "v4"
            }
            variables(mapOf(RunnerSettingsVariables.GIT_DEPTH to "2"))
            variables(mapOf(RunnerSettingsVariables.GET_SOURCES_ATTEMPTS to 1)) {
                RunnerSettingsVariables.ARTIFACT_DOWNLOAD_ATTEMPTS to 3
            }
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                    variables:
                      "k1": "v1"
                      "k2": "v2"
                      "k3": "v3"
                      "k4": "v4"
                      "GIT_DEPTH": "2"
                      "GET_SOURCES_ATTEMPTS": "1"
                      "ARTIFACT_DOWNLOAD_ATTEMPTS": "3"
                """
        )
    }

    @Test
    fun `should allow different secrets options`() {
        // given
        val testee = createJob("test") {
            script("test command")

            secrets {
                "k1" to "v1"
            }
            secrets(mapOf("k2" to "v2"))
            secrets(mapOf("k3" to "v3")) {
                "k4" to "v4"
            }
            secrets(mapOf("k5" to createSecret("v5")))
            secrets(mapOf("k6" to createSecret("v6"))) {
                "k7" to "v7"
            }
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                    secrets:
                      "k1":
                        vault: "v1"
                      "k2":
                        vault: "v2"
                      "k3":
                        vault: "v3"
                      "k4":
                        vault: "v4"
                      "k5":
                        vault: "v5"
                      "k6":
                        vault: "v6"
                      "k7":
                        vault: "v7"
                """
        )
    }

    @Test
    fun `should allow different cache options`() {
        // given
        val testee = createJob("test") {
            script("test command")

            cache {
                paths("p1")
            }
            cache("p2")
            cache(listOf("p3", "p4"))
            +createCache {
                key("some-key")
                paths("p5")
            }
        }

        // then
        assertDsl(
            testee,
            """
                    cache:
                    - paths:
                      - "p1"
                    - paths:
                      - "p2"
                    - paths:
                      - "p3"
                      - "p4"
                    - paths:
                      - "p5"
                      key: "some-key"
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should allow different artifacts options`() {
        // given
        val testee = createJob("test") {
            script("test command")

            artifacts {
                paths("a1")
            }
            artifacts("a2")
            artifacts(listOf("a3"))
        }

        // then
        assertDsl(
            testee,
            """
                    artifacts:
                      paths:
                      - "a1"
                      - "a2"
                      - "a3"
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should allow different only options`() {
        // given
        val testee = createJob("test") {
            script("test command")

            only {
                kubernetes = KubernetesState.ACTIVE
            }
            only("o1", "o2")
            only(listOf("o3", "o4"))
        }

        // then
        assertDsl(
            testee,
            """
                    only:
                      refs:
                      - "o1"
                      - "o2"
                      - "o3"
                      - "o4"
                      kubernetes: "active"
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should allow different except options`() {
        // given
        val testee = createJob("test") {
            script("test command")

            except {
                kubernetes = KubernetesState.ACTIVE
            }
            except("e1", "e2")
            except(listOf("e3", "e4"))
        }

        // then
        assertDsl(
            testee,
            """
                    except:
                      refs:
                      - "e1"
                      - "e2"
                      - "e3"
                      - "e4"
                      kubernetes: "active"
                    script:
                    - "test command"
                """
        )
    }

    @Test
    fun `should allow trigger from project name`() {
        // given
        val testee = createJob("test") {
            script("test command")

            trigger("testProject")
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                    trigger:
                      project: "testProject"
                """
        )
    }

    @Test
    fun `should allow trigger from project name, branch and strategy`() {
        // given
        val testee = createJob("test") {
            script("test command")

            trigger("testProject", "test-branch", TriggerStrategy.DEPEND)
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                    trigger:
                      project: "testProject"
                      branch: "test-branch"
                      strategy: "depend"
                """
        )
    }

    @Test
    fun `should allow trigger from block`() {
        // given
        val testee = createJob("test") {
            script("test command")

            trigger {
                project = "testProject"
            }
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                    trigger:
                      project: "testProject"
                """
        )
    }

    @Test
    fun `should allow trigger from project name, branch, strategy and block`() {
        // given
        val testee = createJob("test") {
            script("test command")

            trigger("testProject", "test-branch", TriggerStrategy.DEPEND) {
                branch = "test-branch-2"
            }
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                    trigger:
                      project: "testProject"
                      branch: "test-branch-2"
                      strategy: "depend"
                """
        )
    }

    @Test
    fun `should allow trigger from project name and block`() {
        // given
        val testee = createJob("test") {
            script("test command")

            trigger("testProject") {
                branch = "test-branch-2"
            }
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                    trigger:
                      project: "testProject"
                      branch: "test-branch-2"
                """
        )
    }

    @Test
    fun `should allow environment by name configuration`() {
        // given
        val testee = createJob("test") {
            script("test command")

            environment("test")
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                    environment:
                      name: "test"
                """
        )
    }

    @Test
    fun `should allow environment by block configuration`() {
        // given
        val testee = createJob("test") {
            script("test command")

            environment {
                name = "staging"
            }
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                    environment:
                      name: "staging"
                """
        )
    }

    @Test
    fun `should allow environment by name and block configuration`() {
        // given
        val testee = createJob("test") {
            script("test command")

            environment("production") {
                url = "https://test.com"
            }
        }

        // then
        assertDsl(
            testee,
            """
                    script:
                    - "test command"
                    environment:
                      name: "production"
                      url: "https://test.com"
                """
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val scriptDsl = createScript("test command")
        val imageDsl = createImage("testImage")
        val inheritDsl = createInherit {
            default(true)
        }
        val retryDsl = createRetry(2)
        val servicesDsl = createServices("testService")
        val needsDsl = createNeeds("testNeeds")
        val cacheDsl = createCache("testCache")
        val artifactsDsl = createArtifacts("testArt")
        val onlyDsl = createOnlyExcept("testOnly")
        val exceptDsl = createOnlyExcept("testExcept")
        val rulesDsl = createRules {
            rule {
                ifCondition = "condition"
            }
        }
        val beforeScriptDsl = createBeforeScript("before")
        val afterScriptDsl = createAfterScript("after")
        val releaseDsl = createRelease("tag 1")
        val triggerDsl = createTrigger("testProject")
        val variablesDsl = createVariables {
            add("KEY1", "VALUE1")
        }
        val secretsDsl = createSecrets {
            add("KEY2", "VALUE2")
        }
        val environmentDsl = createEnvironment("production")

        val testee = createJob("test") {
            script = scriptDsl

            extends = mutableListOf("testExtends")
            image = imageDsl
            stage = "testStage"
            tags = mutableSetOf("testTag")
            inherit = inheritDsl
            retry = retryDsl
            services = servicesDsl
            needs = needsDsl
            dependencies = mutableSetOf("testDep")
            cache = mutableListOf(cacheDsl)
            artifacts = artifactsDsl
            only = onlyDsl
            except = exceptDsl
            rules = rulesDsl
            beforeScript = beforeScriptDsl
            afterScript = afterScriptDsl
            release = releaseDsl
            trigger = triggerDsl
            variables = variablesDsl
            secrets = secretsDsl
            environment = environmentDsl
        }

        // then
        assertDsl(
            testee,
            """
                    extends:
                    - "testExtends"
                    image:
                      name: "testImage"
                    stage: "testStage"
                    tags:
                    - "testTag"
                    inherit:
                      default: true
                    retry:
                      max: 2
                    services:
                    - name: "testService"
                    needs:
                    - job: "testNeeds"
                    dependencies:
                    - "testDep"
                    cache:
                    - paths:
                      - "testCache"
                    artifacts:
                      paths:
                      - "testArt"
                    only:
                      refs:
                      - "testOnly"
                    except:
                      refs:
                      - "testExcept"
                    rules:
                    - if: "condition"
                    before_script:
                    - "before"
                    script:
                    - "test command"
                    after_script:
                    - "after"
                    release:
                      tag_name: "tag 1"
                    trigger:
                      project: "testProject"
                    environment:
                      name: "production"
                    variables:
                      "KEY1": "VALUE1"
                    secrets:
                      "KEY2":
                        vault: "VALUE2"
                """
        )
    }

    @Test
    fun `should be equal`() {
        // given
        val testee = createJob("test") {
            script("test command")

            extends("testExtends")
            image("testImage")
            stage = "testStage"
            tags("testTag")
            inherit {
                default(true)
            }
            allowFailure = true
            whenRun = WhenRunType.DELAYED
            startIn = Duration(minutes = 10)
            timeout = Duration(hours = 2)
            retry(2)
            interruptible = true
            parallel = 2
            resourceGroup = "test resource"
            services("testService")
            needs("testNeeds")
            dependencies("testDep")
            cache("testCache")
            artifacts("testArt")
            only("onlyBranch")
            except("exceptBranch")
            rules {
                rule {
                    ifCondition = "condition"
                }
            }
            beforeScript("before")
            afterScript("after")
            release("tag 1")
            trigger("testProject")
            coverage = "/Code coverage: \\d+\\.\\d+/"
            environment("env")
            variables {
                add("KEY1", "VALUE1")
            }
            secrets {
                add("KEY2", "VALUE2")
            }
        }

        val expected = createJob("test") {
            script("test command")

            extends("testExtends")
            image("testImage")
            stage = "testStage"
            tags("testTag")
            inherit {
                default(true)
            }
            allowFailure = true
            whenRun = WhenRunType.DELAYED
            startIn = Duration(minutes = 10)
            timeout = Duration(hours = 2)
            retry(2)
            interruptible = true
            parallel = 2
            resourceGroup = "test resource"
            services("testService")
            needs("testNeeds")
            dependencies("testDep")
            cache("testCache")
            artifacts("testArt")
            only("onlyBranch")
            except("exceptBranch")
            rules {
                rule {
                    ifCondition = "condition"
                }
            }
            beforeScript("before")
            afterScript("after")
            release("tag 1")
            trigger("testProject")
            coverage = "/Code coverage: \\d+\\.\\d+/"
            environment("env")
            variables {
                add("KEY1", "VALUE1")
            }
            secrets {
                add("KEY2", "VALUE2")
            }
        }

        // then
        assertEquals(expected, testee)
    }
}