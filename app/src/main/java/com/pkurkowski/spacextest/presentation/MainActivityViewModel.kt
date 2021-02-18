package com.pkurkowski.spacextest.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pkurkowski.spacextest.BuildConfig
import com.pkurkowski.spacextest.application.di.SchedulersProvider
import com.pkurkowski.spacextest.domain.CompanyData
import com.pkurkowski.spacextest.domain.DataInterface
import com.pkurkowski.spacextest.domain.LaunchData
import com.pkurkowski.spacextest.domain.Response
import com.pkurkowski.spacextest.presentation.dialog.FilterUpdate
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit

class MainActivityViewModel(
    private val source: DataInterface,
    private val schedulers: SchedulersProvider
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()
    private val launchesObservable = Observable
        .interval(0, BuildConfig.RELOAD_DELAY_LAUNCHES_SEC, TimeUnit.SECONDS)
        .flatMap {
            source.getLaunchesList().toObservable()
        }
        .subscribeOn(schedulers.ioScheduler)
        .observeOn(schedulers.uiScheduler)


    private val companyObservable = Observable
        .interval(0, BuildConfig.RELOAD_DELAY_LAUNCHES_SEC, TimeUnit.SECONDS)
        .flatMap {
            source.getCompany().toObservable()
        }
        .subscribeOn(schedulers.ioScheduler)
        .observeOn(schedulers.uiScheduler)


    private val filterSubject =
        BehaviorSubject.createDefault<FilterData>(FilterData.All(Order.ASCENDING))

    val launchData: MutableLiveData<ResponseWithFilter> =
        MutableLiveData(ResponseWithFilter(Response.InProgress, filterSubject.value!!))

    val companyData: MutableLiveData<Response<CompanyData>> =
        MutableLiveData(Response.InProgress)


    override fun onCleared() {
        compositeDisposable.clear()
        super.onCleared()
    }

    fun start() {
        compositeDisposable.clear()

        compositeDisposable.add(
            companyObservable
                .map {
                    companyData.value = it
                }.subscribe()
        )

        compositeDisposable.add(
            Observables.combineLatest(
                launchesObservable,
                filterSubject.hide()
            ) { launches, filter -> ResponseWithFilter(launches, filter) }
                .map {
                    when (it.data) {
                        is Response.FailNoOfflineData -> it
                        is Response.FailWithOfflineData -> it.copy(
                            data = it.data.copy(
                                offlineData = it.filter.process(
                                    it.data.offlineData
                                )
                            )
                        )
                        Response.InProgress -> it
                        is Response.Success -> it.copy(
                            data = it.data.copy(
                                data = it.filter.process(
                                    it.data.data
                                )
                            )
                        )
                    }
                }
                .map {
                    launchData.value = it
                }.subscribe()

        )
    }

    fun stop() {
        compositeDisposable.clear()
    }

    fun filterUpdate(update: FilterUpdate) {
        val current = filterSubject.value!!
        filterSubject.onNext(
            when (update) {
                FilterUpdate.All -> FilterData.All(current.order)
                FilterUpdate.OnlyNotSuccess -> FilterData.Success(false, current.order)
                FilterUpdate.OnlySuccess -> FilterData.Success(true, current.order)
                FilterUpdate.ReverseOrder -> current.reverse()
            }
        )
    }

    data class ResponseWithFilter(val data: Response<List<LaunchData>>, val filter: FilterData)

}