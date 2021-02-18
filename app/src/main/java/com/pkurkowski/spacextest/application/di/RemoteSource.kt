package com.pkurkowski.spacextest.application.di

import com.pkurkowski.spacextest.BuildConfig
import com.pkurkowski.spacextest.data.moshi.adapter.DatePrecisionAdapter
import com.pkurkowski.spacextest.data.retrofit.SpaceXApiInterface
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.*
import java.util.concurrent.TimeUnit

val remoteDataSourceModule = module {

    //OkHttp
    single { OkHttpClient.Builder()
        .connectTimeout(10L, TimeUnit.SECONDS)
        .readTimeout(10L, TimeUnit.SECONDS)
        .also {
            if (BuildConfig.DEBUG) it.addInterceptor(
                HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }
            )
        }
        .build()


    }

    //Moshi
    single {
        Moshi.Builder()
            .add(DatePrecisionAdapter())
            .add(Date::class.java,  Rfc3339DateJsonAdapter())
            .addLast(KotlinJsonAdapterFactory()).build()
    }


    //RxAdapter
    single {
        RxJava2CallAdapterFactory.createWithScheduler(get<SchedulersProvider>().ioScheduler)
    }

    //RemoteSource
    single { Retrofit.Builder()
        .baseUrl(BuildConfig.SPACE_X_API_URL)
        .client(get())
        .addConverterFactory(MoshiConverterFactory.create(get()))
        .addCallAdapterFactory(get<RxJava2CallAdapterFactory>())
        .build().create(SpaceXApiInterface::class.java)
    }

}