package com.qianyanhuyu.app_large.data

import com.qianyanhuyu.app_large.BuildConfig
import com.qianyanhuyu.app_large.constants.Net
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.Proxy
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


/***
 * @Author : Cheng
 * @CreateDate : 2023/9/13 16:41
 * @Description : 这里使用了SingletonComponent，因此 NetworkModule 绑定到 Application 的整个生命周期
 */
@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideOkHttpClient() = run {
        val logging = HttpLoggingInterceptor()
        logging.level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.BASIC
        OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(Net.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
            .readTimeout(Net.READ_TIME_OUT, TimeUnit.SECONDS)
            .proxy(Proxy.NO_PROXY)
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(Net.BASE_URL)
        .client(okHttpClient)
        .build()

    @Singleton
    @Provides
    fun provideContentApiService(retrofit: Retrofit): ContentApi = retrofit.create(ContentApi::class.java)

}