package com.krachkovsky.cryptoapp.models

import com.google.gson.annotations.SerializedName

data class CoinInfoListOfData(
    @SerializedName("Data")
    val data: List<Data>
)