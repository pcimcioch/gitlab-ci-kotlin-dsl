package pcimcioch.gitlabci.dsl

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class DurationTest {

    @Test
    fun `should forbid negative durations`() {
        assertThrows<IllegalArgumentException> { Duration(years = -1) }
        assertThrows<IllegalArgumentException> { Duration(months = -1) }
        assertThrows<IllegalArgumentException> { Duration(days = -1) }
        assertThrows<IllegalArgumentException> { Duration(hours = -1) }
        assertThrows<IllegalArgumentException> { Duration(minutes = -1) }
        assertThrows<IllegalArgumentException> { Duration(seconds = -1) }
    }

    @Test
    fun `should forbid zero durations`() {
        assertThrows<IllegalArgumentException> { Duration(years = 0) }
    }

    @Test
    fun `should format simple duration`() {
        assertThat(Duration(years = 1).toString()).isEqualTo("1 y")
        assertThat(Duration(years = 5).toString()).isEqualTo("5 y")
        assertThat(Duration(months = 1).toString()).isEqualTo("1 mo")
        assertThat(Duration(months = 5).toString()).isEqualTo("5 mo")
        assertThat(Duration(days = 1).toString()).isEqualTo("1 day")
        assertThat(Duration(days = 5).toString()).isEqualTo("5 day")
        assertThat(Duration(hours = 1).toString()).isEqualTo("1 hr")
        assertThat(Duration(hours = 5).toString()).isEqualTo("5 hr")
        assertThat(Duration(minutes = 1).toString()).isEqualTo("1 min")
        assertThat(Duration(minutes = 5).toString()).isEqualTo("5 min")
        assertThat(Duration(seconds = 1).toString()).isEqualTo("1 sec")
        assertThat(Duration(seconds = 5).toString()).isEqualTo("5 sec")
    }

    @Test
    fun `should format complex durations`() {
        assertThat(Duration(years = 1, months = 2, days = 3, hours = 4, minutes = 5, seconds = 6).toString()).isEqualTo("1 y 2 mo 3 day 4 hr 5 min 6 sec")
        assertThat(Duration(years = 10, minutes = 50, seconds = 60).toString()).isEqualTo("10 y 50 min 60 sec")
        assertThat(Duration(minutes = 360000).toString()).isEqualTo("360000 min")
    }

    @Test
    fun `should format never`() {
        assertThat(Duration(never = true).toString()).isEqualTo("never")
        assertThat(Duration(never = true, minutes = 10, hours = 2).toString()).isEqualTo("never")
    }
}