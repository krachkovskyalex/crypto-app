package com.krachkovsky.cryptoapp.view_models

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.krachkovsky.cryptoapp.api.CoinRetrofit
import com.krachkovsky.cryptoapp.database.AppDatabase
import com.krachkovsky.cryptoapp.models.CoinPriceInfo
import com.krachkovsky.cryptoapp.models.CoinPriceInfoRawData
import com.krachkovsky.cryptoapp.util.Constants.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class CoinDetailsFragmentViewModel(private val request: CoinRetrofit, application: Application) :
    ViewModel() {

    private val db = AppDatabase.getInstance(application)

    private var isRequestRepeat = true

    fun getCoinDetailInfo(fSym: String): LiveData<CoinPriceInfo> {
        return db.coinPriceInfoDao().getPriceInfoAboutCoin(fSym)
    }

    fun loadCoinData(fSyms: String) {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "1 ${Thread.currentThread().name}")
            while (isRequestRepeat) {
                getCoinInfo(fSyms)
                delay(5_000)
            }
        }
    }

    fun stopRequest() {
        isRequestRepeat = false
    }

    fun startRequest() {
        isRequestRepeat = true
    }

    private suspend fun getCoinInfo(fSyms: String) {
        try {
            val response = request.apiRequest().getCoinInfo(fSyms = fSyms)
            if (response.isSuccessful) {
                val result = response.body()?.let { getPriceListFromRawData(it) }
                result?.let { db.coinPriceInfoDao().insertPriceList(it) }
                Log.e(TAG, result.toString())
            } else {
                Log.e(TAG, "Response getFullPriceList isNotSuccessful")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Response Exception getFullPriceList $e")
        }
    }

    private fun getPriceListFromRawData(
        coinPriceInfoRawData: CoinPriceInfoRawData
    ): List<CoinPriceInfo> {
        val result = ArrayList<CoinPriceInfo>()
        val jsonObject = coinPriceInfoRawData.coinPriceInfoJsonObject
        val coinKeySet = jsonObject.keySet()
        for (coinKey in coinKeySet) {
            val currencyJson = jsonObject.getAsJsonObject(coinKey)
            val currencyKeySet = currencyJson.keySet()
            for (currencyKey in currencyKeySet) {
                val priceInfo = Gson().fromJson(
                    currencyJson.getAsJsonObject(currencyKey),
                    CoinPriceInfo::class.java
                )
                result.add(priceInfo)
            }
        }
        return result
    }
}