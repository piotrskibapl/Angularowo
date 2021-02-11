package pl.piotrskiba.angularowo.data.login.di

import dagger.Module
import dagger.Provides
import pl.piotrskiba.angularowo.data.login.LoginApiService
import pl.piotrskiba.angularowo.data.network.di.NetworkModule
import retrofit2.Retrofit

@Module(includes = [NetworkModule::class])
class LoginApiModule {

    @Provides
    fun bindLoginApiService(retrofit: Retrofit) = retrofit.create(LoginApiService::class.java)
}