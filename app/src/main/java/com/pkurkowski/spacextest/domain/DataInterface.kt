package com.pkurkowski.spacextest.domain

import io.reactivex.Single

interface DataInterface {
    fun getLaunchesList(): Single<Response<List<LaunchData>>>
    fun getCompany(): Single<Response<CompanyData>>
}

sealed class Response<out T: Any> {
    object InProgress: Response<Nothing>()
    data class FailWithOfflineData<out T: Any>(val reason: Throwable, val offlineData: T) : Response<T>()
    data class FailNoOfflineData(val reason: Throwable): Response<Nothing>()
    data class Success<out T: Any>(val data: T) : Response<T>()
}

