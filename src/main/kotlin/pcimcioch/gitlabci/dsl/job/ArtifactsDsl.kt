package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.Duration
import pcimcioch.gitlabci.dsl.StringRepresentation
import pcimcioch.gitlabci.dsl.serializer.StringRepresentationSerializer

@Serializable
class ArtifactsDsl : DslBase() {
    var name: String? = null

    @SerialName("expose_as")
    var exposeAs: String? = null
    var untracked: Boolean? = null
    var public: Boolean? = null

    @SerialName("when")
    var whenUpload: WhenUploadType? = null

    @SerialName("expire_in")
    var expireIn: Duration? = null
    var paths: MutableSet<String>? = null
    var exclude: MutableSet<String>? = null
    var reports: ArtifactsReportsDsl? = null

    fun paths(vararg elements: String) = paths(elements.toList())
    fun paths(elements: Iterable<String>) = ensurePaths().addAll(elements)

    fun exclude(vararg elements: String) = exclude(elements.toList())
    fun exclude(elements: Iterable<String>) = ensureExclude().addAll(elements)

    fun reports(block: ArtifactsReportsDsl.() -> Unit) = ensureReports().apply(block)

    override fun validate(errors: MutableList<String>) {
        addErrors(errors, "[artifacts]", reports)
    }

    private fun ensureReports() = reports ?: ArtifactsReportsDsl().also { reports = it }
    private fun ensurePaths() = paths ?: mutableSetOf<String>().also { paths = it }
    private fun ensureExclude() = exclude ?: mutableSetOf<String>().also { exclude = it }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArtifactsDsl

        if (name != other.name) return false
        if (exposeAs != other.exposeAs) return false
        if (untracked != other.untracked) return false
        if (public != other.public) return false
        if (whenUpload != other.whenUpload) return false
        if (expireIn != other.expireIn) return false
        if (paths != other.paths) return false
        if (exclude != other.exclude) return false
        if (reports != other.reports) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name?.hashCode() ?: 0
        result = 31 * result + (exposeAs?.hashCode() ?: 0)
        result = 31 * result + (untracked?.hashCode() ?: 0)
        result = 31 * result + (public?.hashCode() ?: 0)
        result = 31 * result + (whenUpload?.hashCode() ?: 0)
        result = 31 * result + (expireIn?.hashCode() ?: 0)
        result = 31 * result + (paths?.hashCode() ?: 0)
        result = 31 * result + (exclude?.hashCode() ?: 0)
        result = 31 * result + (reports?.hashCode() ?: 0)
        return result
    }


    companion object {
        init {
            addSerializer(ArtifactsDsl::class, serializer())
        }
    }
}

fun createArtifacts(block: ArtifactsDsl.() -> Unit = {}) = ArtifactsDsl().apply(block)
fun createArtifacts(vararg elements: String, block: ArtifactsDsl.() -> Unit = {}) =
    createArtifacts(elements.toList(), block)

fun createArtifacts(elements: Iterable<String>, block: ArtifactsDsl.() -> Unit = {}) =
    ArtifactsDsl().apply { paths(elements) }.apply(block)

@Serializable(with = WhenUploadType.WhenUploadTypeSerializer::class)
enum class WhenUploadType(
    override val stringRepresentation: String
) : StringRepresentation {
    ON_SUCCESS("on_success"),
    ON_FAILURE("on_failure"),
    ALWAYS("always");

    object WhenUploadTypeSerializer : StringRepresentationSerializer<WhenUploadType>("WhenUploadType")
}

@Serializable
class ArtifactsReportsDsl : DslBase() {
    var junit: MutableSet<String>? = null
    var dotenv: MutableSet<String>? = null
    var codequality: MutableSet<String>? = null
    var sast: MutableSet<String>? = null

    @SerialName("dependency_scanning")
    var dependencyScanning: MutableSet<String>? = null

    @SerialName("container_scanning")
    var containerScanning: MutableSet<String>? = null
    var dast: MutableSet<String>? = null

    @SerialName("license_management")
    var licenseManagement: MutableSet<String>? = null

    @SerialName("license_scanning")
    var licenseScanning: MutableSet<String>? = null
    var performance: MutableSet<String>? = null
    var metrics: MutableSet<String>? = null
    var cobertura: MutableSet<String>? = null

    @SerialName("load_performance")
    var loadPerformance: MutableSet<String>? = null
    var terraform: MutableSet<String>? = null

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

    fun cobertura(vararg elements: String) = cobertura(elements.toList())
    fun cobertura(elements: Iterable<String>) = ensureCobertura().addAll(elements)

    fun loadPerformance(vararg elements: String) = loadPerformance(elements.toList())
    fun loadPerformance(elements: Iterable<String>) = ensureLoadPerformance().addAll(elements)

    fun terraform(vararg elements: String) = terraform(elements.toList())
    fun terraform(elements: Iterable<String>) = ensureTerraform().addAll(elements)

    private fun ensureJunit() = junit ?: mutableSetOf<String>().also { junit = it }
    private fun ensureDotenv() = dotenv ?: mutableSetOf<String>().also { dotenv = it }
    private fun ensureCodequality() = codequality ?: mutableSetOf<String>().also { codequality = it }
    private fun ensureSast() = sast ?: mutableSetOf<String>().also { sast = it }
    private fun ensureDependencyScanning() =
        dependencyScanning ?: mutableSetOf<String>().also { dependencyScanning = it }

    private fun ensureContainerScanning() = containerScanning ?: mutableSetOf<String>().also { containerScanning = it }
    private fun ensureDast() = dast ?: mutableSetOf<String>().also { dast = it }
    private fun ensureLicenseManagement() = licenseManagement ?: mutableSetOf<String>().also { licenseManagement = it }
    private fun ensureLicenseScanning() = licenseScanning ?: mutableSetOf<String>().also { licenseScanning = it }
    private fun ensurePerformance() = performance ?: mutableSetOf<String>().also { performance = it }
    private fun ensureMetrics() = metrics ?: mutableSetOf<String>().also { metrics = it }
    private fun ensureCobertura() = cobertura ?: mutableSetOf<String>().also { cobertura = it }
    private fun ensureLoadPerformance() = loadPerformance ?: mutableSetOf<String>().also { loadPerformance = it }
    private fun ensureTerraform() = terraform ?: mutableSetOf<String>().also { terraform = it }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as ArtifactsReportsDsl


        if (junit != other.junit) return false
        if (dotenv != other.dotenv) return false
        if (codequality != other.codequality) return false
        if (sast != other.sast) return false
        if (dependencyScanning != other.dependencyScanning) return false
        if (containerScanning != other.containerScanning) return false
        if (dast != other.dast) return false
        if (licenseManagement != other.licenseManagement) return false
        if (licenseScanning != other.licenseScanning) return false
        if (performance != other.performance) return false
        if (metrics != other.metrics) return false
        if (cobertura != other.cobertura) return false
        if (loadPerformance != other.loadPerformance) return false
        if (terraform != other.terraform) return false

        return true
    }

    override fun hashCode(): Int {
        var result = junit?.hashCode() ?: 0
        result = 31 * result + (dotenv?.hashCode() ?: 0)
        result = 31 * result + (codequality?.hashCode() ?: 0)
        result = 31 * result + (sast?.hashCode() ?: 0)
        result = 31 * result + (dependencyScanning?.hashCode() ?: 0)
        result = 31 * result + (containerScanning?.hashCode() ?: 0)
        result = 31 * result + (dast?.hashCode() ?: 0)
        result = 31 * result + (licenseManagement?.hashCode() ?: 0)
        result = 31 * result + (licenseScanning?.hashCode() ?: 0)
        result = 31 * result + (performance?.hashCode() ?: 0)
        result = 31 * result + (metrics?.hashCode() ?: 0)
        result = 31 * result + (cobertura?.hashCode() ?: 0)
        result = 31 * result + (loadPerformance?.hashCode() ?: 0)
        result = 31 * result + (terraform?.hashCode() ?: 0)
        return result
    }


    companion object {
        init {
            addSerializer(ArtifactsReportsDsl::class, serializer())
        }
    }
}

fun createArtifactsReports(block: ArtifactsReportsDsl.() -> Unit = {}) = ArtifactsReportsDsl().apply(block)