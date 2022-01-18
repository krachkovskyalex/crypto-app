package com.krachkovsky.cryptoapp.api

import com.krachkovsky.cryptoapp.models.CoinInfoListOfData
import com.krachkovsky.cryptoapp.models.CoinPriceInfoRawData
import com.krachkovsky.cryptoapp.util.Constants.CURRENCY
import com.krachkovsky.cryptoapp.util.Constants.QUERY_PARAM_API_KEY
import com.krachkovsky.cryptoapp.util.Constants.QUERY_PARAM_FROM_SYMBOLS
import com.krachkovsky.cryptoapp.util.Constants.QUERY_PARAM_LIMIT
import com.krachkovsky.cryptoapp.util.Constants.QUERY_PARAM_PAGE
import com.krachkovsky.cryptoapp.util.Constants.QUERY_PARAM_TO_SYMBOL
import com.krachkovsky.cryptoapp.util.Constants.QUERY_PARAM_TO_SYMBOLS
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("top/totalvolfull")
    suspend fun getTopCoinsInfo(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "",
        @Query(QUERY_PARAM_LIMIT) limit: Int,
        @Query(QUERY_PARAM_PAGE) page: Int = 0,
        @Query(QUERY_PARAM_TO_SYMBOL) tSym: String = CURRENCY
    ): Response<CoinInfoListOfData>

    @GET("pricemultifull")
    suspend fun getFullPriceList(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "",
        @Query(QUERY_PARAM_FROM_SYMBOLS) fSyms: String,
        @Query(QUERY_PARAM_TO_SYMBOLS) tSyms: String = CURRENCY
    ): Response<CoinPriceInfoRawData>

    @GET("pricemultifull")
    suspend fun getCoinInfo(
        @Query(QUERY_PARAM_API_KEY) apiKey: String = "",
        @Query(QUERY_PARAM_FROM_SYMBOLS) fSyms: String,
        @Query(QUERY_PARAM_TO_SYMBOLS) tSyms: String = CURRENCY
    ): Response<CoinPriceInfoRawData>
}