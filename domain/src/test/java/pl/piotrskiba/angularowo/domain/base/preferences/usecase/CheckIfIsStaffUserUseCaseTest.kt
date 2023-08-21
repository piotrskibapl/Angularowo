package pl.piotrskiba.angularowo.domain.base.preferences.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import pl.piotrskiba.angularowo.domain.base.preferences.repository.PreferencesRepository
import pl.piotrskiba.angularowo.domain.rank.model.RankModel
import pl.piotrskiba.angularowo.domain.rank.repository.RankRepository

class CheckIfIsStaffUserUseCaseTest {

    val preferencesRepository: PreferencesRepository = mockk()
    val rankRepository: RankRepository = mockk()
    val tested = CheckIfIsStaffUserUseCase(preferencesRepository, rankRepository)

    @ParameterizedTest
    @ValueSource(booleans = [true, false])
    fun `SHOULD return value based on rank staff parameter`(isStaff: Boolean) {
        val rankName = "rank"
        val rankModels: List<RankModel> = listOf(
            mockk {
                every { name } returns "rank"
                every { staff } returns isStaff
            },
        )
        every { preferencesRepository.rankName() } returns Maybe.just(rankName)
        every { rankRepository.getAllRanks() } returns Single.just(rankModels)

        val result = tested.execute().test()

        result.assertValue(isStaff)
    }
}
