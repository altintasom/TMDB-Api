package com.altintasomer.tmdbapi.di

import com.altintasomer.tmdbapi.R
import com.altintasomer.tmdbapi.data.romete.TMDBApi
import com.altintasomer.tmdbapi.common.Utils.Companion.BASE_URL
import com.altintasomer.tmdbapi.data.repository.RepositoryImp
import com.altintasomer.tmdbapi.domain.repository.Repository
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor() : HttpLoggingInterceptor {
        return HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor : HttpLoggingInterceptor) : OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    fun provideRetrofitBuilder(client : OkHttpClient) : TMDBApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TMDBApi::class.java)

    @Singleton
    @Provides
    fun provideRepoImp(api : TMDBApi) : Repository = RepositoryImp(api = api)

    @Singleton
    @Provides
    fun providesFirebaseRemoteConfig() : FirebaseRemoteConfig {
       val firebaseRemoteConfig =  FirebaseRemoteConfig.getInstance()
        val firebaseRemoteConfigSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }
        firebaseRemoteConfig.setConfigSettingsAsync(firebaseRemoteConfigSettings)
        firebaseRemoteConfig.setDefaultsAsync(R.xml.remote_config_defaults)
        firebaseRemoteConfig.fetchAndActivate();
        return firebaseRemoteConfig
    }

}