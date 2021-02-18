package com.pkurkowski.spacextest.data.retrofit

import com.pkurkowski.spacextest.data.moshi.model.CompanyMoshi
import com.pkurkowski.spacextest.data.moshi.model.DateAfterQueryMoshi
import com.pkurkowski.spacextest.data.moshi.model.LaunchDataMoshi
import com.pkurkowski.spacextest.data.moshi.model.LaunchQueryResponseMoshi
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SpaceXApiInterface {

    @POST("launches/query")
    fun getLaunchesAfter(@Body after: DateAfterQueryMoshi): Single<LaunchQueryResponseMoshi>

    @GET("launches")
    fun getLaunches(): Single<List<LaunchDataMoshi>>

    @GET("company")
    fun getCompany(): Single<CompanyMoshi>

}