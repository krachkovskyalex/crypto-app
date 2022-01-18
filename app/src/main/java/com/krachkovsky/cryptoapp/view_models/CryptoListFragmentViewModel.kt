package com.krachkovsky.cryptoapp.view_models

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.krachkovsky.cryptoapp.api.CoinRetrofit
import com.krachkovsky.cryptoapp.database.AppDatabase
import com.krachkovsky.cryptoapp.models.CoinPriceInfo
import com.krachkovsky.cryptoapp.models.CoinPriceInfoRawData
import com.krachkovsky.cryptoapp.util.Constants.QUERY_PAGE_SIZE
import com.krachkovsky.cryptoapp.util.Constants.TAG
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

class CryptoListFragmentViewModel(private val request: CoinRetrofit, application: Application) :
    ViewModel() {

    private val db = AppDatabase.getInstance(application)

    private var isRequestRepeat = true

    private var currentPage = 0

    private val listFSyms = mutableListOf<String>()

    val priceList = db.coinPriceInfoDao().getPriceList()

    fun loadCoinData() {
        viewModelScope.launch(Dispatchers.IO) {
            Log.d(TAG, "1 ${Thread.currentThread().name}")
            while (isRequestRepeat) {
                val topCoins = getTopCoinsInfo()
                if (topCoins.isNotEmpty()) {
                    getFullPriceList(topCoins)
                }
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

    fun addPage() {
        if (QUERY_PAGE_SIZE * currentPage < 40) {
            currentPage++
        }
    }

    private suspend fun getTopCoinsInfo(): String {
        try {
            if (listFSyms != db.coinPriceInfoDao().getFromSymList()) {
                Log.d(TAG, "Write list of FSyms")
                listFSyms.clear()
                listFSyms.addAll(db.coinPriceInfoDao().getFromSymList())
            }
            val response =
                request.apiRequest().getTopCoinsInfo(limit = QUERY_PAGE_SIZE, page = currentPage)
            if (response.isSuccessful) {
                val a = response.body()?.data
                a?.let { it ->
                    it.map {
                        if (!listFSyms.contains(it.coinInfo.name)) {
                            listFSyms.add(it.coinInfo.name)
                        }
                    }
                    Log.e(TAG, listFSyms.joinToString(","))
                    return listFSyms.joinToString(",")
                }
            }
        } catch (e: Exception) {
            Log.e(TAG, "Response getTopCoinsInfo isNotSuccessful -> ${e.message}")
        }
        Log.e(TAG, "Response getTopCoinsInfo isNotSuccessful the last field")
        return ""
    }

    private suspend fun getFullPriceList(fSyms: String) {
        try {
            val response = request.apiRequest().getFullPriceList(fSyms = fSyms)
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