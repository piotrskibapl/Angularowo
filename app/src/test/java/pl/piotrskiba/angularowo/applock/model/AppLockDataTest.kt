package pl.piotrskiba.angularowo.applock.model

import org.amshove.kluent.shouldBeEqualTo
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.applock.model.AppLockConfigModel

class AppLockDataTest {

    @Test
    fun `SHOULD map domain model to ui`() {
        val tested = AppLockConfigModel(
            title = "title",
            body = "body",
            startTimestamp = 123L,
            endTimestamp = 456L,
        )

        tested.toUi() shouldBeEqualTo AppLockData(
            title = "title",
            body = "body",
        )
    }
}
