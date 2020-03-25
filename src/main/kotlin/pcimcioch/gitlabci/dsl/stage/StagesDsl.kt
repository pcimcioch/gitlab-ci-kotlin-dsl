package pcimcioch.gitlabci.dsl.stage

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.list
import kotlinx.serialization.builtins.serializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.GitlabCiDslMarker
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@GitlabCiDslMarker
@Serializable(with = StagesDsl.StagesDslSerializer::class)
class StagesDsl : DslBase {
    var stages: MutableList<String> = mutableListOf()

    fun stage(stage: String) = stages.add(stage)
    operator fun String.unaryPlus() = this@StagesDsl.stages.add(this)

    object StagesDslSerializer : ValueSerializer<StagesDsl, List<String>>(String.serializer().list, StagesDsl::stages)
}