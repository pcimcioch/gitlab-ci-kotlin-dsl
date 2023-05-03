package pcimcioch.gitlabci.dsl.job

import kotlinx.serialization.Serializable
import pcimcioch.gitlabci.dsl.StringRepresentation
import pcimcioch.gitlabci.dsl.serializer.StringRepresentationSerializer

@Serializable(with = PullPolicy.PullPolicySerializer::class)
enum class PullPolicy(
    override val stringRepresentation: String
) : StringRepresentation {
    ALWAYS("always"),
    IF_NOT_PRESENT("if-not-present"),
    NEVER("never");

    object PullPolicySerializer : StringRepresentationSerializer<PullPolicy>("PullPolicy")
}