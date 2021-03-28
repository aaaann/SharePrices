package com.annevonwolffen.shareprices.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.annevonwolffen.shareprices.domain.StocksInteractor
import com.annevonwolffen.shareprices.domain.company.CompanyInfoInteractor
import com.annevonwolffen.shareprices.models.domain.CompanyInfoModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class StockDetailsViewModel @Inject constructor(
    private val companyInfoInteractor: CompanyInfoInteractor,
    private val stocksInteractor: StocksInteractor
) : BaseViewModel() {

    private val _companyInfo = MutableLiveData<CompanyInfoModel>()
    val companyInfo: LiveData<CompanyInfoModel> = _companyInfo

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    fun loadCompanyData(ticker: String) {
        compositeDisposable.add(
            companyInfoInteractor.getCompanyInfo(ticker)
                .doOnSubscribe { _isLoading.postValue(true) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doFinally { _isLoading.value = false }
                .subscribe({ _companyInfo.value = it }, {
                    _companyInfo.value = CompanyInfoModel(ticker)
                    Log.d(TAG, it?.message.orEmpty())
                })
        )
    }

    fun clickFavorite(ticker: String) {
        _isFavorite.value = stocksInteractor.setFavorite(ticker)
    }

    private companion object {
        const val TAG = "StockDetailsViewModel"
    }
}