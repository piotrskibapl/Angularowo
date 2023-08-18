package pl.piotrskiba.angularowo.domain.offers.usecase

import io.mockk.every
import io.mockk.mockk
import io.reactivex.rxjava3.core.Single
import org.junit.jupiter.api.Test
import pl.piotrskiba.angularowo.domain.offers.model.OffersInfoModel
import pl.piotrskiba.angularowo.domain.offers.repository.OffersRepository

class GetOffersInfoUseCaseTest {

    val offersRepository: OffersRepository = mockk()
    val tested = GetOffersInfoUseCase(offersRepository)

    @Test
    fun `SHOULD get offers info`() {
        val offersInfoModel: OffersInfoModel = mockk()
        every { offersRepository.getOffersInfo() } returns Single.just(offersInfoModel)

        val result = tested.execute().test()

        result.assertValue(offersInfoModel)
    }
}
