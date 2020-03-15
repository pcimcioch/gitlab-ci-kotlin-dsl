package pcimcioch.gitlabci.dsl.job

import org.junit.jupiter.api.Test
import pcimcioch.gitlabci.dsl.DslTestBase
import java.time.Duration

internal class ArtifactsDslTest : DslTestBase() {

    @Test
    fun `should create empty artifacts`() {
        // given
        val testee = artifacts {}

        // then
        assertDsl(ArtifactsDsl.serializer(), testee,
                """
                    {}
                """.trimIndent()
        )
    }

    @Test
    fun `should create full artifacts`() {
        // given
        val testee = artifacts {
            name = "test"
            exposeAs = "exposed"
            untracked = true
            whenUpload = WhenUploadType.ALWAYS
            expireIn = Duration.ofDays(1)
            paths("p 1")
            reports {
                junit("junit 1")
            }
        }

        // then
        assertDsl(ArtifactsDsl.serializer(), testee,
                """
                    name: "test"
                    expose_as: "exposed"
                    untracked: true
                    when: "always"
                    expire_in: "1"
                    paths:
                    - "p 1"
                    reports:
                      junit:
                      - "junit 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create artifacts from block`() {
        // given
        val testee = artifacts {
            name = "test"
            paths("p 1")
        }

        // then
        assertDsl(ArtifactsDsl.serializer(), testee,
                """
                    name: "test"
                    paths:
                    - "p 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create artifacts from paths`() {
        // given
        val testee = artifacts("p 1", "p 2")

        // then
        assertDsl(ArtifactsDsl.serializer(), testee,
                """
                    paths:
                    - "p 1"
                    - "p 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create artifacts from paths list`() {
        // given
        val testee = artifacts(listOf("p 1", "p 2"))

        // then
        assertDsl(ArtifactsDsl.serializer(), testee,
                """
                    paths:
                    - "p 1"
                    - "p 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create single path artifacts`() {
        // given
        val testee = artifacts {
            paths("p 1")
        }

        // then
        assertDsl(ArtifactsDsl.serializer(), testee,
                """
                    paths:
                    - "p 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create multiple paths artifacts`() {
        // given
        val testee = artifacts {
            paths("p 1", "p 2")
        }

        // then
        assertDsl(ArtifactsDsl.serializer(), testee,
                """
                    paths:
                    - "p 1"
                    - "p 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create no paths artifacts`() {
        // given
        val testee = artifacts {
            paths()
        }

        // then
        assertDsl(ArtifactsDsl.serializer(), testee,
                """
                    paths: []
                """.trimIndent()
        )
    }

    @Test
    fun `should merge paths`() {
        // given
        val testee = artifacts {
            paths("p 1", "p 2")
            paths(listOf("p 3", "p 4"))
        }

        // then
        assertDsl(ArtifactsDsl.serializer(), testee,
                """
                    paths:
                    - "p 1"
                    - "p 2"
                    - "p 3"
                    - "p 4"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val r = reports {
            junit("junit 1")
        }

        val testee = artifacts {
            paths = mutableSetOf("p 1", "p 2")
            reports = r
        }

        // then
        assertDsl(ArtifactsDsl.serializer(), testee,
                """
                    paths:
                    - "p 1"
                    - "p 2"
                    reports:
                      junit:
                      - "junit 1"
                """.trimIndent()
        )
    }
}

internal class ArtifactsReportsDslTest : DslTestBase() {

    @Test
    fun `should create empty reports`() {
        // given
        val testee = reports {}

        // then
        assertDsl(ArtifactsReportsDsl.serializer(), testee,
                """
                    {}
                """.trimIndent()
        )
    }

    @Test
    fun `should create reports with one path`() {
        // given
        val testee = reports {
            junit("junit 1")
            dotenv("dotenv 1")
            codequality("codequality 1")
            sast("sast 1")
            dependencyScanning("dependencyScanning 1")
            containerScanning("containerScanning 1")
            dast("dast 1")
            licenseManagement("licenseManagement 1")
            licenseScanning("licenseScanning 1")
            performance("performance 1")
            metrics("metrics 1")
        }

        // then
        assertDsl(ArtifactsReportsDsl.serializer(), testee,
                """
                    junit:
                    - "junit 1"
                    dotenv:
                    - "dotenv 1"
                    codequality:
                    - "codequality 1"
                    sast:
                    - "sast 1"
                    dependencyScanning:
                    - "dependencyScanning 1"
                    containerScanning:
                    - "containerScanning 1"
                    dast:
                    - "dast 1"
                    licenseManagement:
                    - "licenseManagement 1"
                    licenseScanning:
                    - "licenseScanning 1"
                    performance:
                    - "performance 1"
                    metrics:
                    - "metrics 1"
                """.trimIndent()
        )
    }

    @Test
    fun `should create reports with multiple paths`() {
        // given
        val testee = reports {
            junit("junit 1", "junit 2")
            dotenv("dotenv 1", "dotenv 2")
            codequality("codequality 1", "codequality 2")
            sast("sast 1", "sast 2")
            dependencyScanning("dependencyScanning 1", "dependencyScanning 2")
            containerScanning("containerScanning 1", "containerScanning 2")
            dast("dast 1", "dast 2")
            licenseManagement("licenseManagement 1", "licenseManagement 2")
            licenseScanning("licenseScanning 1", "licenseScanning 2")
            performance("performance 1", "performance 2")
            metrics("metrics 1", "metrics 2")
        }

        // then
        assertDsl(ArtifactsReportsDsl.serializer(), testee,
                """
                    junit:
                    - "junit 1"
                    - "junit 2"
                    dotenv:
                    - "dotenv 1"
                    - "dotenv 2"
                    codequality:
                    - "codequality 1"
                    - "codequality 2"
                    sast:
                    - "sast 1"
                    - "sast 2"
                    dependencyScanning:
                    - "dependencyScanning 1"
                    - "dependencyScanning 2"
                    containerScanning:
                    - "containerScanning 1"
                    - "containerScanning 2"
                    dast:
                    - "dast 1"
                    - "dast 2"
                    licenseManagement:
                    - "licenseManagement 1"
                    - "licenseManagement 2"
                    licenseScanning:
                    - "licenseScanning 1"
                    - "licenseScanning 2"
                    performance:
                    - "performance 1"
                    - "performance 2"
                    metrics:
                    - "metrics 1"
                    - "metrics 2"
                """.trimIndent()
        )
    }

    @Test
    fun `should create reports with no path`() {
        // given
        val testee = reports {
            junit()
            dotenv()
            codequality()
            sast()
            dependencyScanning()
            containerScanning()
            dast()
            licenseManagement()
            licenseScanning()
            performance()
            metrics()
        }

        // then
        assertDsl(ArtifactsReportsDsl.serializer(), testee,
                """
                    junit: []
                    dotenv: []
                    codequality: []
                    sast: []
                    dependencyScanning: []
                    containerScanning: []
                    dast: []
                    licenseManagement: []
                    licenseScanning: []
                    performance: []
                    metrics: []
                """.trimIndent()
        )
    }

    @Test
    fun `should merge paths`() {
        // given
        val testee = reports {
            junit("junit 1")
            dotenv("dotenv 1")
            codequality("codequality 1")
            sast("sast 1")
            dependencyScanning("dependencyScanning 1")
            containerScanning("containerScanning 1")
            dast("dast 1")
            licenseManagement("licenseManagement 1")
            licenseScanning("licenseScanning 1")
            performance("performance 1")
            metrics("metrics 1")

            junit(listOf("junit 2", "junit 3"))
            dotenv(listOf("dotenv 2", "dotenv 3"))
            codequality(listOf("codequality 2", "codequality 3"))
            sast(listOf("sast 2", "sast 3"))
            dependencyScanning(listOf("dependencyScanning 2", "dependencyScanning 3"))
            containerScanning(listOf("containerScanning 2", "containerScanning 3"))
            dast(listOf("dast 2", "dast 3"))
            licenseManagement(listOf("licenseManagement 2", "licenseManagement 3"))
            licenseScanning(listOf("licenseScanning 2", "licenseScanning 3"))
            performance(listOf("performance 2", "performance 3"))
            metrics(listOf("metrics 2", "metrics 3"))
        }

        // then
        assertDsl(ArtifactsReportsDsl.serializer(), testee,
                """
                    junit:
                    - "junit 1"
                    - "junit 2"
                    - "junit 3"
                    dotenv:
                    - "dotenv 1"
                    - "dotenv 2"
                    - "dotenv 3"
                    codequality:
                    - "codequality 1"
                    - "codequality 2"
                    - "codequality 3"
                    sast:
                    - "sast 1"
                    - "sast 2"
                    - "sast 3"
                    dependencyScanning:
                    - "dependencyScanning 1"
                    - "dependencyScanning 2"
                    - "dependencyScanning 3"
                    containerScanning:
                    - "containerScanning 1"
                    - "containerScanning 2"
                    - "containerScanning 3"
                    dast:
                    - "dast 1"
                    - "dast 2"
                    - "dast 3"
                    licenseManagement:
                    - "licenseManagement 1"
                    - "licenseManagement 2"
                    - "licenseManagement 3"
                    licenseScanning:
                    - "licenseScanning 1"
                    - "licenseScanning 2"
                    - "licenseScanning 3"
                    performance:
                    - "performance 1"
                    - "performance 2"
                    - "performance 3"
                    metrics:
                    - "metrics 1"
                    - "metrics 2"
                    - "metrics 3"
                """.trimIndent()
        )
    }

    @Test
    fun `should allow direct access`() {
        // given
        val testee = reports {
            junit = mutableSetOf("junit 1", "junit 2")
            dotenv = mutableSetOf("dotenv 1", "dotenv 2")
            codequality = mutableSetOf("codequality 1", "codequality 2")
            sast = mutableSetOf("sast 1", "sast 2")
            dependencyScanning = mutableSetOf("dependencyScanning 1", "dependencyScanning 2")
            containerScanning = mutableSetOf("containerScanning 1", "containerScanning 2")
            dast = mutableSetOf("dast 1", "dast 2")
            licenseManagement = mutableSetOf("licenseManagement 1", "licenseManagement 2")
            licenseScanning = mutableSetOf("licenseScanning 1", "licenseScanning 2")
            performance = mutableSetOf("performance 1", "performance 2")
            metrics = mutableSetOf("metrics 1", "metrics 2")
        }

        // then
        assertDsl(ArtifactsReportsDsl.serializer(), testee,
                """
                    junit:
                    - "junit 1"
                    - "junit 2"
                    dotenv:
                    - "dotenv 1"
                    - "dotenv 2"
                    codequality:
                    - "codequality 1"
                    - "codequality 2"
                    sast:
                    - "sast 1"
                    - "sast 2"
                    dependencyScanning:
                    - "dependencyScanning 1"
                    - "dependencyScanning 2"
                    containerScanning:
                    - "containerScanning 1"
                    - "containerScanning 2"
                    dast:
                    - "dast 1"
                    - "dast 2"
                    licenseManagement:
                    - "licenseManagement 1"
                    - "licenseManagement 2"
                    licenseScanning:
                    - "licenseScanning 1"
                    - "licenseScanning 2"
                    performance:
                    - "performance 1"
                    - "performance 2"
                    metrics:
                    - "metrics 1"
                    - "metrics 2"
                """.trimIndent()
        )
    }
}