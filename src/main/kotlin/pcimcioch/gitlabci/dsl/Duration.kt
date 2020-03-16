package pcimcioch.gitlabci.dsl

import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.serializer
import pcimcioch.gitlabci.dsl.serializer.ValueSerializer

@Serializable(with = Duration.DurationSerializer::class)
data class Duration(
        val years: Int = 0,
        val months: Int = 0,
        val days: Int = 0,
        val hours: Int = 0,
        val minutes: Int = 0,
        val seconds: Int = 0
) {
    init {
        require(years >= 0) { "Argument |years| must be greater or equal zero. years=$years" }
        require(months >= 0) { "Argument |months| must be greater or equal zero. months=$months" }
        require(days >= 0) { "Argument |days| must be greater or equal zero. days=$days" }
        require(hours >= 0) { "Argument |hours| must be greater or equal zero. hours=$hours" }
        require(minutes >= 0) { "Argument |minutes| must be greater or equal zero. minutes=$minutes" }
        require(seconds >= 0) { "Argument |seconds| must be greater or equal zero. seconds=$seconds" }
        require(years + months + days + hours + minutes + seconds > 0) { "Duration can't be zero" }
    }

    override fun toString(): String {
        val terms = mutableListOf<String>()

        if (years > 0) terms.add("$years y")
        if (months > 0) terms.add("$months mo")
        if (days > 0) terms.add("$days day")
        if (hours > 0) terms.add("$hours hr")
        if (minutes > 0) terms.add("$minutes min")
        if (seconds > 0) terms.add("$seconds sec")

        return terms.joinToString(" ")
    }

    object DurationSerializer : ValueSerializer<Duration, String>(String.serializer(), Duration::toString)
}