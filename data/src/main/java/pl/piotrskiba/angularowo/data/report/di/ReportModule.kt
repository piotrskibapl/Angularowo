package pl.piotrskiba.angularowo.data.report.di

import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.network.di.NetworkModule
import pl.piotrskiba.angularowo.data.report.ReportApiService
import pl.piotrskiba.angularowo.data.report.repository.ReportRepositoryImpl
import pl.piotrskiba.angularowo.domain.report.repository.ReportRepository
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
class ReportModule {

    @Provides
    fun provideReportApiService(retrofit: Retrofit): ReportApiService =
        retrofit.create(ReportApiService::class.java)

    @Provides
    fun provideReportRepository(reportApiService: ReportApiService): ReportRepository =
        ReportRepositoryImpl(reportApiService)
}
