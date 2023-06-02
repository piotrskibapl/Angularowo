package pl.piotrskiba.angularowo.data.server.di

import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.network.di.NetworkModule
import pl.piotrskiba.angularowo.data.server.ServerApiService
import pl.piotrskiba.angularowo.data.server.repository.ServerRepositoryImpl
import pl.piotrskiba.angularowo.domain.server.repository.ServerRepository
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
class ServerModule {

    @Provides
    fun provideServerApiService(retrofit: Retrofit): ServerApiService =
        retrofit.create(ServerApiService::class.java)

    @Provides
    fun provideServerRepository(serverApiService: ServerApiService): ServerRepository =
        ServerRepositoryImpl(serverApiService)
}
