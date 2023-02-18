package pcimcioch.gitlabci.dsl.stage

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.serializer
import pcimcioch.gitlabci.dsl.DslBase
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable(with = StagesDsl.StagesDslSerializer::class)
class StagesDsl : DslBase() {
    var stages: MutableList<String> = mutableListOf()

    fun stage(stage: String) = stages.add(stage)
    operator fun String.unaryPlus() = this@StagesDsl.stages.add(this)
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as StagesDsl

        if (stages != other.stages) return false

        return true
    }

    override fun hashCode(): Int {
        return stages.hashCode()
    }

    object StagesDslSerializer :
        ValueSerializer<StagesDsl, List<String>>(ListSerializer(String.serializer()), StagesDsl::stages)

    companion object {
        init {
            addSerializer(StagesDsl::class, serializer())
        }
    }
}