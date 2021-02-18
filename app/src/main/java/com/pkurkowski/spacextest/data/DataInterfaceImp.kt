package com.pkurkowski.spacextest.data

import com.pkurkowski.spacextest.data.moshi.model.CompanyMoshi
import com.pkurkowski.spacextest.data.moshi.model.LaunchDataMoshi
import com.pkurkowski.spacextest.data.moshi.model.toEntity
import com.pkurkowski.spacextest.data.moshi.model.toLaunchEntity
import com.pkurkowski.spacextest.data.retrofit.SpaceXApiInterface
import com.pkurkowski.spacextest.data.room.dao.CompanyDao
import com.pkurkowski.spacextest.data.room.dao.LaunchesDao
import com.pkurkowski.spacextest.data.room.entity.toCompanyData
import com.pkurkowski.spacextest.data.room.entity.toLaunchData
import com.pkurkowski.spacextest.domain.CompanyData
import com.pkurkowski.spacextest.domain.DataInterface
import com.pkurkowski.spacextest.domain.LaunchData
import com.pkurkowski.spacextest.domain.Response
import io.reactivex.Single

class DataInterfaceImp(
    private val remote: SpaceXApiInterface,
    private val launchesDao: LaunchesDao,
    private val companyDao: CompanyDao,
) : DataInterface {

    override fun getLaunchesList(): Single<Response<List<LaunchData>>> {

        return remote.getLaunches()
            .map { ApiCallResult.Success(it) as ApiCallResult<List<LaunchDataMoshi>> }
            .onErrorReturn { ApiCallResult.Fail(it) }
            .flatMap { apiCallResult ->

                when (apiCallResult) {
                    is ApiCallResult.Fail -> Single.just(apiCallResult)
                    is ApiCallResult.Success -> {
                        launchesDao.insertLaunches(apiCallResult.data.map { it.toLaunchEntity() })
                            .toSingleDefault(apiCallResult as ApiCallResult<List<LaunchDataMoshi>>)
                            .onErrorReturn { ApiCallResult.Fail(it) }
                    }
                }
            }
            .flatMap { apiCallResult ->
                launchesDao.getAllLaunches().map { entitiesList ->
                    val data = entitiesList.map { entity -> entity.toLaunchData() }
                    when (apiCallResult) {
                        is ApiCallResult.Fail -> {
                            when {
                                data.isNotEmpty() -> Response.FailWithOfflineData(
                                    apiCallResult.reason,
                                    data
                                )
                                else -> Response.FailNoOfflineData(apiCallResult.reason)
                            }
                        }
                        is ApiCallResult.Success -> Response.Success(data)
                    }
                }
            }

    }

    override fun getCompany(): Single<Response<CompanyData>> {
        return remote.getCompany()
            .map {
                ApiCallResult.Success(it) as ApiCallResult<CompanyMoshi>
            }
            .onErrorReturn { ApiCallResult.Fail(it) }
            .flatMap { apiCallResult ->
                when (apiCallResult) {
                    is ApiCallResult.Fail -> Single.just(apiCallResult)
                    is ApiCallResult.Success -> {
                        companyDao.insertCompany(apiCallResult.data.toEntity())
                            .toSingleDefault(apiCallResult as ApiCallResult<CompanyMoshi>)
                            .onErrorReturn { ApiCallResult.Fail(it) }
                    }
                }
            }
            .flatMap { apiCallResult ->

                companyDao.getCompany().map { company ->
                    when (apiCallResult) {
                        is ApiCallResult.Fail -> Response.FailWithOfflineData(
                            apiCallResult.reason,
                            company.toCompanyData()
                        )

                        is ApiCallResult.Success -> Response.Success(company.toCompanyData())
                    }
                }.toSingle(
                    Response.FailNoOfflineData(
                        when(apiCallResult) {
                            is ApiCallResult.Fail -> apiCallResult.reason
                            else -> Throwable("Unexpected exception loading data from room")
                        }
                    )
                )
            }
    }


    sealed class ApiCallResult<out T : Any> {
        data class Success<out T : Any>(val data: T) : ApiCallResult<T>()
        data class Fail(val reason: Throwable) : ApiCallResult<Nothing>()
    }


}