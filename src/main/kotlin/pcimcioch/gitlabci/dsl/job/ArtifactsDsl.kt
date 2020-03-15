package pcimcioch.gitlabci.dsl.job

import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.DslBase.Companion.addErrors
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import java.time.Duration

@GitlabCiDslMarker
class ArtifactsDsl : DslBase {
    var paths: MutableSet<String>? = null
    var name: String? = null
    var exposeAs: String? = null
    var untracked: Boolean? = null
    var whenUpload: WhenUploadType? = null
    var expireIn: Duration? = null
    private var reports: ArtifactsReportsDsl? = null

    fun paths(vararg elements: String) = paths(elements.toList())
    fun paths(elements: Iterable<String>) = ensurePaths().addAll(elements)

    fun reports(block: ArtifactsReportsDsl.() -> Unit) = ensureReports().apply(block)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, reports, "[artifacts]")
    }

    private fun ensureReports() = reports ?: ArtifactsReportsDsl().also { reports = it }
    private fun ensurePaths() = paths ?: mutableSetOf<String>().also { paths = it }
}

fun artifacts(block: ArtifactsDsl.() -> Unit) = ArtifactsDsl().apply(block)
fun artifacts(vararg elements: String) = artifacts(elements.toList())
fun artifacts(elements: Iterable<String>) = ArtifactsDsl().apply { paths(elements) }

enum class WhenUploadType(private val value: String) {
    ON_SUCCESS("on_success"),
    ON_FAILURE("on_failure"),
    ALWAYS("always");

    override fun toString() = value
}

@GitlabCiDslMarker
class ArtifactsReportsDsl : DslBase {
    var junit: MutableSet<String>? = null
    var dotenv: MutableSet<String>? = null
    var codequality: MutableSet<String>? = null
    var sast: MutableSet<String>? = null
    var dependencyScanning: MutableSet<String>? = null
    var containerScanning: MutableSet<String>? = null
    var dast: MutableSet<String>? = null
    var licenseManagement: MutableSet<String>? = null
    var licenseScanning: MutableSet<String>? = null
    var performance: MutableSet<String>? = null
    var metrics: MutableSet<String>? = null

    fun junit(vararg elements: String) = junit(elements.toList())
    fun junit(elements: Iterable<String>) = ensureJunit().addAll(elements)

    fun dotenv(vararg elements: String) = dotenv(elements.toList())
    fun dotenv(elements: Iterable<String>) = ensureDotenv().addAll(elements)

    fun codequality(vararg elements: String) = codequality(elements.toList())
    fun codequality(elements: Iterable<String>) = ensureCodequality().addAll(elements)

    fun sast(vararg elements: String) = sast(elements.toList())
    fun sast(elements: Iterable<String>) = ensureSast().addAll(elements)

    fun dependencyScanning(vararg elements: String) = dependencyScanning(elements.toList())
    fun dependencyScanning(elements: Iterable<String>) = ensureDependencyScanning().addAll(elements)

    fun containerScanning(vararg elements: String) = containerScanning(elements.toList())
    fun containerScanning(elements: Iterable<String>) = ensureContainerScanning().addAll(elements)

    fun dast(vararg elements: String) = dast(elements.toList())
    fun dast(elements: Iterable<String>) = ensureDast().addAll(elements)

    fun licenseManagement(vararg elements: String) = licenseManagement(elements.toList())
    fun licenseManagement(elements: Iterable<String>) = ensureLicenseManagement().addAll(elements)

    fun licenseScanning(vararg elements: String) = licenseScanning(elements.toList())
    fun licenseScanning(elements: Iterable<String>) = ensureLicenseScanning().addAll(elements)

    fun performance(vararg elements: String) = performance(elements.toList())
    fun performance(elements: Iterable<String>) = ensurePerformance().addAll(elements)

    fun metrics(vararg elements: String) = metrics(elements.toList())
    fun metrics(elements: Iterable<String>) = ensureMetrics().addAll(elements)

    private fun ensureJunit() = junit ?: mutableSetOf<String>().also { junit = it }
    private fun ensureDotenv() = dotenv ?: mutableSetOf<String>().also { dotenv = it }
    private fun ensureCodequality() = codequality ?: mutableSetOf<String>().also { codequality = it }
    private fun ensureSast() = sast ?: mutableSetOf<String>().also { sast = it }
    private fun ensureDependencyScanning() = dependencyScanning ?: mutableSetOf<String>().also { dependencyScanning = it }
    private fun ensureContainerScanning() = containerScanning ?: mutableSetOf<String>().also { containerScanning = it }
    private fun ensureDast() = dast ?: mutableSetOf<String>().also { dast = it }
    private fun ensureLicenseManagement() = licenseManagement ?: mutableSetOf<String>().also { licenseManagement = it }
    private fun ensureLicenseScanning() = licenseScanning ?: mutableSetOf<String>().also { licenseScanning = it }
    private fun ensurePerformance() = performance ?: mutableSetOf<String>().also { performance = it }
    private fun ensureMetrics() = metrics ?: mutableSetOf<String>().also { metrics = it }
}

fun reports(block: ArtifactsReportsDsl.() -> Unit) = ArtifactsReportsDsl().apply(block)